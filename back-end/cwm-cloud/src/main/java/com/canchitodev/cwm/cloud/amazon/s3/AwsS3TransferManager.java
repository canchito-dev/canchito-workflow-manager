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

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using the AWS SDK for Java.
 * 
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer account, and be signed up to use 
 * Amazon S3. For more information on Amazon S3, see http://aws.amazon.com/s3.
 * 
 * Depending on the size of the data you are uploading, Amazon S3 offers the following options:
 * 	- Upload objects in a single operation: With a single PUT operation, you can upload objects up to 5 GB in size.
 * 	- Upload objects in parts: Using the multipart upload API, you can upload large objects, up to 5 TB.
 * 
 * This class focuses on operations using the multipart upload API, which is designed to improve the upload experience 
 * for larger objects. You can upload objects in parts. These object parts can be uploaded independently, in any order, 
 * and in parallel. You can use a multipart upload for objects from 5 MB to 5 TB in size.
 * 
 * We recommend that you use multipart uploading in the following ways:
 * 	- If you're uploading large objects over a stable high-bandwidth network, use multipart uploading to maximize the 
 * 	  use of your available bandwidth by uploading object parts in parallel for multi-threaded performance.
 * 	- If you're uploading over a spotty network, use multipart uploading to increase resiliency to network errors by 
 * 	  avoiding upload restarts. When using multipart uploading, you need to retry uploading only parts that are interrupted during the upload. You don't need to restart uploading your object from the beginning.
 **/
public final class AwsS3TransferManager {
	
	public AwsS3TransferManager() {}

	/**
	 * TransferManager with configured AmazonS3 client.
	 * @param s3Client	-  The client to use
	 * @return TransferManager with configured AmazonS3 client.
	 **/
	public TransferManager buildTransferManager(AmazonS3 s3Client) {
		return TransferManagerBuilder
    			.standard()
    			.withS3Client(s3Client)
    			.build();
	}
	
