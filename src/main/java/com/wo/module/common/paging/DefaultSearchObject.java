/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.common.paging;

/**
 *
 * @author hendra
 */
public class DefaultSearchObject implements SearchObject<Object> {
    private String column;
    private Object value;

    public DefaultSearchObject(String column, Object value) {
        this.column = column;
        this.value = value;
    }
    
    
    public String getSearchColumn() {
        return this.column;
    }

    public Object getSearchValue() {
        return this.value;
    }

    public boolean isEmpty() {
        return false;
    }

    public String getSearchValueAsString() {
        return (this.value != null) ? this.value.toString() : null;
    }
    
}
