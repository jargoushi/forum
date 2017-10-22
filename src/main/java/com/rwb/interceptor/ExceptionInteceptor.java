package com.rwb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.rwb.contanst.ForumException;

public class ExceptionInteceptor implements HandlerExceptionResolver{

	private static final Logger log = LoggerFactory.getLogger(ExceptionInteceptor.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception e) {
		// 写日志文件
		log.error(ForumException.RUNTIME_EXCEPTION, e);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error");
		modelAndView.addObject("message", "您的网络异常，请稍后重试么么哒");
		return modelAndView;
	}

}
