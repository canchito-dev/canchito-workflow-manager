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
package com.canchitodev.cwm.threadpool.service;

import java.util.List;

import org.springframework.context.annotation.DependsOn;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.repository.GenericTaskRepository;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
@DependsOn("applicationContextProvider")
public class GenericTaskService {
	
	private static final Logger logger = Logger.getLogger(GenericTaskService.class);
	
	private Object lock = new Object();
	
	@Autowired
	private GenericTaskRepository genericTaskRepository;
	
	public GenericTaskService() {}
	
	public void save(GenericTaskEntity task) {
		synchronized (lock) {
			this.genericTaskRepository.save(task);
		}
	}
	
	public void delete(GenericTaskEntity task) {
		synchronized (lock) {
			this.genericTaskRepository.delete(task);
			logger.info("Delete task " + task.toString());
		}
	}
	
	public GenericTaskEntity findByProcessDefinitionIdAndProcessInstanceIdAndExecutionId(String processDefinitionId, String processInstanceId, String executionId) {
		return this.genericTaskRepository.findByProcessDefinitionIdAndProcessInstanceIdAndExecutionId(processDefinitionId, processInstanceId, executionId);
	}
	
	public GenericTaskEntity findByUuid(String uuid) {
		return this.genericTaskRepository.findByUuid(uuid);
	}
	
	public List<GenericTaskEntity> findAll() {
		return this.genericTaskRepository.findAll();
	}
	
	public GenericTaskEntity findByExecutionId(String executionId) {
		return this.genericTaskRepository.findByExecutionId(executionId);
	}
}