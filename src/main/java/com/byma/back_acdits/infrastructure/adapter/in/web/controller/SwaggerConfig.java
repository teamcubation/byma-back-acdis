package com.byma.back_acdits.infrastructure.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;

@OpenAPIDefinition(
        info = @Info(
                title = "Api ACDIS",
                description = "Api para gestionar ACDIs",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        description = "DEV SERVER",
                        url = "http://localhost:${server.port}",
                        variables = {
                                @ServerVariable(name = "port", defaultValue = "${server.port}")
                        }
                )
        }
)
public class SwaggerConfig {
}
