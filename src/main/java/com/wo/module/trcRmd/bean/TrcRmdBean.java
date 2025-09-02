package com.wo.module.trcRmd.bean;

import java.io.Serializable;
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

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.reportType.model.ReportType;
import com.wo.module.reportType.service.ReportTypeService;
import com.wo.module.tmpRmd.service.TmpRmdService;
import com.wo.module.trcRmd.constant.TrcRmdConstants;
import com.wo.module.trcRmd.model.TrcRmd;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.service.TrcRmdService;
import com.wo.module.trcRmd.vo.TrcRmdSearchVo;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class TrcRmdBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(TrcRmdBean.class);

	private String searchReportType;
	private String searchReportName;
	private String searchStatus;
	private Date searchTargetDate;

	private TrcRmdService trcRmdService;
	private ReportTypeService reportTypeService;
	//private ParameterDetailService parameterDetailService;
	private UserService userService;
	private TmpRmdService tmpRmdService;
	
	private List<TrcRmd> trcRmdList;
	
	private List<TrcRmdPicFollowup> listInquiryData;

	private DBLazyDataModel<TrcRmdSearchVo> tableModel;

	public FacesUtil facesUtil;

	private String navigateEdit = TrcRmdConstants.NAVIGATE_EDIT;

	private List<SelectItem> reportTypeList;
	private List<SelectItem> statusList;

	public void postProcessXLS(Object document) {
		try {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
				
		List<TrcRmdSearchVo> listDataXls =  trcRmdService.searchData(
				Arrays.asList(
	                    new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_REPORT_TYPE, searchReportType),
	                    new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_REPORT_NAME, searchReportName),
	                    new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_STATUS, searchStatus)
	             ), 0, Integer.MAX_VALUE, null, null);
		//create Header
		HSSFRow header = sheet.createRow(0);
		for(int i=0;i<4;i++) {
			HSSFCell cell = header.createCell((short) i);
			if(i==0) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdReportName"));
			}
			else if(i==1) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdReportType"));
			}
			else if(i==2) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdPic"));
			}
			else if(i==3) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdStatus"));
			}
			/*else if(i==4) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdPicFollowupStatus"));
			}
			else if(i==5) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdPicConfirmation"));
			}
			else if(i==6) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdConfirmationDate"));
			}
			else if(i==7) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdFollowupDate"));
			}
			else if(i==8) {
				cell.setCellValue(facesUtil.retrieveMessage("formTrcRmdNote"));
			}*/
			
		}
		
		//create Data
		int rowNum = 1;
		for(int i=0;i<listDataXls.size();i++) {
			TrcRmdSearchVo data = (TrcRmdSearchVo)listDataXls.get(i);
			HSSFRow row = sheet.createRow(rowNum);
			for(int x=0;x<4;x++) {
				HSSFCell cell = row.createCell((short) x);
				if(x==0) {
					cell.setCellValue(data.getReportName()!=null?data.getReportName():"");
				}
				else if(x==1) {
					cell.setCellValue(data.getReportTypeName()!=null?data.getReportTypeName():"");
				}
				else if(x==2) {
					cell.setCellValue(data.getPic1()!=null?data.getPic1():"");
				}
				else if(x==3) {
					cell.setCellValue(data.getStatusName()!=null?data.getStatusName():"");
				}
				/*else if(x==4) {
					cell.setCellValue(data.getFollowupStatusName()!=null?data.getFollowupStatusName():"");
				}
				else if(x==5) {
					cell.setCellValue(data.getPicConfirmationName()!=null?data.getPicConfirmationName():"");
				}
				else if(x==6) {
					cell.setCellValue(data.getConfirmationDate()!=null?data.getConfirmationDate():null);
				}
				else if(x==7) {
					cell.setCellValue(data.getFollowupDate()!=null?data.getFollowupDate():null);
				}
				else if(x==8) {
					cell.setCellValue(data.getNote()!=null?data.getNote():"");
				}*/
				
			}
			rowNum++;
		}
		
		//style
		HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
         
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@SuppressWarnings("rawtypes")
	@PostConstruct
	public void init() {
		super.init();
		
		tableModel = new DBLazyDataModel<TrcRmdSearchVo>(trcRmdService, getPaging());
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableModel.setSearchCriteria(searchCriteria);	
		initList();
	}

	public void initList() {
		
		try {
			reportTypeList = new ArrayList<SelectItem>();
			List<ReportType> listRepsonsibility = reportTypeService.getAllReportType();
			for (ReportType vo : listRepsonsibility) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getReportType());
				si.setValue(vo.getReportTypeId());
				reportTypeList.add(si);
			}
			
			statusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listParamDtl = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_CODE_DATA_STATUS);
			
			for (ParameterDetail vo : listParamDtl) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				statusList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
		if (searchReportType != null && !searchReportType.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_REPORT_TYPE, searchReportType));
		}

		if (searchReportName != null && !searchReportName.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_REPORT_NAME, searchReportName));
		}

		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_STATUS, searchStatus));
		}
		
		if (searchTargetDate != null) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_TARGET_DATE, searchTargetDate != null ? sdf.format(searchTargetDate) : ""));
		}
		

		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(TrcRmdConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
		}

		tableModel.setSearchCriteria(searchCriteria);
		/*
		 * tableModel.setSearchCriteria( Arrays.asList( new
		 * DefaultSearchObject(SearchObject.ALL_COLUMNS, searchVal)));
		 */
	}
	
	public void reset(ActionEvent actionEvent) {
		searchReportType = "";
		searchReportName = "";
		searchStatus = "";
		setSearchTargetDate(null);
		search(actionEvent);
	}
	
	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception {
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	public void onViewInquiryData(Long rmdId) {
		if(listInquiryData == null) {
			listInquiryData = new ArrayList<>();
		}else {
			listInquiryData.clear();
		}
		
		listInquiryData = tmpRmdService.getRmdPicFollowupByRmdId(rmdId);
		
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

	public TrcRmdService getTrcRmdService() {
		return trcRmdService;
	}

	public void setTrcRmdService(TrcRmdService trcRmdService) {
		this.trcRmdService = trcRmdService;
	}

	public List<TrcRmd> getTrcRmdList() {
		return trcRmdList;
	}

	public void setTrcRmdList(List<TrcRmd> trcRmdList) {
		this.trcRmdList = trcRmdList;
	}

	public DBLazyDataModel<TrcRmdSearchVo> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<TrcRmdSearchVo> tableModel) {
		this.tableModel = tableModel;
	}
	
	public String getSearchReportType() {
		return searchReportType;
	}

	public void setSearchReportType(String searchReportType) {
		this.searchReportType = searchReportType;
	}

	public String getSearchReportName() {
		return searchReportName;
	}

	public void setSearchReportName(String searchReportName) {
		this.searchReportName = searchReportName;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public List<SelectItem> getReportTypeList() {
		return reportTypeList;
	}

	public void setReportTypeList(List<SelectItem> reportTypeList) {
		this.reportTypeList = reportTypeList;
	}

	public List<SelectItem> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<SelectItem> statusList) {
		this.statusList = statusList;
	}

	public ReportTypeService getReportTypeService() {
		return reportTypeService;
	}

	public void setReportTypeService(ReportTypeService reportTypeService) {
		this.reportTypeService = reportTypeService;
	}

	/**
	 * @return the parameterDetailService
	 */
	/*public ParameterDetailService getParameterDetailService() {
		return parameterDetailService;
	}*/

	/**
	 * @param parameterDetailService the parameterDetailService to set
	 */
	/*public void setParameterDetailService(ParameterDetailService parameterDetailService) {
		this.parameterDetailService = parameterDetailService;
	}*/

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Date getSearchTargetDate() {
		return searchTargetDate;
	}

	public void setSearchTargetDate(Date searchTargetDate) {
		this.searchTargetDate = searchTargetDate;
	}

	public List<TrcRmdPicFollowup> getListInquiryData() {
		return listInquiryData;
	}

	public void setListInquiryData(List<TrcRmdPicFollowup> listInquiryData) {
		this.listInquiryData = listInquiryData;
	}

	public TmpRmdService getTmpRmdService() {
		return tmpRmdService;
	}

	public void setTmpRmdService(TmpRmdService tmpRmdService) {
		this.tmpRmdService = tmpRmdService;
	}

	
}