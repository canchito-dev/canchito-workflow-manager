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
 **/
package com.canchitodev.cwm.tasks.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.canchitodev.cwm.cloud.amazon.s3.AwsClientBuilder;
import com.canchitodev.cwm.cloud.amazon.s3.AwsS3BasicManager;
import com.canchitodev.cwm.cloud.amazon.s3.AwsS3TransferManager;
import com.canchitodev.cwm.cloud.configuration.properties.AmazonProperties;
import com.canchitodev.cwm.domain.FileHandler;
import com.canchitodev.cwm.exception.SmbTransferException;
import com.canchitodev.cwm.exception.TransferException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

@Component
@EnableConfigurationProperties(AmazonProperties.class)
public final class FileHandlerTransferManager {
	
	private static final Logger logger = Logger.getLogger(FileHandlerTransferManager.class);
	
	@Autowired
	private AmazonProperties amazonProperties;
	
	private AwsClientBuilder awsClientBuilder;
	private AwsS3TransferManager awsS3TransferManager;
	private AwsS3BasicManager awsS3BasicManager;
	private FileHandlerClient fileHandlerClient;

	public FileHandlerTransferManager() {
		this.awsClientBuilder = new AwsClientBuilder();
		this.awsS3TransferManager = new AwsS3TransferManager();
		this.awsS3BasicManager = new AwsS3BasicManager();
		this.fileHandlerClient = new FileHandlerClient();
	}
	
	/**
	 * Transfers a file from a SMB network resource to another SMB network resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A SMB network resource used as source
	 * @param to			- A SMB network resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws SmbTransferException
	 **/
	public void transferFromSmbToSmb(FileHandler from, FileHandler to, Boolean deleteFrom) throws SmbTransferException {
		try {
			SmbFile smbSource = this.fileHandlerClient.getSmbFile(from);
			SmbFile smbDestination = this.fileHandlerClient.getSmbFile(to);
			
			smbSource.copyTo(smbDestination);
			
			if(deleteFrom)
				smbSource.delete();
			
		} catch (MalformedURLException | JSONException | SmbException e) {
			throw new SmbTransferException("Error when transfering file - Exception: " + e.getMessage());
		}
	}
	
