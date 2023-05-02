package com.lucas.bank.shared.mySql;

import com.lucas.bank.shared.secretManager.SecretManagerClient;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Component
public class MySqlCommandClient {
    private final Logger log = LoggerFactory.getLogger(MySqlCommandClient.class);
    @Autowired
    private SecretManagerClient secretManagerClient;

    public void executeCommand(String query, Map<Integer, List<Object>> parameters) {
        Connection conn = null;
        try {
            var authentication = secretManagerClient.getDatabaseAuth();
            conn = DriverManager.getConnection(StaticInformation.DATABASE_URL(), authentication.getUsername(), authentication.getPassword());

            log.info("Starting batch, query: {}", query);

            for(Map.Entry<Integer, List<Object>> entry : parameters.entrySet()){
                var statement = conn.prepareStatement(query);
                MySqlExtensions.fillParameters(statement, entry.getValue());
                statement.executeUpdate();
                log.info("Query executed!");
            }

            log.info("Batch executed");

        } catch (SQLException se) {
            se.printStackTrace();
            log.error(se.getMessage());
            throw new RuntimeException(se);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                log.error(se.getMessage());
            }
        }
    }

    public void executeCommand(String query, List<Object> parameters) {
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            var authentication = secretManagerClient.getDatabaseAuth();
            conn = DriverManager.getConnection(StaticInformation.DATABASE_URL(), authentication.getUsername(), authentication.getPassword());

            statement = conn.prepareStatement(query);
            log.info("Starting to fill parameters, query: {}", query);
            MySqlExtensions.fillParameters(statement, parameters);
            statement.executeUpdate();
            log.info("Query executed!");
        } catch (SQLException se) {
            se.printStackTrace();
            log.error(se.getMessage());
            throw new RuntimeException(se);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                log.error(se.getMessage());
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                log.error(se.getMessage());
            }
        }
    }
}

