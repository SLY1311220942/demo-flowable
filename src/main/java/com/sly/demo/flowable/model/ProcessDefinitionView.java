package com.sly.demo.flowable.model;

import java.io.Serializable;

import org.flowable.engine.repository.ProcessDefinition;

/**
 * 流程定义视图模型
 * 
 * @author sly
 * @time 2019年2月11日
 */
public class ProcessDefinitionView implements Serializable {

	private static final long serialVersionUID = -3241348460673449097L;

	private String id;
	private String name;
	private String key;
	private Integer version;

	public ProcessDefinitionView() {

	}

	public ProcessDefinitionView(ProcessDefinition processDefinition) {
		this.id = processDefinition.getId();
		this.name = processDefinition.getName();
		this.key = processDefinition.getKey();
		this.version = processDefinition.getVersion();
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
