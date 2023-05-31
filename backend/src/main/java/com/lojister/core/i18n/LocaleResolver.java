package com.lojister.core.i18n;

import com.lojister.core.util.SecurityContextUtil;
import com.lojister.model.enums.Language;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
@Slf4j
public class LocaleResolver extends AcceptHeaderLocaleResolver implements WebMvcConfigurer {



    private final SecurityContextUtil securityContextUtil;

    public LocaleResolver(SecurityContextUtil securityContextUtil) {
        this.securityContextUtil = securityContextUtil;
    }

    private static final List<Locale> LOCALES = Arrays.asList(
            new Locale("tr"),
            new Locale("de"),
            new Locale("ru"),
            new Locale("ar"),
            new Locale("pl"),
            new Locale("fr"),
            new Locale("sq"),
            new Locale("es"),
            new Locale("en")
    );

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String language = request.getHeader("Accept-Language");


        if(securityContextUtil.IsAnonymousUser()){
            if (language == null || language.isEmpty()) {
                return Locale.getDefault();
            }
            List<Locale.LanguageRange> list = Locale.LanguageRange.parse(language);
            Locale locale = Locale.lookup(list, LOCALES);
            return locale;
        }
        if(securityContextUtil.IsAuthenticationUser()){
              return Language.toLocale(securityContextUtil.getCurrentUser().getLanguage());
        }

     return Locale.getDefault();

    }
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.addBasenames("messages");
        rs.addBasenames("ValidationMessages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }
    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        System.out.println(bean);
        return bean;
    }
}
