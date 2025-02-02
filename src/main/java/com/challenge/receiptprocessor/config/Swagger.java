package com.challenge.receiptprocessor.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Receipt Processor", version = "1.0.0", description = "A simple receipt processor"))
public class Swagger {
}