	/**
	 * TransferManager with configured AmazonS3 client.
	 * @param s3Client					-  The client to use
	 * @param minimumUploadPartSize		-  New minimum threshold for upload part size
	 * @param multipartUploadThreshold	- Threshold in which multipart uploads will be performed.
	 * @return TransferManager with configured AmazonS3 client.
	 **/
	public TransferManager buildTransferManager(AmazonS3 s3Client, Integer minimumUploadPartSize, Integer multipartUploadThreshold) {
		return TransferManagerBuilder
    			.standard()
    			.withS3Client(s3Client)
    			.withMinimumUploadPartSize((long) (minimumUploadPartSize*1024*1024))
    			.withMultipartUploadThreshold((long) (multipartUploadThreshold*1024*1024))
    			.build();
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm			- The TransferManager to use
	 * @param bucket		- The name of an existing bucket to which the new object will be uploaded
	 * @param key 			- The key under which to store the new object
	 * @param file			- The path of the file to upload to Amazon S3
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	public void upload(AmazonS3 s3Client, String bucket, String key, File file) throws AmazonServiceException, AmazonClientException {
		/**
		 * Each instance of TransferManager maintains its own thread pool where transfers 
		 * are processed, so share an instance when possible
		 **/
		TransferManager tm = null;
		try {
			tm = this.buildTransferManager(s3Client);
			this.doUpload(tm, bucket, key, file);
		} catch (AmazonServiceException ase) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ "Error Message:    " + ase.getMessage()
	    			+ "HTTP Status Code: " + ase.getStatusCode()
	    			+ "AWS Error Code:   " + ase.getErrorCode()
	    			+ "Error Type:       " + ase.getErrorType()
	    			+ "Request ID:       " + ase.getRequestId();
	    	throw new AmazonServiceException(msg);
	    } catch (AmazonClientException ace) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ ace.getMessage();
	    	throw new AmazonClientException(msg);
	    } finally {
	    	// After the download is complete, call shutdownNow to release the resources.
			if(tm != null)
		    	tm.shutdownNow();
		}
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm						- The TransferManager to use
	 * @param bucket					- The name of an existing bucket to which the new object will be uploaded
	 * @param key 						- The key under which to store the new object
	 * @param file						- The path of the file to upload to Amazon S3
	 * @param minimumUploadPartSize		-  New minimum threshold for upload part size
	 * @param multipartUploadThreshold	- Threshold in which multipart uploads will be performed.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	public void upload(AmazonS3 s3Client, String bucket, String key, File file, Integer minimumUploadPartSize, Integer multipartUploadThreshold) throws AmazonServiceException, AmazonClientException {
		/**
		 * Each instance of TransferManager maintains its own thread pool where transfers 
		 * are processed, so share an instance when possible
		 **/
		TransferManager tm = null;
		try {
			tm = this.buildTransferManager(s3Client, minimumUploadPartSize, multipartUploadThreshold);
			this.doUpload(tm, bucket, key, file);
		} catch (AmazonServiceException ase) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ "Error Message:    " + ase.getMessage()
	    			+ "HTTP Status Code: " + ase.getStatusCode()
	    			+ "AWS Error Code:   " + ase.getErrorCode()
	    			+ "Error Type:       " + ase.getErrorType()
	    			+ "Request ID:       " + ase.getRequestId();
	    	throw new AmazonServiceException(msg);
	    } catch (AmazonClientException ace) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ ace.getMessage();
	    	throw new AmazonClientException(msg);
	    } finally {
	    	// After the download is complete, call shutdownNow to release the resources.
			if(tm != null)
		    	tm.shutdownNow();
		}
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm			- The TransferManager to use
	 * @param bucket		- The name of an existing bucket to which the new object will be uploaded.
	 * @param key			- The key under which to store the new object
	 * @param inputStream	- The stream of data to upload to Amazon S3
	 * @param contentLength	- The content length for the stream of data being uploaded.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	public void upload(AmazonS3 s3Client, String bucket, String key, InputStream inputStream, Long contentLength) throws AmazonServiceException, AmazonClientException {
		/**
		 * Each instance of TransferManager maintains its own thread pool where transfers 
		 * are processed, so share an instance when possible
		 **/
		TransferManager tm = null;
		try {
			tm = this.buildTransferManager(s3Client);
			this.doUpload(tm, bucket, key, inputStream, contentLength);
		} catch (AmazonServiceException ase) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ "Error Message:    " + ase.getMessage()
	    			+ "HTTP Status Code: " + ase.getStatusCode()
	    			+ "AWS Error Code:   " + ase.getErrorCode()
	    			+ "Error Type:       " + ase.getErrorType()
	    			+ "Request ID:       " + ase.getRequestId();
	    	throw new AmazonServiceException(msg);
	    } catch (AmazonClientException ace) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ ace.getMessage();
	    	throw new AmazonClientException(msg);
	    } finally {
	    	// After the download is complete, call shutdownNow to release the resources.
			if(tm != null)
		    	tm.shutdownNow();
		}
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm			- The TransferManager to use
	 * @param bucket		- The name of an existing bucket to which the new object will be uploaded.
	 * @param key			- The key under which to store the new object
	 * @param inputStream	- The stream of data to upload to Amazon S3
	 * @param contentLength	- The content length for the stream of data being uploaded.
	 * @param minimumUploadPartSize		-  New minimum threshold for upload part size
	 * @param multipartUploadThreshold	- Threshold in which multipart uploads will be performed.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	public void upload(AmazonS3 s3Client, String bucket, String key, InputStream inputStream, Long contentLength, 
			Integer minimumUploadPartSize, Integer multipartUploadThreshold) throws AmazonServiceException, AmazonClientException {
		/**
		 * Each instance of TransferManager maintains its own thread pool where transfers 
		 * are processed, so share an instance when possible
		 **/
		TransferManager tm = null;
		try {
			tm = this.buildTransferManager(s3Client, minimumUploadPartSize, multipartUploadThreshold);
			this.doUpload(tm, bucket, key, inputStream, contentLength);
		} catch (AmazonServiceException ase) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ "Error Message:    " + ase.getMessage()
	    			+ "HTTP Status Code: " + ase.getStatusCode()
	    			+ "AWS Error Code:   " + ase.getErrorCode()
	    			+ "Error Type:       " + ase.getErrorType()
	    			+ "Request ID:       " + ase.getRequestId();
	    	throw new AmazonServiceException(msg);
	    } catch (AmazonClientException ace) {
	    	String msg = "There was an error when uploading file '" + key + "' to Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ ace.getMessage();
	    	throw new AmazonClientException(msg);
	    } finally {
	    	// After the download is complete, call shutdownNow to release the resources.
			if(tm != null)
		    	tm.shutdownNow();
		}
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm			- The TransferManager to use
	 * @param bucket		- The name of an existing bucket to which the new object will be uploaded
	 * @param key 			- The key under which to store the new object
	 * @param file			- The path of the file to upload to Amazon S3
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	private void doUpload(TransferManager tm, String bucket, String key, File file) throws AmazonServiceException, AmazonClientException {
		/**
		 * The upload and download methods return immediately, while TransferManager 
		 * processes the transfer in the background thread pool
		 **/
    	Upload upload = tm.upload(new PutObjectRequest(bucket, key, file));
    	
    	/**
    	 * Or you can block the current thread and wait for your transfer to complete. 
    	 * If the transfer fails, this method will throw an AmazonClientException or 
    	 * AmazonServiceException detailing the reason.
    	 **/
    	while(!upload.isDone()) {}
//    	upload.waitForCompletion();
	}
	
	/**
	 * Schedules a new transfer to upload data to Amazon S3. 
	 * @param tm			- The TransferManager to use
	 * @param bucket		- The name of an existing bucket to which the new object will be uploaded.
	 * @param key			- The key under which to store the new object
	 * @param inputStream	- The stream of data to upload to Amazon S3
	 * @param contentLength	- The content length for the stream of data being uploaded.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	private void doUpload(TransferManager tm, String bucket, String key, InputStream inputStream, Long contentLength) 
			throws AmazonServiceException, AmazonClientException {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(contentLength);
		
		/**
		 * The upload and download methods return immediately, while TransferManager 
		 * processes the transfer in the background thread pool
		 **/
    	Upload upload = tm.upload(new PutObjectRequest(bucket, key, inputStream, objectMetadata));
    	
    	/**
    	 * Or you can block the current thread and wait for your transfer to complete. 
    	 * If the transfer fails, this method will throw an AmazonClientException or 
    	 * AmazonServiceException detailing the reason.
    	 **/
    	while(!upload.isDone()) {}
