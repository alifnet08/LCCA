/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.notary.service;

import java.util.List;

import javax.faces.model.SelectItem;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.notary.vo.NotaryVo;

public interface NotaryService extends RetrieverDataPage<Notary> {

	public void save(Notary entity);

	public void update(Notary entity);

	public void delete(Notary entity);

	public Notary findById(Long id);
	
	public void saveHistory(Notary notary, String historyStatus, String catatanRevisi, String userLogin);
	
	public List<NotaryHistory> getHistoryByNotaryId(Long notaryId);
	
	public NotaryVo saveUpload(UploadedFile fileUploadCpsa, FacesUtil facesUtil);
	
	public StreamedContent generateDataExcel(List<Notary> listDataXls, List<SelectItem> categoryList) throws Exception;
	
	public StreamedContent generateDataError(List<SelectItem> errorList)  throws Exception;
	
}
