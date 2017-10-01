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
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 * 
 * http://codippa.com/how-to-autowire-objects-in-non-spring-classes/
 * https://blog.jdriven.com/2015/03/using-spring-managed-bean-in-non-managed-object/
 **/
package com.canchitodev.cwm.cloud.amazon.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.retry.PredefinedRetryPolicies;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public final class AwsClientBuilder {
	
	public AwsClientBuilder() {}
	
	/**
	 * Created an Amazon S3 client by explicitly specifying credentials and region
	 * @param accessKeyId		- The access key ID to use
	 * @param secretAccessKey	- the secret access key to use
	 * @param region			- The region to connect to
	 * @return AmazonS3 client
	 * @throws AmazonClientException
	 **/
	public AmazonS3 getAmazonS3Client(String accessKeyId, String secretAccessKey, String region) throws AmazonClientException {
		BasicAWSCredentials credentials = null;
        try {
        	credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location, and is in valid format.", e
            );
        }

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setRetryPolicy(PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(5));
        
		return AmazonS3ClientBuilder
				.standard()
				.withClientConfiguration(clientConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
	}

	/**
	 * Created an Amazon S3 client using the credential profile specified by <code>profileName</code> and region
	 * @param profileName	- The profile to use
	 * @param region		- The region to connect to
	 * @return AmazonS3 client
	 * @throws AmazonClientException
	 **/
	public AmazonS3 getAmazonS3Client(String profileName, String region) throws AmazonClientException {
		/**
         * The ProfileCredentialsProvider will return the credential profile specified by <code>profileName</code>,
         * by reading it from the credentials file located at ...\\.aws\\credentials.
         **/
		AWSCredentials credentials = null;
        try {
        	credentials = new ProfileCredentialsProvider(profileName).getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location, and is in valid format.", e
            );
        }

		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setRetryPolicy(PredefinedRetryPolicies.getDefaultRetryPolicyWithCustomMaxRetries(5));
        
		return AmazonS3ClientBuilder
				.standard()
				.withClientConfiguration(clientConfiguration)
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(region)
				.build();
	}
}