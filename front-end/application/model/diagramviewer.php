<?php
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
namespace Application\Model;

use Application\Libs\Http\HttpClient;

class DiagramViewer extends HttpClient {
	
	private $uri = array(
	    'DIAGRAM_LAYOUT_BY_DEFINITION_ID' => 'repository/process-definitions/{processDefinitionId}/model-json',
	    'HIGHLIGHTS_BY_PROCESS_INSTANCE_ID' => 'runtime/process-instances/{processInstanceId}/model-json',
	    'HIGHLIGHTS_BY_HISTORIC_PROCESS_INSTANCE_ID' => 'runtime/process-instances/{processInstanceId}/history-model-json'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BASE_REST_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the diagram of the process definition
	 * @param string $processDefinitionId	- The id of the process definition to get
	 * @return	200 - Indicates the process definition was found and returned
	 * 			404 - Indicates the requested process definition was not found
	 **/
	public function getDiagramLayoutByProcessDefinitionId($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['DIAGRAM_LAYOUT_BY_DEFINITION_ID']);
		return $this->get($uri);
	}
	
	/**
	 * Get th diagram highlights by process instance id
	 * @param string $processDefinitionId	- The id of the process definition to get
	 * @return	200 - Indicates the process definition was found and returned
	 * 			404 - Indicates the requested process definition was not found
	 **/
	public function getDiagramHighlightsByProcessInstanceId($processInstanceId) {
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['HIGHLIGHTS_BY_PROCESS_INSTANCE_ID']);
	    return $this->get($uri);
	}
	
	/**
	 * Get th diagram highlights by process instance id
	 * @param string $processDefinitionId	- The id of the process definition to get
	 * @return	200 - Indicates the process definition was found and returned
	 * 			404 - Indicates the requested process definition was not found
	 **/
	public function getDiagramHighlightsByHistoricProcessInstanceId($processInstanceId) {
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['HIGHLIGHTS_BY_HISTORIC_PROCESS_INSTANCE_ID']);
	    return $this->get($uri);
	}
}