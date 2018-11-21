package com.canchitodev.cwm.security;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.flowable.engine.IdentityService;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.Privilege;
import org.flowable.rest.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Joram Barrez
 */
public class BasicAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    protected IdentityService identityService;
    
    @Autowired
    protected IdmIdentityService idmIdentityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userId = authentication.getName();
        String password = authentication.getCredentials().toString();

        boolean authenticated = idmIdentityService.checkPassword(userId, password);
        if (authenticated) {

            Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>(1);
        	List<Privilege> privileges = idmIdentityService.createPrivilegeQuery().userId(userId).list();
        	if(privileges.isEmpty())
        		// Always add the role when it's not verified: this makes the config easier (i.e. user needs to have it)
                grantedAuthorities.add(new SimpleGrantedAuthority(SecurityConstants.PRIVILEGE_ACCESS_REST_API));
        	else {
                for (Privilege privilege : privileges) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getName()));
                }	
        	}
            
            return new UsernamePasswordAuthenticationToken(userId, password, grantedAuthorities);
        } else {
            throw new BadCredentialsException("Authentication failed for this username and password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}