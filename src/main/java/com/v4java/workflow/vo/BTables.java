package com.v4java.workflow.vo;

import java.io.Serializable;
import java.util.List;

public class BTables<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2123173530953865697L;
	private Integer total;
	private List<T> rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
