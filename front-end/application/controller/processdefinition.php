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

class ProcessDefinition extends Controller {
	
	private $modelToLoad = 'ProcessDefinition';
	
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
     * PAGE: index
     * This method handles what happens when you move to http://yourproject/process-deployments/index
     **/
    public function index() {
    	$breadcrumbs = array(
    			'Home' => 'home', 
    			'Processes' => '',
    			'Process Management' => '',
    			'Process Definition' => 'processdefinition'
    	);
    	
    	$localCss = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'flowablediagramviewer/jquery.qtip.min',
    	    'flowablediagramviewer/jquery.growl',
    	    'flowablediagramviewer/jquery-confirm.min',
    	    'flowablediagramviewer/displaymodel',
    	    'selectize/selectize.default.min'
    	);
    	$localJs = array(
    	    'selectize/selectize.min',
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min',
    	    'cwm/flowablediagramviewer/jquery.qtip.min',
    	    'cwm/flowablediagramviewer/jquery.growl',
    	    'cwm/flowablediagramviewer/jquery-confirm.min',
    	    'cwm/flowablediagramviewer/raphael.min',
    	    'cwm/flowablediagramviewer/bpmn-draw',
    	    'cwm/flowablediagramviewer/bpmn-icons',
    	    'cwm/flowablediagramviewer/Polyline',
    	    'cwm/flowablediagramviewer/displayDiagram',
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/users/user.min',
    	    'cwm/groups/group.min',
    	    'cwm/form/form',
    	    'cwm/deployment/deployment.min',
    	    'cwm/processinstance/processinstance.min',
    	    'cwm/processdefinition/processdefinition.min',
    	    'cwm/processdefinition/processdefinitions'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/processdefinition/index.php';
        require APP . 'view/processdefinition/modal/filterprocessdefinition.php';
        require APP . 'view/_templates/submitForm.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSingleProcessDefinition() {
        echo json_encode($this->model->getSingleProcessDefinition($_POST['id']));
        exit();
    }
    
    public function getListOfProcessDefinitions() {
        echo json_encode($this->model->getListOfProcessDefinitions($_POST));
        exit();
    }
    
    public function updateCategory() {
        $processDefinitionId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->updateCategory($processDefinitionId, array('category' => $_POST['category'])));
        exit();
    }
    
    public function getSingleProcessDefinitionResourceContent() {
        echo json_encode($this->model->getSingleProcessDefinitionResourceContent($_POST['processDefinitionId']));
        exit();
    }
    
    public function getProcessDefinitionBpmnModel() {
        echo json_encode($this->model->getProcessDefinitionBpmnModel($_POST['processDefinitionId']));
        exit();
    }
    
    public function activateOrSuspendProcessDefinition() {
        $processDefinitionId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->activateOrSuspendProcessDefinition($processDefinitionId, $_POST));
        exit();
    }
    
    public function getAllCandidateStarters() {
        echo json_encode($this->model->getAllCandidateStarters($_POST['id']));
        exit();
    }
    
    public function addCandidateStarter() {
        $processDefinitionId = $_POST['processDefinitionId'];
        unset($_POST['processDefinitionId']);
        echo json_encode($this->model->addCandidateStarter($processDefinitionId, $_POST));
        exit();
    }
    
    public function deleteCandidateStarter() {
        echo json_encode($this->model->deleteCandidateStarter($_POST['processDefinitionId'], $_POST['family'], $_POST['identityId']));
        exit();
    }
}