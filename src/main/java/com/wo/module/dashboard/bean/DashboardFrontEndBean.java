package com.wo.module.dashboard.bean;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.announcement.service.AnnouncementService;
import com.wo.module.calendar.model.Calendar;
import com.wo.module.calendar.service.CalendarService;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.dashboard.service.DashboardFEService;
import com.wo.module.dashboard.vo.MapVO;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.externalRegulationFE.service.ExternalRegulationFEService;
import com.wo.module.externalRegulationFE.vo.ExternalRegulationFEVO;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulation.service.InternalRegulationService;
import com.wo.module.internalRegulationFE.constant.InternalRegulationFEConstants;
import com.wo.module.internalRegulationFE.service.InternalRegulationFEService;
import com.wo.module.internalRegulationFE.vo.InternalRegulationFEVO;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.service.ParameterDetailService;

public class DashboardFrontEndBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(DashboardFrontEndBean.class);

	private String searchVal;
	
	private Long countUserLogin;
	
	private DashboardFEService dashboardFEService;
	
	private ExternalRegulationFEService externalRegulationFEService;
	
	private InternalRegulationFEService internalRegulationFEService;
	
//	private ParameterDetailService parameterDetailService;
	
	private CalendarService calendarService;
	
	private AnnouncementService announcementService;
	
	private InternalRegulationService internalRegulationService;
	
	private DocumentTypeService documentTypeService;
	
	private FacesUtil facesUtil;
	
	private List<MapVO> listMap;
	
	private List<InternalRegulationFEVO> listPeraturanInternal;
	
	private List<ExternalRegulationFEVO> listPeraturanEksternal;
	
	private List listInformasiTerbaru;
	
	private List listPengumuman;
	
	private String linkTentangMaybank;
	private String linkElearning;
	
	private Long docTypeId;
	
	

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
		try {
			
			/*try {
				ParameterDetail d = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LINK_TENTANG_MAYBANK);
				linkTentangMaybank = d.getName();
			} catch (Exception ex) {
				linkTentangMaybank = "#";
			}
			try {
				ParameterDetail d = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_LINK_ELEARNING); 
				linkElearning = d.getName();
			} catch (Exception ex) {
				linkElearning = "#";
			}*/
			
			linkTentangMaybank = facesUtil.retrieveCorporatePortalLink();
			linkElearning = facesUtil.retrieveElearningLink();
			
			List<SearchObject> searchCriteria = new ArrayList<SearchObject>();
			if (searchVal != null && !searchVal.isEmpty()) {
				searchCriteria.add(new DefaultSearchObject(InternalRegulationFEConstants.SEARCH_BY_NAME_IN, searchVal));
			}
			
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_COMBO_BOX, CommonConstants.DATA_ACTIVE));
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, facesUtil.getUserLogin().getUserId()));
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE_IS_NOT_ANOUNCEMENT, "TRUE"));
			
			listPeraturanInternal = internalRegulationFEService.searchData(searchCriteria, 0, 5, null, null);
			
			listPeraturanEksternal = externalRegulationFEService.searchData(searchCriteria, 0, 5, null, null);
			
			listPengumuman = announcementService.searchData(searchCriteria, 0, 5, null, null);
			
			List<SearchObject> searchCriteria2 = new ArrayList<SearchObject>();
			docTypeId = documentTypeService.getDocumentTypeIdByProvAndType(InternalRegulationConstants.PARAM_DTL_CODE_KETENTUAN_INTERNAL, InternalRegulationConstants.DOCUMENT_TYPE_INFORMASI_LAINNYA);
			if(docTypeId !=null && docTypeId > 0){
				searchCriteria2.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE, docTypeId));
			}
			searchCriteria2.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_COMBO_BOX, CommonConstants.DATA_ACTIVE));
			searchCriteria2.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, facesUtil.getUserLogin().getUserId()));
			listInformasiTerbaru = internalRegulationFEService.searchData(searchCriteria2, 0, 5, null, null);
			
			List<Calendar> calendar = calendarService.searchData(searchCriteria, 0, Integer.MAX_VALUE, null, null);
			SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
			String script = "var eventNew = [";
			for(int i=0;i<calendar.size();i++){
				Calendar cal = calendar.get(i);
				if(i==(calendar.size()-1)){
					java.util.Calendar c = java.util.Calendar.getInstance();
					c.setTime(cal.getStartDate());
					
					if (c.getTime().compareTo(cal.getEndDate()) == 0) {
						script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"') }";
					} else {
						//while(c.getTime().compareTo(cal.getEndDate()) <= 0){
						for (int j=0; c.getTime().compareTo(cal.getEndDate()) <= 0; j++) {
							if (j == 0)
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'head' }";
							else if (c.getTime().compareTo(cal.getEndDate()) == 0)
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'tail' }";
							else
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'mid' }";
								
							c.add(java.util.Calendar.DATE, 1);
						}
					}
				}else{
					java.util.Calendar c = java.util.Calendar.getInstance();
					c.setTime(cal.getStartDate());
					
					if (c.getTime().compareTo(cal.getEndDate()) == 0) {
						script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"') },";
					} else {
						//while(c.getTime().compareTo(cal.getEndDate()) <= 0){
						for (int j=0; c.getTime().compareTo(cal.getEndDate()) <= 0; j++) {
							if (j == 0)
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'head' },";
							else if (c.getTime().compareTo(cal.getEndDate()) == 0)
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'tail' },";
							else
								script = script+"{ Title: '"+cal.getCalendarEvent()+"', Date: new Date('"+sdf2.format(c.getTime())+"'), Staticclass: 'mid' },";
							
							c.add(java.util.Calendar.DATE, 1);
						}
					}
				}
				
			}
			script=script+"]; calendarNew(eventNew);wSize();contentSize();";
			System.out.println("Script=="+script);
			PrimeFaces.current().executeScript(script);
			
			countUserLogin = dashboardFEService.getCountUserLogin();
			listMap = dashboardFEService.getMapData();
			for(int i=0;i<listMap.size();i++){
				MapVO vo = listMap.get(i);
				PrimeFaces.current().executeScript("setPin("+vo.getPinTop()+","+vo.getPinLeft()+","+i+",'"+vo.getProvince()+"');");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void searchAll(){
		String searchAll = facesUtil.retrieveRequestParam("SEARCH_VAL");
		
		String result = "/pages/searchAllFE/searchAllFE.faces?SEARCH_VAL="+(searchAll!=null?searchAll.replace("&", ":and").replace("%", ":percent"):searchAll);
		String baseContextPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(baseContextPath + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String encryptId(Long id){
		return Constants.encryptString(id.toString());
	}
	

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		DashboardFrontEndBean.logger = logger;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public Long getCountUserLogin() {
		return countUserLogin;
	}

	public void setCountUserLogin(Long countUserLogin) {
		this.countUserLogin = countUserLogin;
	}

	public DashboardFEService getDashboardFEService() {
		return dashboardFEService;
	}

	public void setDashboardFEService(DashboardFEService dashboardFEService) {
		this.dashboardFEService = dashboardFEService;
	}

	public List<MapVO> getListMap() {
		return listMap;
	}

	public void setListMap(List<MapVO> listMap) {
		this.listMap = listMap;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public List<InternalRegulationFEVO> getListPeraturanInternal() {
		return listPeraturanInternal;
	}

	public void setListPeraturanInternal(List<InternalRegulationFEVO> listPeraturanInternal) {
		this.listPeraturanInternal = listPeraturanInternal;
	}

	public List<ExternalRegulationFEVO> getListPeraturanEksternal() {
		return listPeraturanEksternal;
	}

	public void setListPeraturanEksternal(List<ExternalRegulationFEVO> listPeraturanEksternal) {
		this.listPeraturanEksternal = listPeraturanEksternal;
	}

	public List getListInformasiTerbaru() {
		return listInformasiTerbaru;
	}

	public void setListInformasiTerbaru(List listInformasiTerbaru) {
		this.listInformasiTerbaru = listInformasiTerbaru;
	}

	public List getListPengumuman() {
		return listPengumuman;
	}

	public void setListPengumuman(List listPengumuman) {
		this.listPengumuman = listPengumuman;
	}

	public InternalRegulationFEService getInternalRegulationFEService() {
		return internalRegulationFEService;
	}

	public void setInternalRegulationFEService(InternalRegulationFEService internalRegulationFEService) {
		this.internalRegulationFEService = internalRegulationFEService;
	}

	public ExternalRegulationFEService getExternalRegulationFEService() {
		return externalRegulationFEService;
	}

	public void setExternalRegulationFEService(ExternalRegulationFEService externalRegulationFEService) {
		this.externalRegulationFEService = externalRegulationFEService;
	}

	public CalendarService getCalendarService() {
		return calendarService;
	}

	public void setCalendarService(CalendarService calendarService) {
		this.calendarService = calendarService;
	}

	public AnnouncementService getAnnouncementService() {
		return announcementService;
	}

	public void setAnnouncementService(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	public InternalRegulationService getInternalRegulationService() {
		return internalRegulationService;
	}

	public void setInternalRegulationService(InternalRegulationService internalRegulationService) {
		this.internalRegulationService = internalRegulationService;
	}

	public DocumentTypeService getDocumentTypeService() {
		return documentTypeService;
	}

	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}

	public Long getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Long docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getLinkTentangMaybank() {
		return linkTentangMaybank;
	}

	public void setLinkTentangMaybank(String linkTentangMaybank) {
		this.linkTentangMaybank = linkTentangMaybank;
	}

	public String getLinkElearning() {
		return linkElearning;
	}

	public void setLinkElearning(String linkElearning) {
		this.linkElearning = linkElearning;
	}

//	public ParameterDetailService getParameterDetailService() {
//		return parameterDetailService;
//	}
//
//	public void setParameterDetailService(ParameterDetailService parameterDetailService) {
//		this.parameterDetailService = parameterDetailService;
//	}
	
}