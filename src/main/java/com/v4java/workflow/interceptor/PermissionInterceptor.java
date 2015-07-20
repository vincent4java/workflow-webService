package com.v4java.workflow.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class PermissionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
/*		String url = request.getServletPath();
		if(ServletPathConst.ADMIN_MAPPING_URLS.contains(url)){
			return true;
		}
		@SuppressWarnings("unchecked")
		List<String> adminUserPermissions= (List<String>) request.getSession().getAttribute(SessionConst.ADMIN_USER_PERMISSIONS);
		if (url.endsWith("Json.do")) {
			url = url.replace("Json.do", ".do").replace("get", "find");
		}
		if (adminUserPermissions.contains(url)) {
			return true;
		}
		PrintWriter out = null;
		response.setContentType("text/html; charset=utf-8");
		out = response.getWriter();
		response.setContentType("text/html; charset=utf-8");
		out.println("没有权限!");
		out.flush();
		out.close();*/
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
