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
package com.canchitodev.cwm.tasks.runnable;

import org.flowable.engine.RuntimeService;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.canchitodev.cwm.domain.FileHandler;
import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.exception.TransferException;
import com.canchitodev.cwm.tasks.utils.FileHandlerTransferManager;
import com.canchitodev.cwm.threadpool.runnable.TaskRunnable;
import com.canchitodev.cwm.utils.ApplicationContextProvider;
import com.canchitodev.cwm.utils.enums.FileSystemType;

public class MoveTaskRunnable implements TaskRunnable {
	
	private static final Logger logger = Logger.getLogger(MoveTaskRunnable.class);
	
	private GenericTaskEntity task;
	private RuntimeService runtimeService;
	private FileHandlerTransferManager fileHandlerTransferManager;

	public MoveTaskRunnable() {
		this.runtimeService = ApplicationContextProvider.getApplicationContext().getBean(RuntimeService.class);
		this.fileHandlerTransferManager = ApplicationContextProvider.getApplicationContext().getBean(FileHandlerTransferManager.class);
	}
	
	public MoveTaskRunnable(GenericTaskEntity task) {
		this();
		this.task = task;
	}

	public GenericTaskEntity getTask() {
		return task;
	}

	public void setTask(GenericTaskEntity task) {
		this.task = task;
	}

	@Override
	public void execute() {
		try {
			logger.info("Executing task " + task.toString());
			
			FileHandler from = (FileHandler) this.runtimeService.getVariable(this.task.getExecutionId(), this.task.getDetails().getString("sourceFileHandler"));
			FileHandler to = (FileHandler) this.runtimeService.getVariable(this.task.getExecutionId(), this.task.getDetails().getString("destinationFileHandler"));
			
			// Copy from local to local file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType())
				this.fileHandlerTransferManager.transferFromLocalToLocal(from, to, true);
			
			// Copy from local to SMB file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType())
				this.fileHandlerTransferManager.transferFromLocalToSmb(from, to, true);
			
			// Copy from local to FTP file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType())
				this.fileHandlerTransferManager.transferFromLocalToFtp(from, to, true);
			
			// Copy from local to Amazon S3 file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType())
				this.fileHandlerTransferManager.transferFromLocalToAmazonS3(from, to, true);
			
			// Copy from FTP to Local filey system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType())
				this.fileHandlerTransferManager.transferFromFtpToLocal(from, to, true);
			
			// Copy from FTP to SMB filey system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType())
				this.fileHandlerTransferManager.transferFromFtpToSmb(from, to, true);
			
			// Copy from FTP to FTP filey system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType())
				this.fileHandlerTransferManager.transferFromFtpToFtp(from, to, true);
			
			// Copy from FTP to Amazon S3 filey system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType())
				this.fileHandlerTransferManager.transferFromFtpToAmazonS3(from, to, true);
			
			// Copy from SMB to Local file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType())
				this.fileHandlerTransferManager.transferFromSmbToLocal(from, to, true);
			
			// Copy from SMB to SMB file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType())
				this.fileHandlerTransferManager.transferFromSmbToSmb(from, to, true);
			
			// Copy from SMB to FTP file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.FTP.getType())
				this.fileHandlerTransferManager.transferFromSmbToFtp(from, to, true);
			
			// Copy from SMB to Amazon S3 file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.SMB.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType())
				this.fileHandlerTransferManager.transferFromSmbToAmazonS3(from, to, true);
		
			// Copy from Amazon S3 file system to Amazon S3 file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType())
				this.fileHandlerTransferManager.transferFromAmazonS3ToAmazonS3(from, to, true);
			
			// Copy from Amazon S3 file system to local file system
			if(from.getFolderHandler().getFileSystemType() == FileSystemType.AMAZON_S3.getType()
					&& to.getFolderHandler().getFileSystemType() == FileSystemType.LOCAL.getType())
				this.fileHandlerTransferManager.transferFromAmazonS3ToLocal(from, to, true);
			
		} catch (JSONException | TransferException e) {
			String msg = "Could not successfully execute MOVE task. Exception: " + e.getMessage();
			logger.error(msg);
			throw new TransferException(msg);
		} finally {
			logger.info("Done executing task " + task.toString());
		}
	}
}