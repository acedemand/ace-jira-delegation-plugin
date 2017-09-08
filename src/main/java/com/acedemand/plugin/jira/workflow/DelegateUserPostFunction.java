package com.acedemand.plugin.jira.workflow;

import com.acedemand.plugin.jira.services.AssigneeService;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Pamir on 8/22/2016.
 */
@Component
public class DelegateUserPostFunction extends AbstractJiraFunctionProvider implements ApplicationContextAware {



    private AssigneeService assigneeService;

    public DelegateUserPostFunction( AssigneeService assigneeService){
        this.assigneeService = assigneeService;
    }


    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException {
        MutableIssue mutableIssue = getIssue(transientVars);
        assigneeService.assignToUser(mutableIssue);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //assigneeService = (AssigneeService) applicationContext.getBeansOfType(AssigneeService.class).get("");
    }
}
