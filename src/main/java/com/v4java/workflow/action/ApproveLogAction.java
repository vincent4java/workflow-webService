package com.v4java.workflow.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.pojo.ApproveLog;
import com.v4java.workflow.service.webservice.IApproveService;

@Controller
@Scope("prototype")
public class ApproveLogAction {

	@Autowired
	private IApproveService approveService;


	@RequestMapping(value = "/getApproveLogByWorkflowId/{workflowId}")
	public @ResponseBody List<ApproveLog> getApproveLogByWorkflowId(@PathVariable Integer workflowId){
		List<ApproveLog> approveLogs = approveService.findApproveLogsByWorkfLowId(workflowId);
		return approveLogs;
	}
	
}
