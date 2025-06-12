package com.uddangtangtang.global.config;

import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("uddangtangtang API")
                        .version("v1.0"))
                .servers(List.of(
                        new Server().url("https://api.uddangtangtang-crew.com")
                ));
    }

    @PostConstruct
    public void setup() {
        PrimitiveType.enablePartialTime();
    }
}
