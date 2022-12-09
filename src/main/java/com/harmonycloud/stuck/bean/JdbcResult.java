package com.harmonycloud.stuck.bean;

import java.io.Serializable;

public class JdbcResult implements Serializable {
    private static final long serialVersionUID = 1038000597069922790L;
    
    private final String jdbcType;
    private final String city;
    private final int tempLow;
    private final int tempHi;
    private final double prcpe;
    
    public JdbcResult(String jdbcType, String city, int tempLow, int tempHi, double prcpe) {
        this.jdbcType = jdbcType;
        this.city = city;
        this.tempLow = tempLow;
        this.tempHi = tempHi;
        this.prcpe = prcpe;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public String getCity() {
        return city;
    }

    public int getTempLow() {
        return tempLow;
    }

    public int getTempHi() {
        return tempHi;
    }

    public double getPrcpe() {
        return prcpe;
    }
}
