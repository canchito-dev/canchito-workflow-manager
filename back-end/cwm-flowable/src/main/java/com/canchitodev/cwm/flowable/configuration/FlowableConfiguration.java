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
package com.canchitodev.cwm.flowable.configuration;

import java.util.Arrays;

import javax.sql.DataSource;

import org.flowable.engine.IdentityService;
import org.flowable.engine.common.api.delegate.event.FlowableEventListener;
import org.flowable.engine.common.impl.persistence.StrongUuidGenerator;
import org.flowable.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.User;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import com.canchitodev.cwm.flowable.configuration.properties.FlowableAsycExecutorProperties;
import com.canchitodev.cwm.flowable.configuration.properties.FlowableDataSourceProperties;
import com.canchitodev.cwm.flowable.eventhandlers.DefaultEventHandler;

@Configuration
@ConditionalOnClass(EmbeddedDatabaseType.class)
@EnableConfigurationProperties(value={
		FlowableAsycExecutorProperties.class, 
		FlowableDataSourceProperties.class
	})
public class FlowableConfiguration {
	
	@Autowired
	private DataSourceProperties properties;
	
	@Autowired
	private FlowableAsycExecutorProperties flowableAsycExecutorProperties;
	
	@Autowired
	private FlowableDataSourceProperties flowableDataSourceProperties;
	
	/**
	 * Activiti's REST API is secured by basic auth, and won’t have any users by default. Let’s add an admin user to the system as shown below. 
	 * Don’t do this in a production system of course, there you’ll want to hook in the authentication to LDAP or something else.
	 * https://spring.io/blog/2015/03/08/getting-started-with-activiti-and-spring-boot
	 * 
	 * If you are not using an in-memory database like H2, this bean is only required once. After the user and group have been created, you can
	 * comment this block
	 **/
	@Bean
	InitializingBean usersAndGroupsInitializer(final IdentityService identityService) {

	    return new InitializingBean() {
	        public void afterPropertiesSet() throws Exception {

	        	if(identityService.createGroupQuery().groupId("user").count() == 0) {
		            Group group = identityService.newGroup("user");
		            group.setName("users");
		            group.setType("security-role");
		            identityService.saveGroup(group);
	
		            User admin = identityService.newUser("admin");
		            admin.setPassword("admin");
		            identityService.saveUser(admin);
	        	}
	        }
	    };
	}
	
	/**
	 * Flowable DOCS (v 6.1.2)'s user guide - 5.7.3. Changing the database and connection pool
	 * http://www.flowable.org/docs/userguide/index.html#_changing_the_database_and_connection_pool
	 **/
	@Bean(name = "datasource.cwm")
	@ConfigurationProperties(prefix="cwm.datasource")
	public DataSource cwmDataSource() {
		return DataSourceBuilder.create(this.properties.getClassLoader())
				.url(this.properties.getUrl())
				.username(this.properties.getUsername())
				.password(this.properties.getPassword())
				.driverClassName(this.properties.getDriverClassName())
		        .build();
	}
	
	/**
	 * Exposing configuration beans with Spring Boot
	 * https://forums.activiti.org/content/exposing-configuration-beans-spring-boot
	 **/
	@Bean
	public BeanPostProcessor flowableSpringProcessEngineConfigurer() {
	    return new BeanPostProcessor() {

	    	@Override
	        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
	            if (bean instanceof SpringProcessEngineConfiguration) {
	            	// If it is the SpringProcessEngineConfiguration, we want to add or modify some configuration.
                    SpringProcessEngineConfiguration config = (SpringProcessEngineConfiguration) bean;
                    
                    /**
                     * Flowable DOCS (v 6.1.2)'s user guide - 18.3. UUID ID generator for high concurrency
                     * http://www.flowable.org/docs/userguide/index.html#advanced.uuid.generator
                     **/
                    config.setIdGenerator(new StrongUuidGenerator());
                    
                    /**
                     * Flowable DOCS (v 6.1.2)'s user guide - 3.3. Database configuration
                     * http://www.flowable.org/docs/userguide/index.html#databaseConfiguration
                     **/
                    config.setJdbcMaxActiveConnections(flowableDataSourceProperties.getJdbcMaxActiveConnections());
                    config.setJdbcMaxIdleConnections(flowableDataSourceProperties.getJdbcMaxIdleConnections());
                    config.setJdbcMaxCheckoutTime(flowableDataSourceProperties.getJdbcMaxCheckoutTime());
                    config.setJdbcMaxWaitTime(flowableDataSourceProperties.getJdbcMaxWaitTime());
                    config.setEnableDatabaseEventLogging(flowableDataSourceProperties.getDbEnableEventLogging());
                    
                    /**
                     * Flowable DOCS (v 6.1.2)'s user guide - 18.1.2. Async executor configuration
                     * http://www.flowable.org/docs/userguide/index.html#_async_executor_configuration
                     **/
                    DefaultAsyncJobExecutor asyncExecutor = new DefaultAsyncJobExecutor();
                    asyncExecutor.setQueueSize(flowableAsycExecutorProperties.getThreadPoolQueueSize());
                    asyncExecutor.setCorePoolSize(flowableAsycExecutorProperties.getCorePoolSize());
                    asyncExecutor.setMaxPoolSize(flowableAsycExecutorProperties.getMaxPoolSize());
                    asyncExecutor.setKeepAliveTime(flowableAsycExecutorProperties.getThreadKeepAliveTime());
                    asyncExecutor.setMaxTimerJobsPerAcquisition(flowableAsycExecutorProperties.getMaxTimerJobsPerAcquisition());
                    asyncExecutor.setMaxAsyncJobsDuePerAcquisition(flowableAsycExecutorProperties.getMaxAsyncJobsDuePerAcquisition());
                    asyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(flowableAsycExecutorProperties.getDefaultTimerJobAcquireWaitTime());
                    asyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(flowableAsycExecutorProperties.getDefaultAsyncJobAcquireWaitTime());
                    asyncExecutor.setDefaultQueueSizeFullWaitTimeInMillis(flowableAsycExecutorProperties.getDefaultQueueSizeFullWaitTime());
                    asyncExecutor.setTimerLockTimeInMillis(flowableAsycExecutorProperties.getTimerLockTimeInMillis());                    
                    asyncExecutor.setAsyncJobLockTimeInMillis(flowableAsycExecutorProperties.getAsyncJobLockTimeInMillis());
                    asyncExecutor.setSecondsToWaitOnShutdown(flowableAsycExecutorProperties.getSecondsToWaitOnShutdown());
                    asyncExecutor.setResetExpiredJobsInterval(flowableAsycExecutorProperties.getResetExpiredJobsInterval());
                    asyncExecutor.setResetExpiredJobsPageSize(flowableAsycExecutorProperties.getResetExpiredJobsPageSize());
                    config.setAsyncExecutor(asyncExecutor);
                    
                    // Set custome event listener
                    config.setEventListeners(Arrays.<FlowableEventListener>asList(new DefaultEventHandler()));
                    
                    /**
                     * Flowable DOCS (v 6.1.2)'s user guide - 18.10. Event logging
                     * http://www.flowable.org/docs/userguide/index.html#advanced.event.logging
                     **/
                    config.setEnableDatabaseEventLogging(true);
	            }
	            return bean;
	        }

	    	@Override
	        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
	            return bean;
	        }           
	    };
	}
}