package com.sly.demo.flowable.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sly.demo.flowable.model.ProcessDefinitionView;

/**
 * 流程定义controller
 * @author sly
 * @time 2019年2月11日
 */
@Controller
@RequestMapping("/proDef")
public class ProcessDefinitionController {
	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping("/toProDefList")
	public String toProDefList(HttpServletRequest request,HttpServletResponse response) {
		return "/proDefList.html";
	}
	
	/**
	 * 获取流程定义列表
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/proDefList")
	public Object proDefList(HttpServletRequest request,HttpServletResponse response) {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		List<ProcessDefinitionView> definitionViews = new ArrayList<>();
		for (ProcessDefinition processDefinition : list) {
			definitionViews.add(new ProcessDefinitionView(processDefinition));
		}
		return definitionViews;
	}
	
	/**
	 * 去部署流程定义页面
	 * @param request
	 * @param response
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toDeployProDef")
	public String toDeployProDef(HttpServletRequest request,HttpServletResponse response) {
		return "/deployProDef.html";
	}
	
	/**
	 * 部署流程定义
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 * @author sly
	 * @throws Exception 
	 * @time 2019年2月11日
	 */
	@ResponseBody
	@RequestMapping("/deployProDef")
	public Object deployProDef(HttpServletRequest request,HttpServletResponse response,@RequestPart(value = "file") MultipartFile file,String name) throws Exception {
		Map<String, Object> result = new HashMap<>(16);
		ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
		Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).name(name).deploy();
		System.out.println("部署ID:" + deployment.getId());
		
		return result;
	}
	
	/**
	 * 去流程图片页面
	 * @param request
	 * @param response
	 * @param proDefId
	 * @return
	 * @author sly
	 * @time 2019年2月11日
	 */
	@RequestMapping("/toProDefPic")
	public String toProDefPic(HttpServletRequest request,HttpServletResponse response,String proDefId) {
		request.setAttribute("proDefId", proDefId);
		return "proDefPic.html";
	}
	
	/**
	 * 获取流程定义图片
	 * @param request
	 * @param response
	 * @param proDefId
	 * @author sly
	 * @throws Exception 
	 * @time 2019年2月11日
	 */
	@RequestMapping("/proDefPic")
	public void proDefPic(HttpServletRequest request,HttpServletResponse response,String proDefId) throws Exception {
		InputStream inputStream = repositoryService.getProcessDiagram(proDefId);
		byte[] b = new byte[1024 * 8];
		int length = 0;
		response.setHeader("Content-Type","image/jped");
		ServletOutputStream outputStream = response.getOutputStream();
		while((length=inputStream.read(b)) > 0){
			outputStream.write(b, 0, length);
		}
	}
}
