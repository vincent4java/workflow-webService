package com.v4java.workflow.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.v4java.workflow.pojo.Xf9System;
import com.v4java.workflow.service.webservice.IXf9SystemService;

public class BaseAction {

	@Autowired
	private IXf9SystemService xf9SystemService;

	
	Xf9System getXf9System(String systemCode) throws Exception{
		return xf9SystemService.findXf9SystemBySystemCode(systemCode);}
}
