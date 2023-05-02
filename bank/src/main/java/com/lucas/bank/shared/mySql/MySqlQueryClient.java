package com.lucas.bank.shared.mySql;

import com.lucas.bank.shared.secretManager.SecretManagerClient;
import com.lucas.bank.shared.staticInformation.StaticInformation;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
public class MySqlQueryClient<T> {
    private final Logger log = LoggerFactory.getLogger(MySqlQueryClient.class);
    @Autowired
    private SecretManagerClient secretManagerClient;

   public List<T> executeQuery(String query, Class<T> clazz, Object... params){

       Connection conn = null;
       try {
           var authentication = secretManagerClient.getDatabaseAuth();
           conn = DriverManager.getConnection(StaticInformation.DATABASE_URL(), authentication.getUsername(), authentication.getPassword());

           log.info("Executing query: {}", query);

           QueryRunner run = new QueryRunner();

           List<T> result = run.query(conn, query,
                   new BeanListHandler<>(clazz),
                   params);

           return result;

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
}

