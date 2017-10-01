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
 * @author 		José Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/aws-s3-showcase
 **/
package com.canchitodev.cwm.cloud.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="amazon")
public class AmazonProperties {
	
	// Credential properties
	private String credentialsAccessKeyId = "credentialsAccessKeyId";
	private String credentialsSecretAccessKey = "credentialsSecretAccessKey";
	
	// Configuration properties
	private String configurationRegion = "configurationRegion";
	
	// TransferManager properties
	private Integer tmMinimumUploadPartSize = 64;
	private Integer tmMultipartUploadThreshold = 16;
	
	public String getCredentialsAccessKeyId() {
		return credentialsAccessKeyId;
	}
	public void setCredentialsAccessKeyId(String credentialsAccessKeyId) {
		this.credentialsAccessKeyId = credentialsAccessKeyId;
	}
	
	public String getCredentialsSecretAccessKey() {
		return credentialsSecretAccessKey;
	}
	public void setCredentialsSecretAccessKey(String credentialsSecretAccessKey) {
		this.credentialsSecretAccessKey = credentialsSecretAccessKey;
	}
	
	public String getConfigurationRegion() {
		return configurationRegion;
	}
	public void setConfigurationRegion(String configurationRegion) {
		this.configurationRegion = configurationRegion;
	}
	
	public Integer getTmMinimumUploadPartSize() {
		return tmMinimumUploadPartSize;
	}
	public void setTmMinimumUploadPartSize(Integer tmMinimumUploadPartSize) {
		this.tmMinimumUploadPartSize = tmMinimumUploadPartSize;
	}
	
	public Integer getTmMultipartUploadThreshold() {
		return tmMultipartUploadThreshold;
	}
	public void setTmMultipartUploadThreshold(Integer tmMultipartUploadThreshold) {
		this.tmMultipartUploadThreshold = tmMultipartUploadThreshold;
	}
	
	@Override
	public String toString() {
		return "AmazonProperties [credentialsAccessKeyId=" + credentialsAccessKeyId + ", credentialsSecretAccessKey="
				+ credentialsSecretAccessKey + ", configurationRegion=" + configurationRegion
				+ ", tmMinimumUploadPartSize=" + tmMinimumUploadPartSize + ", tmMultipartUploadThreshold="
				+ tmMultipartUploadThreshold + "]";
	}
}