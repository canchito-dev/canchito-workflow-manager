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

class Groups extends Controller {
	
	private $modelToLoad = 'Groups';
	
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
		
		if(isset($_POST['id']))
		    $_POST['id'] = $this->mergeTenant($_POST['id']);
	}
	
    /**
     * PAGE: index
     * This method handles what happens when you move to http://yourproject/users/index
     **/
    public function index() {
    	$breadcrumbs = array(
    			'Home' => 'home', 
    			'Management' => '',
    			'Identity Access' => '',
    			'Groups' => 'groups'
    	);
    	
    	$localCss = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'selectize/selectize.default',
    	);
    	$localJs = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min',
    	    'selectize/selectize.min',
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/users/user.min',
    	    'cwm/groups/group',
    	    'cwm/groups/groups'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/groups/index.php';
        require APP . 'view/groups/modal/viewgroup.php';
        require APP . 'view/groups/modal/filtergroup.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSingleGroup() {
        echo json_encode($this->model->getSingleGroup($_POST['id']));
        exit();
    }
    
    public function getListOfGroups() {
        echo json_encode($this->model->getListOfGroups($_POST));
        exit();
    }
    
    public function saveGroup() {
        $_POST['name'] = ucwords(strtolower($_POST['name']));
        $_POST['type'] = ucwords(strtolower($_POST['type']));
        
        if($_POST['action'] == 'new')        
            echo json_encode($this->model->createGroup($_POST));
        else
            echo json_encode($this->model->updateGroup($_POST['id'], $_POST));
        
        exit();
    }
    
    public function deleteGroup() {
        echo json_encode($this->model->deleteGroup($_POST['id']));
        exit();
    }
    
    public function getGroupMembers() {
        $_POST['memberOfGroup'] = $this->mergeTenant($_POST['memberOfGroup']);
        echo json_encode($this->model->getGroupMembers($_POST));
        exit();
    }
    
    public function addMemberToGroup() {
        $_POST['userId'] = $this->mergeTenant($_POST['userId']);
        echo json_encode($this->model->addMemberToGroup($_POST['id'], $_POST['userId']));
        exit();
    }
    
    public function deleteMemberFromGroup() {
        $_POST['userId'] = $this->mergeTenant($_POST['userId']);
        echo json_encode($this->model->deleteMemberFromGroup($_POST['id'], $_POST['userId']));
        exit();
    }
}
