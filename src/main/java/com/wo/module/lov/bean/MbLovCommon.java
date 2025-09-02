/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;

import com.wo.module.lov.model.ColumnModel;
import com.wo.module.lov.service.SelectorNativeService;
import com.wo.module.lov.service.SelectorService;

/**
 *
 * @author hendra
 */

public class MbLovCommon implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String JS_ONSELECTED = "JS_ONSELECTED";
    public static final String JS_FUNCTION_ONSELECTED = "JS_FUNCTION_ONSELECTED";
    
    static Logger logger = Logger.getLogger(MbLovCommon.class);
    
    private SelectorService selectorService;
    
    private SelectorNativeService selectorNativeService;
    
    public FacesUtil facesUtil;
    
    protected Map <String, SelectorModel> mapModel = new HashMap<String, SelectorModel>();
    
    
    public void reset(String compClientId) {
        //js function first, and then js onselected event follow
        String jsOnSelected = facesUtil.retrieveRequestParam(JS_ONSELECTED);
        String jsFunctionOnSelected = facesUtil.retrieveRequestParam(JS_FUNCTION_ONSELECTED);
        
        
        SelectorModel model = mapModel.get(compClientId);
        model.setJsOnSelected(jsOnSelected);
        model.setJsFunctionOnSelected(jsFunctionOnSelected);
        model.search("","");
        
        setToFirstPage(compClientId);
    }
    
    private void setToFirstPage(String compClientId) {
        DataTable tbl = (DataTable)FacesContext.getCurrentInstance().getViewRoot()
                .findComponent(compClientId)
                .findComponent("selectorForm")
                .findComponent("selectorTable");
        tbl.setFirst(0);
        
    }
    
    public void search (String clientId) {
    	
		/*
		 * String clientId = facesUtil.retrieveRequestParam("PARAM_CLIENT_ID"); String
		 * userQuery = facesUtil.retrieveRequestParam("PARAM_SEARCH");
		 */
    	//String clientId = facesUtil.retrieveRequestParam("hdnClientId");
    	
        String param = facesUtil.retrieveRequestParam("hdnParam"+clientId);
        String type = facesUtil.retrieveRequestParam("hdnType"+clientId);
        String userQuery = facesUtil.retrieveRequestParam("txtSearch"+clientId);
        
        param = !StringUtils.isEmpty(param)?param:"%";
        type = !StringUtils.isEmpty(type)?type:"%";
        
        //add by hendra 20130715
        //setToFirstPage(clientId);
        SelectorModel model = mapModel.get(clientId);
        //model.search(userQuery,param);
        model.search(userQuery,param,type);
        //end
        
        /*reset(clientId);
        SelectorModel model = mapModel.get(clientId);
        model.search(userQuery);*/

    }
    
    public void searchWithType (String clientId) {
    	
		/*
		 * String clientId = facesUtil.retrieveRequestParam("PARAM_CLIENT_ID"); String
		 * userQuery = facesUtil.retrieveRequestParam("PARAM_SEARCH");
		 */
    	//String clientId = facesUtil.retrieveRequestParam("hdnClientId");
    	
        String param = facesUtil.retrieveRequestParam("hdnParam"+clientId);
        String userQuery = facesUtil.retrieveRequestParam("txtSearch"+clientId);
        String typeQuery = facesUtil.retrieveRequestParam("txtProvType"+clientId);
        
        if (typeQuery != null && typeQuery.equalsIgnoreCase("x"))
        	typeQuery = "";
        
        //add by hendra 20130715
        //setToFirstPage(clientId);
        SelectorModel model = mapModel.get(clientId);
        model.search(userQuery,param,typeQuery);
        //end
        
        /*reset(clientId);
        SelectorModel model = mapModel.get(clientId);
        model.search(userQuery);*/

    }
    
    public String getUserQueryString(String compClientId) {
        SelectorModel model = mapModel.get(compClientId);
        if (model != null) {
            return model.getUserQueryString();
        } else {
            return "";
        }
    }
    
    public SelectorModel getSelectorModel(
            String compClientId,
            SelectorModel.SelectorInfo selectorInfo) {
        SelectorModel model = mapModel.get(compClientId);
        if (model == null) {
            if (selectorInfo.getIsHql()) {
                model = new SelectorModel(selectorService, selectorInfo, compClientId, 5);                
            } else {
                model = new SelectorModel(selectorNativeService, selectorInfo, compClientId, 5);                
            }
            mapModel.put(compClientId, model);
        }
        return model;
    }
    
    @SuppressWarnings("rawtypes")
	public DataModel getDataModel(
            String compClientId,
            SelectorModel.SelectorInfo selectorInfo
            ) {
        
        SelectorModel model = getSelectorModel(compClientId, selectorInfo);
        return model.getDataModel();
    }
    
    public List<ColumnModel> getColumnModel(String compClientId) {
        SelectorModel model = mapModel.get(compClientId);
        if (model != null) {
             return model.getColumnModels();
        } else {
            return new ArrayList<ColumnModel>();
        }        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void select(
            String compClientId, 
            String widgetVar,
            SelectorListener beanListener,
            Object item,
            SelectorModel.SelectorInfo selectorInfo) {
//        System.out.println("select ");
        beanListener.itemSelected(compClientId, widgetVar, item);
        
        invokeJs(compClientId, selectorInfo, item);
    }
    
    private void invokeJs(String compClientId, SelectorModel.SelectorInfo selectorInfo, Object item) {
        String jsParamAware = getSelectorModel(compClientId, selectorInfo).constructJsFunctionParamAware(item);
        String jsOnSelected = getSelectorModel(compClientId, selectorInfo).getJsOnSelected();
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(jsParamAware)) {
            sb.append(jsParamAware);
        }
        if (StringUtils.isNotBlank(jsOnSelected)) {
            sb.append(jsOnSelected);
        }
        
        if (StringUtils.isNotBlank(sb.toString())) {
        	PrimeFaces.current().executeScript(sb.toString());            
        }        
    }
    
//    public void select(
//            String compClientId, 
//            String widgetVar,
//            SelectorListenerEventAware beanListener,
//            Object item) {
////        System.out.println("select event aware");
//        //beanListener.itemSelected(compClientId, widgetVar, item);
//    }

    public void setSelectorService(SelectorService selectorService) {
        this.selectorService = selectorService;
    }

    public void setFacesUtils(FacesUtil facesUtil) {
        this.facesUtil = facesUtil;
    }

    public boolean isNumeric(String prop) {
        return StringUtils.isNumeric(prop);
    }


    public Boolean alignLeft(Object value) {
        if (value instanceof Number) {
            return false;
        } else {
            return true;
        }
    }
    
    public Boolean alignRight(Object value) {
        if (value instanceof Number) {
            return true;
        } else {
            return false;
        }
    }
    
    public void setSelectorNativeService(SelectorNativeService selectorNativeService) {
        this.selectorNativeService = selectorNativeService;
    }

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Map<String, SelectorModel> getMapModel() {
		return mapModel;
	}

	public void setMapModel(Map<String, SelectorModel> mapModel) {
		this.mapModel = mapModel;
	}

	public SelectorService getSelectorService() {
		return selectorService;
	}

	public SelectorNativeService getSelectorNativeService() {
		return selectorNativeService;
	}

	
    
    

}
