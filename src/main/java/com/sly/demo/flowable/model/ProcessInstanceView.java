package com.sly.demo.flowable.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;

/**
 * 流程实例视图模型
 * 
 * @author sly
 * @time 2019年2月11日
 */
public class ProcessInstanceView implements Serializable {

	private static final long serialVersionUID = 3428596192957531653L;

	private String id;
	private String name;
	private String startUserId;
	private String processDefinitionId;
	private String processDefinitionKey;
	private String processDefinitionName;
	private Integer processDefinitionVersion;
	private String businessKey;
	private Date startTime;
	private Date endTime;
	private int suspensionState;
	
	public ProcessInstanceView() {

	}

	public ProcessInstanceView(ExecutionEntityImpl processInstance) {
		this.id = processInstance.getId();
		this.name = processInstance.getName();
		this.startUserId = processInstance.getStartUserId();
		this.processDefinitionId = processInstance.getProcessDefinitionId();
		this.processDefinitionKey = processInstance.getProcessDefinitionKey();
		this.processDefinitionName = processInstance.getProcessDefinitionName();
		this.processDefinitionVersion = processInstance.getProcessDefinitionVersion();
		this.businessKey = processInstance.getBusinessKey();
		this.startTime = processInstance.getStartTime();
		this.suspensionState = processInstance.getSuspensionState();
	}

	public ProcessInstanceView(HistoricProcessInstanceEntityImpl historicProcessInstance) {
		this.id = historicProcessInstance.getId();
		this.name = historicProcessInstance.getName();
		this.startUserId = historicProcessInstance.getStartUserId();
		this.processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		this.processDefinitionKey = historicProcessInstance.getProcessDefinitionKey();
		this.processDefinitionName = historicProcessInstance.getProcessDefinitionName();
		this.processDefinitionVersion = historicProcessInstance.getProcessDefinitionVersion();
		this.businessKey = historicProcessInstance.getBusinessKey();
		this.startTime = historicProcessInstance.getStartTime();
		this.endTime = historicProcessInstance.getEndTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartUserId() {
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public Integer getProcessDefinitionVersion() {
		return processDefinitionVersion;
	}

	public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
		this.processDefinitionVersion = processDefinitionVersion;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public Date getStartTime() {
		return startTime;
	}
	
	public String getStartTimeStr() {
		if(startTime != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(startTime);
		}
		return "";
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}
	public String getEndTimeStr() {
		if(endTime != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(endTime);
		}
		return "";
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getSuspensionState() {
		return suspensionState;
	}
	
	public String getSuspensionStateStr() {
		if(suspensionState == 1) {
			return "激活";
		}else if(suspensionState == 2) {
			return "挂起";
		}else {
			return suspensionState + "";
		}
	}

	public void setSuspensionState(int suspensionState) {
		this.suspensionState = suspensionState;
	}
	
	
}
