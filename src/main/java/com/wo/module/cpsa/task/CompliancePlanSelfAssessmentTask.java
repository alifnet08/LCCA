package com.wo.module.cpsa.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.service.CompliancePlanSelfAssessmentService;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class CompliancePlanSelfAssessmentTask implements Runnable {

	private Long cpsaId;
	
	private ParameterDetailService parameterDetailService;
	private CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService;
	
	public CompliancePlanSelfAssessmentTask(Long cpsaId, 
			ParameterDetailService parameterDetailService, 
			CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService) {
		super();
		this.cpsaId = cpsaId;
		this.parameterDetailService = parameterDetailService;
		this.compliancePlanSelfAssessmentService = compliancePlanSelfAssessmentService;
	}
	
	@Override
	public void run() {
		CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssessmentService.findById(cpsaId);

		FileOutputStream fos = null;
		FileInputStream fis = null;
		List<String> filenameList = new ArrayList<>();
		
		try {
			ParameterDetail pdPathFileDownload = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
			filenameList = compliancePlanSelfAssessmentService.fileExcelCpsaList(cpsaId, pdPathFileDownload.getNameIn());
			
			if (filenameList != null && !filenameList.isEmpty()) {
				SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
				String fileName = "CPSA_KERTAS_KERJA_" + sdfTemp.format(new Date()) + ".zip";
				String filePathTemp = "CPSA_ZIP"+ CommonConstants.FILE_SEPARATOR +"CPSA_" + cpsaId + CommonConstants.FILE_SEPARATOR + fileName;
				String fullPath = pdPathFileDownload.getNameIn() + filePathTemp;
				
				fos = new FileOutputStream(fullPath);
				ZipOutputStream zos = new ZipOutputStream(fos);
				try {
					for (String directoryFile : filenameList) {
						File input = new File(directoryFile);
						fis = new FileInputStream(input);
						ZipEntry zipEntry = new ZipEntry(input.getPath().replace(pdPathFileDownload.getNameIn() +
								"CPSA_ZIP"+CommonConstants.FILE_SEPARATOR, ""));
						zos.putNextEntry(zipEntry);
						byte[] tmp = new byte[4*1024];
		                int size = 0;
		                while((size = fis.read(tmp)) != -1){
		                	zos.write(tmp, 0, size);
		                }
		                zos.flush();
		                fis.close();
		                if (input != null) {
							input.delete();
						}
					}
					zos.close();
					compliancePlanSelfAssessment.setStatusDownload("SUCCESS");
					compliancePlanSelfAssessment.setFilePathDownload(filePathTemp);
				} catch (Exception e) {
					e.printStackTrace();
					compliancePlanSelfAssessment.setStatusDownload("FAILED");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			compliancePlanSelfAssessment.setStatusDownload("FAILED");
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					compliancePlanSelfAssessment.setStatusDownload("FAILED");
				}
			}
		}
		compliancePlanSelfAssessmentService.update(compliancePlanSelfAssessment);
	}
	
	public Long getCpsaId() {
		return cpsaId;
	}

	public void setCpsaId(Long cpsaId) {
		this.cpsaId = cpsaId;
	}

	public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}

	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}

	public CompliancePlanSelfAssessmentService getCompliancePlanSelfAssessmentService() {
		return compliancePlanSelfAssessmentService;
	}

	public void setCompliancePlanSelfAssessmentService(
			CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService) {
		this.compliancePlanSelfAssessmentService = compliancePlanSelfAssessmentService;
	}

}
