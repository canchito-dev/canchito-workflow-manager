package com.canchitodev.cwm.flowable.idm;

import java.util.List;

import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.NativeGroupQuery;
import org.flowable.idm.api.NativeTokenQuery;
import org.flowable.idm.api.NativeUserQuery;
import org.flowable.idm.api.Picture;
import org.flowable.idm.api.Privilege;
import org.flowable.idm.api.PrivilegeMapping;
import org.flowable.idm.api.PrivilegeQuery;
import org.flowable.idm.api.Token;
import org.flowable.idm.api.TokenQuery;
import org.flowable.idm.api.User;
import org.flowable.idm.api.UserQuery;

public class CWMIdmIdentityService implements IdmIdentityService {

	@Override
	public void addGroupPrivilegeMapping(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUserPrivilegeMapping(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkPassword(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GroupQuery createGroupQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createMembership(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NativeGroupQuery createNativeGroupQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NativeTokenQuery createNativeTokenQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NativeUserQuery createNativeUserQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Privilege createPrivilege(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrivilegeQuery createPrivilegeQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenQuery createTokenQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserQuery createUserQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteGroup(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroupPrivilegeMapping(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMembership(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePrivilege(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteToken(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUserPrivilegeMapping(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Group> getGroupsWithPrivilege(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PrivilegeMapping> getPrivilegeMappingsByPrivilegeId(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserInfo(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getUserInfoKeys(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Picture getUserPicture(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsersWithPrivilege(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group newGroup(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Token newToken(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User newUser(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveGroup(Group arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveToken(Token arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUser(User arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserInfo(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUserPicture(String arg0, Picture arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserPassword(User arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAuthenticatedUserId(String arg0) {
		// TODO Auto-generated method stub
		
	}

}