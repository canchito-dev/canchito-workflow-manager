/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2017, canchito-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author 		José Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
package com.canchitodev.cwm;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.canchitodev.cwm.domain.FileHandler;
import com.canchitodev.cwm.services.FolderHandlerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CanchitoWorkfowManagerApplicationTests {
	
	private static String DEFAULT_TENANT_ID = "canchito-dev.com";

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private FolderHandlerService folderHandlerService;
	
	private void createDeployment(String deploymentName, String tenantId, String category, String key, String resourceName, String classPathResource) throws FileNotFoundException, IOException {
		this.repositoryService.createDeployment()
			.name(deploymentName)
			.tenantId(tenantId)
			.category(category)
			.key(key)
			.addInputStream(
					resourceName,
					new FileInputStream(new ClassPathResource(classPathResource).getFile()))
			.deploy();
	}
	
	private void startProcessInstanceByKeyAndTenantId(String processDefinitionKey, Map<String, Object> variables, String tenantId) {
		ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, variables, tenantId);
		
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
	
	@SuppressWarnings("static-access")
	private void sleepThread() {
		try {
			Thread.currentThread().sleep(50000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore("Status... Passed")
	public void startLocalToFtoToLocalProcess() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_LOCAL_TO_FTP_TO_LOCAL", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_LOCAL_TO_FTP_TO_LOCAL", "copyLocalToFtoToLocalProcess.bpmn20.xml", "/processes/CopyTask/CopyLocalToFtpToLocal.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"localToFtp.MPG",
			this.folderHandlerService.findByNameAndTenantId("LOCAL", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyLocalToFtoToLocalProcess", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}

	@Test
	@Ignore("Status... Passed")
	public void startLocalToAmazonS3ToLocalProcess() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_LOCAL_TO_AMAZON_S3_TO_LOCAL", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_LOCAL_TO_AMAZON_S3_TO_LOCAL", "copyLocalToAmazonS3ToLocal.bpmn20.xml", "/processes/CopyTask/CopyLocalToAmazonS3ToLocal.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"localToAmazonS3.MPG",
			this.folderHandlerService.findByNameAndTenantId("LOCAL", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyLocalToAmazonS3ToLocalProcess", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
	
	@Test
	@Ignore("Status... Passed")
	public void startLocalToLocalProcess() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_LOCAL_TO_LOCAL", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_LOCAL_TO_LOCAL", "copyLocalToLocalProcess.bpmn20.xml", "/processes/CopyTask/CopyLocalToLocal.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"localToLocal1.mpg",
			this.folderHandlerService.findByNameAndTenantId("LOCAL", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyLocalToLocalProcess", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
	
	@Test
	@Ignore("Status... Passed")
	public void startFtpToFtpProcess() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_FTP_TO_FTP", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_FTP_TO_FTP", "ftpToFtpProcess.bpmn20.xml", "/processes/CopyTask/CopyFtpToFtp.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"ftpToLocal.MPG",
			this.folderHandlerService.findByNameAndTenantId("FTP", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyFtpToFtpProcess", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
	
	@Test
	@Ignore("Status... Passed")
	public void startFtpToAmazonS3Process() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_FTP_TO_AMAZON_S3", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_FTP_TO_AMAZON_S3", "ftpToAmazonS3Process.bpmn20.xml", "/processes/CopyTask/CopyFtpToAmazonS3.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"ftpToLocal.MPG",
			this.folderHandlerService.findByNameAndTenantId("FTP", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyFtpToAmazonS3Process", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
	
	@Test
	@Ignore("Status... Passed")
	public void startAmazonS3ToAmazonS3Process() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
		this.createDeployment("TEST_COPY_AMAZON_S3_TO_AMAZON_S3", DEFAULT_TENANT_ID, "TEST", "TEST_COPY_AMAZON_S3_TO_AMAZON_S3", "amazonS3ToAmazonS3Process.bpmn20.xml", "/processes/CopyTask/CopyAmazonS3ToAmazonS3.bpmn");
		
		// Set source file handler
		FileHandler sfh = new FileHandler(
			"Original",
			"localToAmazonS3.MPG",
			this.folderHandlerService.findByNameAndTenantId("AMAZON_S3", Long.parseLong(DEFAULT_TENANT_ID))
		);
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put(sfh.getName(), sfh);
		
		this.startProcessInstanceByKeyAndTenantId("copyAmazonS3ToAmazonS3Process", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
	
	@Test
//	@Ignore("Status... Passed")
	public void startMultipleProcesses() throws Exception {
		// Uncomment this line if you want to manually deploy a process definition and assigned to a specific tenant id
		// Make sure you also have the property spring.activiti.check-process-definitions=false in the application.properties file
//		this.createDeployment("TEST_DUMMY_RUNNABLE_TASKS", DEFAULT_TENANT_ID, "TEST", "TEST_DUMMY_RUNNABLE_TASKS", "testTaskProcess.bpmn20.xml", "/processes/TestTaskProcess.bpmn");
		
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("priority", 10);
		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 15);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
//		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 20);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
//		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 13);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
//		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 14);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
//		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 19);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
//		
//		variableMap = new HashMap<String, Object>();
//		variableMap.put("priority", 21);
//		this.startProcessInstanceByKeyAndTenantId("testTaskProcess", variableMap, DEFAULT_TENANT_ID);
		
		this.sleepThread();
	}
}