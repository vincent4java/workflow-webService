package com.v4java.workflow.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.constat.WorkFLowMsgConst;
import com.v4java.workflow.query.webservice.WorkFlowQuery;
import com.v4java.workflow.service.webservice.IWorkFlowService;
import com.v4java.workflow.vo.BTables;
import com.v4java.workflow.vo.webservice.WorkFlowVO;

	
@Controller
@Scope("prototype")
public class WorkFlowAction {
	
	
	@Autowired
	private IWorkFlowService workFlowService;
	
	private static final Logger logger = Logger.getLogger(WorkFlowAction.class);
	
	@RequestMapping(value = "/insertWorkFlow")
	public @ResponseBody WorkFLowMsgConst insertWorkFlow(){
		return null;
	}

	
	@RequestMapping(value = "/getWorkFlow")
	public @ResponseBody BTables<WorkFlowVO> getWorkFlow(){
		WorkFlowQuery workFlowQuery = new WorkFlowQuery();
		workFlowQuery.setSystemId(7);
		workFlowQuery.setUserCode("vz110");
		BTables<WorkFlowVO> bTables = new BTables<WorkFlowVO>();
		try {
			List<WorkFlowVO> workFlowVOs = workFlowService.findUserWorkFlowVOByUserCodeAndSystemId(workFlowQuery);
			int count = workFlowService.findUserWorkFlowVOCountByUserCodeAndSystemId(workFlowQuery);
			if (workFlowVOs != null && workFlowVOs.size() !=0) {
				bTables.setRows(workFlowVOs);
				bTables.setTotal(count);
			}
		} catch (Exception e) {
			logger.error("查询--"+workFlowQuery.getSystemId()+"--系统中用户"+workFlowQuery.getUserCode()+"待办审批任务错误", e);
		}
		return bTables;
	}
	
}
