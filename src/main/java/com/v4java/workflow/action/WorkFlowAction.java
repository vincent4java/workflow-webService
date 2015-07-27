package com.v4java.workflow.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.workflow.constant.FlowConst;
import com.v4java.workflow.constant.WorkFlowErrorConst;
import com.v4java.workflow.constat.WorkFLowMsgConst;
import com.v4java.workflow.pojo.JobsUser;
import com.v4java.workflow.pojo.WorkFlow;
import com.v4java.workflow.query.webservice.JobsUserQuery;
import com.v4java.workflow.query.webservice.WorkFlowQuery;
import com.v4java.workflow.service.webservice.IJobsUserService;
import com.v4java.workflow.service.webservice.IWorkFlowService;
import com.v4java.workflow.vo.BTables;
import com.v4java.workflow.vo.webservice.UserVO;
import com.v4java.workflow.vo.webservice.WorkFlowVO;

	
@Controller
@Scope("prototype")
public class WorkFlowAction {
	
	
	@Autowired
	private IWorkFlowService workFlowService;
	@Autowired
	private IJobsUserService jobsUserService;
	private static final Logger logger = Logger.getLogger(WorkFlowAction.class);
	
	@RequestMapping(value = "/insertWorkFlow")
	public @ResponseBody WorkFLowMsgConst insertWorkFlow(){
		return null;
	}

	
	@RequestMapping(value = "/getWorkFlow/{systemId}/{userCode}")
	public @ResponseBody BTables<WorkFlowVO> getWorkFlow(@PathVariable Integer systemId,@PathVariable String userCode){
		WorkFlowQuery workFlowQuery = new WorkFlowQuery();
		workFlowQuery.setUserCode(userCode);
		workFlowQuery.setSystemId(systemId);
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
	
	
	
	
	@RequestMapping(value = "/submitWorkFlow/{systemId}/{busyTypeId}/{userCode}/{userName}/{workFlowCode}/{json}")
	public @ResponseBody WorkFLowMsgConst submitWorkFlow(@PathVariable Integer systemId,@PathVariable Integer busyTypeId, @PathVariable String userCode, @PathVariable String userName, @PathVariable String  workFlowCode, @PathVariable String  json){
		WorkFLowMsgConst workFLowMsgConst = new WorkFLowMsgConst();
		JobsUserQuery jobsUserQuery = new JobsUserQuery();
		jobsUserQuery.setSystemId(systemId);
		jobsUserQuery.setUserCode(userCode);
		UserVO userVO = new UserVO();
		userVO.setSystemId(systemId);
		userVO.setUserCode(userCode);
		userVO.setUserName(userName);
		WorkFlow flow = new WorkFlow();
		flow.setBusyTypeId(busyTypeId);
		flow.setSystemId(systemId);
		flow.setJson(json);
		flow.setWorkFlowCode(workFlowCode);
		try {
			setUerJobs(userVO);
			int n =workFlowService.insertWorkFlow(flow, userVO);
			workFLowMsgConst.setIsSuccess(n);
			if (n!=1) {
				workFLowMsgConst.setMsg(WorkFlowErrorConst.MSG[-n]);
			}
		} catch (Exception e) {
			logger.error("查询--"+systemId+"--系统中用户"+userCode+"--"+userName+"待办审批任务错误", e);
		}
		
		return workFLowMsgConst;
	}
	
	
	@RequestMapping(value = "/agree/{systemId}/{workFlowId}/{userCode}/{userName}")
	public @ResponseBody WorkFLowMsgConst agree(@PathVariable Integer systemId,@PathVariable Integer workFlowId, @PathVariable String userCode,@PathVariable String userName){
		WorkFLowMsgConst workFLowMsgConst = new WorkFLowMsgConst();
		UserVO userVO = new UserVO();
		userVO.setUserCode(userCode);
		userVO.setUserName(userName);
		userVO.setSystemId(systemId);
		try {
			Integer n =null;
			setUerJobs(userVO);
			if (userVO.getJobsIds()==null||userVO.getJobsIds().size()==0) {
				n = WorkFlowErrorConst.USER_NO_JOBS;
			}else {
				n = workFlowService.doWorkFlow(workFlowId, userVO, FlowConst.AGREE_TRUE);
			}
			if (n!=null&&n!=1) {
				workFLowMsgConst.setIsSuccess(n);
				workFLowMsgConst.setMsg(WorkFlowErrorConst.MSG[-n]);
			}else {
				workFLowMsgConst.setMsg("成功");
			}
			workFLowMsgConst.setIsSuccess(n);
		} catch (Exception e) {
			//logger.error("查询--"+systemId+"--系统中用户"+userCode+"--"+userName+"待办审批任务错误", e);
		}
		return workFLowMsgConst;
	}

	
	public @ResponseBody WorkFLowMsgConst disAgree(){

		return null;
	}


	private void setUerJobs(UserVO userVO) {
		JobsUserQuery jobsUserQuery = new JobsUserQuery();
		jobsUserQuery.setSystemId(userVO.getSystemId());
		jobsUserQuery.setUserCode(userVO.getUserCode());
		List<JobsUser> jobsUsers;
		try {
			jobsUsers = jobsUserService.selectjobsUserByUserCodeAndSystemId(jobsUserQuery);
			List<Integer> jobIds  = new ArrayList<Integer>();
			for (JobsUser jobsUser : jobsUsers) {
				jobIds.add(jobsUser.getJobsId());
			}
			userVO.setJobsIds(jobIds);
			jobIds = null;
			jobsUserQuery = null;
		} catch (Exception e) {
		}
	
	}
}

