package com.canchitodev.cwm.security;

import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service()
public class GlobalSecurity {
	
	@Autowired
    protected IdmIdentityService identityService;

	public boolean hasPrivilege(String name) {
		String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Privilege privilege = this.getPrivilegeByNameAndUserId(name, userId);
		return (privilege == null) ? false : true; 
	}
	
	protected Privilege getPrivilegeByNameAndUserId(String name, String userId) {
        return identityService.createPrivilegeQuery().privilegeName(name).userId(userId).singleResult();
    }
}
