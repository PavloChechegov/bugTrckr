package com.softserverinc.edu.controllers;

import com.softserverinc.edu.constants.PageConstant;
import com.softserverinc.edu.entities.User;
import com.softserverinc.edu.entities.enums.UserRole;
import com.softserverinc.edu.forms.UserFormValidator;
import com.softserverinc.edu.services.HistoryService;
import com.softserverinc.edu.services.ProjectService;
import com.softserverinc.edu.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@SessionAttributes("fileUploadForm")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserFormValidator userFormValidator;

    @GetMapping("/users")
    public String userForm(Model model, Pageable pageable) {
        populateDefaultModel(model);
        model.addAttribute("userList", userService.findAllUsers(pageable));
        return "users";
    }

    @GetMapping("/user/{id}/remove")
    public String removeUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.setIsDeletedTrue(id);
        redirectAttributes.addFlashAttribute("css", "success");
        redirectAttributes.addFlashAttribute("msg", "User is deleted!");
        return "redirect:/users";
    }

    @GetMapping("/user/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findOne(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAvailableRolesForUser(user.getRole()));
        model.addAttribute("projects", projectService.findAll());
        return "user_form_edit";
    }

    @PostMapping("/user/{id}/edit")
    public String editUserPost(@PathVariable Long id, @RequestParam String email,
                               @RequestParam String firstName, @RequestParam String lastName,
                               @RequestParam Long project, @RequestParam UserRole role,
                               @RequestParam String description, RedirectAttributes redirectAttributes){
        if(userService.isEmailUnique(email, id)){
            redirectAttributes.addFlashAttribute("msg", "User with the same email already exists");
            return"redirect:/user/" + id + "/edit";
        }
        userService.saveEditedUser(id, email, firstName, lastName, project, role, description);
        return "redirect:/users";
    }

    @GetMapping("/user/add")
    public String addUser(Model model) {
        populateDefaultModel(model);
        model.addAttribute("user", new User());
        return "userform";
    }

    @PostMapping("/user/add")
    public String addUserPost(@ModelAttribute @Valid User user, BindingResult result,
                              RedirectAttributes redirectAttributes) {
        userFormValidator.validate(user, result);
        if (result.hasErrors()) {
            return "userform";
        }
        userService.saveUser(user, redirectAttributes);
        return "redirect:/users";
    }

    @GetMapping("/user/{id}/view")
    public String viewUser(@PathVariable long id, Model model,
                           @PageableDefault(PageConstant.AMOUNT_PROJECT_ELEMENTS) Pageable pageable) {
        User user = userService.findOne(id);
        model.addAttribute("allHistory", historyService.findAllHistoryForUser(user, pageable));
        model.addAttribute("commentHistory", historyService.findCommentHistoryForUser(user, pageable));
        model.addAttribute("user", user);
        return "userview";
    }

    @GetMapping("/user/details")
    public String viewUserByDetails(Principal principal) {
        String loggedInUserName = principal.getName();
        User user = userService.findByEmailIs(loggedInUserName);
        long id = user.getId();
        return "redirect:/user/" + id + "/view";
    }

    @PostMapping("/users/searchByName")
    public String userSearchByName(@RequestParam String firstName, @RequestParam String lastName, Model model,
                                   Pageable pageable) {
        if (!firstName.isEmpty() && !lastName.isEmpty())
            model.addAttribute("userList", userService.findByFullName(firstName, lastName, pageable));
        else if (!firstName.isEmpty())
            model.addAttribute("userList", userService.findByFirstNameContaining(firstName, pageable));
        else
            model.addAttribute("userList", userService.findByLastNameContaining(lastName, pageable));
        populateDefaultModel(model);
        return "users";
    }

    @PostMapping("/users/searchByEmail")
    public String userSearchByEmailPost(@RequestParam(value = "email") String userEmail, Model model) {
        model.addAttribute("userList", userService.findByEmailContaining(userEmail));
        populateDefaultModel(model);
        LOGGER.debug("User search list ByEmail");
        return "users";
    }

    @PostMapping(value = "/users/searchByRole")
    public String userSearchByRole(@RequestParam(value = "role") UserRole role, Model model) {
        model.addAttribute("userList", userService.findByRole(role));
        populateDefaultModel(model);
        LOGGER.debug("User search list ByRole POST");
        return "users";
    }

    private void populateDefaultModel(Model model) {
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.ROLE_USER);
        roles.add(UserRole.ROLE_QA);
        roles.add(UserRole.ROLE_DEVELOPER);
        roles.add(UserRole.ROLE_PROJECT_MANAGER);
        model.addAttribute("roles", roles);
        model.addAttribute("projects", projectService.findAll());
    }
}