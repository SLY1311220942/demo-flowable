package com.sly.demo.flowable.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntityImpl;


/**
 * 流程任务视图模型
 * @author sly
 * @time 2019年2月11日
 */
public class ProcessTaskView implements Serializable{

	private static final long serialVersionUID = -8611366864869239567L;
	
	private String id;
	private String name;
	private String assignee;
	private String taskDefinitionId;
	private String taskDefinitionKey;
	private String processInstanceId;
	private String processDefinitionId;
	private Date createTime;
	private String deleteReason;
	
	public ProcessTaskView() {
	
	}

	public ProcessTaskView(Task task) {
		this.id = task.getId();
		this.name = task.getName();
		this.assignee = task.getAssignee();
		this.taskDefinitionId = task.getTaskDefinitionId();
		this.taskDefinitionKey = task.getTaskDefinitionKey();
		this.processInstanceId = task.getProcessInstanceId();
		this.processDefinitionId = task.getProcessDefinitionId();
		this.createTime = task.getCreateTime();
	}

	public ProcessTaskView(HistoricTaskInstanceEntityImpl historicTaskInstance) {
		this.id = historicTaskInstance.getId();
		this.name = historicTaskInstance.getName();
		this.assignee = historicTaskInstance.getAssignee();
		this.taskDefinitionId = historicTaskInstance.getTaskDefinitionId();
		this.taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
		this.processInstanceId = historicTaskInstance.getProcessInstanceId();
		this.processDefinitionId = historicTaskInstance.getProcessDefinitionId();
		this.createTime = historicTaskInstance.getCreateTime();
		this.deleteReason = historicTaskInstance.getDeleteReason();
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

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTaskDefinitionId() {
		return taskDefinitionId;
	}

	public void setTaskDefinitionId(String taskDefinitionId) {
		this.taskDefinitionId = taskDefinitionId;
	}

	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Date getCreateTime() {
		return createTime;
	}
	
	public String getCreateTimeStr() {
		if(createTime != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.format(createTime);
		}
		return "";
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}
	
}
