/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;


import java.io.Serializable;

public class MbLovRegulationSocializationCommon extends MbLovCommon implements Serializable {

	private static final long serialVersionUID = 742358015007461420L;
//	private List<SelectItem> searchType; 
    
	@SuppressWarnings("unused")
	@Override
	public void search (String clientId) {
    	
        String param = facesUtil.retrieveRequestParam("hdnParam"+clientId);
        String userQuery = facesUtil.retrieveRequestParam("txtSearch"+clientId);
        String searchSelectType = facesUtil.retrieveRequestParam("selectSearch"+clientId);
        
        SelectorModel model = mapModel.get(clientId);
        model.search(userQuery,param);
        
//        List<String> searchSelectType = model.getSelectSearchType();
//        searchType = new ArrayList<SelectItem>();
//        for (String s : searchSelectType) {
//        	searchType.add(new SelectItem(s));
//		}

    }

//	public List<SelectItem> getSearchType() {
//		return searchType;
//	}
//
//	public void setSearchType(List<SelectItem> searchType) {
//		this.searchType = searchType;
//	}
}
