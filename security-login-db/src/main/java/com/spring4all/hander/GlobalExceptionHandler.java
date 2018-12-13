package com.spring4all.hander;


import com.spring4all.exception.PowerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


/**
  * @description 异常处理
  * @author wangchao
  * @date
  * @param
  * @return
  */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(Exception ex) {
        logger.error(ex.getMessage(),ex);

        if(ex instanceof PowerException){
            return ex.getMessage();
        }else{
            return "error";
        }
    }
}
