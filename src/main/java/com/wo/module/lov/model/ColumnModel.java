/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.model;

import java.io.Serializable;

/**
 *
 * @author hendra
 */
public class ColumnModel implements Serializable {

    private static final long serialVersionUID = -7311808953320426632L;
    private String header;
    private String property;

    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }
}
