package com.harmonycloud.stuck.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
public class BigResult {

    private List<HashMap<String, InterResult>> list;

    private HashMap<String, List<InterResult>> map;

    private String a;

    private Integer b;



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterResult {

        private String c;

        private Integer d;

        private List<HashMap<String, Object>> list;

        private HashMap<String, List<String>> map;
    }


}
