package com.v4java.workflow.query;


public class ApproveLogQuery extends Query {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4284532457277822830L;
	private Integer workflowId;
	private String systemCode;

	public Integer getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(Integer workflowId) {
		this.workflowId = workflowId;
	}

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

}
