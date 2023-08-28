package com.sky.exception;

import cn.hutool.core.util.StrUtil;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//全局异常处理类
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //预期异常（业务异常） BusinessException
    @ExceptionHandler(BusinessException.class)
    public Result handlerBusinessException(BusinessException e) {
        log.error("出现业务异常：{}", e);
        return Result.error(e.getCode(), e.getMessage());
    }

    //非预期异常 Exception 兜底异常处理
    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e) {
        log.error("出现未知异常：{}", e);
        return Result.error(500, "未知异常，请稍后重试");
    }

    //专门处理重复键异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handlerDuplicateKeyException(Exception e) {
        log.error("出现未知异常：{}", e);

        //只有mysql版本高于8  才能使用下面这段详细判断的功能
        if (e.getMessage().contains("employee.username")){
            return Result.error(500, "用户账户重复");
        }
        if (e.getMessage().contains("employee.phone")){
            return Result.error(500, "用户手机号重复");
        }
        if (e.getMessage().contains("employee.id_number")){
            return Result.error(500, "用户身份证号重复");
        }

        if (StrUtil.contains(e.getMessage(),"category.name")){
            return Result.error("分类名称重复");
        }
        return Result.error(500, "字段重复异常");
    }
}
