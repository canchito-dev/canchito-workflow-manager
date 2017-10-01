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
package com.canchitodev.cwm.flowable.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="flowable.async-executor")
public class FlowableAsycExecutorProperties {

	private Integer threadPoolQueueSize = 100;
	private Integer corePoolSize = 2;
	private Integer maxPoolSize = 10;
	private Long threadKeepAliveTime = 5000L;
	private Integer numberOfRetries = 3;
	private Integer maxTimerJobsPerAcquisition = 1;
	private Integer maxAsyncJobsDuePerAcquisition = 1;
	private Integer defaultTimerJobAcquireWaitTime = 10 * 1000;
	private Integer defaultAsyncJobAcquireWaitTime = 10 * 1000;
	private Integer defaultQueueSizeFullWaitTime = 0;
	private Integer timerLockTimeInMillis = 5 * 60 * 1000;
	private Integer asyncJobLockTimeInMillis = 5 * 60 * 1000;;
	private Long secondsToWaitOnShutdown = 60L;
	private Integer resetExpiredJobsInterval = 60 * 1000;
	private Integer resetExpiredJobsPageSize = 3;

	public Integer getThreadPoolQueueSize() {
		return threadPoolQueueSize;
	}
	public void setThreadPoolQueueSize(Integer threadPoolQueueSize) {
		this.threadPoolQueueSize = threadPoolQueueSize;
	}
	
	public Integer getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(Integer corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	
	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	
	public Long getThreadKeepAliveTime() {
		return threadKeepAliveTime;
	}
	public void setThreadKeepAliveTime(Long threadKeepAliveTime) {
		this.threadKeepAliveTime = threadKeepAliveTime;
	}
	
	public Integer getNumberOfRetries() {
		return numberOfRetries;
	}
	public void setNumberOfRetries(Integer numberOfRetries) {
		this.numberOfRetries = numberOfRetries;
	}
	
	public Integer getMaxTimerJobsPerAcquisition() {
		return maxTimerJobsPerAcquisition;
	}
	public void setMaxTimerJobsPerAcquisition(Integer maxTimerJobsPerAcquisition) {
		this.maxTimerJobsPerAcquisition = maxTimerJobsPerAcquisition;
	}
	
	public Integer getMaxAsyncJobsDuePerAcquisition() {
		return maxAsyncJobsDuePerAcquisition;
	}
	public void setMaxAsyncJobsDuePerAcquisition(Integer maxAsyncJobsDuePerAcquisition) {
		this.maxAsyncJobsDuePerAcquisition = maxAsyncJobsDuePerAcquisition;
	}
	
	public Integer getDefaultTimerJobAcquireWaitTime() {
		return defaultTimerJobAcquireWaitTime;
	}
	public void setDefaultTimerJobAcquireWaitTime(Integer defaultTimerJobAcquireWaitTime) {
		this.defaultTimerJobAcquireWaitTime = defaultTimerJobAcquireWaitTime;
	}
	
	public Integer getDefaultAsyncJobAcquireWaitTime() {
		return defaultAsyncJobAcquireWaitTime;
	}
	public void setDefaultAsyncJobAcquireWaitTime(Integer defaultAsyncJobAcquireWaitTime) {
		this.defaultAsyncJobAcquireWaitTime = defaultAsyncJobAcquireWaitTime;
	}
	
	public Integer getDefaultQueueSizeFullWaitTime() {
		return defaultQueueSizeFullWaitTime;
	}
	public void setDefaultQueueSizeFullWaitTime(Integer defaultQueueSizeFullWaitTime) {
		this.defaultQueueSizeFullWaitTime = defaultQueueSizeFullWaitTime;
	}
	
	public Integer getTimerLockTimeInMillis() {
		return timerLockTimeInMillis;
	}
	public void setTimerLockTimeInMillis(Integer timerLockTimeInMillis) {
		this.timerLockTimeInMillis = timerLockTimeInMillis;
	}
	
	public Integer getAsyncJobLockTimeInMillis() {
		return asyncJobLockTimeInMillis;
	}
	public void setAsyncJobLockTimeInMillis(Integer asyncJobLockTimeInMillis) {
		this.asyncJobLockTimeInMillis = asyncJobLockTimeInMillis;
	}
	
	public Long getSecondsToWaitOnShutdown() {
		return secondsToWaitOnShutdown;
	}
	public void setSecondsToWaitOnShutdown(Long secondsToWaitOnShutdown) {
		this.secondsToWaitOnShutdown = secondsToWaitOnShutdown;
	}
	
	public Integer getResetExpiredJobsInterval() {
		return resetExpiredJobsInterval;
	}
	public void setResetExpiredJobsInterval(Integer resetExpiredJobsInterval) {
		this.resetExpiredJobsInterval = resetExpiredJobsInterval;
	}
	
	public Integer getResetExpiredJobsPageSize() {
		return resetExpiredJobsPageSize;
	}
	public void setResetExpiredJobsPageSize(Integer resetExpiredJobsPageSize) {
		this.resetExpiredJobsPageSize = resetExpiredJobsPageSize;
	}
	@Override
	public String toString() {
		return "FlowableAsycExecutorProperties [corePoolSize=" + corePoolSize + ", maxPoolSize=" + maxPoolSize
				+ ", threadKeepAliveTime=" + threadKeepAliveTime + ", numberOfRetries=" + numberOfRetries
				+ ", threadPoolQueueSize=" + threadPoolQueueSize + ", maxTimerJobsPerAcquisition="
				+ maxTimerJobsPerAcquisition + ", maxAsyncJobsDuePerAcquisition=" + maxAsyncJobsDuePerAcquisition
				+ ", defaultTimerJobAcquireWaitTime=" + defaultTimerJobAcquireWaitTime
				+ ", defaultAsyncJobAcquireWaitTime=" + defaultAsyncJobAcquireWaitTime
				+ ", defaultQueueSizeFullWaitTime=" + defaultQueueSizeFullWaitTime + ", timerLockTimeInMillis="
				+ timerLockTimeInMillis + ", asyncJobLockTimeInMillis=" + asyncJobLockTimeInMillis
				+ ", secondsToWaitOnShutdown=" + secondsToWaitOnShutdown + ", resetExpiredJobsInterval="
				+ resetExpiredJobsInterval + ", resetExpiredJobsPageSize=" + resetExpiredJobsPageSize + "]";
	}
}