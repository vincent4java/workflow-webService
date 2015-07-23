package com.v4java.workflow.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.constat.WorkFLowMsgConst;
import com.v4java.workflow.service.webservice.IWorkFlowService;


@Controller
@Scope("prototype")
public class WorkFlowAction {
	
	
	@Autowired
	private IWorkFlowService workFlowService;
	
	
	@RequestMapping(value = "/insertWorkFlow")
	public @ResponseBody WorkFLowMsgConst findAdminUserJson(){
		return null;
	}
	
}
