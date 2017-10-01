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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.json.JSONException;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.canchitodev.cwm.domain.FileHandler;
import com.canchitodev.cwm.domain.FolderHandler;
import com.canchitodev.cwm.exception.TransferException;
import com.canchitodev.cwm.utils.enums.FileSystemType;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public final class FileHandlerClient {
	
	public FileHandlerClient() {}

	/**
	 * Connects to a remote FTP site using the information provided by the FileHandler passed as argument
	 * @param fileHandler	- An object representing a resource in a FTP network
	 * @return An instance of type FTPClient
	 * @throws JSONException				- Error when parsing the FileHandler's folder handler detail information
	 * @throws IOException					- An I/O exception was found
	 **/
	public FTPClient getFTPClient(FileHandler fileHandler) throws JSONException, IOException, SocketException {
		FTPClient client = new FTPClient();
		FolderHandler fh = fileHandler.getFolderHandler();
		
		client.connect(fh.getHost(), fh.getPort());
		// After connection attempt, you should check the reply code to verify it was successfull
		if (!FTPReply.isPositiveCompletion(client.getReplyCode()))
            throw new IOException("An I/O exception was found. FTP server refused connection");
        
		// Finally do the log in
		if(!client.login(fh.getUsername(), fh.getPassword()))
			 throw new IOException("An I/O exception was found. Incorrect username and/or password");
		
		// Now we set the transfer mode
		if(fh.getTransferMode())
			client.enterLocalPassiveMode();
		else
			client.enterLocalActiveMode();
		
		// Set file type to be transferred to binary.
		if(!client.setFileType(FTP.BINARY_FILE_TYPE))
			throw new IOException("Error when transfering file - Exception: Could not set file type");
		
		if(!client.changeWorkingDirectory(fh.getPath()))
			throw new TransferException("Could not change to source working directory '" + fh.getPath() + "'");
		
		return client;
	}
	
	/**
	 * Constructs an SmbFile representing a resource on an SMB network such as a file or directory. 
	 * @param fileHandler	- An object representing a resource in a SMB network
	 * @return a SmbFile representing a resource on a SMB network
	 * @throws JSONException			- Error when parsing the FileHandler's folder handler detail information
	 * @throws MalformedURLException	- If the parent and child parameters do not follow the prescribed syntax
	 **/
	public SmbFile getSmbFile(FileHandler fileHandler) throws JSONException, MalformedURLException {
		FolderHandler fh = fileHandler.getFolderHandler();
		
		NtlmPasswordAuthentication smbAuth = new NtlmPasswordAuthentication(null, fh.getUsername(), fh.getPassword());
		try {
			return new SmbFile(FilenameUtils.separatorsToUnix("smb://" + fh.getHost() + fh.getPath() + fileHandler.getFilename()), smbAuth);
		} catch (MalformedURLException e) {
			throw new MalformedURLException("Could not connect to Samba share");
		}
	}
	
	/**
	 * Constructs an File representing a resource on a local drive such as a file or directory.
	 * @param fileHandler	- An object representing a resource in a local drive
	 * @return a resource in a local drive
	 * @throws JSONException	- Error when parsing the FileHandler's folder handler detail information
	 **/
	public File getLocalFile(FileHandler fileHandler) throws JSONException {
		FolderHandler fh = fileHandler.getFolderHandler();
		
		return new File(FilenameUtils.separatorsToUnix(fh.getPath() + fileHandler.getFilename()));
	}
	
	/**
	 * Constructs an S3ObjectSummary representing a resource in an Amazon S3 bucket. This object doesn't contain 
	 * the object's full metadata or any of its contents.
	 * @param fileHandler	- An object representing a resource in an Amazon S3 bucket
	 * @return a resource in an Amazon S3 bucket
	 * @throws JSONException	- Error when parsing the FileHandler's folder handler detail information
	 **/
	public S3ObjectSummary getAmazonS3File(FileHandler fileHandler) throws JSONException {
		FolderHandler fh = fileHandler.getFolderHandler();
		
		S3ObjectSummary s3ObjectSummary = new S3ObjectSummary();
		s3ObjectSummary.setBucketName(fh.getBucket());
		s3ObjectSummary.setKey(FilenameUtils.separatorsToUnix(fh.getPath() + fileHandler.getFilename()));
		return s3ObjectSummary;
	}
	
	/**
	 * Creates an InputStream out of a file handler which is located in a local, SMB or FTP resource.<br>
	 * You must close the InputStream when you finish reading from it. The InputStream itself will take care of
     * closing the parent data connection socket upon being closed.
	 * @param fileHandler	- An object representing a resource in a local drive or SMB network
	 * @return An input stream representing a resource in a local drive or SMB network, null otherwise
	 * @throws JSONException			- Error when parsing the FileHandler's folder handler detail information
	 * @throws MalformedURLException	- If the parent and child parameters do not follow the prescribed syntax
	 * @throws IOException				- An I/O exception was found
	 **/
	public InputStream getInputStreamFromFileHandler(FileHandler fileHandler) throws JSONException, MalformedURLException, IOException {
		FolderHandler fh = fileHandler.getFolderHandler();
		InputStream is = null;
		
		if(fh.getFileSystemType() == FileSystemType.LOCAL.getType())
			is = new FileInputStream(getLocalFile(fileHandler));
		else if(fh.getFileSystemType() == FileSystemType.SMB.getType())
			is = getSmbFile(fileHandler).getInputStream();
		else if(fh.getFileSystemType() == FileSystemType.FTP.getType())
			is = getFTPClient(fileHandler).retrieveFileStream(fileHandler.getFilename());
		return is;
	}
	
	/**
	 * Creates an OutputStream out of a file handler which is located in a local or SMB resource.<br>
	 * You must close the OutputStream when you finish writing to it.  The OutputStream itself will 
	 * take care of closing the parent data connection socket upon being closed.
	 * @param fileHandler	- An object representing a resource in a local drive or SMB network
	 * @return An output stream representing a resource in a local drive or SMB network, null otherwise
	 * @throws JSONException			- Error when parsing the FileHandler's folder handler detail information
	 * @throws MalformedURLException	- If the parent and child parameters do not follow the prescribed syntax
	 * @throws IOException				- An I/O exception was found
	 **/
	public OutputStream getOutputStreamFromFileHandler(FileHandler fileHandler) throws JSONException, MalformedURLException, IOException {
		FolderHandler fh = fileHandler.getFolderHandler();
		OutputStream os = null;
		
		if(fh.getFileSystemType() == FileSystemType.LOCAL.getType())
			os = new FileOutputStream(getLocalFile(fileHandler));
		else if(fh.getFileSystemType() == FileSystemType.SMB.getType())
			os = getSmbFile(fileHandler).getOutputStream();
		else if(fh.getFileSystemType() == FileSystemType.FTP.getType())
			os = getFTPClient(fileHandler).storeFileStream(fileHandler.getFilename());
		return os;
	}
}