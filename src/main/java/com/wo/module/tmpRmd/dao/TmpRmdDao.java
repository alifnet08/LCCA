/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmd.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.tmpRmd.model.TmpRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;



/**
 *
 * @author hendra
 */
public interface TmpRmdDao extends  GenericDAO<TmpRmd, Long>, RetrieverDataPage<TmpRmd> {
	public Boolean isDataDuplicate(TmpRmd entity);
	
	@SuppressWarnings("rawtypes")
	public List<TmpRmd> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception;
	
	public Integer getTmpRmdByNameIn(String reportNameIn, Long reportTypeId) throws Exception;
	
	public Integer getTmpRmdByIdAndNameIn(Long id, String reportNameIn, Long reportTypeId) throws Exception;
	
	public List<TrcRmdPicFollowup> getRmdPicFollowupByRmdId(Long rmdId);
}