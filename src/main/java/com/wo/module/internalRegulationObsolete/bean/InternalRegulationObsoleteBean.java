package com.wo.module.internalRegulationObsolete.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.util.FileUtil;
import com.wo.module.internalRegulationObsolete.constant.InternalRegulationObsoleteConstants;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.service.InternalRegulationObsoleteService;
import com.wo.module.internalRegulationObsolete.vo.InternalRegulationObsoleteVo;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

/**
 * @author WhiteOpen Teknologi
 *
 */
public class InternalRegulationObsoleteBean extends CommonBean implements SelectorListener<Object>,  Serializable {

	private static final long serialVersionUID = -6874088224575777712L;

	static Logger logger = Logger.getLogger(InternalRegulationObsoleteBean.class);

	private String navigateEdit = InternalRegulationObsoleteConstants.NAVIGATE_EDIT;

	private int paging;
	
	private String localLanguange;
	
	private Long unitKerjaPenerbitId;
	private Long openCloseRegObsoleteId;
	private Long picIrgUserId;
	private String judulObsolete;
	private String noObsolete;
	private String infoObsolete;
	private String tipeRegObsoleteCode;
	private Date startDate;
	private Date endDate;

	public FacesUtil facesUtil;
	
	private FileUtil fileUtil;
	
	private DBLazyDataModel<InternalRegulationObsoleteVo> tableModel;
	
	private List<SelectItem> tipeRegulasiObsoletes;
	private List<SelectItem> unitKerjas;
	private List<SelectItem> openCloseRegulationObsoletes;
	private List<SelectItem> picIrgNames;
	
	private InternalRegulationObsoleteService internalRegulationObsoleteService;
	private UserService userService;
	
	private String reportFileName;
	
	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	
	@SuppressWarnings("static-access")
	@PostConstruct
	public void init() {
		super.init();
		initSelectItemList();
		//set null for init search
		startDate = null;
		endDate = null;
		//set null for init search - end
		paging = Constants.DEFAULT_PAGING_NUMBER;
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		localLanguange = "IN";
		if (locale != null && locale.equals(locale.ENGLISH)) {
			localLanguange = "EN";
		}
		fileUtil = FileUtil.getInstance();
		tableModel =  new DBLazyDataModel<InternalRegulationObsoleteVo>(internalRegulationObsoleteService, paging);
	}

	public void search(ActionEvent actionEvent) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		System.out.println("\"Search\" button pressed");
//		System.out.println();
//		System.out.println("Judul Obsolete        : " + judulObsolete);
//		System.out.println("Tipe Regulasi Obsolete: " + tipeRegObsoleteCode);
//		System.out.println("Start Date            : " + startDate);
//		System.out.println("End Date              : " + endDate);
//		System.out.println("No Obsolete           : " + noObsolete);
//		System.out.println("Info Obsolete         : " + infoObsolete);
//		System.out.println("Publisher Div ID      : " + unitKerjaPenerbitId);
		
