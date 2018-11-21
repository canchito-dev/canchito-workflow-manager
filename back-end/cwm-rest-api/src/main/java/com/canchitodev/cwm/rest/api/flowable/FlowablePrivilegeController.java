/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canchitodev.cwm.rest.api.flowable;

import static org.flowable.common.rest.api.PaginateListUtil.paginateList;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.rest.api.DataResponse;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.Privilege;
import org.flowable.idm.api.PrivilegeQuery;
import org.flowable.idm.api.User;
import org.flowable.idm.rest.service.api.IdmRestResponseFactory;
import org.flowable.idm.rest.service.api.group.GroupResponse;
import org.flowable.idm.rest.service.api.privilege.AddGroupPrivilegeRequest;
import org.flowable.idm.rest.service.api.privilege.AddUserPrivilegeRequest;
import org.flowable.idm.rest.service.api.privilege.PrivilegeResponse;
import org.flowable.idm.rest.service.api.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/**
 * @author Joram Barrez
 */
@RestController
@Api(tags = { "Privileges" }, description = "Manage Privileges", authorizations = { @Authorization(value = "basicAuth") })
public class FlowablePrivilegeController {
	
	@Autowired
	private IdmIdentityService identityService;
    
    private IdmRestResponseFactory idmRestResponseFactory = new IdmRestResponseFactory();
    
    private IdmRestResponseFactory restResponseFactory = new IdmRestResponseFactory();

    @ApiOperation(value = "List privileges", nickname="listPrivileges", tags = { "Privileges" }, produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "string", value = "Only return privileges with the given id", paramType = "query"),
            @ApiImplicitParam(name = "name", dataType = "string", value = "Only return privileges with the given name", paramType = "query"),
            @ApiImplicitParam(name = "userId", dataType = "string", value = "Only return privileges with the given userId", paramType = "query"),
            @ApiImplicitParam(name = "groupId", dataType = "string", value = "Only return privileges with the given groupId", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the requested privileges were returned.")
    })
    @RequestMapping(value = "/privileges", method = RequestMethod.GET)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public DataResponse<PrivilegeResponse> getPrivileges(@ApiParam(hidden = true) @RequestParam Map<String, String> allRequestParams, HttpServletRequest request) {
        PrivilegeQuery query = identityService.createPrivilegeQuery();
        
        if (allRequestParams.containsKey("id")) {
            query.privilegeId(allRequestParams.get("id"));
        }
        if (allRequestParams.containsKey("name")) {
            query.privilegeName(allRequestParams.get("name"));
        }
        if (allRequestParams.containsKey("userId")) {
            query.userId(allRequestParams.get("userId"));
        }
        if (allRequestParams.containsKey("groupId")) {
            query.groupId(allRequestParams.get("groupId"));
        }
        
        return paginateList(allRequestParams, query, "id", null, idmRestResponseFactory::createPrivilegeResponseList);
    }

    @ApiOperation(value = "List all users for a given privilege", nickname = "listPrivilegeUsers", tags = { "Privileges" }, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the privilege exists and its users are returned.")
    })
    @RequestMapping(value = "/privileges/{privilegeId}/users", method = RequestMethod.GET)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public List<UserResponse> getUsers(@PathVariable String privilegeId) {
        List<User> users = identityService.getUsersWithPrivilege(privilegeId);
        return idmRestResponseFactory.createUserResponseList(users, false);
    }

    @ApiOperation(value = "Deletes a privilege for a user", nickname = "deleteUserPrivilege", tags = { "Privileges" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the user privilege has been deleted")
    })
    @RequestMapping(value = "/privileges/{privilegeId}/users/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public void deleteUserPrivilege(@PathVariable String privilegeId, @PathVariable String userId) {
        identityService.deleteUserPrivilegeMapping(privilegeId, userId);
    }
    
    @ApiOperation(value = "Adds a privilege for a user", nickname = "addUserPrivilege", tags = { "Privileges" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the user privilege has been added")
    })
    @RequestMapping(value = "privileges/{privilegeId}/users", method = RequestMethod.POST)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public void addUserPrivilege(@PathVariable String privilegeId, @RequestBody AddUserPrivilegeRequest request) {
        identityService.addUserPrivilegeMapping(privilegeId, request.getUserId());
    }
    
    @ApiOperation(value = "List all groups for a given privilege", nickname = "listPrivilegeGroups", tags = { "Privileges" }, produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the privilege exists and its groups are returned.")
    })
    @RequestMapping(value = "/privileges/{privilegeId}/groups", method = RequestMethod.GET)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public List<GroupResponse> getGroups(@PathVariable String privilegeId) {
        List<Group> groups = identityService.getGroupsWithPrivilege(privilegeId);
        return idmRestResponseFactory.createGroupResponseList(groups);
    }

    @ApiOperation(value = "Deletes a privilege for a group", nickname = "deleteGroupPrivilege", tags = { "Privileges" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the group privilege has been deleted")
    })
    @RequestMapping(value = "/privileges/{privilegeId}/group/{groupId}", method = RequestMethod.DELETE)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public void deleteGroupPrivilege(@PathVariable String privilegeId, @PathVariable String groupId) {
        identityService.deleteUserPrivilegeMapping(privilegeId, groupId);
    }
    
    @ApiOperation(value = "Adds a privilege for a group", nickname = "addGroupPrivilege", tags = { "Privileges" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the group privilege has been added")
    })
    @RequestMapping(value = "privileges/{privilegeId}/groups", method = RequestMethod.POST)
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public void addGroupPrivilege(@PathVariable String privilegeId, @RequestBody AddGroupPrivilegeRequest request) {
        identityService.addGroupPrivilegeMapping(privilegeId, request.getGroupId());
    }
    
    @ApiOperation(value = "Get a single privilege", tags = { "Privileges" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Indicates the privilege exists and is returned."),
            @ApiResponse(code = 404, message = "Indicates the requested privilege does not exist.")
    })
    @GetMapping(value = "/privileges/{privilegeId}", produces = "application/json")
    @PreAuthorize("@globalSecurity.hasPrivilege('access-admin')")
    public PrivilegeResponse getUser(@ApiParam(name = "privilegeId") @PathVariable String privilegeId, HttpServletRequest request) {
        Privilege privilege = identityService.createPrivilegeQuery().privilegeId(privilegeId).singleResult();
        
        if (privilege == null) {
            throw new FlowableObjectNotFoundException("Could not find privilege with id " + privilegeId, Privilege.class);
        }
        
        List<User> users = identityService.getUsersWithPrivilege(privilege.getId());
        List<Group> groups = identityService.getGroupsWithPrivilege(privilege.getId());
        
        return restResponseFactory.createPrivilegeResponse(privilege, users, groups); 
    }

    protected Privilege getPrivilegeById(String privilegeId) {
        Privilege privilege = identityService.createPrivilegeQuery().privilegeId(privilegeId).singleResult();
        
        if (privilege == null) {
            throw new FlowableObjectNotFoundException("Could not find privilege with id " + privilegeId, Privilege.class);
        }
        
        return privilege;
    }
}
