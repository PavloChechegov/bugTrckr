package com.softserverinc.edu.controllers;

import com.softserverinc.edu.entities.WorkLog;
import com.softserverinc.edu.forms.WorkLogFormValidator;
import com.softserverinc.edu.services.WorkLogService;
import com.softserverinc.edu.services.securityServices.WorkLogSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class WorkLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkLogController.class);

    @Autowired
    private WorkLogService workLogService;

    @Autowired
    private WorkLogFormValidator workLogFormValidator;

    @Autowired
    private WorkLogSecurityService workLogSecurityService;

    @PreAuthorize("@workLogSecurityService.hasPermissionToSaveWorkLog(#issueId, #workLog)")
    @RequestMapping(value = "issue/{issueId}/worklog/save", method = RequestMethod.POST)
    public String addWorkLogPOST(@PathVariable @P("issueId") Long issueId,
                                 @ModelAttribute("worklog") @P("workLog") @Valid WorkLog workLog,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (!workLogFormValidator.validateWorklogUI(workLog, workLogSecurityService.getActiveUser(), issueId) ||
                result.hasErrors()) {
            redirectAttributes.addFlashAttribute("msg", "Unable to save. Please fix your data.");
            return "redirect:/issue/" + issueId;
        }
        workLogService.save(workLog);
        LOGGER.info("Worklog saved, id= " + workLog.getId());
        redirectAttributes.addFlashAttribute("msg", "Work log entry saved.");
        return "redirect:/issue/" + issueId;
    }

    @PreAuthorize("@workLogSecurityService.hasPermissionToRemoveWorkLog(#worklogId)")
    @RequestMapping(value = "issue/{issueId}/worklog/{worklogId}/remove", method = RequestMethod.GET)
    public String removeWorkLog(@PathVariable @P("worklogId") Long worklogId,
                                RedirectAttributes redirectAttributes) {
        workLogService.delete(worklogId);
        LOGGER.debug("Worklog " + worklogId + " is removed!");
        redirectAttributes.addFlashAttribute("msg", "Work log entry has been deleted.");
        return "redirect:/issue/{issueId}";
    }
}