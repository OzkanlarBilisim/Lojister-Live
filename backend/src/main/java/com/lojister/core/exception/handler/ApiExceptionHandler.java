package com.lojister.core.exception.handler;

import com.lojister.core.exception.PhotoExceptionTest;
import com.lojister.core.exception.GlobalException;
import com.lojister.core.exception.InvalidRoleException;
import com.lojister.core.exception.base.BaseException;
import com.lojister.core.message.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {


    private GlobalException globalExceptionMethod(BaseException e, HttpServletRequest servletRequest, HttpStatus httpStatus) {

        List<String> message = new ArrayList<>();
        String className = e.getStackTrace()[0].getClassName();
        String methodName = e.getStackTrace()[0].getMethodName();
        String errorLine = String.valueOf(e.getStackTrace()[0].getLineNumber());
        String path = servletRequest.getRequestURI();
        String methodType = servletRequest.getMethod();
        LocalDateTime timestamp = LocalDateTime.now();
        // message.add(e.getStackTrace()[0].getFileName());
        //details.add(e.getStackTrace()[1].getMethodName());

        if (servletRequest.getHeader("Accept-Language").equals("tr")) {
            message.add(e.getMessage());

        } else if (servletRequest.getHeader("Accept-Language").equals("en")) {
            message.add(e.getEnMessage());
        } else {
            message.add(e.getMessage());
        }

        // message.add(servletRequest.getServletPath());
        // message.add(servletRequest.getContentType());
        // message.add(servletRequest.getRequestURL().toString());
        return new GlobalException(message, httpStatus, className, methodName, errorLine, path, methodType, timestamp);
    }

    private GlobalException globalExceptionMethodException(Exception e, HttpServletRequest servletRequest, HttpStatus httpStatus) {

        List<String> message = new ArrayList<>();
        String className = e.getStackTrace()[0].getClassName();
        String methodName = e.getStackTrace()[0].getMethodName();
        String errorLine = String.valueOf(e.getStackTrace()[0].getLineNumber());
        String path = servletRequest.getRequestURI();
        String methodType = servletRequest.getMethod();
        LocalDateTime timestamp = LocalDateTime.now();
        // message.add(e.getStackTrace()[0].getFileName());
        //details.add(e.getStackTrace()[1].getMethodName());

        message.add(e.getMessage());


        // message.add(servletRequest.getServletPath());
        // message.add(servletRequest.getContentType());
        // message.add(servletRequest.getRequestURL().toString());
        return new GlobalException(message, httpStatus, className, methodName, errorLine, path, methodType, timestamp);
    }

    //Validation hatalarını override ediyoruz.
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> message = new ArrayList<>();
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.toList());
        HttpStatus httpStatusData = status;
        String className = ex.getStackTrace()[0].getClassName();
        String methodName = ex.getStackTrace()[0].getMethodName();
        String errorLine = String.valueOf(ex.getStackTrace()[0].getLineNumber());
        String methodType = "";
        String path = request.getContextPath();
        LocalDateTime timestamp = LocalDateTime.now();
        //message.add(errorMessage);

        for (String valid : validationList) {

            message.add(valid);

        }
        //message.add(request.getHeader("Accept-Language"));

        GlobalException globalException = new GlobalException(message, httpStatusData, className, methodName, errorLine, path, methodType, timestamp);
        return new ResponseEntity<>(globalException, status);
    }


    //------------------------BAD REQUEST START-------------------------

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> baseException(BaseException e, HttpServletRequest servletRequest) {

        GlobalException globalException = globalExceptionMethod(e, servletRequest, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }


    //-----------------BAD REQUEST END------------------------------------


    //-----------------FORBIDDEN START-----------------------------------

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<Object> invalidRoleException(BaseException e, HttpServletRequest servletRequest) {

        GlobalException globalException = globalExceptionMethod(e, servletRequest, HttpStatus.FORBIDDEN);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> accessDeniedException(Exception e, HttpServletRequest servletRequest) {

        GlobalException globalException = globalExceptionMethodException( e, servletRequest, HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    //------------------FORBIDDEN END--------------------------------------


    //FARKLI
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(Exception e, HttpServletRequest servletRequest) {


        List<String> messageList = new ArrayList<>();
        messageList.add("Silmek istediğiniz kayıt başka yerde kullanıldığı için silemezsiniz");
        GlobalException globalException = new GlobalException(messageList, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }

    //FARKLI
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> sqlIntegrityConstraintViolationException(Exception e, HttpServletRequest servletRequest) {

        List<String> messageList = new ArrayList<>();
        messageList.add("Silmek istediğiniz kayıt başka yerde kullanıldığı için silemezsiniz");
        GlobalException globalException = new GlobalException(messageList, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }

    //FARKLI
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> dataIntegrityViolationException(Exception e, HttpServletRequest servletRequest) {

        List<String> messageList = new ArrayList<>();
        messageList.add("Silmek istediğiniz kayıt başka yerde kullanıldığı için silemezsiniz");
        GlobalException globalException = new GlobalException(messageList, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }

    //FARKLI
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Object> internalServerError(Exception e, HttpServletRequest servletRequest) {

        List<String> messageList = new ArrayList<>();
        messageList.add("Sistemsel Bir Hata Meydana Gelmiştir.");
        GlobalException globalException = new GlobalException(messageList, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());
    }

    //FARKLI
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> expiredJwtException(Exception e, HttpServletRequest servletRequest) {

        return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
    }

    //FARKLI
    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Object> malformedJwtException(Exception e, HttpServletRequest servletRequest) {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //LoadByUsername
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<Object> internalAuthenticationServiceException(BaseException e, HttpServletRequest servletRequest) {

        GlobalException globalException = globalExceptionMethod(e, servletRequest, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(globalException, globalException.getHttpStatus());

    }


    //DENEME
    @ExceptionHandler(PhotoExceptionTest.class)
    public void photoExceptionTest(Exception e, HttpServletRequest servletRequest) {
        System.out.println("fotodeneme exception");
    }

    //FARKLI
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
    }
}
