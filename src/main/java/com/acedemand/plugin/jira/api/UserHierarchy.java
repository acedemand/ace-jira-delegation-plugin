package com.acedemand.plugin.jira.api;

import net.java.ao.Entity;
import net.java.ao.Preload;

/**
 * Created by Pamir on 8/15/2016.
 */

@Preload
public interface UserHierarchy extends Entity {

    public String getProjectKey() ;

    public void setProjectKey(String projectKey);

    public String getFirstDelegation();

    public void setFirstDelegation(String firstDelegation);

    public String getSecondDelegation();

    public void setSecondDelegation(String secondDelegation);

}
