package com.example.gym.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "API GYM CRM",
                description = "Our api provides a CRM GYM",
                version = "1.0.0"
        ),
        security = @SecurityRequirement(
                name = "basicAuth"
        )

)
@SecurityScheme(
        name = "basicAuth",
        description = "Username and password for my api",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {

}
