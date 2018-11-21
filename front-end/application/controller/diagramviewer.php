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

class DiagramViewer extends Controller {
	
	private $modelToLoad = 'DiagramViewer';
	
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
	 * ACTION: get diagram layout by process definition id
	 * This method handles what happens when you move to http://yourproject/diagramviewer/getdiagramlayoutbyprocessdefinitionid
	 **/
	public function getDiagramLayoutByProcessDefinitionId() {
	    echo json_encode($this->model->getDiagramLayoutByProcessDefinitionId($_POST['processDefinitionId']));
	    exit();
	}
	
	/**
	 * ACTION: get diagram highlights by process instance id
	 * This method handles what happens when you move to http://yourproject/diagramviewer/getdiagramhighlightsbyprocessinstanceid
	 **/
	public function getDiagramHighlightsByProcessInstanceId() {
	    echo json_encode($this->model->getDiagramHighlightsByProcessInstanceId($_POST['processInstanceId']));
	    exit();
	}
	
	/**
	 * ACTION: get diagram highlights by process instance id
	 * This method handles what happens when you move to http://yourproject/diagramviewer/getdiagramhighlightsbyhistoricprocessinstanceid
	 **/
	public function getDiagramHighlightsByHistoricProcessInstanceId() {
	    echo json_encode($this->model->getDiagramHighlightsByHistoricProcessInstanceId($_POST['processInstanceId']));
	    exit();
	}
}