		tableModel.setSearchCriteria(Arrays.asList(
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_JUDUL_OBSOLETE, judulObsolete),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_TIPE_OBSOLETE, tipeRegObsoleteCode),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_START_DATE_OBSOLETE, startDate != null ? sdf.format(startDate) : "" ),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_END_DATE_OBSOLETE , endDate != null ? sdf.format(endDate) : ""),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_NO_OBSOLETE, noObsolete),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_INFO_OBSOLETE, infoObsolete), 
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_PUBLISHER_DIVISION, unitKerjaPenerbitId),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_OPEN_CLOSE_OBS_REGULATION, openCloseRegObsoleteId),
				new DefaultSearchObject(InternalRegulationObsoleteConstants.WHERE_PIC_IRG_NAME, picIrgUserId)
				));
	}
	
	public void initSelectItemList() {
		tipeRegulasiObsoletes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION_OBSOLETE");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId().toString());
				tipeRegulasiObsoletes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		unitKerjas = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((Division) pd.get(i)).getDivisionName());
				si.setValue(((Division) pd.get(i)).getDivisionId());
				unitKerjas.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		openCloseRegulationObsoletes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("OPEN_CLOSE_REGULATION_OBSOLETE");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId().toString());
				openCloseRegulationObsoletes.add(si);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		picIrgNames = new ArrayList<SelectItem>();
		try {
			List<User> userList = internalRegulationObsoleteService.findAllUniquePicIrgNames();
			for(User user : userList) {
				SelectItem si = new SelectItem();
				si.setLabel(user.getName());
				si.setValue(user.getUserId());
				picIrgNames.add(si);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void reset(ActionEvent actionEvent) {
		unitKerjaPenerbitId = null;
		judulObsolete = null;
		noObsolete = null;
		infoObsolete = null;
		tipeRegObsoleteCode = null;
		startDate = null;
		endDate = null;
		openCloseRegObsoleteId = null;
		picIrgUserId = null;
		
		tableModel =  new DBLazyDataModel<InternalRegulationObsoleteVo>(internalRegulationObsoleteService, paging);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
//		RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}
	
	public void delete(Long irgObsId) {
		try {
			InternalRegulationObsolete irgObsolete = internalRegulationObsoleteService.findById(irgObsId);
			irgObsolete.setEnabledFlag("N");
			irgObsolete.setLastUpdateBy(facesUtil.retrieveUserLogin());
			irgObsolete.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
			
			internalRegulationObsoleteService.delete(irgObsolete);
			facesUtil.addSuccessMsg(facesUtil.retrieveMessage("deleteSuccess"));
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		//do nothing
		//UNUSED DUE TO IMPLEMENTS - DONT REMOVE BOTH METHOD AND IMPLEMENTS
	}
	
	//EXPORT EXCEL SECTION START
	
	public void postProcessXLS(Object document) {
		try {
			XSSFWorkbook wb = (XSSFWorkbook) document;
			XSSFSheet sheet = wb.getSheetAt(0);
			
			List<InternalRegulationObsoleteVo> irgObsoleteList = 
					internalRegulationObsoleteService.getAllData();
			
			setIRGObsoleteReportHeader(sheet);
			
			fillIRGObsoleteReport(sheet, irgObsoleteList);
			
			styleIRGObsoleteReport(sheet);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setIRGObsoleteReportHeader(XSSFSheet sheet) {
		XSSFRow header1 = sheet.createRow(0);
		XSSFRow header2 = sheet.createRow(1);
		
		//insert first row of header
		for(int i=0; i<16; i++) {
			XSSFCell cell = header1.createCell((short)i);
			XSSFCell cell2 = header2.createCell((short)i);
			switch(i) {
				case 0:
					cell.setCellValue("Judul Obsolete");
					break;
				case 1:
					cell.setCellValue("Tipe Regulasi Obsolete");
					break;
				case 2:
					cell.setCellValue("Nomor Regulasi Obsolete");
					break;
				case 3:
					cell.setCellValue("Obsolete Info");
					break;
				case 4:
					cell.setCellValue("Unit Kerja Penerbit");
					break;
				case 5:
					cell.setCellValue("Direktorat Penerbit");
					break;	
				case 6:
					cell.setCellValue("Tanggal Obsolete");
					break;
				case 7:
					cell.setCellValue("Status Obsolete");
					break;
				case 8:
					cell.setCellValue("PIC TPG Konversi/Obsolete");
					cell2.setCellValue("Unit Kerja");
					break;
				case 9:
					cell2.setCellValue("PIC 1");
					break;
				case 10:
					cell2.setCellValue("PIC 2");
					break;
				case 11:
					cell2.setCellValue("PIC 3");
					break;
				case 12:
					cell2.setCellValue("Target Tanggal Konversi");
					break;
				case 13:
					cell2.setCellValue("Catatan");
					break;
				case 14:
					cell.setCellValue("PIC IRG");
					cell2.setCellValue("PIC 1");
					break;
				case 15:
					cell2.setCellValue("PIC 2");
					break;
				default:
					break;
			}
		}
		
		
		//Merging cells for better style
		//Obsolete Title
		sheet.addMergedRegion(CellRangeAddress.valueOf("A1:A2"));
		//Obsolete Type
		sheet.addMergedRegion(CellRangeAddress.valueOf("B1:B2"));
		//Obsolete Number
		sheet.addMergedRegion(CellRangeAddress.valueOf("C1:C2"));
		//Obsolete Info
		sheet.addMergedRegion(CellRangeAddress.valueOf("D1:D2"));
		//Publish Div Name
		sheet.addMergedRegion(CellRangeAddress.valueOf("E1:E2"));
		//Publish Directorate Name
		sheet.addMergedRegion(CellRangeAddress.valueOf("F1:F2"));
		//Obsolete Date
		sheet.addMergedRegion(CellRangeAddress.valueOf("G1:G2"));
		//Obsolete Status Open Close
		sheet.addMergedRegion(CellRangeAddress.valueOf("H1:H2"));
		//PIC TPG
		sheet.addMergedRegion(CellRangeAddress.valueOf("I1:N1"));
		//PIC IRG
		sheet.addMergedRegion(CellRangeAddress.valueOf("O1:P1"));
	}
	
	public void fillIRGObsoleteReport(XSSFSheet sheet, List<InternalRegulationObsoleteVo> dataList) {
		//create buffer - start
		int rowNum = 2;
		
		for(int i=0;i<dataList.size();i++) {
			XSSFRow row = sheet.createRow(rowNum);
			for (int x = 0; x < 16; x++) {
				XSSFCell cell = row.createCell((short) x);
				cell.setCellValue("");
			}
			rowNum++;
		}
		//create buffer - end
		
		
		
		//fill buffer - start
		rowNum = 2;
		
		for(InternalRegulationObsoleteVo vo : dataList) {
			XSSFRow row = sheet.createRow(rowNum);
			for (int y = 0; y < 16; y++) {
				XSSFCell cell = row.createCell((short) y);
				switch(y) {
				case 0:
					//Judul Obsolete
					cell.setCellValue(vo.getJudulRegulasi());
					break;
				case 1:
					//Tipe Obsolete
					cell.setCellValue(vo.getTipeRegulasiObsolete());
					break;
				case 2:
					//Nomor Obsolete
					cell.setCellValue(vo.getNomorObsolete());
					break;
				case 3:
					//Info Obsolete
					cell.setCellValue(vo.getInfoObsolete());
					break;
				case 4:
					//Publish Div Name
					cell.setCellValue(vo.getPublishDivision());
					break;
				case 5:
					//Publish Directorate
					cell.setCellValue(vo.getPublishDirectorateName() != null ? vo.getPublishDirectorateName() : "");
					break;	
				case 6:
					//Tanggal Obsolete
					cell.setCellValue(vo.getTanggalObsoleteStr() != null ? vo.getTanggalObsoleteStr() : "");
					break;
				case 7:
					//Status Open Close Obsolete
					cell.setCellValue(vo.getStatusOpenCloseObsolete());
					break;
				case 8:
					//PIC TPG Div Name
					cell.setCellValue(vo.getUnitKerjaPicStr() != null ? vo.getUnitKerjaPicStr() : "");
					break;
				case 9:
					//PIC TPG PIC 1 name
					cell.setCellValue(vo.getPicName1() != null ? vo.getPicName1() : "");
					break;
				case 10:
					//PIC TPG PIC 2 name
					cell.setCellValue(vo.getPicName2() != null ? vo.getPicName2() : "");
					break;
				case 11:
					//PIC TPG PIC 3 name
					cell.setCellValue(vo.getPicName3() != null ? vo.getPicName3() : "");
					break;
				case 12:
					//PIC TPG Target Date
					cell.setCellValue(vo.getTanggalKonversiStr() != null ? vo.getTanggalKonversiStr() : "");
					break;
				case 13:
					//PIC TPG Notes
					cell.setCellValue(vo.getNotes() != null ? vo.getNotes() : "");
					break;
				case 14:
					//PIC IRG PIC 1 name
					cell.setCellValue(vo.getPicIrgName());
					break;
				case 15:
					//PIC IRG PIC 1 name
					cell.setCellValue(vo.getPicIrgName2() != null ? vo.getPicIrgName2() : "");
					break;
				default:
					break;
				}
			}
			rowNum++;
		}
		
		//fill buffer - end
		
	}
	
	public void styleIRGObsoleteReport(XSSFSheet sheet) {
		for (int i = 0; i < 16; i++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	//EXPORT EXCEL SECTION END

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		InternalRegulationObsoleteBean.logger = logger;
	}

	public String getNavigateEdit() {
		return navigateEdit;
	}

	public void setNavigateEdit(String navigateEdit) {
		this.navigateEdit = navigateEdit;
	}

	public int getPaging() {
		return paging;
	}

	public void setPaging(int paging) {
		this.paging = paging;
	}

	public Long getUnitKerjaPenerbitId() {
		return unitKerjaPenerbitId;
	}

	public void setUnitKerjaPenerbitId(Long unitKerjaPenerbitId) {
		this.unitKerjaPenerbitId = unitKerjaPenerbitId;
	}

	public String getLocalLanguange() {
		return localLanguange;
	}

	public void setLocalLanguange(String localLanguange) {
		this.localLanguange = localLanguange;
	}

	public String getJudulObsolete() {
		return judulObsolete;
	}

	public void setJudulObsolete(String judulObsolete) {
		this.judulObsolete = judulObsolete;
	}

	public String getInfoObsolete() {
		return infoObsolete;
	}

	public void setInfoObsolete(String infoObsolete) {
		this.infoObsolete = infoObsolete;
	}

	public String getNoObsolete() {
		return noObsolete;
	}

	public void setNoObsolete(String noObsolete) {
		this.noObsolete = noObsolete;
	}
	
	public String getTipeRegObsoleteCode() {
		return tipeRegObsoleteCode;
	}

	public void setTipeRegObsoleteCode(String tipeRegObsoleteCode) {
		this.tipeRegObsoleteCode = tipeRegObsoleteCode;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public DBLazyDataModel<InternalRegulationObsoleteVo> getTableModel() {
		return tableModel;
	}

	public void setTableModel(DBLazyDataModel<InternalRegulationObsoleteVo> tableModel) {
		this.tableModel = tableModel;
	}

	public InternalRegulationObsoleteService getInternalRegulationObsoleteService() {
		return internalRegulationObsoleteService;
	}

	public void setInternalRegulationObsoleteService(InternalRegulationObsoleteService internalRegulationObsoleteService) {
		this.internalRegulationObsoleteService = internalRegulationObsoleteService;
	}

	public List<SelectItem> getTipeRegulasiObsoletes() {
		return tipeRegulasiObsoletes;
	}

	public void setTipeRegulasiObsoletes(List<SelectItem> tipeRegulasiObsoletes) {
		this.tipeRegulasiObsoletes = tipeRegulasiObsoletes;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Long getOpenCloseRegObsoleteId() {
		return openCloseRegObsoleteId;
	}

	public void setOpenCloseRegObsoleteId(Long openCloseRegObsoleteId) {
		this.openCloseRegObsoleteId = openCloseRegObsoleteId;
	}

	public List<SelectItem> getOpenCloseRegulationObsoletes() {
		return openCloseRegulationObsoletes;
	}

	public void setOpenCloseRegulationObsoletes(List<SelectItem> openCloseRegulationObsoletes) {
		this.openCloseRegulationObsoletes = openCloseRegulationObsoletes;
	}

	public Long getPicIrgUserId() {
		return picIrgUserId;
	}

	public void setPicIrgUserId(Long picIrgUserId) {
		this.picIrgUserId = picIrgUserId;
	}

	public List<SelectItem> getPicIrgNames() {
		return picIrgNames;
	}

	public void setPicIrgNames(List<SelectItem> picIrgNames) {
		this.picIrgNames = picIrgNames;
	}

	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}
	
	
}