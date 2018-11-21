<?php
/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2018, canchito-dev
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
 * @author 		Jose Carlos Mendoza Prego
 * @copyright	Copyright (c) 2018, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
namespace Application\Controller;

use Application\Core\Controller;

class Task extends Controller {
	
	private $modelToLoad = 'Task';
	
	public function __construct() {
		/**
		 * This is the code which allows access to the page or not
		 * If the user was correctly logged in, we allow access to
		 * this controller
		 **/
		if(!$this->isLoggedIn()) {
			header('location: ' . URL . 'authentication/index');
			exit();
		}
		
	    $this->loadModel($this->modelToLoad);
	    $this->model->setAuth($_SESSION['ID'], $_SESSION['PASSWORD']);
		
		parent::__construct();
	}
	
    /**
     * PAGE: inbox
     **/
	public function inbox() {
    	$breadcrumbs = array(
			'Home' => 'home',
			'Task Management' => '',
			'Inbox' => 'task/inbox'
    	);
    	
    	$localCss = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'selectize/selectize.default.min'
    	);
    	$localJs = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min',
    	    'selectize/selectize.min',
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/form/form',
    	    'cwm/deployment/deployment.min',
    	    'cwm/users/user.min',
    	    'cwm/task/task.min',
    	    'cwm/task/tasks.min'
    	);
    	
    	$filterField = 'assignee';
    	$filterValue = $_SESSION['ID'];
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/task/tasks.php';
        require APP . 'view/_templates/submitForm.php';
        require APP . 'view/_templates/footer.php';
	}
	
	/**
	 * PAGE: my tasks
	 **/
	public function myTasks() {
	    $breadcrumbs = array(
	        'Home' => 'home',
	        'Task Management' => '',
	        'My Tasks' => 'task/mytasks'
	    );
	    
	    $localCss = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'selectize/selectize.default.min'
	    );
	    $localJs = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'jquery-bootgrid/jquery.bootgrid.defaults.min',
	        'selectize/selectize.min',
	        'bootbox/bootbox.min',
	        'utils/formUtil.min',
	        'cwm/form/form',
	        'cwm/deployment/deployment.min',
	        'cwm/users/user.min',
	        'cwm/task/task',
	        'cwm/task/tasks'
	    );
	    
	    $filterField = 'owner';
	    $filterValue = $_SESSION['ID'];
	    
	    // load views
	    require APP . 'view/_templates/header.php';
	    require APP . 'view/_templates/nav.header.php';
	    require APP . 'view/task/tasks.php';
	    require APP . 'view/_templates/submitForm.php';
	    require APP . 'view/_templates/footer.php';
	}
	
	/**
	 * PAGE: involved tasks
	 **/
	public function involvedTasks() {
	    $breadcrumbs = array(
	        'Home' => 'home',
	        'Task Management' => '',
	        'Involved Tasks' => 'task/involvedtasks'
	    );
	    
	    $localCss = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'selectize/selectize.default.min'
	    );
	    $localJs = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'jquery-bootgrid/jquery.bootgrid.defaults.min',
	        'selectize/selectize.min',
	        'bootbox/bootbox.min',
	        'utils/formUtil.min',
	        'cwm/form/form',
	        'cwm/deployment/deployment.min',
	        'cwm/users/user.min',
	        'cwm/task/task',
	        'cwm/task/tasks'
	    );
	    
	    $filterField = 'involvedUser';
	    $filterValue = $_SESSION['ID'];
	    
	    // load views
	    require APP . 'view/_templates/header.php';
	    require APP . 'view/_templates/nav.header.php';
	    require APP . 'view/task/tasks.php';
	    require APP . 'view/_templates/submitForm.php';
	    require APP . 'view/_templates/footer.php';
	}
	
	/**
	 * PAGE: group tasks
	 **/
	public function groupTasks() {
	    $breadcrumbs = array(
	        'Home' => 'home',
	        'Task Management' => '',
	        'Group Tasks' => 'task/grouptasks'
	    );
	    
	    $localCss = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'selectize/selectize.default.min'
	    );
	    $localJs = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'jquery-bootgrid/jquery.bootgrid.defaults.min',
	        'selectize/selectize.min',
	        'bootbox/bootbox.min',
	        'utils/formUtil.min',
	        'cwm/form/form',
	        'cwm/deployment/deployment.min',
	        'cwm/users/user.min',
	        'cwm/task/task',
	        'cwm/task/tasks'
	    );
	    
	    $groups = array();
	    
	    foreach ($_SESSION['GROUPS'] as $key => $group) {
	        array_push($groups, $group->id);
	    }
	    
	    $filterField = 'candidateGroups';
	    $filterValue = implode(',', $groups);
	    
	    // load views
	    require APP . 'view/_templates/header.php';
	    require APP . 'view/_templates/nav.header.php';
	    require APP . 'view/task/tasks.php';
	    require APP . 'view/_templates/submitForm.php';
	    require APP . 'view/_templates/footer.php';
	}
	
	/**
	 * PAGE: unassigned tasks
	 **/
	public function unassignedTasks() {
	    $breadcrumbs = array(
	        'Home' => 'home',
	        'Task Management' => '',
	        'Unassigned Tasks' => 'task/unassignedtasks'
	    );
	    
	    $localCss = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'selectize/selectize.default.min'
	    );
	    $localJs = array(
	        'jquery-bootgrid/jquery.bootgrid.min',
	        'jquery-bootgrid/jquery.bootgrid.defaults.min',
	        'selectize/selectize.min',
	        'bootbox/bootbox.min',
	        'utils/formUtil.min',
	        'cwm/form/form',
	        'cwm/deployment/deployment.min',
	        'cwm/users/user.min',
	        'cwm/task/task.min',
	        'cwm/task/tasks.min'
	    );
	    
	    $filterField = 'unassigned';
	    $filterValue = true;
	    
	    // load views
	    require APP . 'view/_templates/header.php';
	    require APP . 'view/_templates/nav.header.php';
	    require APP . 'view/task/tasks.php';
	    require APP . 'view/_templates/submitForm.php';
	    require APP . 'view/_templates/footer.php';
	}
    
	public function getSingleTask() {
	    echo json_encode($this->model->getSingleTask($_POST['id']));
        exit();
    }
    
    public function getListOfTasks() {
        echo json_encode($this->model->getListOfTasks($_POST));
        exit();
    }
    
    public function updateTask() {
        $taskId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->updateTask($taskId, $_POST));
        exit();
    }
    
    public function deleteTask() {
        echo json_encode($this->model->deleteTask($_POST['id']));
        exit();
    }
    
    public function completeTask() {
        $taskId = $_POST['id'];
        unset($_POST['id']);
        
        if(isset($_POST['variables'])) {
            $_POST['variables'] = json_decode($_POST['variables']);
        } else {
            $_POST['variables'] = array();
        }
        
        echo json_encode($this->model->taskAction($taskId, $_POST));
        exit();
    }
    
    public function claimTask() {
        $taskId = $_POST['id'];
        unset($_POST['id']);
        $_POST['assignee'] = $_SESSION['ID'];
        echo json_encode($this->model->taskAction($taskId, $_POST));
        exit();
    }
    
    public function delegateTask() {
        $taskId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->taskAction($taskId, $_POST));
        exit();
    }
    
    public function resolveTask() {
        $taskId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->taskAction($taskId, $_POST));
        exit();
    }
    
    public function getAllVariablesForATask() {
        echo json_encode($this->model->getAllVariablesForATask($_POST['id']));
        exit();
    }
    
    public function getTotalTasks() {
        $assignee = $_POST;
        $assignee['assignee'] = $_SESSION['ID'];
        $assignee = $this->model->getListOfTasks($assignee);
        $assignee = ($assignee['code'] >= 400) ? 0 : $assignee['body']->total;
        
        $owner = $_POST;
        $owner['owner'] = $_SESSION['ID'];
        $owner = $this->model->getListOfTasks($owner);
        $owner = ($owner['code'] >= 400) ? 0 : $owner['body']->total;
        
        $involvedUser = $_POST;
        $involvedUser['involvedUser'] = $_SESSION['ID'];
        $involvedUser = $this->model->getListOfTasks($involvedUser);
        $involvedUser = ($involvedUser['code'] >= 400) ? 0 : $involvedUser['body']->total;
        
        $groups = array();
        
        foreach ($_SESSION['GROUPS'] as $key => $group) {
            array_push($groups, $group->id);
        }
        
        $candidateGroups = $_POST;
        $candidateGroups['candidateGroups'] = implode(',', $groups);
        $candidateGroups = $this->model->getListOfTasks($candidateGroups);
        $candidateGroups = ($candidateGroups['code'] >= 400) ? 0 : $candidateGroups['body']->total;
        
        $unassigned = $_POST;
        $unassigned['unassigned'] = true;
        $unassigned = $this->model->getListOfTasks($unassigned);
        $unassigned = ($unassigned['code'] >= 400) ? 0 : $unassigned['body']->total;
        
        echo json_encode([
            'code' => '200',
            'body' => array(
                'assignee' => $assignee,
                'owner' => $owner,
                'involvedUser' => $involvedUser,
                'group' => $candidateGroups,
                'unassigned' => $unassigned
            )
        ]);
        
        exit();
    }
}