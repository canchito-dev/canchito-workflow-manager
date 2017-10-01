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
package com.canchitodev.cwm.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Entity
@Table(name = "cwm_folder_handler")
public class FolderHandler implements Serializable {

	private static final long serialVersionUID = -112223823739969480L;

	@Id	
	@Column(name = "FOLDER_HANDLER_ID_", nullable = false)
	private Long folderHandlerId;
	
	@Column(name = "NAME_", nullable = false, length = 60)
	private String name;
	
	@Column(name = "DESCRIPTION_", nullable = true, length = 255)
	private String description;
	
	@Column(name = "FILE_SYSTEM_TYPE_", nullable = false)
	private Integer fileSystemType;
	
	@Column(name = "HOST_", nullable = true, length = 65)
	private String host;
	
	@Column(name = "BUCKET_", nullable = true, length = 65)
	private String bucket;
	
	@Min(0)
	@Column(name = "PORT_", nullable = false)
	private Integer port;
	
	@Column(name = "PATH_", nullable = true, length = 255)
	private String path;
	
	@Column(name = "TRANSFER_MODE_", nullable = true)
	private Boolean transferMode;
	
	@Column(name = "USERNAME_", nullable = true, length = 65)
	private String username;
	
	@Column(name = "PASSWORD_", nullable = true, length = 65)
	private String password;
	
	@Column(name = "TENANT_ID_", nullable = true)
	private String tenantId;
	
	public FolderHandler() {}

	public FolderHandler(Long folderHandlerId, String name, String description, Integer fileSystemType, String host,
			String bucket, Integer port, String path, Boolean transferMode, String username, String password,
			String tenantId) {
		this.folderHandlerId = folderHandlerId;
		this.name = name;
		this.description = description;
		this.fileSystemType = fileSystemType;
		this.host = host;
		this.bucket = bucket;
		this.port = port;
		this.path = path;
		this.transferMode = transferMode;
		this.username = username;
		this.password = password;
		this.tenantId = tenantId;
	}

	public Long getFolderHandlerId() {
		return folderHandlerId;
	}

	public void setFolderHandlerId(Long folderHandlerId) {
		this.folderHandlerId = folderHandlerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFileSystemType() {
		return fileSystemType;
	}

	public void setFileSystemType(Integer fileSystemType) {
		this.fileSystemType = fileSystemType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getTransferMode() {
		return transferMode;
	}

	public void setTransferMode(Boolean transferMode) {
		this.transferMode = transferMode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bucket == null) ? 0 : bucket.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fileSystemType == null) ? 0 : fileSystemType.hashCode());
		result = prime * result + ((folderHandlerId == null) ? 0 : folderHandlerId.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
		result = prime * result + ((transferMode == null) ? 0 : transferMode.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FolderHandler other = (FolderHandler) obj;
		if (bucket == null) {
			if (other.bucket != null)
				return false;
		} else if (!bucket.equals(other.bucket))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fileSystemType == null) {
			if (other.fileSystemType != null)
				return false;
		} else if (!fileSystemType.equals(other.fileSystemType))
			return false;
		if (folderHandlerId == null) {
			if (other.folderHandlerId != null)
				return false;
		} else if (!folderHandlerId.equals(other.folderHandlerId))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		if (transferMode == null) {
			if (other.transferMode != null)
				return false;
		} else if (!transferMode.equals(other.transferMode))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FolderHandler [folderHandlerId=" + folderHandlerId + ", name=" + name + ", description=" + description
				+ ", fileSystemType=" + fileSystemType + ", host=" + host + ", bucket=" + bucket + ", port=" + port
				+ ", path=" + path + ", transferMode=" + transferMode + ", username=" + username + ", password="
				+ password + ", tenantId=" + tenantId + "]";
	}
}