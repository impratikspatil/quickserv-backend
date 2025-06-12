package com.example.kptech.quickserv.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static com.example.kptech.quickserv.util.Constants.*;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Apply to all endpoints
                .allowedOrigins(BASE_FRONTEND_URL,
                        "http://localhost:3000" )  // Allow your React app's origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allowed HTTP methods
                .allowedHeaders("*")  // Allow all headers (e.g., Authorization, Content-Type)
                .allowCredentials(true);  // Allow cookies/sessions (if needed)
    }
}