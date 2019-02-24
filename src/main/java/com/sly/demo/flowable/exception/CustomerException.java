package com.sly.demo.flowable.exception;

/**
 * 自定义异常
 * 
 * @author sly
 * @time 2019年2月13日
 */
public class CustomerException extends RuntimeException {

	private static final long serialVersionUID = -246589390661288574L;

	private Integer customStatus;
	private String customMessage;

	public CustomerException() {

	}

	public CustomerException(int status, String message, Throwable e) {
		super(e);
		this.customStatus = status;
		this.customMessage = message;
	}

	public Integer getCustomStatus() {
		return customStatus;
	}

	public void setCustomStatus(Integer customStatus) {
		this.customStatus = customStatus;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

}
