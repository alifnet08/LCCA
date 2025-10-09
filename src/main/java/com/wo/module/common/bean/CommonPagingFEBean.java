package com.wo.module.common.bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.SortOrder;
import org.springframework.util.StringUtils;

import com.wo.module.advocateFE.constant.AdvocateFEConstants;
import com.wo.module.article.constant.ArticleConstants;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.correspondenceFE.constant.CorrespondenceFEConstants;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.opinionFE.constant.OpinionFEConstants;
import com.wo.module.qaFE.constant.QAFEConstant;
import com.wo.module.trcCorrespondence.constant.TrcCorrespondenceConstants;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class CommonPagingFEBean<T> extends CommonBean implements RetrieverDataPage<T> {
	static Logger logger = Logger.getLogger(CommonPagingFEBean.class);
	private static final long serialVersionUID = -6844095679623633531L;
	
	private Long rowCount;
	
	private Long totalPage;
	
	@SuppressWarnings("rawtypes")
	private List<SearchObject> searchCriteria;
	
	private List<Integer> totalPageList;
	
	private Integer first;
	
	private Integer pageSize;
	
	private List<T> listData;
	
	public FacesUtil facesUtil;
	
	private String searchVal;
	
	private String searchStatus;
	
	private String searchCategory;
	
	private String articleType;
	
	private Long docType;
	
	private Long docCategory;
	
	private UserService userService;
	
	private Long idLogin;
	
	private String divName;
	
	private String searchTahun;
	
	private String searchArea;
	
	private String searchCabang;
	
	private String searchPartner;
	
	private int firstTemp;
	
	private Integer initFirst;
	
	public void init() {
		super.init();
		first = 0;
		if(pageSize == null || pageSize == 0) {
			pageSize = 5;
		}
		//this.idLogin = facesUtil.getUserLogin().getUserId();
		//this.divName = facesUtil.getUserLogin().getDivisionName();
	}
	
	public void prev(){
		//first = Math.round(first / pageSize.intValue());
		
		if(first >0){
		first  = first - 1;
		
		//System.out.println(first.toString().substring(first.toString().length()-1));
		
		if(first.toString().substring(first.toString().length()-1).equals("9")){
			totalPageList = new ArrayList<>();
			for(int i=(first-9);i<=(first);i++){
				totalPageList.add(i+1);
			}
		}
		
		first = first * pageSize;
		searchDataPrevNext();
		}else{
			PrimeFaces.current().executeScript("$('li').removeClass('active'); $( '.row-"+first+"' ).addClass( 'active' );");
		}
	}
	
	public void next(){
		
		//first = Math.round(first / pageSize.intValue());
		
		if(first < (totalPage-1) && first >= 0){
		first  = first + 1;
		
		if(first.toString().substring(first.toString().length()-1).equals("0")){
			totalPageList = new ArrayList<>();
			for(int i=(first);i<=(first+9);i++){
				if(i<(totalPage)){
				totalPageList.add(i+1);
				}
			}
		}
		
		
		first = first * pageSize;
		searchDataPrevNext();
		}
		else{
			PrimeFaces.current().executeScript("$('li').removeClass('active'); $( '.row-"+first+"' ).addClass( 'active' );");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void searchData(){
		String pageSelected = facesUtil.retrieveRequestParam("PAGE_SELECTED");
		if(pageSelected !=null && !StringUtils.isEmpty(pageSelected)){
			first = new Integer(pageSelected);
		}
		if (initFirst != null) {
			if (pageSelected != null) {
				// do nothing
			} else {
				first = initFirst;
				firstTemp = initFirst;
			}
		} else {
			first = first * pageSize;
		}
		
		String searchValParam = facesUtil.retrieveRequestParam("SEARCH_VAL");
		if(searchValParam !=null){
			searchValParam = searchValParam.replace(":and", "&").replace(":percent", "%");
			searchVal = searchValParam;
		}
		
		String searchStatusParam = facesUtil.retrieveRequestParam("SEARCH_STATUS");
		if(searchStatusParam !=null){
			searchStatus = searchStatusParam;
		}
		
		String searchCategoryParam = facesUtil.retrieveRequestParam("SEARCH_CATEGORY");
		if(searchCategoryParam !=null){
			searchCategory = searchCategoryParam;
		}
		
		
		String searchArticleTypeParam = facesUtil.retrieveRequestParam("ARTICLE_TYPE");
		if(searchArticleTypeParam !=null){
			articleType = searchArticleTypeParam;
		}
		
		Long searchDocTypeParam = facesUtil.retrieveRequestParam("DOC_TYPE") != null ? 
				Long.parseLong(facesUtil.retrieveRequestParam("DOC_TYPE")) : null;
		if(searchDocTypeParam !=null){
			docType = searchDocTypeParam;
		}
		
		Long searchDocCategoryParam = facesUtil.retrieveRequestParam("DOC_CATEGORY") != null ? 
				Long.parseLong(facesUtil.retrieveRequestParam("DOC_CATEGORY")) : null;
		if(searchDocCategoryParam !=null){
			docCategory = searchDocCategoryParam;
		}
		
		String searchTahunParam = facesUtil.retrieveRequestParam("SEARCH_TAHUN");
		if (searchTahunParam != null) {
			searchTahun = searchTahunParam;
		}
		
		searchCriteria = new ArrayList<SearchObject>();
		if (searchVal != null && !searchVal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_TEXT_BOX, searchVal));
		}
		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_COMBO_BOX, searchStatus));
		}
		if (articleType != null && !articleType.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OpinionFEConstants.SEARCH_BY_ARTICLE_TYPE, articleType));
		}
		
		if (docType != null) {
			String docTypeStr = docType.toString();
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE, docTypeStr));
		}
		
		if (docCategory != null) {
			String docCatStr = docCategory.toString();
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_CATEGORY, docCatStr));
		}
		
		if (searchCategory != null && !searchCategory.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAFEConstant.SEARCH_BY_CATEGORY, searchCategory));
		}
		
		if (searchTahun != null && !searchTahun.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_YEAR, searchTahun));
		}
		
		if (searchArea != null && !searchArea.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, searchArea));
		}
		
		if (searchCabang != null && !searchCabang.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateFEConstants.SEARCH_CABANG, searchCabang));
		}
		
		if (searchPartner != null && !searchPartner.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateFEConstants.SEARCH_PARTNER, searchPartner));
		}
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, userLogin.getDivisionName()));
		}else {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, 0));
		}
		
		
		/*if (idLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, idLogin));
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, divName));
		}*/
		try {
			rowCount = searchCountData(searchCriteria);
			
			Double d=  rowCount.doubleValue()/pageSize.doubleValue();
			DecimalFormat df = new DecimalFormat("###.###");
			String dStr = df.format(d);
			dStr = dStr.replaceAll("\\.", ",");
			String[] dSplit = dStr.split(",");
			Long iData = new Long(0);
			if(dSplit.length > 1){
				iData = Long.parseLong(dSplit[0]);
				if(Double.parseDouble(dSplit[1])>0){
					iData++;
				}
			}else{
				iData = Long.parseLong(dSplit[0]);
			}
			
			if(iData == 0){
				iData++;
			}
			
			totalPage = iData;
			
			totalPageList = new ArrayList<>();
			for(int i=0;i<(totalPage <= 10? totalPage : 10);i++){
				totalPageList.add(i+1);
			}
			
			listData = searchData(searchCriteria, first, pageSize, null, null);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		first = Math.round(first / pageSize.intValue());
		PrimeFaces.current().executeScript("$('li').removeClass('active'); $( '.row-"+first+"' ).addClass( 'active' );");
		
		if (facesUtil.getSessionAttribute("BACK_SESSION") != null) {
			facesUtil.removeSessionAttribute("BACK_SESSION");
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	public void searchDataPrevNext(){
		String pageSelected = facesUtil.retrieveRequestParam("PAGE_SELECTED");
		if(pageSelected !=null && !StringUtils.isEmpty(pageSelected)){
			first = new Integer(pageSelected);
			first = first * pageSize;
		}
		
		
		String searchValParam = facesUtil.retrieveRequestParam("SEARCH_VAL");
		if(searchValParam !=null){
			searchVal = searchValParam;
		}
		
		String searchStatusParam = facesUtil.retrieveRequestParam("SEARCH_STATUS");
		if(searchStatusParam !=null){
			searchStatus = searchStatusParam;
		}
		
		String searchArticleTypeParam = facesUtil.retrieveRequestParam("ARTICLE_TYPE");
		if(searchArticleTypeParam !=null){
			articleType = searchArticleTypeParam;
		}
		
		Long searchDocTypeParam = facesUtil.retrieveRequestParam("DOC_TYPE") != null ? 
				Long.parseLong(facesUtil.retrieveRequestParam("DOC_TYPE")) : null;
		if(searchDocTypeParam !=null){
			docType = searchDocTypeParam;
		}
		
		Long searchDocCategoryParam = facesUtil.retrieveRequestParam("DOC_CATEGORY") != null ? 
				Long.parseLong(facesUtil.retrieveRequestParam("DOC_CATEGORY")) : null;
		if(searchDocCategoryParam !=null){
			docCategory = searchDocCategoryParam;
		}
		
		if (searchCategory != null && !searchCategory.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAFEConstant.SEARCH_BY_CATEGORY, searchCategory));
		}
		
		String searchTahunParam = facesUtil.retrieveRequestParam("SEARCH_TAHUN");
		if (searchTahunParam != null) {
			searchTahun = searchTahunParam;
		}
		
		
		searchCriteria = new ArrayList<SearchObject>();
		if (searchVal != null && !searchVal.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_TEXT_BOX, searchVal));
		}
		if (searchStatus != null && !searchStatus.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_COMBO_BOX, searchStatus));
		}
		
		if (articleType != null && !articleType.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(OpinionFEConstants.SEARCH_BY_ARTICLE_TYPE, articleType));
		}
		
		if (docType != null) {
			String docTypeStr = docType.toString();
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_DOC_TYPE, docTypeStr));
		}
		
		if (docCategory != null) {
			String docCatStr = docCategory.toString();
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_CATEGORY, docCatStr));
		}
		
		if (searchCategory != null && !searchCategory.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(QAFEConstant.SEARCH_BY_CATEGORY, searchCategory));
		}
		
		if (searchTahun != null && !searchTahun.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(InternalRegulationConstants.WHERE_YEAR, searchCategory));
		}
		
		if (searchArea != null && !searchArea.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(NotaryConstants.SEARCH_BY_AREA, searchArea));
		}
		
		if (searchCabang != null && !searchCabang.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateFEConstants.SEARCH_CABANG, searchCabang));
		}
		
		if (searchPartner != null && !searchPartner.isEmpty()) {
			searchCriteria.add(new DefaultSearchObject(AdvocateFEConstants.SEARCH_PARTNER, searchPartner));
		}
		
		User userLogin = userService.getUserByNik(facesUtil.retrieveUserLogin());
		if (userLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, userLogin.getUserId()));
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, userLogin.getDivisionName()));
		}
		
		/*if (idLogin != null) {
			searchCriteria.add(new DefaultSearchObject(CommonConstants.SEARCH_BY_USER_LOGIN, idLogin));
			searchCriteria.add(new DefaultSearchObject(ArticleConstants.SEARCH_BY_DIVISION_NAME, divName));
		}*/
		try {
			firstTemp = first;
			listData = searchData(searchCriteria, first, pageSize, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		first = Math.round(first / pageSize.intValue());
		PrimeFaces.current().executeScript("$('li').removeClass('active'); $( '.row-"+first+"' ).addClass( 'active' );");
		
	}
	
	
	
	public void search(){
		searchData();
	}

	public void removeSession() {
		if (facesUtil.getSessionAttribute("BACK_SESSION") != null) {
			Object data = facesUtil.getSessionAttribute("BACK_SESSION");
			Boolean flag = (Boolean) data;
			
			if (!flag) {
				if (facesUtil.getSessionAttribute("FIRST_ADVOCATE_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_ADVOCATE_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_ARTICLE_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_ARTICLE_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_AUDIT_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_AUDIT_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_COMP_TEST_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_COMP_TEST_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_CORRES_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_CORRES_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_CPSA_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_CPSA_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_DISC_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_DISC_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_INTER_REGULATION_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_INTER_REGULATION_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_EXTER_REGULATION_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_EXTER_REGULATION_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_FINE_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_FINE_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_LITIG_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_LITIG_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_NOTARY_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_NOTARY_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_OPINION_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_OPINION_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_REG_MONITOR_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_REG_MONITOR_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_REG_REPORT_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_REG_REPORT_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_SOCIAL_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_SOCIAL_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_TEMPLATE_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_TEMPLATE_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_ANNOUNC_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_ANNOUNC_FE");
				}
				if (facesUtil.getSessionAttribute("FIRST_QA_FE") != null) {
					facesUtil.removeSessionAttribute("FIRST_QA_FE");
				}
				if (facesUtil.getSessionAttribute("BACK_SESSION") != null) {
					facesUtil.removeSessionAttribute("BACK_SESSION");
				}
			}
		}
	}
	

	public String getSearchVal() {
		return searchVal;
	}

	public void setSearchVal(String searchVal) {
		this.searchVal = searchVal;
	}

	public String getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}

	public static Logger getLogger() {
		return logger;
	}


	public static void setLogger(Logger logger) {
		CommonPagingFEBean.logger = logger;
	}


	public Long getRowCount() {
		return rowCount;
	}


	public void setRowCount(Long rowCount) {
		this.rowCount = rowCount;
	}


	public Long getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(Long totalPage) {
		this.totalPage = totalPage;
	}


	public List<Integer> getTotalPageList() {
		return totalPageList;
	}


	public void setTotalPageList(List<Integer> totalPageList) {
		this.totalPageList = totalPageList;
	}


	public Integer getFirst() {
		return first;
	}


	public void setFirst(Integer first) {
		this.first = first;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public List<T> searchData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long searchCountData(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> getListData() {
		return listData;
	}

	public void setListData(List<T> listData) {
		this.listData = listData;
	}

	@SuppressWarnings("rawtypes")
	public List<SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(@SuppressWarnings("rawtypes") List<SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public String getArticleType() {
		return articleType;
	}

	public void setArticleType(String articleType) {
		this.articleType = articleType;
	}

	public Long getDocType() {
		return docType;
	}

	public void setDocType(Long docType) {
		this.docType = docType;
	}

	public Long getDocCategory() {
		return docCategory;
	}

	public void setDocCategory(Long docCategory) {
		this.docCategory = docCategory;
	}

	public Long getIdLogin() {
		return idLogin;
	}

	public void setIdLogin(Long idLogin) {
		this.idLogin = idLogin;
	}

	public String getDivName() {
		return divName;
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	public String getSearchTahun() {
		return searchTahun;
	}

	public void setSearchTahun(String searchTahun) {
		this.searchTahun = searchTahun;
	}

	public String getSearchArea() {
		return searchArea;
	}

	public void setSearchArea(String searchArea) {
		this.searchArea = searchArea;
	}

	public int getFirstTemp() {
		return firstTemp;
	}

	public void setFirstTemp(int firstTemp) {
		this.firstTemp = firstTemp;
	}

	public Integer getInitFirst() {
		return initFirst;
	}

	public void setInitFirst(Integer initFirst) {
		this.initFirst = initFirst;
	}

	public String getSearchCabang() {
		return searchCabang;
	}

	public void setSearchCabang(String searchCabang) {
		this.searchCabang = searchCabang;
	}

	public String getSearchPartner() {
		return searchPartner;
	}

	public void setSearchPartner(String searchPartner) {
		this.searchPartner = searchPartner;
	}

	
	
	
	
	

	
	
	
	
	
}
