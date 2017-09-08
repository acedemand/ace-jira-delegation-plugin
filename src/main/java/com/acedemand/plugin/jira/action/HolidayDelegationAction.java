package com.acedemand.plugin.jira.action;

import com.acedemand.plugin.jira.api.UserHoliday;
import com.acedemand.plugin.jira.services.AssigneeService;
import com.atlassian.crowd.util.UserUtils;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Pamir on 8/22/2016.
 */
public class HolidayDelegationAction extends JiraWebActionSupport {

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private AssigneeService assigneeService;

    private String username;
    private String startDate;
    private String endDate;
    private Collection<UserHoliday> holidayCollection;


    public void setHolidayCollection(Collection<UserHoliday> holidayCollection) {
        this.holidayCollection = holidayCollection;
    }

    public Collection<UserHoliday> getHolidayCollection() {
        return holidayCollection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public HolidayDelegationAction(AssigneeService assigneeService) {
        this.assigneeService = assigneeService;

    }



    @Override
    public HttpServletRequest getHttpRequest() {
        return super.getHttpRequest();
    }

    @Override
    public String execute() throws Exception {
        setHolidayCollection(assigneeService.findAllHolidays());
        //HttpServletRequest httpServletRequest = this.getHttpRequest();
        String userName = username;//httpServletRequest.getParameter("username");
        String strStartDate = startDate;//(httpServletRequest.getParameter("startDate"));
        String strEndDate = endDate;//(httpServletRequest.getParameter("endDate"));
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(strStartDate) || StringUtils.isEmpty(strEndDate))
            return super.execute();

        assigneeService.saveHolidayForUser(com.atlassian.jira.user.UserUtils.getUser(userName), parseDate(strStartDate), parseDate(strEndDate));
        return super.execute();
    }

    @Override
    public String doDefault() throws Exception {

        return super.doDefault();
    }


    private Date parseDate(String date) throws ParseException {
        return formatter.parse(date);
    }


    public String doDelete() throws Exception{
        this.assigneeService.deleteUserHoliday(getHttpRequest().getParameter("username"));
        return getRedirect("/secure/admin/HolidayDelegation!default.jspa");
    }
}
