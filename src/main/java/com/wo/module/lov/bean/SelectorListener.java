/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;

/**
 *
 * @author hendra
 */
public interface SelectorListener <T> {
    void itemSelected(String clientId, String widgetVar, T selectedItem);    
}
