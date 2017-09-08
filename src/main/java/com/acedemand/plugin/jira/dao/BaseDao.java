package com.acedemand.plugin.jira.dao;

import com.atlassian.jira.ofbiz.DefaultOfBizConnectionFactory;
import com.atlassian.jira.ofbiz.OfBizConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Pamir on 8/15/2016.
 */
public abstract class BaseDao {
    protected Connection getConnection() throws SQLException {
        final OfBizConnectionFactory connectionFactory = new DefaultOfBizConnectionFactory();
        return connectionFactory.getConnection();
    }
}
