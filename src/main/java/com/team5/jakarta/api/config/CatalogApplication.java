package com.team5.jakarta.api.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class CatalogApplication extends ResourceConfig {

    public CatalogApplication() {
        packages("com.team5.jakarta.api");
        property("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", true);
    }
}

