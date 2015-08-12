package com.v4java.workflow.action;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v4java.utils.DateUtil;
import com.v4java.workflow.constant.WorkFlowErrorConst;
import com.v4java.workflow.constat.WorkFLowMsg;
import com.v4java.workflow.constat.WorkFLowMsgConst;
import com.v4java.workflow.param.webservice.WorkFlowParam;
import com.v4java.workflow.pojo.WorkFlow;
import com.v4java.workflow.pojo.Xf9System;
import com.v4java.workflow.query.webservice.JobsUserQuery;
import com.v4java.workflow.query.webservice.WorkFlowQuery;
import com.v4java.workflow.service.webservice.IWorkFlowService;
import com.v4java.workflow.vo.BTables;
import com.v4java.workflow.vo.webservice.UserVO;
import com.v4java.workflow.vo.webservice.WorkFlowVO;

	
@Controller
@Scope("prototype")
public class WorkFlowAction extends BaseAction{
	
	
	@Autowired
	private IWorkFlowService workFlowService;
	private static final Logger logger = Logger.getLogger(WorkFlowAction.class);
	
	@RequestMapping(value = "/insertWorkFlow")
	public @ResponseBody WorkFLowMsg insertWorkFlow(){
		return null;
	}

	
	@RequestMapping(value = "/getWorkFlow",method = RequestMethod.POST)
	public @ResponseBody BTables<WorkFlowVO> getWorkFlow(@RequestBody WorkFlowQuery workFlowQuery){
		
		BTables<WorkFlowVO> bTables = new BTables<WorkFlowVO>();
		try {
			Xf9System system = getXf9System(workFlowQuery.getSystemCode());
			if (system==null||system.getStatus()==1) {
				return null;
			}
			workFlowQuery.setSystemId(system.getId());
			List<WorkFlowVO> workFlowVOs = workFlowService.findUserWorkFlowVOByUserCodeAndSystemId(workFlowQuery);
			for (WorkFlowVO workFlowVO : workFlowVOs) {
				workFlowVO.setCreateTimeName(DateUtil.datetimeToStr(workFlowVO.getCreateTime()));
				workFlowVO.setUpdateTimeName(DateUtil.datetimeToStr(workFlowVO.getUpdateTime()));
			}
			int count = workFlowService.findUserWorkFlowVOCountByUserCodeAndSystemId(workFlowQuery);
			if (workFlowVOs != null && count !=0) {
				bTables.setRows(workFlowVOs);
				bTables.setTotal(count);
			}
		} catch (Exception e) {
			logger.error("查询--"+workFlowQuery.getSystemId()+"--系统中用户"+workFlowQuery.getUserCode()+"待办审批任务错误", e);
		}
		return bTables;
	}
	
	
	
	
	@RequestMapping(value = "/submitWorkFlow",method = RequestMethod.POST)
	public @ResponseBody WorkFLowMsg submitWorkFlow(@RequestBody WorkFlowQuery workFlowQuery){
		WorkFLowMsg workFLowMsg = new WorkFLowMsg();
		JobsUserQuery jobsUserQuery = new JobsUserQuery();
		jobsUserQuery.setUserCode(workFlowQuery.getUserCode());
		UserVO userVO = new UserVO();
		userVO.setUserCode(workFlowQuery.getUserCode());
		userVO.setUserName(workFlowQuery.getUserName());
		WorkFlow flow = new WorkFlow();
		flow.setBusyTypeId(workFlowQuery.getBusyTypeId());
		flow.setJson(workFlowQuery.getJson());
		flow.setWorkFlowCode(workFlowQuery.getWorkFlowCode());
		try {
			Xf9System system = getXf9System(workFlowQuery.getSystemCode());
			if (system==null) {
				//该系统不存在
				workFLowMsg.setIsSuccess(WorkFlowErrorConst.SYSTEM_NOT);
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[-WorkFlowErrorConst.SYSTEM_NOT]);
				return workFLowMsg;
			}
			if (system.getStatus()==WorkFLowMsgConst.SYSTEM_FALSE) {
				//该系统已被禁用
				workFLowMsg.setIsSuccess(WorkFlowErrorConst.SYSTEM_FALSE);
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[-WorkFlowErrorConst.SYSTEM_FALSE]);
				return workFLowMsg;
			}
			jobsUserQuery.setSystemId(system.getId());
			flow.setSystemId(system.getId());
			flow.setName(workFlowQuery.getName());
			userVO.setSystemId(system.getId());
			setUerJobs(userVO,workFlowQuery.getSystemCode());
			int n =workFlowService.insertWorkFlow(flow, userVO);
			workFLowMsg.setIsSuccess(n);
			workFLowMsg.setWorkFlow(flow);
			if (n!=1) {
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[-n]);
			}else {
				workFLowMsg.setMsg("成功");
			}
		/*	if (n==1) {
				JedisUtil.getInstance().hincrby("system:"+workFlowQuery.getSystemCode(), flow.getJobsId()+":"+workFlowQuery.getBusyTypeId()+":num", 1);
			}*/
			//JedisUtil.getInstance().hincrby("system:"+workFlowQuery.getSystemCode(), "type:"+workFlowQuery.getBusyTypeId()+":money", 1);
		} catch (Exception e) {
			logger.error("提交--"+workFlowQuery.getSystemCode()+"--系统中用户"+workFlowQuery.getUserCode()+"--"+workFlowQuery.getUserName()+"--"+workFlowQuery.getWorkFlowCode()+"审批任务错误", e);
			workFLowMsg.setMsg("系统错误");
		}
		
		return workFLowMsg;
	}
	
	
	@RequestMapping(value = "/agree",method = RequestMethod.POST)
	public @ResponseBody WorkFLowMsg agree(@RequestBody WorkFlowQuery workFlowQuery){
		WorkFLowMsg workFLowMsg = new WorkFLowMsg();
		UserVO userVO = new UserVO();
		userVO.setUserCode(workFlowQuery.getUserCode());
		userVO.setUserName(workFlowQuery.getUserName());
		WorkFlow workFlow = new WorkFlow();
		try {
			Xf9System system = getXf9System(workFlowQuery.getSystemCode());
			if (system==null) {
				//该系统不存在
				workFLowMsg.setIsSuccess(WorkFlowErrorConst.SYSTEM_NOT);
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[WorkFlowErrorConst.SYSTEM_NOT]);
				return workFLowMsg;
			}
			if (system.getStatus()==WorkFLowMsgConst.SYSTEM_FALSE) {
				//该系统已被禁用
				workFLowMsg.setIsSuccess(WorkFlowErrorConst.SYSTEM_FALSE);
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[WorkFlowErrorConst.SYSTEM_NOT]);
				return workFLowMsg;
			}
			Integer n =null;
			WorkFlowParam flowParam= null; 
			userVO.setSystemId(system.getId());
			setUerJobs(userVO,workFlowQuery.getSystemCode());
			if (userVO.getJobsIds()==null||userVO.getJobsIds().size()==0) {
				n = WorkFlowErrorConst.USER_NO_JOBS;
			}else {
				workFlow.setId(workFlowQuery.getWorkFlowId());
				if (workFlowQuery.getAgree()!=2) {
					flowParam = workFlowService.doWorkFlow(workFlow, userVO, workFlowQuery.getAgree());
				}else {
					flowParam= workFlowService.deleteWorkflow(workFlow, userVO);
				}
				n = flowParam.getCount();
				workFlow.setStatus(flowParam.getNowStatus());
			}
			if (n!=null&&n!=1) {
				workFLowMsg.setIsSuccess(n);
				workFLowMsg.setMsg(WorkFlowErrorConst.MSG[-n]);
			}else {
/*				JedisUtil.getInstance().hincrby("system:"+workFlowQuery.getSystemCode(), workFlowQuery.getBusyTypeId()+":"+flowParam.getOldJobsId(), -1);
				if (flowParam.getIsDelete()!=1) {
					JedisUtil.getInstance().hincrby("system:"+workFlowQuery.getSystemCode(), workFlowQuery.getBusyTypeId()+":"+flowParam.getNowJobsId(), 1);
				}*/
				workFLowMsg.setMsg("成功");
			}
			workFLowMsg.setIsSuccess(n);
			workFLowMsg.setWorkFlow(workFlow);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer();
			error.append(workFlowQuery.getSystemCode());
			error.append("系统中用户");
			error.append(workFlowQuery.getUserCode());
			error.append("--");
			error.append(workFlowQuery.getUserName());
			error.append("审批id为");
			error.append(workFlowQuery.getWorkFlowId());
			error.append("任务失败");
			logger.error(error.toString(), e);
			workFLowMsg.setMsg("系统错误");
		}
		return workFLowMsg;
	}

	@RequestMapping(value = "/updateWorkFLowJson",method = RequestMethod.POST)
	public @ResponseBody WorkFLowMsg updateWorkFLowJson(@RequestBody WorkFlowQuery workFlowQuery){
		WorkFLowMsg workFLowMsg = new WorkFLowMsg();
		try {
			WorkFlow workFlow = new WorkFlow();
			workFlow.setId(workFlowQuery.getWorkFlowId());
			workFlow.setJson(workFlowQuery.getJson());
			workFlowService.updateWorkFlowJson(workFlow);
		} catch (Exception e) {
			StringBuffer error = new StringBuffer();
			error.append(workFlowQuery.getSystemCode());
			error.append("系统中用户");
			error.append(workFlowQuery.getUserCode());
			error.append("--");
			error.append(workFlowQuery.getUserName());
			error.append("更改id为");
			error.append(workFlowQuery.getWorkFlowId());
			error.append("任务json失败");
			logger.error(error.toString(), e);
			workFLowMsg.setMsg("系统错误");
		}
		return workFLowMsg;
		
	}

}

