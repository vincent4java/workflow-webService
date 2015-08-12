package com.v4java.workflow.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.v4java.workflow.pojo.JobsUser;
import com.v4java.workflow.pojo.Xf9System;
import com.v4java.workflow.query.webservice.JobsUserQuery;
import com.v4java.workflow.service.webservice.IJobsUserService;
import com.v4java.workflow.service.webservice.IXf9SystemService;
import com.v4java.workflow.vo.webservice.UserVO;

public class BaseAction {

	@Autowired
	private IXf9SystemService xf9SystemService;
	
	@Autowired
	private IJobsUserService jobsUserService;
	
	Xf9System getXf9System(String systemCode) throws Exception {
/*		Xf9System system = null;
		String json = JedisUtil.getInstance().hget("system:" + systemCode,"ObJson");
		system = JSON.parseObject(json, Xf9System.class);
		system = xf9SystemService.findXf9SystemBySystemCode(systemCode);
		if (system == null) {
			system = xf9SystemService.findXf9SystemBySystemCode(systemCode);
			JedisUtil.getInstance().hset("system:" + systemCode, "ObJson",JSON.toJSONString(system));
		}*/
		return xf9SystemService.findXf9SystemBySystemCode(systemCode);
	}
	
	
	 void setUerJobs(UserVO userVO,String systemCode) throws Exception {
		JobsUserQuery jobsUserQuery = new JobsUserQuery();
		jobsUserQuery.setSystemId(userVO.getSystemId());
		jobsUserQuery.setUserCode(userVO.getUserCode());
		String json = null;
		/*String json = JedisUtil.getInstance().hget(systemCode+":"+userVO.getUserCode(),"userVO");*/
		if (json==null) {
			List<Integer> jobIds = new ArrayList<Integer>();
			List<JobsUser> jobsUsers = jobsUserService.findjobsUserByUserCodeAndSystemId(jobsUserQuery);
			for (JobsUser jobsUser : jobsUsers) {
				if (jobsUser.getStatus()==0) {
					jobIds.add(jobsUser.getJobsId());
				}
			}
			
			userVO.setJobsIds(jobIds);
/*			JedisUtil.getInstance().hdel(systemCode+":"+userVO.getUserCode(),"userVO");
			JedisUtil.getInstance().hset(systemCode+":"+userVO.getUserCode(), "userVO", JSON.toJSONString(userVO));*/
		}else {
			UserVO userVOT = JSON.parseObject(json, UserVO.class);
			userVO.setJobsIds(userVOT.getJobsIds());
		}
	
	}
}
