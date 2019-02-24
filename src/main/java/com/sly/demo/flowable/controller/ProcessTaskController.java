package com.sly.demo.flowable.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.api.FlowableTaskAlreadyClaimedException;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.task.service.impl.persistence.entity.HistoricTaskInstanceEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sly.demo.flowable.exception.CustomerException;
import com.sly.demo.flowable.model.ProcessTaskView;

/**
 * 流程任务controller
 * @author sly
 * @time 2019年2月11日
 */
@Controller
@RequestMapping("/proTask")
public class ProcessTaskController {
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 去用户任务页面
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toUserTaskList")
	public String toUserTaskList(HttpServletRequest request,HttpServletResponse response) {
		return "/userTaskList.html";
	}
	
	/**
	 * 获取用户任务列表
	 * @param request
	 * @param response
	 * @param userId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/userTaskList")
	public Object userTaskList(HttpServletRequest request,HttpServletResponse response,String userId) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		if (StringUtils.isNotBlank(userId)) {
			taskQuery.taskAssignee(userId);
		}
		List<Task> list = taskQuery.list();
		List<ProcessTaskView> processTaskViews = new ArrayList<>();
		for (Task task : list) {
			processTaskViews.add(new ProcessTaskView(task));
		}
		return processTaskViews;
	}
	
	/**
	 * 去组任务页面
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toGroupTaskList")
	public String toGroupTaskList(HttpServletRequest request,HttpServletResponse response) {
		return "/groupTaskList.html";
	}
	
	/**
	 * 获取组任务列表
	 * @param request
	 * @param response
	 * @param groupId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/groupTaskList")
	public Object groupTaskList(HttpServletRequest request,HttpServletResponse response,String groupId) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		if(StringUtils.isNotBlank(groupId)) {
			taskQuery.taskCandidateGroup(groupId);
		}
		List<Task> list = taskQuery.list();
		List<ProcessTaskView> processTaskViews = new ArrayList<>();
		for (Task task : list) {
			processTaskViews.add(new ProcessTaskView(task));
		}
		return processTaskViews;
	}
	
	/**
	 * 去领取组任务页面
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toClaimGroupTask")
	public String toClaimGroupTask(HttpServletRequest request,HttpServletResponse response,String taskId) {
		request.setAttribute("taskId", taskId);
		return "/claimGroupTask.html";
	}
	
	/**
	 * 领取组任务
	 * @param request
	 * @param response
	 * @param taskId
	 * @param userId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/claimGroupTask")
	public Object claimGroupTask(HttpServletRequest request,HttpServletResponse response,String taskId,String userId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			taskService.claim(taskId, userId);
			result.put("status", 200);
			result.put("message", "领取成功!");
			return result;
		} catch (FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该任务不存在!", e);
		} catch (FlowableTaskAlreadyClaimedException e) {
			throw new CustomerException(400, "该任务已被领取!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "领取任务失败!", e);
		}
	}
	
	
	/**
	 * 退回任务
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@ResponseBody
	@RequestMapping("/unclaimTask")
	public Object unclaimTask(HttpServletRequest request,HttpServletResponse response,String taskId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			taskService.unclaim(taskId);
			result.put("status", 200);
			result.put("message", "退回成功!");
			return result;
		} catch (FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该任务不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "退回任务失败!", e);
		}
	}
	
	/**
	 * 去完成任务页面
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toCompleteTask")
	public String toCompleteTask(HttpServletRequest request,HttpServletResponse response,String taskId) {
		request.setAttribute("taskId", taskId);
		return "/completeTask.html";
	}
	
	/**
	 * 完成任务
	 * @param request
	 * @param response
	 * @param taskId
	 * @param auditResult
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/completeTask")
	public Object completeTask(HttpServletRequest request,HttpServletResponse response,String taskId,Integer auditResult) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			Map<String, Object> variables = new HashMap<>(16);
			variables.put("result", auditResult);
			taskService.complete(taskId, variables);
			result.put("status", 200);
			result.put("message", "提交成功!");
			return result;
		} catch (FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该任务不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "提交任务失败!", e);
		}
		
	}
	
	/**
	 * 去任务指派组页面
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toAssignGroup")
	public String toAssignGroup(HttpServletRequest request,HttpServletResponse response,String taskId) {
		request.setAttribute("taskId", taskId);
		return "/assignGroup.html";
	}
	
	/**
	 * 给任务指派组
	 * @param request
	 * @param response
	 * @param taskId
	 * @param groupId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/assignGroup")
	public Object assignGroup(HttpServletRequest request,HttpServletResponse response,String taskId,String groupId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			taskService.addCandidateGroup(taskId, groupId);
			result.put("status", 200);
			result.put("message", "指派成功!");
			return result;
		} catch (FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该任务不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "指派任务失败!", e);
		}
	}
	
	/**
	 * 去给任务指派人页面
	 * @param request
	 * @param response
	 * @param taskId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@RequestMapping("/toAssignUser")
	public String toAssignUser(HttpServletRequest request,HttpServletResponse response,String taskId) {
		request.setAttribute("taskId", taskId);
		return "/assignUser.html";
	}
	
	/**
	 * 给任务指派执行人
	 * @param request
	 * @param response
	 * @param taskId
	 * @param userId
	 * @return
	 * @author sly
	 * @time 2019年2月12日
	 */
	@ResponseBody
	@RequestMapping("/assignUser")
	public Object assignUser(HttpServletRequest request,HttpServletResponse response,String taskId,String userId) {
		Map<String, Object> result = new HashMap<>(16);
		try {
			taskService.setAssignee(taskId, userId);
			result.put("status", 200);
			result.put("message", "指派成功!");
			return result;
		} catch (FlowableObjectNotFoundException e) {
			throw new CustomerException(400, "该任务不存在!", e);
		} catch (Exception e) {
			throw new CustomerException(400, "指派任务失败!", e);
		}
	}
	
	/**
	 * 去流程实例历史任务页面
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@RequestMapping("/toProInsHisTaskList")
	public String toProInsHisTaskList(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		request.setAttribute("proInsId", proInsId);
		return "/proInsHisTaskList.html";
	}
	
	/**
	 * 流程实例历史任务列表
	 * @param request
	 * @param response
	 * @param proInsId
	 * @return
	 * @author sly
	 * @time 2019年2月13日
	 */
	@ResponseBody
	@RequestMapping("/proInsHisTaskList")
	public Object proInsHisTaskList(HttpServletRequest request,HttpServletResponse response,String proInsId) {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		List<HistoricTaskInstance> list = historicTaskInstanceQuery.finished().processInstanceId(proInsId).orderByTaskCreateTime().desc().list();
		List<ProcessTaskView> processTaskViews = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : list) {
			processTaskViews.add(new ProcessTaskView((HistoricTaskInstanceEntityImpl)historicTaskInstance));
		}
		return processTaskViews;
	}
}
