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
package com.canchitodev.cwm.configuration.properties;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix="cwm.queues")
public class QueueConfigurationProperties {

	@NotNull
	private List<Queue> queue = new ArrayList<Queue>();
	
	public static class Queue {

		@NotEmpty
		private String poolName = "notInitName";
		
		@NotEmpty
		private int corePoolSize = 1;
		
		@NotEmpty
		private int maximumPoolSize = 1;
		
		@NotEmpty
		private long keepAliveTime = 1000;
		
		@NotEmpty
		private String beanId = "notInitBeanId";

		public String getPoolName() {
			return poolName;
		}
		public void setPoolName(String poolName) {
			this.poolName = poolName.toLowerCase();
		}
		public int getCorePoolSize() {
			return corePoolSize;
		}
		public void setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
		}
		public int getMaximumPoolSize() {
			return maximumPoolSize;
		}
		public void setMaximumPoolSize(int maximumPoolSize) {
			this.maximumPoolSize = maximumPoolSize;
		}
		public long getKeepAliveTime() {
			return keepAliveTime;
		}
		public void setKeepAliveTime(long keepAliveTime) {
			this.keepAliveTime = keepAliveTime;
		}
		public String getBeanId() {
			return beanId;
		}
		public void setBeanId(String beanId) {
			this.beanId = beanId;
		}
		
		@Override
		public String toString() {
			return "Queue [poolName=" + poolName 
					+ ", corePoolSize=" + corePoolSize 
					+ ", maximumPoolSize=" + maximumPoolSize
					+ ", keepAliveTime=" + keepAliveTime
					+ ", beanId=" + beanId + "]";
		}
	}

	public List<Queue> getQueue() {
		return queue;
	}

	public void setQueues(List<Queue> queue) {
		this.queue = queue;
	}
	
	@Override
	public String toString() {
		return "QueueConfigurationProperties [queues=" + queue.toString() + "]";
	}
}