package com.sly.demo.flowable.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理
 * 
 * @author sly
 * @time 2019年2月13日
 */
@ControllerAdvice
public class GlobalExceptionAdvice {
	
	/**
	 * 拦截处理自定义异常
	 * @param request
	 * @param ex
	 * @return
	 * @throws Exception
	 * @author sly
	 * @time 2019年2月13日
	 */
	@ResponseBody
	@ExceptionHandler(CustomerException.class)
	public Object resolveException(CustomerException customerException) throws Exception {
		Map<String, Object> result = new HashMap<>();
		result.put("status", customerException.getCustomStatus());
		result.put("message", customerException.getCustomMessage());
		return result;
	}
}
