package com.codemeet.configuration;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dwgiqzcnf");
        config.put("api_key", "589849293534553");
        config.put("api_secret", "XswI_m257acwl00x3dESSEMFFWI");
        return new Cloudinary(config);
    }
}
