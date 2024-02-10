package nl.brandfonds.Brandfonds.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig  {
    @Bean
    public WebMvcConfigurer setupCors(){
        return new WebMvcConfigurer(){
            public void addCorsMappings(final CorsRegistry registry){
                registry.addMapping("/**").allowedHeaders("*").allowedMethods("*");
            }
        };
    }
}
