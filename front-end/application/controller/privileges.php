<?php
namespace Application\Controller;

use Application\Core\Controller;
use Application\Libs\Session;
use Application\Libs\Methods;

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
 * @author 		Jose Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
class Privileges extends Controller {
	
	private $modelToLoad = 'Privilege';
	
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
     * This method handles what happens when you move to http://yourproject/users/index
     **/
    public function index() {
    	$breadcrumbs = array(
    			'Home' => 'home', 
    			'Management' => '',
    			'Identity Access' => '',
    			'Privilege' => 'privileges'
    	);
    	
    	$localCss = array('jquery-bootgrid/jquery.bootgrid.min');
    	$localJs = array(
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min', 
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/privileges/privilege.min',
    	    'cwm/privileges/privileges.min'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/privileges/index.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSinglePrivilege() {
        echo json_encode($this->model->getSinglePrivilege($_POST['id']));
        exit();
    }
    
    public function getListOfPrivileges() {
        echo json_encode($this->model->getListOfPrivileges($_POST));
        exit();
    }
    
    public function getUsersWithPrivilege() {
        echo json_encode($this->model->getUsersWithPrivilege($_POST['id']));
        exit();
    }
    
    public function removePrivilegeFromUser() {
        echo json_encode($this->model->removePrivilegeFromUser($_POST['id'], $_POST['userId']));
        exit();
    }
    
    public function addUserPrivilege() {
        $privilegeId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->addUserPrivilege($privilegeId, $_POST));
        exit();
    }
    
    public function getGroupsWithPrivilege() {
        echo json_encode($this->model->getGroupsWithPrivilege($_POST['id']));
        exit();
    }
    
    public function removePrivilegeFromGroup() {
        echo json_encode($this->model->removePrivilegeFromGroup($_POST['id'], $_POST['userId']));
        exit();
    }
    
    public function addGroupPrivilege() {
        $privilegeId = $_POST['id'];
        unset($_POST['id']);
        echo json_encode($this->model->addGroupPrivilege($privilegeId, $_POST));
        exit();
    }
}
