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

class ProcessInstance extends Controller {
	
	private $modelToLoad = 'ProcessInstance';
	
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
     **/
    public function index() {
    	$breadcrumbs = array(
    			'Home' => 'home', 
    			'Processes' => '',
    			'Process Monitor' => '',
    			'Running Instances' => 'processinstance'
    	);
    	
    	$localCss = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'selectize/selectize.default.min'
    	);
    	$localJs = array(
    	    'selectize/selectize.min',
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min',
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/processinstance/processinstance',
    	    'cwm/processinstance/processinstances'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/processinstance/index.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSingleProcessInstance() {
        echo json_encode($this->model->getSingleProcessInstance($_POST['id']));
        exit();
    }
    
    public function getListOfProcessInstances() {
        echo json_encode($this->model->getListOfProcessInstances($_POST));
        exit();
    }
    
    public function activateOrSuspendProcessInstance() {
        $processInstanceId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->activateOrSuspendProcessInstance($processInstanceId, $_POST));
        exit();
    }
    
    public function deleteProcessInstance() {
        echo json_encode($this->model->deleteProcessInstance($_POST['id']));
        exit();
    }
    
    public function startProcessInstance() {
        if(isset($_POST['variables'])) {
            $_POST['variables'] = json_decode($_POST['variables']);
        } else {
            $_POST['variables'] = array();
        }
        
        $_POST['variables'][sizeOf($_POST['variables'])] =  array(
            'name' => 'initiator', 
            'value' => $_SESSION['ID']            
        );
        echo json_encode($this->model->startProcessInstance($_POST));
        exit();
    }
    
    public function getDiagramForProcessInstance() {
        $uri = CWM_REST_PROTOCOL . CWM_REST_DOMAIN . ':' . CWM_REST_PORT . '/' . CWM_REST_FOLDER . '/' . 'runtime/process-instances/{processInstanceId}/diagram';
        $uri = str_replace('{processInstanceId}', $_POST['id'], $uri);
        echo json_encode(array(
            'code' => '200',
            'reason' => 'OK',
            'body' => $uri
        ));
        exit();
    }
}