/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.model;

/**
 *
 * @author hendra
 */
public class CountLimit {
    private Integer minLimit;
    private Integer maxLimit;
    private String minLimitMsg;
    private String maxLimitMsg;

    public CountLimit() {
        this.minLimit = 0;
        this.maxLimit = 0;
        this.minLimitMsg = "Minimum not reached. Please select more.";
        this.maxLimitMsg = "Maximum reached. Please remove some.";
    }

    public CountLimit(Integer minLimit, Integer maxLimit, String minLimitMsg, String maxLimitMsg) {
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.minLimitMsg = minLimitMsg;
        this.maxLimitMsg = maxLimitMsg;
    }
    
    

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public String getMaxLimitMsg() {
        return maxLimitMsg;
    }

    public Integer getMinLimit() {
        return minLimit;
    }

    public String getMinLimitMsg() {
        return minLimitMsg;
    }
    
    public String constructMinLimitMsg() {
        return minLimitMsg.replace("{0}", String.valueOf(minLimit));
    }
    
    public String constructMaxLimitMsg() {
        return maxLimitMsg.replace("{0}", String.valueOf(maxLimit));
    }
    
}
