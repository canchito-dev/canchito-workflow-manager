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
class Users extends Controller {
	
	private $modelToLoad = 'Users';
	
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
    			'Users' => 'users'
    	);
    	
    	$localCss = array('jquery-bootgrid/jquery.bootgrid.min');
    	$localJs = array(
    	    'bootstrap-password-toggler/bootstrap-password-toggler.min',
    	    'jquery-bootgrid/jquery.bootgrid.min',
    	    'jquery-bootgrid/jquery.bootgrid.defaults.min', 
    	    'bootbox/bootbox.min',
    	    'utils/formUtil.min',
    	    'cwm/users/user.min',
    	    'cwm/users/users.min'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/_templates/nav.header.php';
        require APP . 'view/users/index.php';
        require APP . 'view/users/modal/viewuser.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function getSingleUser() {
        echo json_encode($this->model->getSingleUser($_POST['id']));
        exit();
    }
    
    public function getListOfUsers() {
        echo json_encode($this->model->getListOfUsers($_POST));
        exit();
    }
    
    public function saveUser() {
        $_POST['id'] = strtolower($_POST['id']) . '@' . explode(".", $_SESSION['TENANT_ID'], 2)[0];
        $_POST['firstName'] = ucwords(strtolower($_POST['firstName']));
        $_POST['lastName'] = ucwords(strtolower($_POST['lastName']));
        $_POST['email'] = strtolower($_POST['email']);        
        $_POST['password'] = sha1(md5(md5($_POST['password'])));
        
        if($_POST['action'] == 'new')        
            echo json_encode($this->model->createUser($_POST));
        else
            echo json_encode($this->model->updateUser($_POST['id'], $_POST));
        
        exit();
    }
    
    public function deleteUser() {
        echo json_encode($this->model->deleteUser($_POST['id']));
        exit();
    }
}
