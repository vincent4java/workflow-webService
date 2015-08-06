package com.v4java.workflow.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.pojo.ApproveLog;
import com.v4java.workflow.pojo.Xf9System;
import com.v4java.workflow.query.ApproveLogQuery;
import com.v4java.workflow.service.webservice.IApproveService;

@Controller
@Scope("prototype")
public class ApproveLogAction extends BaseAction{

	@Autowired
	private IApproveService approveService;


	@RequestMapping(value = "/getApproveLogByWorkflowId",method = RequestMethod.POST)
	public @ResponseBody List<ApproveLog> getApproveLogByWorkflowId(@RequestBody ApproveLogQuery approveLogQuery){
		try {
			Xf9System system = getXf9System(approveLogQuery.getSystemCode());
			if (system==null||system.getStatus()==1) {
				return null;
			}
			List<ApproveLog> approveLogs = approveService.findApproveLogsByWorkfLowId(approveLogQuery.getWorkflowId());
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
