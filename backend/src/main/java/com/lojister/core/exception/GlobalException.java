package com.lojister.core.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class GlobalException{

    private List<String> message;
    private HttpStatus httpStatus;
    @JsonIgnore
    private String className;
    @JsonIgnore
    private String methodName;
    @JsonIgnore
    private String errorLine;
    @JsonIgnore
    private String path;
    @JsonIgnore
    private String methodType;
    private LocalDateTime timestamp;


    public GlobalException(List<String> message) {
        this.message=message;
    }

    public GlobalException(List<String> message,HttpStatus httpStatus) {
        this.message=message;
        this.httpStatus=httpStatus;
    }
    public GlobalException(List<String> message,String path) {
        this.message=message;
        this.path=path;
    }
    public GlobalException(List<String> message,HttpStatus httpStatus,String path) {
        this.message=message;
        this.httpStatus=httpStatus;
        this.path=path;
    }

    public GlobalException(List<String> message, HttpStatus httpStatus, String className, String methodName, String errorLine, String path, String methodType, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.className = className;
        this.methodName = methodName;
        this.errorLine = errorLine;
        this.path = path;
        this.methodType = methodType;
        this.timestamp = timestamp;
    }
}
