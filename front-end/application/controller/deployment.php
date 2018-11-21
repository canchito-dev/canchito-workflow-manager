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

class Deployment extends Controller {
	
	private $modelToLoad = 'Deployment';
	
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
    			'Process Deployment' => 'deployment'
    	);
    	
    	$localCss = array(
    	    'jquery-bootgrid/jquery.bootgrid.min'
    	);
    	$localJs = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min',
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'jquery-file-upload/jquery.ui.widget.min',
    	    'jquery-file-upload/jquery.iframe-transport.min',
    	    'jquery-file-upload/jquery.fileupload.min',
    	    'cwm/deployment/deployment.min',
    	    'cwm/deployment/deployments.min'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/deployment/index.php';
        require APP . 'view/deployment/modal/filterdeployment.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSingleDeployment() {
        echo json_encode($this->model->getSingleDeployment($_POST['id']));
        exit();
    }
    
    public function getListOfDeployments() {
        echo json_encode($this->model->getListOfDeployments($_POST));
        exit();
    }
    
    public function saveDeployment() {
        if(!empty($_FILES)) {
            $fileHandler = $this->upload();
            if($fileHandler->doUpload($_FILES['files'])) {
                echo json_encode($this->model->createDeployment([
                    [
                        'name'     => 'files',
                        'contents' => fopen($_FILES['files']['tmp_name'], 'r'),
                        'filename' => $_FILES['files']['name']
                    ], [
                        'name'     => 'tenantId',
                        'contents' => $_SESSION['TENANT_ID'],
                    ]
                ]));
            } else {
                echo json_encode(array(
                    'code' => 500,
                    'reason' => $fileHandler->getErrors(),
                    'body' => ''
                ));
            }
        } else {
            echo json_encode(array(
                'code' => 500,
                'reason' => 'No file selected',
                'body' => ''
            ));
        }
        
        exit();
    }
    
    public function deleteDeployment() {
        echo json_encode($this->model->deleteDeployment($_POST['id']));
        exit();
    }
    
    public function getListOfResourcesInDeployment() {
        echo json_encode($this->model->getListOfResourcesInDeployment($_POST['deploymentId']));
        exit();
    }
    
    public function getSingleDeploymentResourceContent() {
        echo json_encode($this->model->getSingleDeploymentResourceContent($_POST['deploymentId'], $_POST['resourceId']));
        exit();
    }
}