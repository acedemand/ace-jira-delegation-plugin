package com.acedemand.plugin.jira.dao;

import com.acedemand.plugin.jira.api.UserHierarchy;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.sal.api.transaction.TransactionCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by Pamir on 8/15/2016.
 */
@Component
public class UserHierarchyDao {


    private ActiveObjects activeObjects;

    public UserHierarchyDao(ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    public UserHierarchy findByProjectKey(String projectKey) {
        UserHierarchy[] userHierarchies = this.activeObjects.find(UserHierarchy.class, net.java.ao.Query.select().where("PROJECT_KEY = ?", projectKey));
        if (userHierarchies != null && userHierarchies.length > 0) {
            return userHierarchies[0];
        }
        return null;
    }

    public Collection<UserHierarchy> findAll(){
        return Arrays.asList(activeObjects.find(UserHierarchy.class));
    }

    public void save(String projectKey, String firstDelegation, String secondDelegation) {

        UserHierarchy userHierarchy = findByProjectKey(projectKey);
        if (userHierarchy == null) {
            userHierarchy = activeObjects.create(UserHierarchy.class);
        }
        userHierarchy.setProjectKey(projectKey);
        userHierarchy.setFirstDelegation(firstDelegation);
        userHierarchy.setSecondDelegation(secondDelegation);
        userHierarchy.save();
    }

    public void delete(String projectKey){
        UserHierarchy userHierarchy = findByProjectKey(projectKey);
        if(userHierarchy != null)
            activeObjects.delete(userHierarchy);
    }


}
