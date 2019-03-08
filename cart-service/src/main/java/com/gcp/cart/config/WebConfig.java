package com.gcp.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Anuj Kumar
 * 
 *   This class has configuration to enable CORS policies
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
 
	/**
	 * This method is used to enable CORS policies for incoming request 
	 * 
	 * @param CorsRegistry
	 * @return void
	 */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "DELETE");
    }
}
   