package com.wo.module.faqFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.documentType.constant.DocumentTypeConstants;
import com.wo.module.documentType.model.DocumentType;
import com.wo.module.documentType.service.DocumentTypeService;
import com.wo.module.faq.model.Faq;
import com.wo.module.faq.service.FaqService;
import com.wo.module.faqFE.vo.CategoryTypeVO;
import com.wo.module.faqFE.vo.InstitutionVO;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.logAccess.model.LogAccess;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.qaFE.service.QAFEService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class FaqFEBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(FaqFEBean.class);

	private String searchVal;
	
	private String category;
	
	private List<SelectItem> categoryList;
	
	private FaqService faqService;

	private FacesUtil facesUtil;
	
	private Long countQuestionNotAnswered;
	
	private String lastUpdateQuestionNotAnswered;
	
	private String id;
	
	private Long faqId;
	
	private List<InstitutionVO> institutionVO;
	
	private Long isAdmin;
	
	private Long userIdLogin;
	
	private QAFEService qafeService;
	
	
	
	private static final String NAVIGATE_BERTANYA = QAFEConstant.NAVIGATE_QA_FE_BERTANYA_FROM_FAQ;

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
		User userLogin = facesUtil.getUserLogin();
		this.userIdLogin = userLogin.getUserId();
		String searchParam = facesUtil.retrieveRequestParam("SEARCH_VAL");
		if(searchParam != null){
			searchVal = searchParam;
		}
		/*String searchCategory = facesUtil.retrieveRequestParam("SEARCH_CATEGORY");
		if(searchCategory != null){
			category = searchCategory;
		}*/
		String idString = facesUtil.retrieveRequestParam("id");
		if (StringUtils.isNotEmpty(idString)) {
			faqId = Long.parseLong(idString);
		}
		
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			faqId = Long.parseLong(Constants.decryptString(token));
		}
		searchDataInit();
		id="#";
		Number admin = qafeService.getIsAdmin(facesUtil.getUserLogin().getUserId());
		
		if (admin == null) {
			isAdmin = new Long(0);
		} else {
			isAdmin = admin.longValue();
		}
		
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void dataOnClick(){
		String id = facesUtil.retrieveRequestParam("faqId");
		String flag = facesUtil.retrieveRequestParam("flag");
		
		if(flag!=null && flag.toUpperCase().equals("FALSE")){
		HttpServletRequest hreq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String viewId = hreq.getRequestURI();
		LogAccess logAccess = new LogAccess();
		logAccess.setAccessTime(new Timestamp(new Date().getTime()));
		logAccess.setAccessId(new Long(id));
		logAccess.setAccessAction(viewId); 
		logAccess.setUser(getUserLogin());
		logAccess.setSourceIp(getIPAddress());
		logAccessService.save(logAccess);
		}
		
	}
	
	public void searchDataInit(){
		institutionVO = new ArrayList<>();
		List<String[]> listInstitution = faqService.getInstitution(userIdLogin);
		for(int x=0;x<listInstitution.size();x++){
		String[] institution = listInstitution.get(x);
		InstitutionVO insVo = new InstitutionVO();	
		insVo.setInstitution(institution[1]);
		List<CategoryTypeVO> categoryFaq = new ArrayList<>();
		List<String[]> listCategory = faqService.getCategoryFAQByInstitution(institution[0],searchVal,category);
		for(int i=0;i<listCategory.size();i++){
			CategoryTypeVO vo = new CategoryTypeVO();
			String[] faqCategory = listCategory.get(i);
			vo.setFaqCategory(faqCategory[1]);
			List<Faq> listFaq = faqService.getQuestionAndAnswerByCategory(faqCategory[0],searchVal,faqId,institution[0]);
			vo.setListFaq(listFaq);
			categoryFaq.add(vo);
		}
		insVo.setCategoryFaq(categoryFaq);
		
		institutionVO.add(insVo);
		}
		
		if(institutionVO!=null && institutionVO.size()>0){
			for(int i=0;i<institutionVO.size();i++){
				InstitutionVO vo = institutionVO.get(i);
				if(vo.getCategoryFaq() == null || vo.getCategoryFaq().size() == 0){
					institutionVO.remove(vo);
				}else{
					for(int x=0;x<vo.getCategoryFaq().size();x++){
						CategoryTypeVO vo2 = vo.getCategoryFaq().get(x);
						if(vo2.getListFaq() == null || vo2.getListFaq().size() == 0){
							vo.getCategoryFaq().remove(vo2);
						}
					}
				}
			}
		}
		
		if(institutionVO!=null && institutionVO.size()>0){
			for(int i=0;i<institutionVO.size();i++){
				InstitutionVO vo = institutionVO.get(i);
				if(vo.getCategoryFaq() == null || vo.getCategoryFaq().size() == 0){
					institutionVO.remove(vo);
				}else{
					for(int x=0;x<vo.getCategoryFaq().size();x++){
						CategoryTypeVO vo2 = vo.getCategoryFaq().get(x);
						if(vo2.getListFaq() == null || vo2.getListFaq().size() == 0){
							vo.getCategoryFaq().remove(vo2);
						}
					}
				}
			}
		}
	}
	
	public void searchData(){
		institutionVO = new ArrayList<>();
		faqId = null;
		List<String[]> listInstitution = faqService.getInstitution(userIdLogin);
		for(int x=0;x<listInstitution.size();x++){
		String[] institution = listInstitution.get(x);
		InstitutionVO insVo = new InstitutionVO();	
		insVo.setInstitution(institution[1]);
		List<CategoryTypeVO> categoryFaq = new ArrayList<>();
		List<String[]> listCategory = faqService.getCategoryFAQByInstitution(institution[0],searchVal,category);
		for(int i=0;i<listCategory.size();i++){
			CategoryTypeVO vo = new CategoryTypeVO();
			String[] faqCategory = listCategory.get(i);
			vo.setFaqCategory(faqCategory[1]);
			List<Faq> listFaq = faqService.getQuestionAndAnswerByCategory(faqCategory[0],searchVal,faqId,institution[0]);
			vo.setListFaq(listFaq);
			categoryFaq.add(vo);
		}
		insVo.setCategoryFaq(categoryFaq);
		
		institutionVO.add(insVo);
		}
		
		if(institutionVO!=null && institutionVO.size()>0){
			for(int i=0;i<institutionVO.size();i++){
				InstitutionVO vo = institutionVO.get(i);
				if(vo.getCategoryFaq() == null || vo.getCategoryFaq().size() == 0){
					institutionVO.remove(vo);
				}else{
					for(int x=0;x<vo.getCategoryFaq().size();x++){
						CategoryTypeVO vo2 = vo.getCategoryFaq().get(x);
						if(vo2.getListFaq() == null || vo2.getListFaq().size() == 0){
							vo.getCategoryFaq().remove(vo2);
						}
					}
				}
			}
		}
		
		if(institutionVO!=null && institutionVO.size()>0){
			for(int i=0;i<institutionVO.size();i++){
				InstitutionVO vo = institutionVO.get(i);
				if(vo.getCategoryFaq() == null || vo.getCategoryFaq().size() == 0){
					institutionVO.remove(vo);
				}else{
					for(int x=0;x<vo.getCategoryFaq().size();x++){
						CategoryTypeVO vo2 = vo.getCategoryFaq().get(x);
						if(vo2.getListFaq() == null || vo2.getListFaq().size() == 0){
							vo.getCategoryFaq().remove(vo2);
						}
					}
				}
			}
		}
	}
	
	public void search(){
		searchVal = facesUtil.retrieveRequestParam("SEARCH_VAL");
		//category = facesUtil.retrieveRequestParam("SEARCH_CATEGORY");
		searchData();
		removeHeader();
	}
	
	public void removeHeader(){
		
		for(int i=0;i<institutionVO.size();i++){
			InstitutionVO vo = institutionVO.get(i);
				for(int x=0;x<vo.getCategoryFaq().size();x++){
					CategoryTypeVO cVo = vo.getCategoryFaq().get(x);
					if(cVo.getListFaq() == null || cVo.getListFaq().size() == 0){
						vo.getCategoryFaq().remove(cVo);
					}
				}
			
		}
		
		for(int i=0;i<institutionVO.size();i++){
			InstitutionVO vo = institutionVO.get(i);
			if(vo.getCategoryFaq() == null || vo.getCategoryFaq().size() == 0){
				institutionVO.remove(vo);
			}
		}
	}
	
	public String toEncrypt(Long faqid){
		try {
			ParameterDetail pd = parameterDetailService.getParameterDetailByParamDtlCode("HOST_NAME_APPLICATION");
			return pd.getNameIn().concat("pages/faqFE/faqFE.faces?token=").concat(Constants.encryptString(faqid.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	

	public FaqService getFaqService() {
		return faqService;
	}

	public void setFaqService(FaqService faqService) {
		this.faqService = faqService;
	}

	public Long getCountQuestionNotAnswered() {
		return countQuestionNotAnswered;
	}

	public void setCountQuestionNotAnswered(Long countQuestionNotAnswered) {
		this.countQuestionNotAnswered = countQuestionNotAnswered;
	}

	public String getLastUpdateQuestionNotAnswered() {
		return lastUpdateQuestionNotAnswered;
	}

	public void setLastUpdateQuestionNotAnswered(String lastUpdateQuestionNotAnswered) {
		this.lastUpdateQuestionNotAnswered = lastUpdateQuestionNotAnswered;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		FaqFEBean.logger = logger;
	}

	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<InstitutionVO> getInstitutionVO() {
		return institutionVO;
	}

	public void setInstitutionVO(List<InstitutionVO> institutionVO) {
		this.institutionVO = institutionVO;
	}

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static String getNavigateBertanya() {
		return NAVIGATE_BERTANYA;
	}

	public Long getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Long isAdmin) {
		this.isAdmin = isAdmin;
	}

	public QAFEService getQafeService() {
		return qafeService;
	}

	public void setQafeService(QAFEService qafeService) {
		this.qafeService = qafeService;
	}

	public Long getUserIdLogin() {
		return userIdLogin;
	}

	public void setUserIdLogin(Long userIdLogin) {
		this.userIdLogin = userIdLogin;
	}

	public Long getFaqId() {
		return faqId;
	}

	public void setFaqId(Long faqId) {
		this.faqId = faqId;
	}

	

	public List<SelectItem> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<SelectItem> categoryList) {
		this.categoryList = categoryList;
	}


	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	

	
	
	
	

}