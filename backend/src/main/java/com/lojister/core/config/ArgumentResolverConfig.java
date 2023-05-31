package com.lojister.core.config;

import com.lojister.core.argumentresolver.FileResultArgumentResolver;
import com.lojister.core.argumentresolver.FileResultListArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Component
public class ArgumentResolverConfig implements WebMvcConfigurer {

    @Autowired
    FileResultArgumentResolver fileResultArgumentResolver;

    @Autowired
    FileResultListArgumentResolver fileResultListArgumentResolver;

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(fileResultArgumentResolver);
        argumentResolvers.add(fileResultListArgumentResolver);
    }

}
