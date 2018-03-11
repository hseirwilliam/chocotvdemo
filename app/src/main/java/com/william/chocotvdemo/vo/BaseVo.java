package com.william.chocotvdemo.vo;

import java.io.Serializable;

public class BaseVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 流程識別碼 (系統會注入)
	 */
	private String processId;

	public final String getProcessId() {
		return this.processId;
	}
	/* fix findbugs: add this */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

}
