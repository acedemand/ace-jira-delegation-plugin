package com.acedemand.plugin.jira.services;

import com.acedemand.plugin.jira.api.UserHierarchy;
import com.acedemand.plugin.jira.api.UserHoliday;
import com.acedemand.plugin.jira.dao.UserHierarchyDao;
import com.acedemand.plugin.jira.dao.UserHolidayDao;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.user.ApplicationUsers;
import com.atlassian.jira.user.util.UserUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Pamir on 8/15/2016.
 */
@Component
public class AssigneeService {

    private static final Logger LOG = Logger.getLogger(AssigneeService.class);


    private UserHolidayDao userHolidayDao = null;


    private UserHierarchyDao userHierarchyDao = null;

    public AssigneeService(UserHolidayDao userHolidayDao, UserHierarchyDao userHierarchyDao){
        this.userHierarchyDao = userHierarchyDao;
        this.userHolidayDao = userHolidayDao;
    }

    public void assignToUser(MutableIssue mutableIssue){
        try {
            String userName = findAppropriateUser(mutableIssue);
            User cwdUser = ApplicationUsers.toDirectoryUser(ComponentAccessor.getComponent(UserUtil.class).getUserByKey(userName));
            mutableIssue.setAssignee(cwdUser);
        }catch (SQLException e){
            LOG.error("Error happned for issue " + mutableIssue.getKey(),e);
        }
    }

    public void saveHolidayForUser(User user, Date startDate, Date endDate){
        userHolidayDao.save(user.getName(),startDate,endDate);
    }



    public void savProjectForUser(String projectKey,User firstDelegation,User secondDelegation){
        this.userHierarchyDao.save(projectKey,firstDelegation.getName(),secondDelegation.getName());
    }

    private String findAppropriateUser(MutableIssue mutableIssue) throws SQLException{
        String assignee = mutableIssue.getAssignee().getName();
        Date today = new Date(System.currentTimeMillis());
        if(!userHolidayDao.isUserInHoliday(assignee,today,today)){
            return assignee;
        }


        LOG.debug("Project owner is on holiday " + assignee);

        //Project Owner is in holiday find first and second delegate for the employee
        String projectKey = mutableIssue.getProjectObject().getKey();
        UserHierarchy userHierarchy = userHierarchyDao.findByProjectKey(projectKey);

        if(userHierarchy == null) {
            LOG.info("Project delegation is not defined " + mutableIssue.getKey());
            return assignee;
        }

        String firstDelegatedEmployee = userHierarchy.getFirstDelegation();
        if(!userHolidayDao.isUserInHoliday(firstDelegatedEmployee,today,today)){
            return firstDelegatedEmployee;
        }

        LOG.debug("Returning second delegation " + userHierarchy.getSecondDelegation() + " " + projectKey);
        String secondDelegatedEmployee = userHierarchy.getSecondDelegation();
        if(!userHolidayDao.isUserInHoliday(secondDelegatedEmployee,today,today)){
            return secondDelegatedEmployee;
        }
        return assignee;

    }

    public Collection<UserHoliday> findAllHolidays(){
        Date date = new Date(System.currentTimeMillis());
        return userHolidayDao.findUsersInHoliday(date);
    }

    public Collection<UserHierarchy> findAllUserHierarchies(){
        return userHierarchyDao.findAll();
    }

    public void deleteUserHierarchy(String projectKey){
         userHierarchyDao.delete(projectKey);
    }
    public void deleteUserHoliday(String userName){
        userHolidayDao.delete(userName);
    }
}
