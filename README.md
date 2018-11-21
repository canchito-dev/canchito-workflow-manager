# canchito-workflow-manager (CWM)
> Please note that this project is a work in progress.

**CANCHITO-WORKFLOW-MANAGER (CWM)** is a powerfull and yet light-weight and easy to use solution for handling workflows and business processes. At its core is a high performance open-source business process engine based on [Flowable](https://www.flowable.org/) with the flexibility and scalability to handle a wide variety of critical processes.  

We really hope that the additional features added to [Flowable](https://www.flowable.org/) can be as beneficial as we hope they are. If you would like to have a look into our other projects, please visit us at [Canchito-Dev](http://www.canchito-dev.com).


## Features
*   Extremely simple, easy to understand
*   Simple but clean structure
*   Easy to scale
*   Front- and Back-end are decoupled, allowing you to have them running in individual servers
*   Commented code
*   Integrates with a range of enterprise clouds, including Amazon Web Service, Microsoft Azure and Google Drive
*   Integrates with FFmpeg and MediaInfo
*   Custome palette perfectly compatible with Flowable Designer plug-in for Eclipse


## Download
Help us find bugs, add new features or simply just feel free to use it. Download **CANCHITO-WORKFLOW-MANAGER (CWM)** from our [GitHub](https://github.com/canchito-dev/canchito-workflow-manager) site.


## Sources
The source code can be found in our [GitHub](https://github.com/canchito-dev/canchito-workflow-manager) site.

*   `back-end`: has the source code for the back-end
*   `front-end`: has the source code for the fron-end
*   `palette`: contains the source code for the Eclipse palette

Here is a detail explanation of the `back-end` modules:

*   `cwm-app`: Main module. Has all the main resources and configurations
*   `cwm-cloud`: Has all the implementations to use the different cloud services
*   `cwm-core`: Handles CWM's Async Job Executor
*   `cwm-dao`: Handles all the database accesses. You can find all the repositories and entities here
*   `cwm-flowable`: Implementations created for seamlessly integrate with [Flowable](https://www.flowable.org/)
*   `cwm-rest-api`: Rest controllers for CWM
*   `cwm-service`: Handles the business logic
*   `cwm-cloud`: Contains all the task behaviors and runnables used in CWM's Async Job Executor
*   `cwm-utils`: Has all the app's utils


## License
The MIT License (MIT)  

Copyright (c) 2017, canchito-dev  

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:  

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.  

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


## Dear haters, trolls and everything-sucks-people...
This project started as a way of helping me understand and learn about Flowable and Spring Boot. But it grew up to include some other features that have been helpful to me. It might not perfectly coded, but it was developed with all the good intensions at heart.

This is a totally unpaid and voluntary project, developed during my free-time and shared on GitHub. It can be used for private or commercially, without a cost. There is only one simple request, do not bash or complain. If you don't like it, don't use it.

If you find an issue, please fill free and open a ticket, we will try to solve it as soon as possible. If you would like to become an active contributor to this project, even better.


## Contribute Code
If you would like to become an active contributor to this project please follow theses simple steps:

1.  Fork it
2.  Create your feature branch
3.  Commit your changes
4.  Push to the branch
5.  Create new Pull Request


## Software Requirement Specifications
*   Decoupled front- and back-end, allowing them run in individual servers
*   Integration with a range of enterprise clouds, including Amazon Web Service, Microsoft Azure and Google Drive
*   Integration with MediaInfo
*   Integration with FFmpeg
*   Allow execution of long-running tasks
*   Prioritize process instances
*   File transferring by FTP and Samba
*   High-available, redundant and scalable clustered platform


## What is Flowable?
[Flowable](https://www.flowable.org/) is a light-weight workflow and Business Process Management (BPM) Platform targeted at business people, developers and system admins. Its core is a super-fast and rock-solid BPMN 2 process engine for Java. It's open-source and distributed under the Apache license. [Flowable](https://www.flowable.org/) runs in any Java application, on a server, on a cluster or in the cloud. It integrates perfectly with Spring, it is extremely lightweight and based on simple concepts.


## Current "limitation"
In order to understand the main _"limitation"_, you need to understand how [Flowable](https://www.flowable.org/)'s asyc job executor works. Please read the following section from [Flowable](https://www.flowable.org/)'s user guide:

*   [3.9. Job Executor (from version 6.0.0 onwards)](http://www.flowable.org/docs/userguide/index.html#jobExecutorConfiguration)
*   [3.10. Job executor activation](http://www.flowable.org/docs/userguide/index.html#_job_executor_activation)
*   [18.1. Async Executor](http://www.flowable.org/docs/userguide/index.html#_async_executor)

And according to [Frederik Heremans](https://www.linkedin.com/in/frederikheremans/) (one of [Flowable](https://www.flowable.org/)'s main developers), in his reply to this [question](https://community.alfresco.com/thread/220468-modelling-an-async-user-wait-on-a-long-running-service-task) rised in [Flowable](https://www.flowable.org/)'s official forum, we also know that when executing long-running tasks the async job executor, bahaves as follow:

*   Executing a service-task (or any other task) keeps a transaction open until a wait-state/process-end/async-task is reached. If you have long-running operations, make sure your database doesn't time out
*   When a jobs is running for 5 minutes, the job aquisistion-thread assumes the async job executor that was running the job, has either died or has failed. The lock of the job is removed and the job will be executed by another thread in the executor-pool. This timeout-setting can be raised, if that is required
*   Long-running tasks modeled in the flowable-process always keep a transaction open and a async job executor thread occupied. Better practice is to use a queue-signal approach where the long-running operation is executed outside of [Flowable](https://www.flowable.org/) (queued to eg. camel using a service-task, providing the neccesary variables needed alongside). When the long-running task is completed, it should signal the execution, which has a recieve-task modeled in

Due to the requirements needed on **[CANCHITO-WORKFLOW-MANAGER](https://github.com/canchito-dev/canchito-workflow-manager)** [(CWM)](http://canchito-dev.com/projects/cwm), the regular way [Flowable](https://www.flowable.org/)'s async job executer behavior was a _"limitation"_. But they are not actual limitations. They are simply the way in which the engine should behave.

Following [Frederik Heremans](https://www.linkedin.com/in/frederikheremans/) reply, with **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager) it was decided to implement the queue-signal approach together with a database table, which will contain the tasks queue.


## Required Software
For the front-end, you need a standard Web server with at least:

*   PHP 5.3.2 or higher
*   Make sure _mod_rewrite_ is enabled and activated
*   Basic knowledge of Composer

For your development environment, you can use [WAMPServer](http://www.wampserver.com/en/), which is a Windows web development environment, and stands for **W**indows, **A**pache, **M**ySQL and **PHP**.

Composer is a very simple and easy to use dependency manager for PHP. It allows you to declare the libraries your project depends on and it will manage (install/update) them for you. You can download the Windows installer from their official [site](https://getcomposer.org/download/).

Now, the back-end runs on a JDK higher than or equal to version 7\. Go to [Oracle Java SE site](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and download it. There are installation instructions on that page as well.

In order to make sure that the installacion was successful, run java -version on the command line. That should print the installed version of your JDK:

```
java -version
java version "1.8.0_141"
Java(TM) SE Runtime Environment (build 1.8.0_141-b15)
Java HotSpot(TM) 64-Bit Server VM (build 25.141-b15, mixed mode)
```

Finally, you need a MySQL database. If you used [WAMPServer](http://www.wampserver.com/en/), it should automatically be installed.


## Definitions
### What are Folder Handlers?
A folder handler is a tag or an alternative name for referencing an actual directory in a FTP or Samba network location, a local storaga or even one in one of the supported clouds. They are used for describing actual paths and how to access them. Folder handlers simplify how you refer to directories, as you only specify a short and descriptive name, instead of a complex and longer path plus file name.

Moreover, folder handler' path and credentials only need to be updated once, and automatically, all actions using that folder will be updated.


### What are File Handlers?
A file handler is used as a tag for referencing a file within a workflow. Thanks to file handlers, you do not need to use the full path and name of each file in every step of a workflow. Consequently, it is only necessary to assign a file handler to a file, and CWM associates the full path and file name of the actual file during each step of the workflow.

It is required that each file is associated to a unique file handler. If the same file handler is used for a second file, the first values are overwritten.


## Configuration and Setup
### Database Setup
Create the database schema and all the database tables. Simply execute the _.sql_ statements in the `database`- folder (with [PHPMyAdmin](https://www.phpmyadmin.net/) for example).


### Front-End Setup
Copy the content of the `front-end`- folder into the Web server default folder and just follow these steps:

1.  Make sure you have _mod_rewrite_ activated on your server / in your environment.
2.  Install Composer and run `composer install` in the project's folder to download the dependencies and create the autoloading stuff from Composer automatically. For those who are not familiar with Composer, just remember back in the days, when you were using a PHP files with all the includes you needed. Well, Composer creates classes that automatically do this.
3.  Create a virtual host. For instance `cwm.canchito-dev.com`.
4.  Go to folder `application/config` and open the file `config.php`. Here you will have to modify some parameters.
    *   `TENANT_ID_PART`: Configuration for specifying which part of the `$_SERVER['HTTP_HOST']` variable use as a tenant id. For instance: `$_SERVER['HTTP_HOST'] = cwm.canchito-dev.com`. The system splits 'nahual.domain' by the '.' into two substrings  
        If TENANT\_ID\_PART = 0, 'cwm' will be used as TENANT_ID  
        If TENANT\_ID\_PART = 1, 'canchito-dev' will be used as TENANT_ID and so on
    *   `CWM_URL_DOMAIN`: the domain where the back-end is running
    *   `CWM_URL_PORT`: the port in which the back-end is listening
    *   `CWM_URL_SUB_FOLDER`: the path of the main dispatcher servlet

### Back-End Setup
Open the configuration file `src/main/resources/application.properties` and modify:

*   `server.address`: Network address to which the server should bind to
*   `server.connection-timeout`: Time in milliseconds that connectors will wait for another HTTP request before closing the connection. When not set, the connector's container-specific default will be used. Use a value of -1 to indicate no (i.e. infinite) timeout
*   `server.display-name`: Display name of the application
*   `server.servlet-path`: Path of the main dispatcher servlet
*   `server.port`: Server HTTP port
*   `server.tomcat.max-connections`: Maximum number of connections that the server will accept and process at any given time
*   `server.tomcat.max-threads`: Maximum amount of worker threads
*   `server.tomcat.uri-encodin`: Character encoding to use to decode the URI

For instance:

```
server.address=localhost
server.connection-timeout=60000
server.display-name=Canchito Workflow Manager
server.servlet-path=/canchito-dev-rest
server.port=10000
server.tomcat.max-connections=100
server.tomcat.max-threads=100
server.tomcat.uri-encoding=UTF-8
```

### Mail Configuration
Mails can be sent from both the front- and the back-end. Let's first do the configuration for the front-ent by opening the `application/config/config.php` file and modifying the following parameter to suit your needs:

*   `IS_SMTP`: Tells PHPMailer to use SMTP
*   `MAIL_SERVER`: Sets the hostname of the mail server
*   `SMTP_PORT`: Sets the SMTP port number - 587 for authenticated TLS, a.k.a. RFC4409 SMTP submission
*   `SMTP_USERNAME`: Username to use for SMTP authentication - use full email address for gmail
*   `SMTP_PWD`: Password to use for SMTP authentication
*   `SMTP_DEBUG`: Enable SMTP debugging: (0) = off (for production use); (1) = client messages; (2) = client and server messages. Default is 0
*   `SMTP_SECURE`: Set the encryption system to use - ssl (deprecated) or tls. Default is tls
*   `SMTP_AUTH`: Whether to use SMTP authentication. Default is true
*   `MAIL_CHARSET`: Sets the character set
*   `DEBUG_OUTPUT`: Ask for HTML-friendly debug output. Default is html

Now, the mail configuration for the back-end is needed so that [Flowable](https://www.flowable.org/) can send mails by its mail task. To configure it, open the file `src/main/resources/application.properties` and modify the following fields:

*   `flowable.mail-server-default-from`: The name of the sender as it will appear in mail
*   `flowable.mail-server-host`: SMTP server host. For instance `smtp.example.com`
*   `flowable.mail-server-password`: Login password of the SMTP server
*   `flowable.mail-server-port`: SMTP server port
*   `flowable.mail-server-use-tls`: Use tls or not
*   `flowable.mail-server-user-name`: Login user of the SMTP server


### Cloud Configuration
At the moment, the system can integrate with Amazon Web Services (AWS), but the roadmap includes integration with other clouds such as Microsoft's Azure and Google. For any cloud, it is required a valid account. To obtain a free AWS account, go to the [AWS Free Tier home page](https://aws.amazon.com/free/) and click _Create A Free Account/Create an AWS Account_. Signing up will enable you to use all of the services offered by AWS.

To configure it, open the file `src/main/resources/application.properties` and modify the following fields:

*   `amazon.credentials-access-key-id`: The access key of the account
*   `amazon.credentials-secret-access-key`: The secret access key related to the account
*   `amazon.configuration-region`: The region to which the account should log in


### Database Configuration
The database configuration is done in the `src/main/resources/application.properties` file. Simply, modify the following parameter to suit your needs:

*   `spring.datasource.continue-on-error`: Do not stop if an error occurs while initializing the database
*   `spring.datasource.driver-class-name`: Fully qualified name of the JDBC driver. Auto-detected based on the URL by default
*   `spring.datasource.password`: Login password of the databaser
*   `spring.datasource.url`: JDBC url of the database
*   `spring.datasource.username`: Login username of the database
*   `cwm.datasource.*`: Datasource specific settings

For instance:

```
spring.datasource.continue-on-error=false
spring.datasource.url=jdbc:mysql://localhost:3306/database?autoReconnect=true
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
cwm.datasource.test-on-borrow=true
cwm.datasource.test-on-return=true
cwm.datasource.test-while-idle=true
cwm.datasource.validation-query=SELECT 1
cwm.datasource.validation-interval=30000
cwm.datasource.connection-timeout=60000
cwm.datasource.connection-test-query=SELECT 1
cwm.datasource.log-validation-errors=true
cwm.datasource.max-idle=1
cwm.datasource.min-idle=1
cwm.datasource.max-active=5
cwm.datasource.initial-size=5
cwm.datasource.time-between-eviction-runs-millis=5000
cwm.datasource.min-evictable-idle-time-millis=30000
```


## Async Job Executor
Put in simple words, **[CANCHITO-WORKFLOW-MANAGER](https://github.com/canchito-dev/canchito-workflow-manager)** [(CWM)](http://canchito-dev.com/projects/cwm)'s async job executor are individual threads that are started once when the application is started. Each thread starts a thread pool that reuses a (configurable) fixed number of threads operating off database table called _CWM\_TASKS\_QUEUE_ and acting as a priority blocking list, using the provided ThreadFactory to create new threads when needed. At any point, at most _n_ threads will be active processing tasks.

![CANCHITO-DEV: Task Queue Service](http://canchito-dev.com/img/cwm/userguide/canchito_dev_task_queue_service.png)

Periodically, pending tasks are pulled from the database. The number of pending tasks that are pulled at once, depends on the number of available threads on **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager)'s async job executor for a specific task type. If additional tasks are submitted when all threads are active, they will reside in the database until a thread is available.

If any thread terminates due to a failure during execution prior to shutdown, a new one will take its place if needed to execute subsequent tasks. The threads in the pool will exist until it is explicitly shutdown.

Once a task is pulled by a thread, it is locked. By locking it, we can have several **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager) simultaneously running on different servers. Thus allowing us to have a fail-over, clustered system.

If two or more **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager)'s async job executor are started, they all will be raising for pulling the pending tasks. Consequently, only the first one that looks the task, will be the one that will execute it. The other async job executor will get an optimistic locking exception.

### Async Executor's Design
In order to understand the way long-running tasks are added to the queue, lets have a look at a very simple workflow as the one in the below image. As you can see, it is composed of a start event, a copy task (which is a service task), and an end event.

![CANCHITO-DEV: Copy Task sample workflow](http://canchito-dev.com/img/cwm/userguide/canchito_dev_copy_task_sample_workflow.png)

The copy task is a long-running service task, which needs to be processed by the async job executor. Long-runing tasks in **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager) extend _AbstractTaskCanchitoBehavior_, which at the same time extend from [Flowable](https://www.flowable.org/)'s _TriggerableActivityBehavior_ class.

The _TriggerableActivityBehavior_ is the parent class for many BPMN 2.0 task types such as ReceiveTask, ScriptTask, UserTask, etc. This class provides two methods: `execute()`and `trigger()`. The class _AbstractTaskCanchitoBehavior_ provides a two methods: `submitTask()` and `checkSignal(DelegateExecution execution)`. These four methods are the pillars for creating a long-running task implementing the _TriggerableActivityBehavior_ instead of adding two BPMN task (send task and receive task) in your process diagram.

![CANCHITO-DEV: CWM's Async Executor Design](http://canchito-dev.com/img/cwm/userguide/canchito_dev_async_executor_design.png)

The `execute(DelegateExecution execution)` method is invoked when the service task is entered. In our case, it is typically used for data valiation and preparation. There is no business logic here. For instance, it validates that all the needed information for the task to be correctly executed has valid values.

The `submitTask(DelegateExecution execution, JSONObject details, String beanId)` method submits an asynchronous task to the actual service. The submit action is actually storing the task in the database table by calling the `save()` method from the _GenericTaskService_ class. You can modify this method according to your database table structure.

After submitting the task and the method returns, the process engine will **not** continue execution. The _TriggerableActivityBehavior_ acts as a wait state. This means, that the process instances is put on hold, until a trigger is received.

Periodically, the _AcquireTaskThread_ in charge of these kind of service task (in this example, the copy task), read the task que database table. When it finds a new task, it acquires it, and locks it. Once locked, it calls the `run()` method. Here, you will find the business logic for the invoked service task. For our examples, it will problably call functions and method to copy a file from one location to another. When done, _AcquireTaskThread_ will call the `trigger()` method.

The `trigger(DelegateExecution execution, String signalName, Object signalData)` method is invoked as the process engine is being triggered by the callback. The `trigger()` method is responsible for leaving the service task activity and allowing the normal flow of the process instance. But before leaving the service task, the `checkSignal()` is called. It is in this method, were the execution of the service task's logic is analyzed and determined if it finished correctly or with errors.

By having a separate thread pool for executing long-running tasks, **[CANCHITO-WORKFLOW-MANAGER](http://canchito-dev.com/projects/cwm)** [(CWM)](https://github.com/canchito-dev/canchito-workflow-manager) has decoupled the process engine from the service implementation. From the point of view of [Flowable](https://www.flowable.org/)'s process engine, the _TriggerableActivityBehavior_ is a wait state: after the `execute()` method returns, the process engine will stop execution, makes the state of the execution to the database persistance and wait for the callback to occure.

As the long-running task implementation is not directly executed by [Flowable](https://www.flowable.org/)'s process engine and it does not participate in the process engine transaction, if there is an error in the service implementation, the failure will not cause the process engine to roll back.


### Async Executor's Configuration
The async job executor configuration is done in the `src/main/resources/application.properties` file. Simply, modify the following parameter to suit your needs:

The async job executor configuration is done by modifying two XML files (found under `src/main/resources/`):

*   `task-queue-beans.xml`: specified how each CWM's async job executor, dedicated to process a specific task is configured
*   `task-runnable-beans.xml`: here you will find the runnable classes that are used by each CWM's async job executor to execute the task

As you can see, each queue used by the CWM's async job executor needs to have some configuration. Let's describe those parameters found in `task-queue-beans.xml` file.

*   `runnableName`: the id of the runnable that and instance is initialized and afterward executed. This is the id which relates to the information found in `task-runnable-beans.xml`.
*   `poolName`: the name of the thread pool
*   `corePoolSize`: the number of threads to keep in the pool, even if they are idle
*   `maximumPoolSize`: the maximum number of threads to allow in the pool
*   `keepAliveTimeInMillis`: when the number of threads is greater than the core, this is the maximum time that excess idle threads will wait for new tasks before terminating
*   `acquireWaitTimeInMillis`: millis to wait before new tasks are pulled from the database
*   `maxTasksPerAcquisition`: maximum tasks that can be pulled from the database


For instance `task-queue-beans.xml`:

```xml
<bean id="task1Queue" class="com.canchitodev.cwm.threadpool.service.TaskQueue" scope="prototype">
	<property name="runnableName" value="task1Runnable" />
	<property name="poolName" value="task1Queue" />
	<property name="corePoolSize" value="2" />
	<property name="maximumPoolSize" value="5" />
	<property name="keepAliveTimeInMillis" value="300000"/>
	<property name="acquireWaitTimeInMillis" value="5000"/>
	<property name="maxTasksPerAcquisition" value="2"/>
</bean>
```

For instance `task-runnable-beans.xml`:

```xml
<bean id="task1Runnable" class="com.canchitodev.cwm.tasks.runnable.Task1Runnable" scope="prototype"></bean>
```

### Creating a long-running Service Task
A long-running service task is composed of two different classes: _Behavio_r class and _Runnable_ class. The behavior class, extends from _AbstractTaskCanchitoBehavior_, which at the same time extend from [Flowable](https://www.flowable.org/)'s _TaskActivityBehavior_ class. Let's have a look at how to implement it by creating a new class:

```java
@Service("task1")
@Scope("prototype")
public class Task1Behavior extends AbstractTaskCanchitoBehavior {

  private static final long serialVersionUID = -4740654158860004620L;

  @Override
  public void execute(DelegateExecution execution) {
    try {			
      this.submitTask(execution, new JSONObject(), "task1Runnable");
    } catch (Exception e) {
      this.throwException(execution, 
          "There was a problem when trying to execute task 'task1Runnable'"
      );
    }
  }

  @Override
  protected void validateParameters(DelegateExecution execution) throws IllegalArgumentException {
    // TODO Auto-generated method stub
  }
}
```

You can add additional logic before calling the `submitTask()` method if required. For our example, this would be enough. Next, create the _Runnable_ class. Remember, this is the class with all the business logic.

```java
public class Task1Runnable implements TaskRunnable {
  
  private static final Logger logger = Logger.getLogger(Task1Runnable.class);
  
  private GenericTaskEntity task;

  public Task1Runnable() {}
  
  public Task1Runnable(GenericTaskEntity task) {
    this.task = task;
  }

  public GenericTaskEntity getTask() {
    return task;
  }

  public void setTask(GenericTaskEntity task) {
    this.task = task;
  }

  @Override
  public void execute() {
    try {
      logger.info("Executing task " + task.toString());
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      logger.info("Done executing task " + task.toString());
    }
  }
}
```

That's it. Now just add the task queue in the `task-queue-beans.xml` and the _Runnable_ class to the respective `task-runnable-beans.xml` file so that _TaskQueueService_ can start its respective _AcquireTaskThread_.