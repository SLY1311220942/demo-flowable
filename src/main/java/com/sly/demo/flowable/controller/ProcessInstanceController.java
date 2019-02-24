package com.sly.demo.flowable.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sly.demo.flowable.exception.CustomerException;
import com.sly.demo.flowable.model.ProcessInstanceView;

/**
 * 流程实例controller
 * @author sly
 * @time 2019年2月11日
 */
@Controller
@RequestMapping("/proIns")
public class ProcessInstanceController {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;
	
	
	/**
	 * 去流程实例列表页面
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@RequestMapping("/toProInsList")
	public String toProInsList(HttpServletRequest request,HttpServletResponse response) {
		return "/proInsList.html";
	}
	
	/**
	 * 获取流程实例列表
	 * @param request
	 * @param response
	 * @param userId
	 * @param proDefName
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/proInsList")
	public Object proInsList(HttpServletRequest request,HttpServletResponse response,String userId,String proDefName) {
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
		if(StringUtils.isNotBlank(userId)) {
			processInstanceQuery.startedBy(userId);
		}
		if(StringUtils.isNotBlank(proDefName)) {
			processInstanceQuery.processInstanceNameLikeIgnoreCase("%" + proDefName + "%");
		}
		List<ProcessInstance> list = processInstanceQuery.list();
		List<ProcessInstanceView> processInstanceViews = new ArrayList<>();
		for (ProcessInstance processInstance : list) {
			ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) processInstance;
			processInstanceViews.add(new ProcessInstanceView(executionEntity));
		}
		return processInstanceViews;
	}
	
	/**
	 * 去发起流程页面
	 * @param request
	 * @param response
	 * @param proDefId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@RequestMapping("/toCreateProIns")
	public String toCreateProIns(HttpServletRequest request,HttpServletResponse response,String proDefId) {
		request.setAttribute("proDefId", proDefId);
		return "/createProIns.html";
	}
	
	/**
	 * 创建流程实例
	 * @param request
	 * @param response
	 * @param proDefId
	 * @param createUserId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/createProIns")
	public Object createProIns(HttpServletRequest request,HttpServletResponse response,String proDefId,String createUserId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(proDefId).singleResult();
			if(processDefinition == null) {
				result.put("status", 400);
				result.put("message", "该流程定义不存在!");
				return result;
			}
			
			//设置流程发起人
			identityService.setAuthenticatedUserId(createUserId);
			//开启流程
			runtimeService.createProcessInstanceBuilder().processDefinitionId(proDefId).businessKey(UUID.randomUUID().toString()).name(processDefinition.getName()).start();
			
			result.put("status", 200);
			result.put("message", "发起流程成功!");
			return result;
		} catch(FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该流程定义不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "发起流程失败!", e);
		}
	}
	
	/**
	 * 去终止流程实例页面
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@RequestMapping("/toTerminateProIns")
	public String toTerminateProIns(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		request.setAttribute("proInsId", proInsId);
		return "terminateProIns.html";
	}
	
	/**
	 * 手动结束流程实例
	 * @param request
	 * @param response
	 * @param proInsId
	 * @param deleteReason
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/terminateProIns")
	public Object terminateProIns(HttpServletRequest request,HttpServletResponse response,String proInsId,String deleteReason) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			runtimeService.deleteProcessInstance(proInsId, deleteReason);
			result.put("status", 200);
			result.put("message", "终止成功!");
			return result;
		} catch(FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该实例不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "终止流程实例失败!", e);
		}
		
	}
	
	/**
	 * 挂起流程实例
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/suspendProIns")
	public Object suspendProIns(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			runtimeService.suspendProcessInstanceById(proInsId);
			result.put("status", 200);
			result.put("message", "挂起成功!");
			return result;
		} catch(FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该实例不存在!", e);
		} catch(FlowableException e) {
			throw new CustomerException(400, "该实例已挂起!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "挂起失败!", e);
		}
	}
	
	/**
	 * 激活流程实例
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/activeProIns")
	public Object activeProIns(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			runtimeService.activateProcessInstanceById(proInsId);
			result.put("status", 200);
			result.put("message", "激活成功!");
			return result;
		} catch(FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该实例不存在!", e);
		} catch(FlowableException e) {
			throw new CustomerException(400, "该实例已激活!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "激活失败!", e);
		}
	}
	
	/**
	 * 去流程实例状态图片页面
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@RequestMapping("/toProInsPic")
	public String toProInsPic(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		request.setAttribute("proInsId", proInsId);
		return "/proInsPic.html";
	}
	
	/**
	 * 获取流程实例状态图片
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @throws Exception
	 * @author sly
	 * @time 2019年2月13日
	 */
	@RequestMapping("/proInsPic")
	public String proInsPic(HttpServletRequest request,HttpServletResponse response,String proInsId) throws Exception {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(proInsId).singleResult();
		
        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
		//该流程未结束

		List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(proInsId).list();
		// 得到正在执行的Activity的Id
		List<String> activityIds = new ArrayList<String>();
		for (Execution exe : executions) {
			List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
			activityIds.addAll(ids);
		}
		
		DefaultProcessDiagramGenerator diagramGenerator = new DefaultProcessDiagramGenerator();
		
		InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "PNG", activityIds,Collections.<String> emptyList(),"宋体","宋体","宋体",null,1.0);

		byte[] b = new byte[1024 * 8];
		int length = 0;
		response.setHeader("Content-Type","image/jped");
		ServletOutputStream outputStream = response.getOutputStream();
		while((length=inputStream.read(b)) > 0){
			outputStream.write(b, 0, length);
		}
		return null;
	}
	
	/**
	 * 去已完成的流程实例页面
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@RequestMapping("/toProInsHisList")
	public String toProInsHisList(HttpServletRequest request,HttpServletResponse response) {
		return "proInsHisList.html";
	}
	
	/**
	 * 查询已完成的流程实例
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@ResponseBody
	@RequestMapping("/proInsHisList")
	public Object proInsHisList(HttpServletRequest request,HttpServletResponse response) {
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		List<HistoricProcessInstance> list = historicProcessInstanceQuery.finished().orderByProcessInstanceEndTime().desc().list();
		List<ProcessInstanceView> processInstanceViews = new ArrayList<>();
		for (HistoricProcessInstance historicProcessInstance : list) {
			processInstanceViews.add(new ProcessInstanceView((HistoricProcessInstanceEntityImpl)historicProcessInstance));
		}
		return processInstanceViews;
	}
}
