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

import java.io.File;
import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using the
 * AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * 
 * Depending on the size of the data you are uploading, Amazon S3 offers the following options:
 * 	- Upload objects in a single operation: With a single PUT operation, you can upload objects up to 5 GB in size.
 * 	- Upload objects in parts: Using the multipart upload API, you can upload large objects, up to 5 TB.
 * 
 * This class focuses on uploads using a single operation.
 **/
public final class AwsS3BasicManager {
	
	public AwsS3BasicManager() {}

	/**
	 * Lists the objects found in a bucket
	 * @param s3Client		- The client to use
	 * @param bucketName	- The bucket which objects will be listed
	 * @return An ObjectListing object that provides information about the objects in the bucket
     * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
     * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public ObjectListing listObjectsInBucket(AmazonS3 s3Client, String bucketName)
			throws SdkClientException, AmazonServiceException {
		return s3Client.listObjects(bucketName);
	}
	
	/**
	 * Copies a source object to a new destination in Amazon S3.
	 * @param s3Client		- The client to use
	 * @param fromBucket	- The name of the bucket containing the source object to copy.
	 * @param fromObjectKey	- The key in the source bucket under which the source object is stored.
	 * @param toBucket		- The name of the bucket in which the new object will be created. This can be the same name as the source bucket's.
	 * @param toObjectKey	- The key in the destination bucket under which the new object will be created.
	 * @return A CopyObjectResult object containing the information returned by Amazon S3 for the newly created object.
     * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
     * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public CopyObjectResult copyObject(AmazonS3 s3Client, String fromBucket, String fromObjectKey, String toBucket, String toObjectKey) 
			throws SdkClientException, AmazonServiceException {
		return s3Client.copyObject(fromBucket, fromObjectKey, toBucket, toObjectKey);
	}
	
	/**
	 * Deletes the specified object in the specified bucket. The specified bucket and object key must exist, or an error will result.
	 * @param s3Client		- The client to use
	 * @param bucketName	- The name of the Amazon S3 bucket containing the object to delete.
	 * @param objectKey		- The key of the object to delete.
	 * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public void deleteObject(AmazonS3 s3Client, String bucketName, String objectKey) throws SdkClientException, AmazonServiceException {
		s3Client.deleteObject(bucketName, objectKey);
	}
	
	/**
	 * A PutObjectResult object containing the information returned by Amazon S3 for the newly created object.
	 * @param s3Client		- The client to use
	 * @param bucketName	- The name of an existing bucket, to which you have Permission.Write permission.
	 * @param objectKey		- The key under which to store the specified file.
	 * @param file			- The file containing the data to be uploaded to Amazon S3.
	 * @return A PutObjectResult object containing the information returned by Amazon S3 for the newly created object.
	 * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public PutObjectResult uploadObject(AmazonS3 s3Client, String bucketName, String objectKey, File file) 
			throws SdkClientException, AmazonServiceException {
		return s3Client.putObject(bucketName, objectKey, file);
	}
	
	/**
	 * A PutObjectResult object containing the information returned by Amazon S3 for the newly created object.
	 * Content length must be specified before data can be uploaded to Amazon S3.
	 * @param s3Client			- The client to use
	 * @param bucketName		- The name of an existing bucket, to which you have Permission.Write permission.
	 * @param objectKey			- The key under which to store the specified file.
	 * @param inputStream		- The input stream containing the data to be uploaded to Amazon S3.
	 * @param objectMetadata	- Additional metadata instructing Amazon S3 how to handle the uploaded data (e.g. custom user 
	 * 							  metadata, hooks for specifying content type, etc.).
	 * @return A PutObjectResult object containing the information returned by Amazon S3 for the newly created object.
	 * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public PutObjectResult uploadObject(AmazonS3 s3Client, String bucketName, String objectKey, InputStream inputStream, ObjectMetadata objectMetadata) 
			throws SdkClientException, AmazonServiceException {
		return s3Client.putObject(bucketName, objectKey, inputStream, objectMetadata);
	}
	
	/**
	 * Gets the object stored in Amazon S3 under the specified bucket and key. 
	 * @param s3Client			- The client to use
	 * @param bucketName		- The name of the bucket containing the desired object.
	 * @param objectKey			- The key under which the desired object is stored.
	 * @return The object stored in Amazon S3 in the specified bucket and key.
	 * @throws SdkClientException - If any errors are encountered in the client while making the request or handling the response.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 **/
	public S3Object downloadObject(AmazonS3 s3Client, String bucketName, String objectKey) 
			throws SdkClientException, AmazonServiceException {
		return s3Client.getObject(bucketName, objectKey);
	}
}