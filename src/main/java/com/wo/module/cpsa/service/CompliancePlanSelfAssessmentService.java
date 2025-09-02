package com.wo.module.cpsa.service;

import java.util.List;

import org.primefaces.event.FileUploadEvent;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessmentPic;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentVo;

public interface CompliancePlanSelfAssessmentService extends RetrieverDataPage<CompliancePlanSelfAssessmentVo>{

	public void save(CompliancePlanSelfAssessment entity);
	
	public void update(CompliancePlanSelfAssessment entity);
	
	public void delete(CompliancePlanSelfAssessment entity);
	
	public CompliancePlanSelfAssessment findById(Long cpsaId);
	
	public void saveData(CompliancePlanSelfAssessment cpsa, FileUploadEvent fileUploadCpsa, String userLogin, 
			boolean flagNewEdit, List<CompliancePlanSelfAssessmentPic> dataCpsaDeleteList) throws Exception;
	
	public String downloadCpas(Long cpsaId, List<String> filenameList, String filePath);
	
	public List<String> fileExcelCpsaList(Long cpsaId, String filePath);
	
	public void saveLockCPSAPic(Long cpsaPicId, String userLogin);
	
}
