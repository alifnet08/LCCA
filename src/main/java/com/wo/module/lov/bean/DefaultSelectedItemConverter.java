/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author hendra
 */
public class DefaultSelectedItemConverter <T> implements SelectedItemConverter<T> {
    private List<String> columnProp;

    public DefaultSelectedItemConverter(List<String> columnProp) {
        this.columnProp = columnProp;
    }
    
    
    @Override
    public String convertToJsParam(T item) {
        if (item instanceof Object[]) {
            return handlePropByIndex(item);
        } else {
            return handlePropByMethodName(item);
        }
        
    }

    private String handlePropByIndex(Object item) {
        StringBuilder sb = new StringBuilder();
        Object [] itemInArray = (Object[]) item;
        for (String strIdx : columnProp) {
            Integer idx = Integer.parseInt(strIdx);            
            sb.append("'");
            sb.append(itemInArray[idx]);
            sb.append("',");            
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length()-1);
        } else {
            return sb.toString();
        }
        
    }

    private String handlePropByMethodName(Object item) {
        StringBuilder sb = new StringBuilder();
        Method method;
        for (String prop : columnProp) {
            if (StringUtils.isBlank(prop)) {
                continue;
            }
            try {
                method = item.getClass().getMethod(getGetterMethodUppercase(prop));
            } catch (SecurityException e) {
                throw new RuntimeException("Can't invoke getter for prop[" + prop + "]", e);
            } catch (NoSuchMethodException e) {
                try {
                    method = item.getClass().getMethod(getGetterMethodLowercase(prop));                    
                } catch (Exception ex) {
                    throw new RuntimeException("Can't invoke getter for prop[" + prop + "]", e);
                }
            }
            try {
                sb.append("'");
                sb.append(method.invoke(null));
                sb.append("',");
            } catch (Exception ex) {
                throw new RuntimeException("Can't invoke getter for prop[" + prop + "]", ex);
            }            
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length()-1);
        } else {
            return sb.toString();
        }
        
    }
    
    private String getGetterMethodUppercase(String prop) {
        StringBuilder sb = new StringBuilder();
        sb.append("get")
                .append(prop.substring(0, 1).toUpperCase())
                .append(prop.substring(1));
        
        return sb.toString();
    }

    private String getGetterMethodLowercase(String prop) {
        StringBuilder sb = new StringBuilder();
        sb.append("get")
                .append(prop.substring(0, 1).toLowerCase())
                .append(prop.substring(1));
        
        return sb.toString();
    }
    
}
