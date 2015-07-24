package com.v4java.workflow.constat;

import java.io.Serializable;

/**
 * 请求更新或者新增一条审批流返回对象
 * @author vincent
 *
 */
public class WorkFLowMsgConst implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5841547140667620228L;
	
	private int isSuccess;

	private String msg;
	
	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

}