//    	upload.waitForCompletion();
	}
	
	/**
	 * Schedules a new transfer to download data from Amazon S3 and save it to the specified file.
	 * @param tm						- The TransferManager to use
	 * @param bucket					- The name of the bucket containing the object to download
	 * @param key						- The key under which the object to download is stored
	 * @param file						- The file to download the object's data to.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	public void download(AmazonS3 s3Client, String bucket, String key, File file) throws AmazonServiceException, AmazonClientException {
		/**
		 * Each instance of TransferManager maintains its own thread pool where transfers 
		 * are processed, so share an instance when possible
		 **/
		TransferManager tm = null;
		try {
			tm = this.buildTransferManager(s3Client);
			this.doDownload(tm, bucket, key, file);
		} catch (AmazonServiceException ase) {
	    	String msg = "There was an error when downloading file '" + key + "' from Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ "Error Message:    " + ase.getMessage()
	    			+ "HTTP Status Code: " + ase.getStatusCode()
	    			+ "AWS Error Code:   " + ase.getErrorCode()
	    			+ "Error Type:       " + ase.getErrorType()
	    			+ "Request ID:       " + ase.getRequestId();
	    	throw new AmazonServiceException(msg);
	    } catch (AmazonClientException ace) {
	    	String msg = "There was an error when downloading file '" + key + "' from Amazon S3 bucket '" + bucket + "'. - Exception: "
	    			+ ace.getMessage();
	    	throw new AmazonServiceException(msg);
	    } finally {
	    	// After the download is complete, call shutdownNow to release the resources.
			if(tm != null)
		    	tm.shutdownNow();
		}
	}
	
	/**
	 * Schedules a new transfer to download data from Amazon S3 and save it to the specified file.
	 * @param tm		- The TransferManager to use
	 * @param bucket	- The name of the bucket containing the object to download
	 * @param key		- The key under which the object to download is stored
	 * @param file		- The file to download the object's data to.
	 * @throws AmazonServiceException - If any errors occurred in Amazon S3 while processing the request.
	 * @throws AmazonClientException - If any errors are encountered in the client while making the request or handling the response.
	 **/
	private void doDownload(TransferManager tm, String bucket, String key, File file) 
			throws AmazonServiceException, AmazonClientException {
		/**
		 * The upload and download methods return immediately, while TransferManager 
		 * processes the transfer in the background thread pool
		 **/
    	Download download = tm.download(bucket, key, file);
    	
    	/**
    	 * Or you can block the current thread and wait for your transfer to complete. 
    	 * If the transfer fails, this method will throw an AmazonClientException or 
    	 * AmazonServiceException detailing the reason.
    	 **/
    	while(!download.isDone()) {}
//    	download.waitForCompletion();
	}
}