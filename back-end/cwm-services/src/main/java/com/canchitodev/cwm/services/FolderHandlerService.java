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
package com.canchitodev.cwm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.canchitodev.cwm.domain.FolderHandler;
import com.canchitodev.cwm.exception.ObjectNotFoundException;
import com.canchitodev.cwm.repository.FolderHandlerRepository;

@Service
@Transactional
public class FolderHandlerService implements CWMService<FolderHandler, Long> {
	
	@Autowired
	private FolderHandlerRepository folderHandlerRepository;

	@Override
	public FolderHandler findById(Long id) {
		FolderHandler entity = this.folderHandlerRepository.findOne(id);

		if(entity == null)
			throw new ObjectNotFoundException("Could not find a folder handler with id '" + id + "'");
		
		return entity;
	}

	@Override
	public List<FolderHandler> findAll() {
		return this.folderHandlerRepository.findAll();
	}

	@Override
	public void save(FolderHandler entity) {
		this.folderHandlerRepository.save(entity);
	}

	@Override
	public void update(FolderHandler entity) {
		FolderHandler fh = this.findById(entity.getFolderHandlerId());
		
		if(entity.getDescription() == null)
			entity.setDescription(fh.getDescription());
		if(entity.getTransferMode() == null)
			entity.setTransferMode(fh.getTransferMode());
		if(entity.getBucket() == null)
			entity.setBucket(fh.getBucket());
		if(entity.getHost() == null)
			entity.setHost(fh.getHost());
		if(entity.getPath() == null)
			entity.setPath(fh.getPath());
		if(entity.getPort() == null)
			entity.setPort(fh.getPort());
		if(entity.getName() == null)
			entity.setName(fh.getName());
		if(entity.getUsername() == null)
			entity.setUsername(fh.getUsername());
		if(entity.getPassword() == null)
			entity.setPassword(fh.getPassword());
		if(entity.getTenantId() == null)
			entity.setTenantId(fh.getTenantId());
		
		this.save(entity);
	}

	@Override
	public void delete(Long id) {
		FolderHandler entity = this.findById(id);
		this.folderHandlerRepository.delete(entity);
	}

	@Override
	public Page<FolderHandler> findAll(Pageable pageable) {
		return this.folderHandlerRepository.findAll(pageable);
	}

	@Override
	public Page<FolderHandler> findAll(Specification<FolderHandler> spec, Pageable pageable) {
		return this.folderHandlerRepository.findAll(spec, pageable);
	}

	@Override
	public boolean exists(Long id) {
		return this.folderHandlerRepository.exists(id);
	}
	
	public FolderHandler findByNameAndTenantId(String name, Long tenantId) {
		FolderHandler entity = this.folderHandlerRepository.findByNameAndTenantId(name, tenantId);

		if(entity == null)
			throw new ObjectNotFoundException("Could not find a folder handler with name '" + name + "' and belonging to tenant id '" + tenantId + "'");
		
		return entity;
	}
}