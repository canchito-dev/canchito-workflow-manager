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

@ConfigurationProperties(prefix="flowable.datasource")
public class FlowableDataSourceProperties {
	
	private String url = "";
	private String username = "";
	private String password = "";
	private String driverClassName = "";
	private Integer jdbcMaxWaitTime = 20000;
	private Integer jdbcMaxCheckoutTime = 20000;
	private Integer jdbcMaxIdleConnections = 10;
	private Integer jdbcMaxActiveConnections = 10;
	private Boolean dbEnableEventLogging = true;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	
	public Integer getJdbcMaxWaitTime() {
		return jdbcMaxWaitTime;
	}
	public void setJdbcMaxWaitTime(Integer jdbcMaxWaitTime) {
		this.jdbcMaxWaitTime = jdbcMaxWaitTime;
	}
	
	public Integer getJdbcMaxCheckoutTime() {
		return jdbcMaxCheckoutTime;
	}
	public void setJdbcMaxCheckoutTime(Integer jdbcMaxCheckoutTime) {
		this.jdbcMaxCheckoutTime = jdbcMaxCheckoutTime;
	}
	
	public Integer getJdbcMaxIdleConnections() {
		return jdbcMaxIdleConnections;
	}
	public void setJdbcMaxIdleConnections(Integer jdbcMaxIdleConnections) {
		this.jdbcMaxIdleConnections = jdbcMaxIdleConnections;
	}
	
	public Integer getJdbcMaxActiveConnections() {
		return jdbcMaxActiveConnections;
	}
	public void setJdbcMaxActiveConnections(Integer jdbcMaxActiveConnections) {
		this.jdbcMaxActiveConnections = jdbcMaxActiveConnections;
	}
	
	public Boolean getDbEnableEventLogging() {
		return dbEnableEventLogging;
	}
	public void setDbEnableEventLogging(Boolean dbEnableEventLogging) {
		this.dbEnableEventLogging = dbEnableEventLogging;
	}
	
	@Override
	public String toString() {
		return "FlowableDataSourceProperties [url=" + url + ", username=" + username + ", password=" + password
				+ ", driverClassName=" + driverClassName + ", jdbcMaxWaitTime=" + jdbcMaxWaitTime
				+ ", jdbcMaxCheckoutTime=" + jdbcMaxCheckoutTime + ", jdbcMaxIdleConnections=" + jdbcMaxIdleConnections
				+ ", jdbcMaxActiveConnections=" + jdbcMaxActiveConnections + ", dbEnableEventLogging="
				+ dbEnableEventLogging + "]";
	}
}