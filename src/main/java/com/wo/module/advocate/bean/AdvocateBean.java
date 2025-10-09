package com.wo.module.advocate.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.primefaces.event.FileUploadEvent;

import com.wo.module.advocate.constant.AdvocateConstants;
import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocate.model.AdvocateInfo;
import com.wo.module.advocate.model.AdvocatePartners;
import com.wo.module.advocate.service.AdvocateService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.UploadedFileWO;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.externalRegulation.model.RegulationAttachment;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

public class AdvocateBean extends CommonBean  implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(AdvocateBean.class);

	private String region;
	
	private String branch;
	
	private String advocatName;

	private int paging;

	private AdvocateService advocateService;
	
	private List<Advocate> advocatList;

	private DBLazyDataModel<Advocate> tableModel;
	
	private List<SelectItem> categoryList;
	
	private List<SelectItem> statusList;
	
	private List<UploadedFileWO> uploadedFilesAttachment;
	
	private List<UploadedFileWO> uploadedFiles;

	public FacesUtil facesUtil;

	private String navigateEdit = AdvocateConstants.NAVIGATE_EDIT;

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
		initList();
		paging = Constants.DEFAULT_PAGING_NUMBER;
		tableModel = new DBLazyDataModel<Advocate>(advocateService, paging);
	}
	
	public void initList(){
		try {
		categoryList = new ArrayList<SelectItem>();
		List<ParameterDetail> listCategory = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_CATEGORY);
		

		for (ParameterDetail vo : listCategory) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			categoryList.add(si);
		}
		
		statusList = new ArrayList<SelectItem>();
		List<ParameterDetail> listStatus = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_FAQ_STATUS);
		

		for (ParameterDetail vo : listStatus) {
			SelectItem si = new SelectItem();
			si.setLabel(vo.getName());
			si.setValue(vo.getParameterDtlCode());
			statusList.add(si);
		}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void handleFileUpload(FileUploadEvent event) throws Exception {
		try {
			if(uploadedFilesAttachment == null)
				uploadedFilesAttachment = new ArrayList<UploadedFileWO>();
				
			/*uploadedFilesAttachment.add(new UploadedFileWO(
				CallApiManager.callUploadAPI(event.getFile(), Constants.COMPLIANCE_DOC_TYPE_AUDIT_FOLLOWUP,
						parameterDetailService, false, getFileUtil()),
				event.getFile().getFileName(), event.getFile().getContentType(), event.getFile().getSize(), true));*/
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addErrMessage(e.getMessage());
		}
	}
	
	public void downloadFileXls() throws Exception {
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);
		SXSSFSheet sheet = wb.createSheet("Daftar Kantor Hukum");

		List<Advocate> listDataXls = advocateService.searchData(
				Arrays.asList(
						new DefaultSearchObject(AdvocateConstants.SEARCH_BY_CABANG, branch),
						new DefaultSearchObject(AdvocateConstants.SEARCH_BY_REGION, region),
						new DefaultSearchObject(AdvocateConstants.SEARCH_BY_NAMA_KANTOR, advocatName)),
				0, Integer.MAX_VALUE, null, null);
		// create Header
		Row header = sheet.createRow(0);
		for (int i = 0; i < 12; i++) {
			Cell cell = header.createCell((short) i);
			if (i == 0) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatRegion"));
			} else if (i == 1) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatBranch"));
			} else if (i == 2) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatNo"));
			} else if (i == 3) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatOfficeName"));
			} else if (i == 4) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatPartnerName"));
			} else if (i == 5) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatOfficeAddress"));
			} else if (i == 6) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatTelpNo"));
			} else if (i == 7) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatFaxNo"));
			} else if (i == 8) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatEmail"));
			} else if (i == 9) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatWebsite"));
			} else if (i == 10) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatClasification"));
			}else if (i == 11) {
				cell.setCellValue(facesUtil.retrieveMessage("formAdvocatNote"));
			}

		}
		
		//kosongin data
		int rowNum = 1;
		
		
		// create Data
		rowNum = 1;
		for (int i = 0; i < listDataXls.size(); i++) {
			Advocate er = (Advocate) listDataXls.get(i);
			Row row = sheet.createRow(rowNum);
			for (int x = 0; x < 12; x++) {
				Cell cell = row.createCell((short) x);
				if (x == 0) {
					cell.setCellValue(er.getRegion() != null ? er.getRegion() : "");
				} else if (x == 1) {
					cell.setCellValue(er.getBranch() != null ? er.getBranch() : "");
				} else if (x == 2) {
					cell.setCellValue(er.getNo() != null ? er.getNo() : "");
				} else if (x == 3) {
					cell.setCellValue(er.getAdvocateName() != null ? er.getAdvocateName() : "");
				} else if (x == 4) {
					StringBuilder attachment = new StringBuilder();
					if (er.getAdvocatePartners() != null) {
						for (int y = 0; y < er.getAdvocatePartners().size(); y++) {
							AdvocatePartners ra = er.getAdvocatePartners().get(y);
							attachment.append(ra.getPartners()).append(",");
						}
					}
					cell.setCellValue(attachment.toString());
					
				} else if (x == 5) {
					StringBuilder attachment = new StringBuilder();
					if (er.getAdvocateInfos() != null) {
						for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
							AdvocateInfo ra = er.getAdvocateInfos().get(y);
							attachment.append(ra.getOfficeAddress()).append(",");
						}
					}
					cell.setCellValue(attachment.toString());
				} else if (x == 6) {
					StringBuilder attachment = new StringBuilder();
					if (er.getAdvocateInfos() != null) {
						for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
							AdvocateInfo ra = er.getAdvocateInfos().get(y);
							attachment.append(ra.getPhoneNo()).append(",");
						}
					}
					cell.setCellValue(attachment.toString());
				} else if (x == 7) {
					StringBuilder attachment = new StringBuilder();
					if (er.getAdvocateInfos() != null) {
						for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
							AdvocateInfo ra = er.getAdvocateInfos().get(y);
							attachment.append(ra.getFaxNo()).append(",");
						}
					}
					cell.setCellValue(attachment.toString());
				} else if (x == 8) {
					StringBuilder attachment = new StringBuilder();
					if (er.getAdvocateInfos() != null) {
						for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
							AdvocateInfo ra = er.getAdvocateInfos().get(y);
							attachment.append(ra.getEmail()).append(",");
						}
					}
					cell.setCellValue(attachment.toString());
				} else if (x == 9) {
					cell.setCellValue(er.getWebsite() != null ? er.getWebsite() : "");
				} else if (x == 10) {
					cell.setCellValue(er.getClasification() != null ? er.getClasification() : "");
				}else if (x == 11) {
					cell.setCellValue(er.getNote() != null ? er.getNote() : "");
				}

			}
			rowNum++;
		}

		// style
		CellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			Cell cell = header.getCell(i);
			cell.setCellStyle(cellStyle);
			//sheet.autoSizeColumn(i);
			if(i>=4 && i<=8) {
				sheet.setColumnWidth(i, 10000);
			}
			else if(i>8) {
				sheet.setColumnWidth(i, 8000);
			}
			else {
				sheet.setColumnWidth(i, 4000);
			}
		}
		
		
		wb.write(baos);
		byte[] fileByte = baos.toByteArray();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "filename=\""+"DaftarKantorHukum.xls"+"\"");
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
		FacesContext.getCurrentInstance().responseComplete();
		
	}
			
	public void postProcessXLS(Object document) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

			HSSFWorkbook wb = (HSSFWorkbook) document;
			HSSFSheet sheet = wb.getSheetAt(0);

			List<Advocate> listDataXls = advocateService.searchData(
					Arrays.asList(
							new DefaultSearchObject(AdvocateConstants.SEARCH_BY_CABANG, branch),
							new DefaultSearchObject(AdvocateConstants.SEARCH_BY_REGION, region),
							new DefaultSearchObject(AdvocateConstants.SEARCH_BY_NAMA_KANTOR, advocatName)),
					0, Integer.MAX_VALUE, null, null);
			// create Header
			HSSFRow header = sheet.createRow(0);
			for (int i = 0; i < 12; i++) {
				HSSFCell cell = header.createCell((short) i);
				if (i == 0) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatRegion"));
				} else if (i == 1) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatBranch"));
				} else if (i == 2) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatNo"));
				} else if (i == 3) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatOfficeName"));
				} else if (i == 4) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatPartnerName"));
				} else if (i == 5) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatOfficeAddress"));
				} else if (i == 6) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatTelpNo"));
				} else if (i == 7) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatFaxNo"));
				} else if (i == 8) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatEmail"));
				} else if (i == 9) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatWebsite"));
				} else if (i == 10) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatClasification"));
				}else if (i == 11) {
					cell.setCellValue(facesUtil.retrieveMessage("formAdvocatNote"));
				}

			}
			
			//kosongin data
			int rowNum = 1;
			for(int i=0;i<12;i++) {
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 12; x++) {
					HSSFCell cell = row.createCell((short) x);
					cell.setCellValue("");
				}
				rowNum++;
			}
			//kosongin data
			
			// create Data
			rowNum = 1;
			for (int i = 0; i < listDataXls.size(); i++) {
				Advocate er = (Advocate) listDataXls.get(i);
				HSSFRow row = sheet.createRow(rowNum);
				for (int x = 0; x < 12; x++) {
					HSSFCell cell = row.createCell((short) x);
					if (x == 0) {
						cell.setCellValue(er.getRegion() != null ? er.getRegion() : "");
					} else if (x == 1) {
						cell.setCellValue(er.getBranch() != null ? er.getBranch() : "");
					} else if (x == 2) {
						cell.setCellValue(er.getNo() != null ? er.getNo() : "");
					} else if (x == 3) {
						cell.setCellValue(er.getAdvocateName() != null ? er.getAdvocateName() : "");
					} else if (x == 4) {
						StringBuilder attachment = new StringBuilder();
						if (er.getAdvocatePartners() != null) {
							for (int y = 0; y < er.getAdvocatePartners().size(); y++) {
								AdvocatePartners ra = er.getAdvocatePartners().get(y);
								attachment.append(ra.getPartners()).append(",");
							}
						}
						cell.setCellValue(attachment.toString());
						
					} else if (x == 5) {
						StringBuilder attachment = new StringBuilder();
						if (er.getAdvocateInfos() != null) {
							for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
								AdvocateInfo ra = er.getAdvocateInfos().get(y);
								attachment.append(ra.getOfficeAddress()).append(",");
							}
						}
						cell.setCellValue(attachment.toString());
					} else if (x == 6) {
						StringBuilder attachment = new StringBuilder();
						if (er.getAdvocateInfos() != null) {
							for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
								AdvocateInfo ra = er.getAdvocateInfos().get(y);
								attachment.append(ra.getPhoneNo()).append(",");
							}
						}
						cell.setCellValue(attachment.toString());
					} else if (x == 7) {
						StringBuilder attachment = new StringBuilder();
						if (er.getAdvocateInfos() != null) {
							for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
								AdvocateInfo ra = er.getAdvocateInfos().get(y);
								attachment.append(ra.getFaxNo()).append(",");
							}
						}
						cell.setCellValue(attachment.toString());
					} else if (x == 8) {
						StringBuilder attachment = new StringBuilder();
						if (er.getAdvocateInfos() != null) {
							for (int y = 0; y < er.getAdvocateInfos().size(); y++) {
								AdvocateInfo ra = er.getAdvocateInfos().get(y);
								attachment.append(ra.getEmail()).append(",");
							}
						}
						cell.setCellValue(attachment.toString());
					} else if (x == 9) {
						cell.setCellValue(er.getWebsite() != null ? er.getWebsite() : "");
					} else if (x == 10) {
						cell.setCellValue(er.getClasification() != null ? er.getClasification() : "");
					}else if (x == 11) {
						cell.setCellValue(er.getNote() != null ? er.getNote() : "");
					}

				}
				rowNum++;
			}

			// style
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
				HSSFCell cell = header.getCell(i);
				cell.setCellStyle(cellStyle);
				sheet.autoSizeColumn(i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (branch != null && !branch.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateConstants.SEARCH_BY_CABANG, branch));
		}
		if (region != null && !region.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateConstants.SEARCH_BY_REGION, region));
		}
		if (advocatName != null && !advocatName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateConstants.SEARCH_BY_NAMA_KANTOR, advocatName));
		}
		

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}

	public void reset(ActionEvent actionEvent) {
		branch = "";
		region = null;
		advocatName = null;
		
		search(actionEvent);
	}

	public void delete(Long deleteId) {
		try {			
			
				Advocate entity = advocateService.findById(deleteId);
				entity.setEnabledFlag(Constants.CONSTANT_NO);
				entity.setLastUpdateBy(facesUtil.retrieveUserLogin());
				entity.setLastUpdateDate(new Timestamp(new Date().getTime()));
				
				advocateService.update(entity);
				facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
			
			
		} catch (Exception e) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public Boolean getIsLogin() {
		if (facesUtil.getSessionAttribute(Constants.SESSION_EMPLOYEE) != null
				|| facesUtil.getSessionAttribute(Constants.SESSION_KANDIDAT) != null)
			return true;
		else
			return false;
	}

	
	
	

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getAdvocatName() {
		return advocatName;
	}

	public void setAdvocatName(String advocatName) {
		this.advocatName = advocatName;
	}

	public AdvocateService getAdvocateService() {
		return advocateService;
	}

	public void setAdvocateService(AdvocateService advocateService) {
		this.advocateService = advocateService;
	}

	

	public DBLazyDataModel<Advocate> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<Advocate> tableModel) {
		this.tableModel = tableModel;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}


	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		AdvocateBean.logger = logger;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public List<Advocate> getAdvocatList() {
		return advocatList;
	}

	public void setAdvocatList(List<Advocate> advocatList) {
		this.advocatList = advocatList;
	}

	public List<UploadedFileWO> getUploadedFilesAttachment() {
		return uploadedFilesAttachment;
	}

	public void setUploadedFilesAttachment(List<UploadedFileWO> uploadedFilesAttachment) {
		this.uploadedFilesAttachment = uploadedFilesAttachment;
	}

	public List<UploadedFileWO> getUploadedFiles() {
		return uploadedFiles;
	}

	public void setUploadedFiles(List<UploadedFileWO> uploadedFiles) {
		this.uploadedFiles = uploadedFiles;
	}

	

	

	

	
	

}