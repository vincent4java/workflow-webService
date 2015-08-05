package com.v4java.workflow.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.pojo.ApproveLog;
import com.v4java.workflow.pojo.Xf9System;
import com.v4java.workflow.service.webservice.IApproveService;

@Controller
@Scope("prototype")
public class ApproveLogAction extends BaseAction{

	@Autowired
	private IApproveService approveService;


	@RequestMapping(value = "/getApproveLogByWorkflowId/{workflowId}/{systemCode}")
	public @ResponseBody List<ApproveLog> getApproveLogByWorkflowId(@PathVariable Integer workflowId,@PathVariable String systemCode){
		try {
			Xf9System system = getXf9System(systemCode);
			if (system==null||system.getStatus()==1) {
				return null;
			}
			List<ApproveLog> approveLogs = approveService.findApproveLogsByWorkfLowId(workflowId);
			return approveLogs;
		} catch (Exception e) {
			
		}
		return null;
	}

	
/*	@RequestMapping(value = "/getApproveLogByWorkflowCode/{workflowCode}")
	public @ResponseBody List<ApproveLog> getApproveLogByWorkflowCode(@PathVariable String workflowCode){
		Xf9System system = g
		List<ApproveLog> approveLogs = approveService.findApproveLogsByWorkfLowId(workflowId);
		return approveLogs;
	}*/
}
