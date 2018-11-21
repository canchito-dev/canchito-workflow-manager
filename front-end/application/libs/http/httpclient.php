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
* @link			https://github.com/canchito-dev/canchito-workflow-manager
**/
namespace Application\Libs\Http;

use GuzzleHttp\Client;
use Application\Core;

class HttpClient {

	private $client;
	private $options;

	public function __construct($baseUri = CWM_BASE_REST_URL) {
		if(is_null($baseUri) || empty($baseUri))
			throw new Core\Exception('No URL specified');
		
		$this->client = new Client([ 'base_uri' => $baseUri ]);
		
		$this->options = [
			'debug' => FALSE,
			'version' => 1.1,					// Protocol version to use with the request.
// 		    'query' => [],						// Query String Parameters. Associative array of query string values or query string to add to the request.
// 			'json' => [],						// The json option is used to easily upload JSON encoded data as the body of a request. A Content-Type header of application/json will be added if no Content-Type header is already present on the message.
// 			'form_params' => [],				// Sending form fields. Used to send an application/x-www-form-urlencoded POST request
// 			'auth' => [],						// Use basic HTTP authentication in the Authorization header (the default setting used if none is specified)
// 			'headers' => [						// Associative array of headers to add to the request. Each key is the name of a header, and each value is a string or array of strings representing the header field values
// 				'protocol_version' => '1.1',
// 				'Cache-Control' => 'no-cache',
// 				'User-Agent' => 'Mozilla/4.0 (compatible; MSIE 5.01; Windows NT 5.0)',
// 			],
			'http_errors' => FALSE,				// Set to false to disable throwing exceptions on an HTTP protocol errors (i.e., 4xx and 5xx responses). Exceptions are thrown by default when HTTP protocol errors are encountered.
			'json_decode_body' => TRUE,
		    'stream' => FALSE
		];
	}

	public function __destruct() {}
	
	public function setDebug($debug = TRUE) {
		$this->options['debug'] = $debug;
	}
	
	public function getDebug() {
		return $this->options['debug'];
	}
	
	public function setVersion($version = 1.1) {
		$this->options['version'] = $version;
	}
	
	public function getVersion() {
		return $this->options['version'];
	}
	
	public function setQuery($query = []) {
		$this->options['query'] = $query;
	}
	
	public function getQuery() {
		return $this->options['query'];
	}
	
	public function setJsonBody($json = []) {
		$this->options['json'] = $json;
	}
	
	public function setFormParams($formParams = []) {
		$this->options['form_params'] = $formParams;
	}
	
	public function getFormParams() {
		return $this->options['form_params'];
	}
	
	public function setAuth($username = '', $password = '') {
		$this->options['auth'] = [$username, $password];
	}
	
	public function getAuth() {
		return $this->options['auth'];
	}
	
	public function setHeaders($headers = []) {
		$this->options['headers'] = $headers;
	}
	
	public function getHeaders() {
		return $this->options['headers'];
	}
	
	public function setHttpErrors($httpErrors = TRUE) {
		$this->options['http_errors'] = $httpErrors;
	}
	
	public function getHttpErrors() {
		return $this->options['http_errors'];
	}
	
	public function getMultipart() {
	    return $this->options['multipart'];
	}
	
	public function setMultipart($multipart = []) {
// 	    You can send files along with a form (multipart/form-data POST requests), using the multipart request option. multipart accepts an array of associative arrays
//         	    'multipart' => [
//         	        'name'     => '',
//         	        'contents' => '',
//         	        'filename' => ''
//         	    ]
	    if(isset($this->options['form_params'])) unset($this->options['form_params']);
	    $this->options['multipart'] = $multipart;
	}
	
	public function setJsonDecodeBody($jsonDecodeBody = TRUE) {
	    $this->options['json_decode_body'] = $jsonDecodeBody;
	}
	
	public function getJsonDecodeBody() {
	    return $this->options['json_decode_body'];
	}
	
	public function setStream($stream = FALSE) {
	    $this->options['stream'] = $stream;
	}
	
	public function getStream() {
	    return $this->options['stream'];
	}
	
	private function response($response = []) {
	    $body = ($this->getJsonDecodeBody()) ? json_decode($response->getBody()) : (string) $response->getBody();
	    return array(
	        'code' => $response->getStatusCode(),
	        'reason' => $response->getReasonPhrase(),
	        'body' => $body
	    );
	}
	
	private function request($method = 'GET', $uri) {
	    $response = $this->client->request($method, $uri, $this->options);
	    return $this->response($response);
	}
	
	public function get($uri) {
	    return $this->request('GET', $uri);
	}
	
	public function post($uri) {
		return $this->request('POST', $uri);
	}
	
	public function put($uri) {
		return $this->request('PUT', $uri);
	}
	
	public function delete($uri) {
		return $this->request('DELETE', $uri);
	}
}