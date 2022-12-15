package com.harmonycloud.stuck.util.userCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Service
public class UserService2 {

    private static Logger log = LogManager.getLogger(UserService2.class);


    @Transactional(rollbackFor = Exception.class)
    public void createUserWrong1(Connection conn, String name) throws Exception {
        try {
            this.createUserPrivate(name, conn);
        } catch (Exception ex) {
            log.error("create user failed because {}", ex.getMessage());
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void createUserPrivate(String name, Connection conn) throws Exception {

        PreparedStatement pstmt = conn.prepareStatement("insert into test.student (id, name) values (null, 'test')");
        pstmt.execute();
        if (name.contains("test"))
            throw new RuntimeException("invalid username!");
    }


}
