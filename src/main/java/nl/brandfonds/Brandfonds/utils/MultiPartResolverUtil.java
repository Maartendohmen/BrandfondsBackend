package nl.brandfonds.Brandfonds.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
public class MultiPartResolverUtil {

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver
                = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(10485760);
        return multipartResolver;
    }
}
