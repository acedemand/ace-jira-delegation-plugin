package com.acedemand.plugin.jira.api;


import net.java.ao.Entity;
import net.java.ao.Preload;

import java.util.Date;

/**
 * Created by Pamir on 8/15/2016.
 */
@Preload
public interface UserHoliday extends Entity{




    public String getUserName() ;

    public void setUserName(String userName);

    public Date getStartDate();

    public void setStartDate(Date startDate);

    public void setEndDate(Date endDate);
    public Date getEndDate();
}
