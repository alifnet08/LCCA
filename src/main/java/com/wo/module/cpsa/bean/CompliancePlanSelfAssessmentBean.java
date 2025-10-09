package com.wo.module.cpsa.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.task.TaskExecutorBean;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.cpsa.model.CompliancePlanSelfAssessment;
import com.wo.module.cpsa.service.CompliancePlanSelfAssessmentService;
import com.wo.module.cpsa.task.CompliancePlanSelfAssessmentTask;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class CompliancePlanSelfAssessmentBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -1851018659930698121L;
	private static final Logger logger = Logger.getLogger(CompliancePlanSelfAssessmentBean.class);
	private static final String NAVIGATE_EDIT = CompliancePlanSelfAssessmentConstant.NAVIGATE_CPSA_EDIT;
	
	private CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService;
	
	private String searchCpsaType;
	private String searchCpsaName;	
	private String searchLetterNo;
	private String searchBranchSubBranch;
	private String searchStatus;
	
	private Date searchUploadDate;
	private Date searchLetterDate;
	private Date searchPeriodStartFrom;
	private Date searchPeriodStartTo;
	private Date searchPeriodEndFrom;
	private Date searchPeriodEndTo;
	
	private int paging;
	
	private List<SelectItem> cpsaTypeList;
	private List<SelectItem> cpsaStatusList;
	
	List<String> filesListInDir = new ArrayList<String>();
	
	private DBLazyDataModel<CompliancePlanSelfAssessmentVo> cpsaTableModel;
	
	private FacesUtil facesUtil;
	
	private SimpleDateFormat sdfDateSearch = new SimpleDateFormat("yyyy-MM-dd");
	
	private TaskExecutorBean taskExecutorBean;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@PostConstruct
	public void init() {
		super.init();
		initComponent();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		cpsaTableModel = new DBLazyDataModel<CompliancePlanSelfAssessmentVo>(compliancePlanSelfAssessmentService, paging);
	}
	
	private void initComponent() {
		initCpsaTypeList();
	}
	
	private void initCpsaTypeList() {
		try {
			cpsaTypeList = new ArrayList<SelectItem>();
			
			List<ParameterDetail> geteCpsaType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_TYPE);
			for (ParameterDetail pd : geteCpsaType) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaTypeList.add(si);
			}
			
			cpsaStatusList = new ArrayList<SelectItem>();
			
			List<ParameterDetail> getCpsaStatusList = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CPSA_STATUS);
			for(ParameterDetail pd : getCpsaStatusList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getName());
				si.setValue(pd.getParameterDtlCode());
				
				cpsaStatusList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent event) {
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchCpsaType != null && !searchCpsaType.isEmpty() && !searchCpsaType.equals("")) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_TYPE, searchCpsaType));
		}
		if (searchCpsaName != null && !searchCpsaName.isEmpty() && !searchCpsaName.equals("")) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_NAME, searchCpsaName));
		}
		if (searchUploadDate != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_UPLOAD_DATE, sdfDateSearch.format(searchUploadDate)));
		}
		if (searchLetterNo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_NO, searchLetterNo));
		}
		if (searchLetterDate != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_LETTER_DATE, sdfDateSearch.format(searchLetterDate)));
		}
		if (searchPeriodStartFrom != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_FROM, sdfDateSearch.format(searchPeriodStartFrom)));
		}
		if (searchPeriodStartTo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_START_TO, sdfDateSearch.format(searchPeriodStartTo)));
		}
		if (searchPeriodEndFrom != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_FROM, sdfDateSearch.format(searchPeriodEndFrom)));
		}
		if (searchPeriodEndTo != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_PERIOD_END_TO, sdfDateSearch.format(searchPeriodEndTo)));
		}
		if (searchBranchSubBranch != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_BRANCH_SUB_BRANCH, searchBranchSubBranch));
		}
		if (searchStatus != null) {
			searchCriteria.add(new DefaultSearchObject(CompliancePlanSelfAssessmentConstant.SEARCH_BY_CPSA_STATUS, searchStatus));
		}
		
		cpsaTableModel.setSearchCriteria(searchCriteria);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void reset(ActionEvent event) {
		searchCpsaType = null;
		searchCpsaName = "";
		searchUploadDate = null;
		searchLetterNo = "";
		searchLetterDate = null;
		searchPeriodStartFrom = null;
		searchPeriodStartTo = null;
		searchPeriodEndFrom = null;
		searchPeriodEndTo = null;
		searchBranchSubBranch = null;
		searchStatus = null;
		
		search(event);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void delete(Long cpsaId) {
		try {
			CompliancePlanSelfAssessment entity = compliancePlanSelfAssessmentService.findById(cpsaId);
			entity.setEnabledFlag(Constants.CONSTANT_NO);
			entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
			entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
			
			compliancePlanSelfAssessmentService.delete(entity);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	} 
	
	public void generateZipInBackgroundJob(Long cpsaId) {
		CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssessmentService.findById(cpsaId);
		compliancePlanSelfAssessment.setStatusDownload("IN_PROGRESS");
		compliancePlanSelfAssessmentService.update(compliancePlanSelfAssessment);
		
		CompliancePlanSelfAssessmentTask task = new CompliancePlanSelfAssessmentTask(cpsaId, parameterDetailService, compliancePlanSelfAssessmentService);
		
		taskExecutorBean.submitTask(task);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public StreamedContent downloadFile(Long cpsaId) {
		CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssessmentService.findById(cpsaId);
		
		File file = null;
		DefaultStreamedContent fileStream = null;
		ByteArrayInputStream bis = null;
		
		try {
			ParameterDetail pdPathFileDownload = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
			
			file = new File(pdPathFileDownload.getNameIn()+compliancePlanSelfAssessment.getFilePathDownload());
			bis = new ByteArrayInputStream(FileUtils.readFileToByteArray(file));
			fileStream = new DefaultStreamedContent(bis, "application/zip", file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return fileStream;
	}
	
	public StreamedContent downloadZip(Long cpsaId) {
	    ByteArrayInputStream bis = null;
	    InputStream stream = null;
	    DefaultStreamedContent file;
	    List<String> filenameList = new ArrayList<>();
	    String fileName = "";
	    String fullPath = "";
	    
        try {
        	ParameterDetail pdPathFileDownload = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
        	
        	String directory = compliancePlanSelfAssessmentService.downloadCpas(cpsaId, filenameList, pdPathFileDownload.getNameIn());
            if (directory != null) {
                bis = new ByteArrayInputStream(zipBytes2(filenameList)); // Firstly I zip every PDF doc with zipBytes() method
                stream = bis;
                
                SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyymmddHHmmss");
        		fileName = "CPSA_KERTAS_KERJA_" + sdfTemp.format(new Date()) + ".zip";
        		file = new DefaultStreamedContent(stream, "application/zip", fileName);
        		fullPath = "CPSA_ZIP"+Constants.FILE_SEPARATOR+"CPSA_"+cpsaId + Constants.FILE_SEPARATOR +fileName;
        		
        		CompliancePlanSelfAssessment compliancePlanSelfAssessment = compliancePlanSelfAssessmentService.findById(cpsaId);
        		compliancePlanSelfAssessment.setFilePathDownload(fullPath);
        		compliancePlanSelfAssessmentService.update(compliancePlanSelfAssessment);
        		
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        
	    return null;
	}
	
	private byte[] zipBytes2(List<String> filenameList) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		FileInputStream fis = null;
		byte[] result = null;
	    
		try {
			for (String directoryFile : filenameList) {
				File input = new File(directoryFile);
                fis = new FileInputStream(input);
				ZipEntry zipEntry = new ZipEntry(input.getPath());
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
			result =  baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
	            if (baos != null) {
	                baos.close();
	            }	            
	            if (zos != null) {
	                zos.close();
	            }	           
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
		}
		
	    return result;
	}

//	private byte[] zipBytes(String directory) {
//	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	    ZipOutputStream zos = new ZipOutputStream(baos);
//	    DataInputStream pdfDocIs = null;
//	    byte[] result = null;
//	    try {
//	          ZipEntry zipEntry = new ZipEntry(directory);
//	          zos.putNextEntry(zipEntry);
//	          //zos.write(toByteArray(pdfDocIs)); 
//	          zos.close();
//	        result =  baos.toByteArray();
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    finally {
//	        try {
//	            if (baos != null) {
//	                baos.close();
//	            }	            
//	            if (zos != null) {
//	                zos.close();
//	            }	           
//	        } catch (Exception e2) {
//	            e2.printStackTrace();
//	        }
//	    }
//	    return result;
//	}

	public static byte[] toByteArray(InputStream in) {
	    ByteArrayOutputStream os = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    byte[] result = null;
	    int len;
	    // read bytes from the input stream and store them in buffer
	    try {
	        while ((len = in.read(buffer)) != -1) {
	            // write bytes from the buffer into output stream
	            os.write(buffer, 0, len);
	        }
	        result = os.toByteArray();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (os != null) {
	                os.close();
	            }
	        } catch (Exception e2) {
	            e2.printStackTrace();
	        }
	    }
	    return result;
	}
	
//	 private byte[] zipFiles(File directory, String fileName) throws IOException {
//	        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	             ZipOutputStream zos = new ZipOutputStream(baos)) {
//	            byte[] bytes = new byte[2048];
//
//	          //  for (String fileName : files) {
//	                String path = directory.getPath();
//	                try (FileInputStream fis = new FileInputStream(path);
//	                     BufferedInputStream bis = new BufferedInputStream(fis)) {
//
//	                    zos.putNextEntry(new ZipEntry(path));
//
//	                    int bytesRead;
//	                    while ((bytesRead = bis.read(bytes)) != -1) {
//	                        zos.write(bytes, 0, bytesRead);
//	                    }
//	                    zos.closeEntry();
//	                }
//	          //  }
//
//	            zos.close();
//	            return baos.toByteArray();
//	        }
//	    }
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public CompliancePlanSelfAssessmentService getCompliancePlanSelfAssessmentService() {
		return compliancePlanSelfAssessmentService;
	}

	public void setCompliancePlanSelfAssessmentService(
			CompliancePlanSelfAssessmentService compliancePlanSelfAssessmentService) {
		this.compliancePlanSelfAssessmentService = compliancePlanSelfAssessmentService;
	}

	public String getSearchCpsaType() {
		return searchCpsaType;
	}

	public void setSearchCpsaType(String searchCpsaType) {
		this.searchCpsaType = searchCpsaType;
	}

	public String getSearchCpsaName() {
		return searchCpsaName;
	}

	public void setSearchCpsaName(String searchCpsaName) {
		this.searchCpsaName = searchCpsaName;
	}

	public Date getSearchUploadDate() {
		return searchUploadDate;
	}

	public void setSearchUploadDate(Date searchUploadDate) {
		this.searchUploadDate = searchUploadDate;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public List<SelectItem> getCpsaTypeList() {
		return cpsaTypeList;
	}

	public void setCpsaTypeList(List<SelectItem> cpsaTypeList) {
		this.cpsaTypeList = cpsaTypeList;
	}

	public DBLazyDataModel<CompliancePlanSelfAssessmentVo> getCpsaTableModel() {
		return cpsaTableModel;
	}

	public void setCpsaTableModel(DBLazyDataModel<CompliancePlanSelfAssessmentVo> cpsaTableModel) {
		this.cpsaTableModel = cpsaTableModel;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateEdit() {
		return NAVIGATE_EDIT;
	}

	public SimpleDateFormat getSdfDateSearch() {
		return sdfDateSearch;
	}

	public void setSdfDateSearch(SimpleDateFormat sdfDateSearch) {
		this.sdfDateSearch = sdfDateSearch;
	}

	public String getSearchLetterNo() {
		return searchLetterNo;
	}

	public void setSearchLetterNo(String searchLetterNo) {
		this.searchLetterNo = searchLetterNo;
	}

	public Date getSearchLetterDate() {
		return searchLetterDate;
	}

	public void setSearchLetterDate(Date searchLetterDate) {
		this.searchLetterDate = searchLetterDate;
	}

	public Date getSearchPeriodStartFrom() {
		return searchPeriodStartFrom;
	}

	public void setSearchPeriodStartFrom(Date searchPeriodStartFrom) {
		this.searchPeriodStartFrom = searchPeriodStartFrom;
	}

	public Date getSearchPeriodStartTo() {
		return searchPeriodStartTo;
	}

	public void setSearchPeriodStartTo(Date searchPeriodStartTo) {
		this.searchPeriodStartTo = searchPeriodStartTo;
	}

	public Date getSearchPeriodEndFrom() {
		return searchPeriodEndFrom;
	}

	public void setSearchPeriodEndFrom(Date searchPeriodEndFrom) {
		this.searchPeriodEndFrom = searchPeriodEndFrom;
	}

	public Date getSearchPeriodEndTo() {
		return searchPeriodEndTo;
	}

	public void setSearchPeriodEndTo(Date searchPeriodEndTo) {
		this.searchPeriodEndTo = searchPeriodEndTo;
	}

	public List<String> getFilesListInDir() {
		return filesListInDir;
	}

	public void setFilesListInDir(List<String> filesListInDir) {
		this.filesListInDir = filesListInDir;
	}

	public TaskExecutorBean getTaskExecutorBean() {
		return taskExecutorBean;
	}

	public void setTaskExecutorBean(TaskExecutorBean taskExecutorBean) {
		this.taskExecutorBean = taskExecutorBean;
	}

	public String getSearchBranchSubBranch() {
		return searchBranchSubBranch;
	}

	public void setSearchBranchSubBranch(String searchBranchSubBranch) {
		this.searchBranchSubBranch = searchBranchSubBranch;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public List<SelectItem> getCpsaStatusList() {
		return cpsaStatusList;
	}

	public void setCpsaStatusList(List<SelectItem> cpsaStatusList) {
		this.cpsaStatusList = cpsaStatusList;
	}
}
