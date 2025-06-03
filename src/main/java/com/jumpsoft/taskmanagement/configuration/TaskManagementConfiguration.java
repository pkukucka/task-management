package com.jumpsoft.taskmanagement.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration class for Task Management application.
 * This class implements WebMvcConfigurer to customize Spring MVC configuration.
 * It redirects the root URL to the Swagger UI documentation page.
 */
@Configuration
public class TaskManagementConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }

    @Bean
    public OpenAPI taskManagementOpenApi(GitProperties gitProperties, BuildProperties buildProperties) {
        return new OpenAPI()
                .info(getInfo("Task management API", "API for user and task management", gitProperties, buildProperties));
    }

    private Info getInfo(String appName, String description, GitProperties gitProperties, BuildProperties buildProperties){
        return new Info()
                .title(appName)
                .description(description)
                .version(buildProperties.getVersion() + " - " + gitProperties.getShortCommitId())
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

}

