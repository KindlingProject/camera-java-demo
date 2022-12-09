package com.harmonycloud.stuck.web;

import com.alibaba.fastjson.JSON;
import com.harmonycloud.stuck.bean.JdbcResult;
import com.harmonycloud.stuck.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Api(value = "数据库模拟卡顿", tags = {"Db"})
@RestController
@RequestMapping("/db")
public class DbContoller {
    private static Logger LOG = LogManager.getLogger(DbContoller.class);

    @Autowired(required = false)
    @Qualifier("HikariCP")
    private DataSource hikariDataSource;

    @Value("${datasource.maxPool}")
    private int connectionSize;

    private List<Connection> connections = new ArrayList<Connection>();

//    @Trace
    @ApiOperation(value = "查询")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Result query() throws Exception {
        LOG.info("Start Query...");
//        LOG.info("traceId = " + TraceContext.traceId());
        return queryResult(hikariDataSource.getConnection(), "hikariCp");
    }

    @ApiOperation(value = "耗尽所有连接")
    @RequestMapping(value = "/getConnections", method = RequestMethod.GET)
    public Result getConnections() throws Exception {
        int num = connectionSize - connections.size();
        while (connections.size() < connectionSize) {
            connections.add(hikariDataSource.getConnection());
        }
        return Result.success("Get Connections[" + num + "]");
    }

    @ApiOperation(value = "释放所有连接")
    @RequestMapping(value = "/releaseConnections", method = RequestMethod.GET)
    public Result releaseConnections() throws Exception {
        int num = connections.size();
        if (connections.size() > 0) {
            for (Connection connection : connections) {
                releaseConnection(connection);
            }
            connections.clear();
        }
        return Result.success("Release Connections[" + num + "]");
    }

    private Result queryResult(Connection conn, String jdbcType) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("select city, temp_lo, temp_hi, prcpe from weather where temp_lo<=? and temp_hi>=?");
            pstmt.setInt(1, 10);
            pstmt.setInt(2, 12);
            pstmt.executeQuery();
            String city = "???";
            int tempLow = 0, tempHi = 0;
            double prcpe = 0.0d;

            pstmt.executeQuery();
            ResultSet rs = pstmt.getResultSet();
            while (rs.next()) {
                city = rs.getString(1);
                tempLow = rs.getInt(2);
                tempHi = rs.getInt(3);
                prcpe = rs.getDouble(4);
            }
            JdbcResult result = new JdbcResult(jdbcType, city, tempLow, tempHi, prcpe);
            LOG.info("Result: {}", JSON.toJSONString(result));
            return Result.success(result);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            releaseConnection(conn);
        }
        return Result.fail("SQL Exception " + jdbcType);
    }

    private void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}