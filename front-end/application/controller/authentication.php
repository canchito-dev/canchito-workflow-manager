<?php
namespace Application\Controller;

use Application\Core\Controller;
use Application\Libs\Session;
use Application\Libs\Methods;

/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2016, canchito-dev
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
 * @copyright	Copyright (c) 2016, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/mini-framework-mvc
 **/
class Authentication extends Controller {
    
    private $groupModel = 'Groups';

    public function __construct() {
        $this->loadModel('Users');
        
        parent::__construct();
    }
	
    /**
     * PAGE: index
     * This method handles what happens when you move to http://yourproject/home/index (which is the default page btw)
     **/
    public function index() {
    	$localJs = array(
    	    'cwm/authentication/md5.min', 
    	    'cwm/authentication/utf8_encode.min',
    	    'bootstrap-sign-in-web-component/bootstrap-sign-in-web-component.min',
     	    'cwm/authentication/signin.min'
    	);
    	
        // load views
        require APP . 'view/_templates/header.php';
        require APP . 'view/authentication/index.php';
        require APP . 'view/_templates/footer.php';
    }
    
    public function signIn() {
        $httpHost = $this->getHttpHostPart();
        $usernameDomain = explode("@", $_POST['username'], 2)[1];
        
        if($httpHost != $usernameDomain) {
            echo json_encode(
                array(
                    'code' => '400',
                    'reason' => 'Incorrect username or password',
                    'body' => ''
                )
            );
            exit();
        }
        
        $_POST['username'] = explode(".", $_POST['username'], 2)[0];
        $_POST['password'] = sha1(md5($_POST['password']));
        
        $this->model->setAuth($_POST['username'], $_POST['password']);
        $user = $this->model->getSingleUser($_POST['username']);
        
        if ($user['code'] >= 400) {
            $user['reason'] .= '. Incorrect username or password';
            $this->session()->destroySession();
            echo json_encode($user);
            exit();
        }
        
        $this->groupModel = $this->loadModelAndReturnIt($this->groupModel);
        $this->groupModel->setAuth($_POST['username'], $_POST['password']);
        
        $groups = $this->groupModel->getListOfGroups(array('member' => $user['body']->id));
        if ($groups['code'] >= 400) {
            $groups = array();
        } else {
            $groups = $groups['body']->data;
        }
        
        $this->session()->createSession();                
        $this->session()->setVariable('SESSION_CREATED_TIMESTAMP', time());
        $this->session()->setVariable(
            array(
                'ID' => $user['body']->id,
                'FIRST_NAME' =>  $user['body']->firstName,
                'LAST_NAME' =>  $user['body']->lastName,
                'EMAIL' => $user['body']->email,
                'PASSWORD' => $_POST['password'],
                'TENANT_ID' => $httpHost,
                'GROUPS' => $groups
            )
        );
        
        $response['reason'] = 'Valid credentials. Redirecting...';        
        $response['redirect'] = 'home/index';
        
        echo json_encode($response);
        exit();
    }
    
    public function signOut() {
    	$this->session()->destroySession();

    	header('location: ' . URL . 'authentication');
    	exit();
    }
    
    public function getSignInWebComponent() {
        echo file_get_contents(APP . 'view/_templates/_web_components/signin.html');
        exit();
    }
}
