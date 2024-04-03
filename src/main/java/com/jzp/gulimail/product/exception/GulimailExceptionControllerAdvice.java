package com.jzp.gulimail.product.exception;

import com.jzp.common.exception.BizCodeEnume;
import com.jzp.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
* 集中处理所有校验异常
* */
@Slf4j
//@ControllerAdvice(basePackages = "com.jzp.gulimail.product.controller")
@RestControllerAdvice(basePackages = "com.jzp.gulimail.product.controller")
public class GulimailExceptionControllerAdvice {
    //第一个方法用于精确匹配某个异常，之后若还想精确·匹配某个异常可以通过查看控制台输出的异常信息进行定义
   @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
       log.error("数据校验出现问题{},异常类型:{}",e.getMessage(),e.getClass());
       BindingResult bindingResult=e.getBindingResult();
       Map<String,String> errorMap=new HashMap<>();
       bindingResult.getFieldErrors().forEach((fieldError) -> {
           errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
       });
       return R.error(BizCodeEnume.VALID_EXCEPTION.getCode(),BizCodeEnume.VALID_EXCEPTION.getMsg()).put("data",errorMap);
   }
   //该方法用于匹配所有异常
    @ExceptionHandler(value = Throwable.class)
   public R handleException(Throwable throwable){
//       log.error("错误：",throwable);
       return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
   }
}
