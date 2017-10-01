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
package com.canchitodev.cwm.threadpool.runnable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.exception.GenericException;

@Component
@Scope("prototype")
public class GenericTaskRunnable implements Comparable<Runnable>, Runnable {
	
	@Autowired
	private TaskRunnableFactory taskRunnableFactory;
	
	private GenericTaskEntity task;

	@Override
	public void run() {
		TaskRunnable taskRunnable = this.taskRunnableFactory.getRunnable(task.getBeanId());
		
		/**
		 * TODO: If the exception is just thrown, Activiti execution of the process instance is not stopped. Neither the registry in the queue's database table.
		 * So, there are two options that can be followed:
		 * 1 - Remove the registry from the queue's table and propagate the exception to Activiti, so that the process instance is stopped and finishes in error; or
		 * 2 - Move the queue registry to another table, include the exception, and let a system administrator decide how to proceed 
		 **/
		if(taskRunnable == null)
			throw new GenericException("No valid runnable found. Task: " + task.toString());
		
		taskRunnable.setTask(task);
		
		taskRunnable.execute();
	}

	public GenericTaskEntity getTask() {
		return task;
	}

	public void setTask(GenericTaskEntity task) {
		this.task = task;
	}

	@Override
	public String toString() {
		return "GenericTaskRunnable [task=" + task.toString() + "]";
	}

	/**
	 * Tasks are ordered from the highest to the lowest priority
	 **/
	@Override
	public int compareTo(Runnable arg0) {
		GenericTaskRunnable runnable = (GenericTaskRunnable) arg0;
		return runnable.getTask().getPriority() - this.getTask().getPriority(); 
	}
}