	/**
	 * Transfers a file from a SMB network resource to a FTP network resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A SMB network resource used as source
	 * @param to			- A FTP network resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromSmbToFtp(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		InputStream smbInputStream = null;
		FTPClient ftpDestination = null;
		
		try {
			SmbFile smbSource = this.fileHandlerClient.getSmbFile(from);
			smbInputStream = smbSource.getInputStream();
			ftpDestination = this.fileHandlerClient.getFTPClient(to);
			
			if(!ftpDestination.storeFile(to.getFilename(), smbInputStream))
				throw new TransferException("Could not store file from SMB network resource '" 
						+ from.getFolderHandler().getPath() + "' to FTP network resource '" + to.getFolderHandler().getPath() + "'");
			
			if(deleteFrom)
				smbSource.delete();
			
		} catch (JSONException | IOException  e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(smbInputStream != null)
					smbInputStream.close();
			} catch (IOException e) {}

			try {
				if(ftpDestination != null) {
					if(!ftpDestination.logout())
						logger.warn("Could not log out from FTP server with IP '" + to.getFolderHandler().getHost() + "'");
					
					if(ftpDestination.isConnected())
						ftpDestination.disconnect();
				}
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a SMB network resource to a local resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A SMB network resource used as source
	 * @param to			- A local resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromSmbToLocal(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		InputStream smbInputStream = null;
		OutputStream localOutputStream = null;
		
		try {
			SmbFile smbSource = this.fileHandlerClient.getSmbFile(from);
			smbInputStream = smbSource.getInputStream();
			localOutputStream = this.fileHandlerClient.getOutputStreamFromFileHandler(to);
			
			byte[] buffer = new byte[8192];
	        int bytesRead;
	        //read from is to buffer
	        while((bytesRead = smbInputStream.read(buffer)) != -1){
	        	localOutputStream.write(buffer, 0, bytesRead);
	        }
			
			if(deleteFrom)
				smbSource.delete();
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(smbInputStream != null)
					smbInputStream.close();
			} catch (IOException e) {}

			try {
				if(localOutputStream != null)
					localOutputStream.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a SMB network resource to an Amazon S3 bucket. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A SMB network resource used as source
	 * @param to			- An Amazon S3 bucket used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromSmbToAmazonS3(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		AmazonS3 s3Client = this.awsClientBuilder.getAmazonS3Client(
				amazonProperties.getCredentialsAccessKeyId(), 
				amazonProperties.getCredentialsSecretAccessKey(), 
				amazonProperties.getConfigurationRegion()
		);
		
		InputStream smbInputStream = null;
		
		try {
			SmbFile smbSource = this.fileHandlerClient.getSmbFile(from);
			smbInputStream = smbSource.getInputStream();
			
			S3ObjectSummary s3Destination = this.fileHandlerClient.getAmazonS3File(to);
			
			this.awsS3TransferManager.upload(s3Client, 
					s3Destination.getBucketName(), 
					s3Destination.getKey(), 
					smbInputStream, ((Integer) smbSource.getContentLength()).longValue());
			
			if(deleteFrom)
				smbSource.delete();
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(smbInputStream != null)
					smbInputStream.close();
			} catch (IOException e) {}
		}
	}

	/**
	 * Transfers a file from a FTP network resource to a SMB network resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A FTP network resource used as source
	 * @param to			- A SMB network resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromFtpToSmb(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		FTPClient ftpSource = null;
		OutputStream smbOutputStream = null;
		
		try {
			ftpSource = this.fileHandlerClient.getFTPClient(from);
			SmbFile smbDestination = this.fileHandlerClient.getSmbFile(to);
			smbOutputStream = smbDestination.getOutputStream();
			
			if(!ftpSource.retrieveFile(from.getFilename(), smbOutputStream))
				throw new TransferException("Could not retrieve file from FTP network resource '" 
						+ from.getFolderHandler().getPath() + "' to SMB network resource '" + to.getFolderHandler().getPath() + "'");
			
			if(deleteFrom)
				ftpSource.deleteFile(from.getFilename());
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(smbOutputStream != null)
					smbOutputStream.close();
			} catch (IOException e) {}

			try {
				if(ftpSource != null) {
					if(!ftpSource.logout())
						logger.warn("Could not log out from FTP server with IP '" + from.getFolderHandler().getHost() + "'");
					
					if(ftpSource.isConnected())
						ftpSource.disconnect();
				}
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a FTP network resource to a SMB network resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A FTP network resource used as source
	 * @param to			- A SMB network resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromFtpToFtp(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		FTPClient ftpSource = null;
		FTPClient ftpDestination = null;
		InputStream ftpInputStream = null;
		
		try {
			ftpSource = this.fileHandlerClient.getFTPClient(from);
			ftpDestination = this.fileHandlerClient.getFTPClient(to);

			ftpInputStream = ftpSource.retrieveFileStream(from.getFilename());
			
			if(!ftpDestination.storeFile(to.getFilename(), ftpInputStream))
				throw new TransferException("Could not store file from FTP network resource '" 
						+ from.getFolderHandler().getPath() + "' to FTP network resource '" + to.getFolderHandler().getPath() + "'");
			
			if(deleteFrom)
				ftpSource.deleteFile(from.getFilename());
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(ftpInputStream != null)
					ftpInputStream.close();
			} catch (IOException e) {}
			
			try {
				if(ftpSource != null) {
					// Must call completePendingCommand() to finish command.
					if(!ftpSource.completePendingCommand())
						throw new TransferException("Error when transfering file - Exception: Could not complete transfer");
					
					if(!ftpSource.logout())
						logger.warn("Could not log out from FTP server with IP '" + from.getFolderHandler().getHost() + "'");
					
					if(ftpSource.isConnected())
						ftpSource.disconnect();
				}
			} catch (IOException e) {}
			
			try {
				if(ftpDestination != null) {
					if(!ftpDestination.logout())
						logger.warn("Could not log out from FTP server with IP '" + to.getFolderHandler().getHost() + "'");
					
					if(ftpDestination.isConnected())
						ftpDestination.disconnect();
				}
			} catch (IOException e) {}
		}
	}

	/**
	 * Transfers a file from a FTP network resource to a local resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A FTP network resource used as source
	 * @param to			- A local resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromFtpToLocal(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		FTPClient ftpSource = null;
		OutputStream localOutputSteam = null;
		
		try {
			ftpSource = this.fileHandlerClient.getFTPClient(from);
			localOutputSteam = this.fileHandlerClient.getOutputStreamFromFileHandler(to);
			
			if(!ftpSource.retrieveFile(from.getFilename(), localOutputSteam))
				throw new TransferException("Could not retrieve file from FTP network resource '" 
						+ from.getFolderHandler().getPath() + "' to local resource '" + to.getFolderHandler().getPath() + "'");
			
			if(deleteFrom)
				ftpSource.deleteFile(from.getFilename());
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(localOutputSteam != null)
					localOutputSteam.close();
			} catch (IOException e) {}
			
			try {
				if(ftpSource != null) {
					if(!ftpSource.logout())
						logger.warn("Could not log out from FTP server with IP '" + from.getFolderHandler().getHost() + "'");
					
					if(ftpSource.isConnected())
						ftpSource.disconnect();
				}
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a FTP network resource to an Amazon S3 bucket. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A FTP network resource used as source
	 * @param to			- An Amazon S3 bucket used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromFtpToAmazonS3(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		FTPClient ftpSource = null;
		
		AmazonS3 s3Client = this.awsClientBuilder.getAmazonS3Client(
				amazonProperties.getCredentialsAccessKeyId(), 
				amazonProperties.getCredentialsSecretAccessKey(), 
				amazonProperties.getConfigurationRegion()
		);
		
		InputStream ftpInputStream = null;
		
		try {
			ftpSource = this.fileHandlerClient.getFTPClient(from);
			FTPFile fileFtpSource = ftpSource.mlistFile(from.getFilename());
			Long ftpSourceSize = fileFtpSource.getSize();
			
			ftpInputStream = ftpSource.retrieveFileStream(from.getFilename());
			
			S3ObjectSummary s3Destination = this.fileHandlerClient.getAmazonS3File(to);
			
			this.awsS3TransferManager.upload(s3Client, 
					s3Destination.getBucketName(), 
					s3Destination.getKey(), 
					ftpInputStream, ftpSourceSize);
			
			if(deleteFrom)
				ftpSource.deleteFile(from.getFilename());
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(ftpInputStream != null)
					ftpInputStream.close();
			} catch (IOException e) {}
			
			try {
				if(ftpSource != null) {
					// Must call completePendingCommand() to finish command.
					if(!ftpSource.completePendingCommand())
						throw new TransferException("Error when transfering file - Exception: Could not complete transfer");

					if(!ftpSource.logout())
						logger.warn("Could not log out from FTP server with IP '" + from.getFolderHandler().getHost() + "'");
					
					if(ftpSource.isConnected())
						ftpSource.disconnect();
				}
			} catch (IOException e) {}
		}
	}

	/**
	 * Transfers a file from a local resource to another local resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A local resource used as source
	 * @param to			- A local resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromLocalToLocal(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		InputStream localInputStream = null;
		OutputStream localOutputStream = null;
		
		try {
			File fileSource = this.fileHandlerClient.getLocalFile(from);
			localInputStream = this.fileHandlerClient.getInputStreamFromFileHandler(from);
			localOutputStream = this.fileHandlerClient.getOutputStreamFromFileHandler(to);
			
			byte[] buffer = new byte[8192];
	        int bytesRead;
	        //read from is to buffer
	        while((bytesRead = localInputStream.read(buffer)) != -1){
	        	localOutputStream.write(buffer, 0, bytesRead);
	        }
			
			if(deleteFrom)
				fileSource.delete();
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(localInputStream != null)
					localInputStream.close();
			} catch (IOException e) {}

			try {
				if(localOutputStream != null)
					localOutputStream.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a local resource to a SMB network resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A local resource used as source
	 * @param to			- A SMB network resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromLocalToSmb(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		InputStream localInputStream = null;
		OutputStream smbOutputStream = null;
		
		try {
			File fileSource = this.fileHandlerClient.getLocalFile(from);
			localInputStream = this.fileHandlerClient.getInputStreamFromFileHandler(from);
			SmbFile smbSource = this.fileHandlerClient.getSmbFile(to);
			smbOutputStream = smbSource.getOutputStream();
			
			byte[] buffer = new byte[8192];
	        int bytesRead;
	        //read from is to buffer
	        while((bytesRead = localInputStream.read(buffer)) != -1){
	        	smbOutputStream.write(buffer, 0, bytesRead);
	        }
			
			if(deleteFrom)
				fileSource.delete();
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(localInputStream != null)
					localInputStream.close();
			} catch (IOException e) {}

			try {
				if(smbOutputStream != null)
					smbOutputStream.close();
			} catch (IOException e) {}
		}
	}
	
	/**
	 * Transfers a file from a FTP network resource to a local resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A FTP network resource used as source
	 * @param to			- A local resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromLocalToFtp(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		InputStream localInputStream = null;
		FTPClient ftpDestination = null;
		
		try {
			File fileSource = this.fileHandlerClient.getLocalFile(from);
			localInputStream = this.fileHandlerClient.getInputStreamFromFileHandler(from);
			
			ftpDestination = this.fileHandlerClient.getFTPClient(to);
			
			if(!ftpDestination.storeFile(to.getFilename(), localInputStream))
				throw new TransferException("Could not store file from local resource '" 
						+ from.getFolderHandler().getPath() + "' to FTP network resource '" + to.getFolderHandler().getPath() + "'");
			
			if(deleteFrom)
				fileSource.delete();
			
		} catch (JSONException | IOException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		} finally {
			try {
				if(localInputStream != null)
					localInputStream.close();
			} catch (IOException e) {}

			try {
				if(ftpDestination != null) {
					if(!ftpDestination.logout())
						logger.warn("Could not log out from FTP server with IP '" + to.getFolderHandler().getHost() + "'");
					
					if(ftpDestination.isConnected())
						ftpDestination.disconnect();
				}
			} catch (IOException e) {}
		}
	}

	/**
	 * Transfers a file from a local resource to an Amazon S3 bucket. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- A local resource used as source
	 * @param to			- An Amazon S3 bucket used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromLocalToAmazonS3(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		AmazonS3 s3Client = this.awsClientBuilder.getAmazonS3Client(
				amazonProperties.getCredentialsAccessKeyId(), 
				amazonProperties.getCredentialsSecretAccessKey(), 
				amazonProperties.getConfigurationRegion()
		);
		
		try {
			File fileSource = this.fileHandlerClient.getLocalFile(from);
			
			S3ObjectSummary s3Destination = this.fileHandlerClient.getAmazonS3File(to);
			
			this.awsS3TransferManager.upload(s3Client, s3Destination.getBucketName(), 
					s3Destination.getKey(), fileSource);
			
			if(deleteFrom)
				fileSource.delete();
			
		} catch (JSONException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		}
	}
	
	/**
	 * Transfers a file from an Amazon S3 bucket to a local resource. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- An Amazon S3 bucket used as source
	 * @param to			- A local resource used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromAmazonS3ToLocal(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		AmazonS3 s3Client = this.awsClientBuilder.getAmazonS3Client(
				amazonProperties.getCredentialsAccessKeyId(), 
				amazonProperties.getCredentialsSecretAccessKey(), 
				amazonProperties.getConfigurationRegion()
		);
		
		try {
			File localDestination = this.fileHandlerClient.getLocalFile(to);
			
			S3ObjectSummary s3Source = this.fileHandlerClient.getAmazonS3File(from);
			
			this.awsS3TransferManager.download(s3Client, s3Source.getBucketName(), s3Source.getKey(), localDestination);
			
			if(deleteFrom)
				this.awsS3BasicManager.deleteObject(s3Client, s3Source.getBucketName(), s3Source.getKey());
			
		} catch (JSONException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		}
	}
	
	/**
	 * Transfers a file from an Amazon S3 bucket to an Amazon S3 bucket. <br>
	 * <strong>NOTE:</strong> If <code>deleteFrom</code> is set to true, the source file will be delete 
	 * afterwards
	 * @param from			- An Amazon S3 bucket used as source
	 * @param to			- An Amazon S3 bucket used as destination
	 * @param deleteFrom	- If true, source will be deleted at the end
	 * @throws TransferException
	 **/
	public void transferFromAmazonS3ToAmazonS3(FileHandler from, FileHandler to, Boolean deleteFrom) throws TransferException {
		AmazonS3 s3Client = this.awsClientBuilder.getAmazonS3Client(
				amazonProperties.getCredentialsAccessKeyId(), 
				amazonProperties.getCredentialsSecretAccessKey(), 
				amazonProperties.getConfigurationRegion()
		);
		
		try {
			S3ObjectSummary s3Source = this.fileHandlerClient.getAmazonS3File(from);
			S3ObjectSummary s3Destination = this.fileHandlerClient.getAmazonS3File(to);
			
			this.awsS3BasicManager.copyObject(
					s3Client, 
					s3Source.getBucketName(), 
					s3Source.getKey(), 
					s3Destination.getBucketName(), 
					s3Destination.getKey()
			);
			
			if(deleteFrom)
				this.awsS3BasicManager.deleteObject(s3Client, s3Source.getBucketName(), s3Source.getKey());
			
		} catch (JSONException e) {
			throw new TransferException("Error when transfering file - Exception: " + e.getMessage());
		}
	}
}