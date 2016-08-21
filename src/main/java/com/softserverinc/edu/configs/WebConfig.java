package com.softserverinc.edu.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Configuration of Spring MVC
 */
@EnableWebMvc
@Configuration
@EnableSpringDataWebSupport
@ComponentScan(basePackages = {"com.softserverinc.edu"})
@Import({DBConfig.class, TilesConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);

    /**
     * Configures properties for application from properties file
     *
     * @return empty properties configurer where Spring put data from properties file by annotation mechanism
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * Configure implisite controllers for views
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/history");
        registry.addViewController("/release");
        registry.addViewController("/label");
        registry.addViewController("/worklog");
        registry.addViewController("/about");
        registry.addViewController("/admin");
        registry.addViewController("/accessDenied");
    }

    /**
     * Make resources aviable to web client. Spring handle the folders starting from WEB-INF
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/resources/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/resources/js/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/resources/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
    }

    /**
     * Set properties file for validation
     *
     * @return resource bundle
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
        rb.setBasenames(new String[]{"validation"});
        return rb;
    }


    /**
     * Instructs Spring to use CommonsMultipartResolver class as realization of multipartResolver
     * @return CommonsMultipartResolver instance
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

}