package com.lojister.core.argumentresolver;

import com.lojister.core.util.FileUploadUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class FileResultArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameter().getType() == FileUploadUtil.FileResult.class;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) {

        HttpServletRequest request = ((ServletWebRequest) webRequest)
                .getRequest();

        List<FileUploadUtil.FileResult> list = FileUploadUtil.getFiles(request);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }
}