package com.acedemand.plugin.jira.dao;

import com.acedemand.plugin.jira.api.UserHoliday;
import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Pamir on 8/15/2016.
 */
@Component
public class UserHolidayDao {


    private ActiveObjects activeObjects;

    public UserHolidayDao(ActiveObjects activeObjects) {
        this.activeObjects = activeObjects;
    }

    public Collection<UserHoliday> findByName(String username) {
        UserHoliday[] userHolidays = activeObjects.find(UserHoliday.class, net.java.ao.Query.select().where("USER_NAME = ?", username));
        if (userHolidays != null && userHolidays.length > 0) {
            return Arrays.asList(userHolidays);
        }
        return null;
    }

    public void save(String username, Date startDate, Date endDate) {

        Collection<UserHoliday> userHolidayCollection = findByName(username);
        UserHoliday userHoliday = null;
        if (userHolidayCollection == null || userHolidayCollection.size() == 0) {
            userHoliday = this.activeObjects.create(UserHoliday.class);
        } else {
            userHoliday = userHolidayCollection.iterator().next();
        }
        userHoliday.setUserName(username);
        userHoliday.setStartDate(startDate);
        userHoliday.setEndDate(endDate);
        userHoliday.save();
    }

    public Collection<UserHoliday> findUsersInHoliday(Date finishDate) {
        return Arrays.asList(activeObjects.find(UserHoliday.class, net.java.ao.Query.select().where("END_DATE > ?", finishDate)));
    }

    public boolean isUserInHoliday(String userName, Date startDate, Date finishDate) {
        Collection<UserHoliday> userHolidayCollection = Arrays.asList(activeObjects.find(UserHoliday.class, net.java.ao.Query.select().where("USER_NAME = ? and START_DATE < ? and  END_DATE > ?", userName, startDate, finishDate)));
        return userHolidayCollection.size() > 0;

    }

    public void delete(String username) {
        Collection<UserHoliday> holidayCollection = findByName(username);
        if (holidayCollection == null || holidayCollection.size() == 0)
            return;
        else {
            for (UserHoliday userHoliday : holidayCollection)
                activeObjects.delete(userHoliday);
        }
    }

}
