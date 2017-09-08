package com.acedemand.plugin.jira.action;

import com.acedemand.plugin.jira.api.UserHierarchy;
import com.acedemand.plugin.jira.services.AssigneeService;
import com.atlassian.jira.user.UserUtils;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created by Pamir on 8/22/2016.
 */
public class ProjectDelegationAction extends JiraWebActionSupport {

    private AssigneeService assigneeService;
    private Collection<UserHierarchy> userHierarchyCollection;

    public AssigneeService getAssigneeService() {
        return assigneeService;
    }

    public void setAssigneeService(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    public Collection<UserHierarchy> getUserHierarchyCollection() {
        return userHierarchyCollection;
    }

    public void setUserHierarchyCollection(Collection<UserHierarchy> userHierarchyCollection) {
        this.userHierarchyCollection = userHierarchyCollection;
    }

    private String projectKey;
    private String firstDelegation;
    private String secondDelegation;



    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getFirstDelegation() {
        return firstDelegation;
    }

    public void setFirstDelegation(String firstDelegation) {
        this.firstDelegation = firstDelegation;
    }

    public String getSecondDelegation() {
        return secondDelegation;
    }

    public void setSecondDelegation(String secondDelegation) {
        this.secondDelegation = secondDelegation;
    }

    public ProjectDelegationAction(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;
    }

    @Override
    public HttpServletRequest getHttpRequest() {
        return super.getHttpRequest();
    }

    @Override
    public String execute() throws Exception {

        if(StringUtils.isEmpty(projectKey) || StringUtils.isEmpty(firstDelegation) || StringUtils.isEmpty(secondDelegation)){
            return super.execute();
        }
        assigneeService.savProjectForUser(projectKey, UserUtils.getUser(firstDelegation),UserUtils.getUser(secondDelegation));
        return super.execute();
    }

    @Override
    public String doDefault() throws Exception {
        setUserHierarchyCollection(assigneeService.findAllUserHierarchies());
        return super.doDefault();
    }

    public String doDelete() throws Exception {
        this.assigneeService.deleteUserHierarchy(getHttpRequest().getParameter("project"));
        return getRedirect("/secure/admin/ProjectDelegation!default.jspa");
    }


}
