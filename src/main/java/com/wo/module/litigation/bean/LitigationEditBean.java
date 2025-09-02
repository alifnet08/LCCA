package com.wo.module.litigation.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.util.EntityUtil;
import com.wo.module.common.util.FileUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigation.model.LitigationNew;
import com.wo.module.litigation.model.LitigationPailitPkpu;
import com.wo.module.litigation.model.LitigationPerdataTergugat;
import com.wo.module.litigation.model.LitigationPerdataInspectLevel;
import com.wo.module.litigation.model.LitigationPerdataPenggugat;
import com.wo.module.litigation.model.LitigationPic;
import com.wo.module.litigation.model.LitigationPicTableModel;
import com.wo.module.litigation.model.LitigationPidanaPelapor;
import com.wo.module.litigation.model.LitigationPidanaTerlapor;
import com.wo.module.litigation.model.LitigationPihakKuratorTurutTergugat;
import com.wo.module.litigation.model.LitigationPihakKuratorTurutTergugatTableModel;
import com.wo.module.litigation.model.LitigationPihakPenggugatPemohon;
import com.wo.module.litigation.model.LitigationPihakPenggugatPemohonTableModel;
import com.wo.module.litigation.model.LitigationPihakTergugatTermohon;
import com.wo.module.litigation.model.LitigationPihakTergugatTermohonTableModel;
import com.wo.module.litigation.model.LitigationProgressPerkara;
import com.wo.module.litigation.model.LitigationProgressPerkaraTableModel;
import com.wo.module.litigation.model.LitigationPutusanPengadilan;
import com.wo.module.litigation.model.LitigationPutusanPengadilanTableModel;
import com.wo.module.litigation.service.LitigationNewService;
import com.wo.module.logActivity.model.LogActivity;
import com.wo.module.logActivity.service.LogActivityService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LitigationEditBean extends CommonBean implements  SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(LitigationEditBean.class);

	private LitigationNew litigationNew;
	//add
	private LogActivity logActivity;
	private HashMap<String, String> recordCaseTypeDtl = new HashMap<String, String>();
	
	private Boolean isViewOnly;
	private String actionMode;
	private List<String> keywords;
	private String editedId;
	private String textWarningUpload;

	private LitigationNewService litigationNewService;
	private UserService userService;
	private LogActivityService logActivityService;
	private RCService rcService;
	
	public FacesUtil facesUtil;
	
	private Integer lastSequenceOfDtl;

	
	private List<SelectItem> caseTypeList;
	private List<SelectItem> courtTypeList;
	private List<SelectItem> caseTypePerdataDtlList;
	private List<SelectItem> caseTypePidanaDtlList;
	private List<SelectItem> caseHandlerList;
	private List<SelectItem> decisionStatusList;
	private List<SelectItem> judgementWarningList;
	private List<SelectItem> countryCourtList;
	private List<SelectItem> caseTeamHandlerList;
	private List<SelectItem> caseMainTopicList;
	private List<SelectItem> caseSubTopicList;
	private List<SelectItem> foreignCurrencyList;
	private List<SelectItem> rcList;
	private List<SelectItem> commercialCourtList;
	
	private SelectorInfo selectorPic;
	
	//UNUSED
	private Integer indexDtlPic;
	private Integer lastSequenceLitigationPic;
	private LitigationPic[] selectedLitigationPic;
	private LitigationPicTableModel<LitigationPic> tableModelLitigationPic;
	//UNUSED
	
	private Integer indexDtlProgressPerkara;
	private Integer lastSequenceLitigationProgressPerkara;
	private LitigationProgressPerkara[] selectedLitigationProgressPerkara;
	private LitigationProgressPerkaraTableModel<LitigationProgressPerkara> tableModelLitigationProgressPerkara;
	
	private Integer indexDtlPihakPenggugatPemohon;
	private Integer lastSequencePihakPenggugatPemohon;
	private LitigationPihakPenggugatPemohon[] selectedLitigationPihakPenggugatPemohon;
	private LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> tableModelLitigationPihakPenggugatPemohon;
	
	private Integer indexDtlPihakTergugatTermohon;
	private Integer lastSequencePihakTergugatTermohon;
	private LitigationPihakTergugatTermohon[] selectedLitigationPihakTergugatTermohon;
	private LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> tableModelLitigationPihakTergugatTermohon;
	
	private Integer indexDtlPihakKuratorTurutTergugat;
	private Integer lastSequencePihakKuratorTurutTergugat;
	private LitigationPihakKuratorTurutTergugat[] selectedLitigationPihakKuratorTurutTergugat;
	private LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> tableModelLitigationPihakKuratorTurutTergugat;
	
	private Integer indexDtlPutusanPengadilan;
	private Integer lastSequencePutusanPengadilan;
	private LitigationPutusanPengadilan[] selectedLitigationPutusanPengadilan;
	private LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> tableModelLitigationPutusanPengadilan;
	
	private List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon;
	private List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon;
	private List<LitigationProgressPerkara> deletedListProgressPerkara;
	private List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat;
	private List<LitigationPutusanPengadilan> deletedListPutusanPengadilan;
	
	private List<LitigationPihakPenggugatPemohon> listPihakPenggugatPerdataTergugat;
	private List<LitigationPihakPenggugatPemohon> listPihakPenggugatPerdataPenggugat;
	private List<LitigationPihakPenggugatPemohon> listPihakPemohonPailitPkpu;
	private List<LitigationPihakPenggugatPemohon> listPihakPelaporPidanaTerlapor;
	private List<LitigationPihakPenggugatPemohon> listPihakPelaporPidanaPelapor;
	
	private List<LitigationPihakTergugatTermohon> listPihakTergugatPerdataTergugat;
	private List<LitigationPihakTergugatTermohon> listPihakTergugatPerdataPenggugat;
	private List<LitigationPihakTergugatTermohon> listPihakTermohonPailitPkpu;
	private List<LitigationPihakTergugatTermohon> listPihakTerlaporPidanaTerlapor;
	private List<LitigationPihakTergugatTermohon> listPihakTerlaporPidanaPelapor;
	
	private List<LitigationProgressPerkara> listProgressPerkaraPerdataTergugat;
	private List<LitigationProgressPerkara> listProgressPerkaraPerdataPenggugat;
	private List<LitigationProgressPerkara> listProgressPerkaraPailitPkpu;
	private List<LitigationProgressPerkara> listProgressPerkaraPidanaTerlapor;
	private List<LitigationProgressPerkara> listProgressPerkaraPidanaPelapor;
	
	private List<LitigationPihakKuratorTurutTergugat> listPihakTurutTergugatPerdataTergugat;
	private List<LitigationPihakKuratorTurutTergugat> listPihakTurutTergugatPerdataPenggugat;
	private List<LitigationPihakKuratorTurutTergugat> listPihakKuratorPailitPkpu;
	
	private boolean disabled = true;
	
	private FileUtil fileUtil;

	private boolean checkAll;

	private String navigateSealitigationh = LitigationConstants.NAVIGATE_SEARCH;

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addErrMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public static SelectorInfo buildSelectorUser() {
		SelectorModel.SelectorInfo info = new SelectorModel.SelectorInfo(
                " select USER_ID, NIK, NAME, division_name from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " and case when ('{1}' = '0') then (division_id like '%%' or division_id is null) else division_id like '%{1}%' end "
                //+ " AND ('{1}' = '0' OR division_id LIKE '%0%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' "
                + " order by NAME "  ,
                " SELECT COUNT(1) from wo_mst_user "
                + " where 1=1 "
                + " and ( upper(NAME) like upper('%{0}%') or upper(NIK) like upper('%{0}%') or upper(division_name) like upper('%{0}%') ) "
                //+ " AND ('{1}' = '0' OR division_id LIKE '%0%') AND ('{1}' <> '0' OR division_id LIKE '%%' OR division_id IS NULL) "
                + " and enabled_flag = 'Y' ",
                Arrays.asList("NPK", "Name", "Division Name"),
                Arrays.asList("1", "2", "3"),false);
        return info;
	}

	@PostConstruct
	public void init() {
		super.init();
		initList();
		checkNewOrEdit();
		selectorPic = buildSelectorUser();
		tableModelLitigationPic = new LitigationPicTableModel<LitigationPic>(litigationNew.getLitigationPics());
		tableModelLitigationPutusanPengadilan = new LitigationPutusanPengadilanTableModel<>(litigationNew.getListPutusanPengadilan());
		
		deletedListPihakKuratorTurutTergugat = new ArrayList<LitigationPihakKuratorTurutTergugat>();
		deletedListPihakPenggugatPemohon = new ArrayList<LitigationPihakPenggugatPemohon>();
		deletedListPihakTergugatTermohon = new ArrayList<LitigationPihakTergugatTermohon>();
		deletedListProgressPerkara = new ArrayList<LitigationProgressPerkara>();
		deletedListPutusanPengadilan = new ArrayList<LitigationPutusanPengadilan>();
		
		fileUtil = FileUtil.getInstance();
	}
	
	public void initList(){
		try {
			caseTypeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCaseType = parameterDetailService
						.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);
	
			for (ParameterDetail vo : listCaseType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypeList.add(si);
				
			}
		
			caseTypePerdataDtlList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCaseTypeDtlPerdata = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PERDATA);
			
			for (ParameterDetail vo : listCaseTypeDtlPerdata) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypePerdataDtlList.add(si);
				recordCaseTypeDtl.put(vo.getParameterDtlCode(), vo.getName());
			}
		
			caseTypePidanaDtlList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCaseTypeDtlPidana = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PIDANA);
			
			for (ParameterDetail vo : listCaseTypeDtlPidana) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypePidanaDtlList.add(si);
			}
		
			caseHandlerList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCaseHandler = parameterDetailService
					.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_HANDLER);
			
			for(ParameterDetail vo : listCaseHandler) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseHandlerList.add(si);
			}
		
			decisionStatusList = new ArrayList<SelectItem>();
			List<ParameterDetail> listDecisionStatus = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_DECISION_STATUS);
				
			for(ParameterDetail vo : listDecisionStatus) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				decisionStatusList.add(si);
			}
		
			judgementWarningList = new ArrayList<SelectItem>();
			List<ParameterDetail> listJudgementWarning = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_JUDGEMENT_WARNING);
			
			for(ParameterDetail vo : listJudgementWarning) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				judgementWarningList.add(si);
			}
			
			courtTypeList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCourtType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_COURT_TYPE);
			
			for(ParameterDetail vo : listCourtType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				courtTypeList.add(si);
			}
			
			caseTeamHandlerList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCaseTeamHandler = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TEAM_HANDLER);
			
			for(ParameterDetail vo : listCaseTeamHandler) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTeamHandlerList.add(si);
			}
			
			foreignCurrencyList = new ArrayList<SelectItem>();
			List<ParameterDetail> listForeignCurrency = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_FOREIGN_CURRENCY_TYPE);
			
			for(ParameterDetail vo : listForeignCurrency) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				foreignCurrencyList.add(si);
			}
			
			caseMainTopicList = new ArrayList<SelectItem>();
			List<ParameterDetail> listMainTopic = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_MAIN_TOPIC);
			
			for(ParameterDetail vo : listMainTopic) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseMainTopicList.add(si);
			}
			
			caseSubTopicList = new ArrayList<SelectItem>();
			List<ParameterDetail> listSubTopic = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_SUB_TOPIC);
			
			for(ParameterDetail vo : listSubTopic) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseSubTopicList.add(si);
			}
			
			rcList = new ArrayList<SelectItem>();
			List<RC> listRC = rcService.getRCList();
			for(RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
			
			commercialCourtList = new ArrayList<SelectItem>();
			List<ParameterDetail> listCommercialCourt = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_COMMERCIAL_COURT);
			
			for(ParameterDetail vo : listCommercialCourt) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				commercialCourtList.add(si);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		
	}

	public void onChangeCaseType() {
		if(actionMode.equalsIgnoreCase("ADD")) {
			this.resetForm();
		}
		
		System.out.println("selected case type: "+litigationNew.getSelectCaseType());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onChangeCaseTypeDtl() {
		if(actionMode.equalsIgnoreCase("ADD")) {
			this.resetForm();
		}
		
		try {
			litigationNew.setCaseTypeDtl(parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTypeDtl()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("selected case type dtl Perdata: " + litigationNew.getSelectCaseTypeDtlPerdata() != null ?  litigationNew.getSelectCaseTypeDtlPerdata() : "");
		System.out.println("selected case type dtl Pidana: " + litigationNew.getSelectCaseTypeDtlPerdata() != null ? litigationNew.getSelectCaseTypeDtl() : "");
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void onChangeCaseTypeDtlPerdata() {
		if(actionMode.equalsIgnoreCase("ADD")) {
			this.resetForm();
		}
		
		try {
			litigationNew.setCaseTypeDtl(parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTypeDtlPerdata()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("selected case type dtl perdata: " + litigationNew.getSelectCaseTypeDtlPerdata());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}	
	
	private void resetForm() {
		litigationNew.setSelectedPicName("");
		litigationNew.setPicDescription("");
		litigationNew.setCaseTeamHandler(new ParameterDetail());
		litigationNew.setPicDescPerdataTer("");litigationNew.setPicDescPerdataPeng("");litigationNew.setPicDescPailitPkpu("");litigationNew.setPicDescPidanaTer("");litigationNew.setPicDescPidanaPel("");
		litigationNew.setSelectCaseTeamHandlerPidanaTer("");litigationNew.setSelectCaseTeamHandlerPidanaPel("");
		litigationNew.setSelectCaseTeamHandlerPerdataPeng("");litigationNew.setSelectCaseTeamHandlerPerdataTer("");
		litigationNew.setSelectCaseTeamHandlerPailitPkpu("");
		
		litigationNew.setLitigationPerdataTergugat(new LitigationPerdataTergugat());
		litigationNew.getLitigationPerdataTergugat().setLitigationPerdataInspectLevel(new LitigationPerdataInspectLevel());
		
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPaFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(false);
		litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(false);
		
		litigationNew.setLitigationPerdataPenggugat(new LitigationPerdataPenggugat());
		
		litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(false);
		litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(false);
		
		litigationNew.setLitigationPailitPkpu(new LitigationPailitPkpu());
		
		litigationNew.setLitigationPidanaPelapor(new LitigationPidanaPelapor());
		
		litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(false);
		litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(false);
		litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(false);
		litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(false);
		
		
		litigationNew.setLitigationPidanaTerlapor(new LitigationPidanaTerlapor());
		
		litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(false);
		litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(false);
		litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(false);
		litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(false);
		
		litigationNew.setListPihakPenggugatPemohon(new ArrayList<LitigationPihakPenggugatPemohon>());
		litigationNew.setListPihakTergugatTermohon(new ArrayList<LitigationPihakTergugatTermohon>());
		litigationNew.setListPihakKuratorTurutTergugat(new ArrayList<LitigationPihakKuratorTurutTergugat>());
		litigationNew.setListProgresPerkara(new ArrayList<LitigationProgressPerkara>());
		litigationNew.setListPutusanPengadilan(new ArrayList<LitigationPutusanPengadilan>());
		
		listPihakPenggugatPerdataTergugat = new ArrayList<LitigationPihakPenggugatPemohon>();
		listPihakPenggugatPerdataPenggugat = new ArrayList<LitigationPihakPenggugatPemohon>();
		listPihakPemohonPailitPkpu = new ArrayList<LitigationPihakPenggugatPemohon>();
		listPihakPelaporPidanaTerlapor = new ArrayList<LitigationPihakPenggugatPemohon>();
		listPihakPelaporPidanaPelapor = new ArrayList<LitigationPihakPenggugatPemohon>();
		
		listPihakTergugatPerdataTergugat = new ArrayList<LitigationPihakTergugatTermohon>();
		listPihakTergugatPerdataPenggugat = new ArrayList<LitigationPihakTergugatTermohon>();
		listPihakTermohonPailitPkpu = new ArrayList<LitigationPihakTergugatTermohon>();
		listPihakTerlaporPidanaTerlapor = new ArrayList<LitigationPihakTergugatTermohon>();
		listPihakTerlaporPidanaPelapor = new ArrayList<LitigationPihakTergugatTermohon>();
		
		listProgressPerkaraPerdataTergugat = new ArrayList<LitigationProgressPerkara>();
		listProgressPerkaraPerdataPenggugat = new ArrayList<LitigationProgressPerkara>();
		listProgressPerkaraPailitPkpu = new ArrayList<LitigationProgressPerkara>();
		listProgressPerkaraPidanaTerlapor = new ArrayList<LitigationProgressPerkara>();
		listProgressPerkaraPidanaPelapor = new ArrayList<LitigationProgressPerkara>();
		
		listPihakTurutTergugatPerdataTergugat = new ArrayList<LitigationPihakKuratorTurutTergugat>();
		listPihakTurutTergugatPerdataPenggugat = new ArrayList<LitigationPihakKuratorTurutTergugat>();
		listPihakKuratorPailitPkpu = new ArrayList<LitigationPihakKuratorTurutTergugat>();
		
		
		
		if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA)) {
			if(StringUtils.isNotBlank(litigationNew.getSelectCaseTypeDtlPerdata())) {
					if(litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
				
					tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPenggugatPerdataTergugat);
					tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTergugatPerdataTergugat);
					tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakTurutTergugatPerdataTergugat);
					tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPerdataTergugat);
					
				}else if(!litigationNew.getSelectCaseTypeDtlPerdata().isEmpty() && litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
					
					tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPenggugatPerdataPenggugat);
					tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTergugatPerdataPenggugat);
					tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakTurutTergugatPerdataPenggugat);
					tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPerdataPenggugat);
					
				}
			}
		}else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)) {
			if(StringUtils.isNotBlank(litigationNew.getSelectCaseTypeDtl())) {
				if(litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
			
					tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPelaporPidanaTerlapor);
					tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTerlaporPidanaTerlapor);
					tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPidanaTerlapor);
					
				}else if(!litigationNew.getSelectCaseTypeDtl().isEmpty() && litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
					
					tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPelaporPidanaPelapor);
					tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTerlaporPidanaPelapor);
					tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPidanaPelapor);
					
				}
			}
		}else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
			
			tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPemohonPailitPkpu);
			tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTermohonPailitPkpu);
			tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakKuratorPailitPkpu);
			tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPailitPkpu);
		
		}
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	private void checkNewOrEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("id");String token = facesUtil.retrieveRequestParam("token"); 
			
			String viewId = facesUtil.retrieveRequestParam("viewId");
			isViewOnly = false;
			if (viewId != null && !viewId.isEmpty()) {
				if (viewId.trim().equalsIgnoreCase("true")) {
					isViewOnly = true;
				}
			}
			if (StringUtils.isBlank(editId) && StringUtils.isBlank(token)) {
				this.handleNew();
				
				actionMode = "ADD";
			} else {
				this.handleEdit(editId);
				
				actionMode = "EDIT";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleNew() {
		try {
			litigationNew = new LitigationNew();
			this.resetForm();
			
			PrimeFaces.current().executeScript("disable();");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		
		lastSequencePihakPenggugatPemohon = 0; lastSequencePihakTergugatTermohon = 0; lastSequencePihakKuratorTurutTergugat = 0; lastSequenceLitigationProgressPerkara = 0; lastSequencePutusanPengadilan = 0;
		listPihakPenggugatPerdataTergugat = new ArrayList<>();
		listPihakPenggugatPerdataPenggugat = new ArrayList<>();
		listPihakPemohonPailitPkpu = new ArrayList<>();
		listPihakPelaporPidanaTerlapor = new ArrayList<>();
		listPihakPelaporPidanaPelapor = new ArrayList<>();
		
		listPihakTergugatPerdataTergugat = new ArrayList<>();
		listPihakTergugatPerdataPenggugat = new ArrayList<>();
		listPihakTermohonPailitPkpu = new ArrayList<>();
		listPihakTerlaporPidanaTerlapor = new ArrayList<>();
		listPihakTerlaporPidanaPelapor = new ArrayList<>();
		
		listPihakTurutTergugatPerdataTergugat = new ArrayList<>();
		listPihakTurutTergugatPerdataPenggugat = new ArrayList<>();
		listPihakKuratorPailitPkpu = new ArrayList<>();
		
		listProgressPerkaraPerdataTergugat = new ArrayList<>();
		listProgressPerkaraPerdataPenggugat = new ArrayList<>();
		listProgressPerkaraPailitPkpu = new ArrayList<>();
		listProgressPerkaraPidanaTerlapor = new ArrayList<>();
		listProgressPerkaraPidanaPelapor = new ArrayList<>();
		
		
		litigationNew = litigationNewService.findById(idLong);
//		litigationNew.setPic(new User());
		
		if(litigationNew.getListPihakPenggugatPemohon() != null) {
			lastSequencePihakPenggugatPemohon = litigationNew.getListPihakPenggugatPemohon().size();
			for(LitigationPihakPenggugatPemohon dtl : litigationNew.getListPihakPenggugatPemohon()) {
				lastSequencePihakPenggugatPemohon += 1;
				dtl.setSequence(lastSequencePihakPenggugatPemohon);
				dtl.setIsEditable(false);
			}
		}
		
		if(litigationNew.getListPihakTergugatTermohon() != null) {
			lastSequencePihakTergugatTermohon = litigationNew.getListPihakTergugatTermohon().size();
			for(LitigationPihakTergugatTermohon dtl : litigationNew.getListPihakTergugatTermohon()) {
				lastSequencePihakTergugatTermohon += 1;
				dtl.setSequence(lastSequencePihakTergugatTermohon);
				dtl.setIsEditable(false);
			}
		}
		
		if(litigationNew.getListPihakKuratorTurutTergugat() != null) {
			lastSequencePihakKuratorTurutTergugat = litigationNew.getListPihakKuratorTurutTergugat().size();
			for(LitigationPihakKuratorTurutTergugat dtl : litigationNew.getListPihakKuratorTurutTergugat()) {
				lastSequencePihakKuratorTurutTergugat += 1;
				dtl.setSequence(lastSequencePihakKuratorTurutTergugat);
				dtl.setIsEditable(false);
			}
		}
		
		if(litigationNew.getListProgresPerkara() != null) {
			lastSequenceLitigationProgressPerkara = litigationNew.getListProgresPerkara().size();
			for(LitigationProgressPerkara dtl : litigationNew.getListProgresPerkara()) {
				lastSequenceLitigationProgressPerkara += 1;
				dtl.setSequence(lastSequenceLitigationProgressPerkara);
				dtl.setIsEditable(false);
			}
		}
		
		if(litigationNew.getListPutusanPengadilan() != null) {
			lastSequencePutusanPengadilan = litigationNew.getListPutusanPengadilan().size();
			for(LitigationPutusanPengadilan dtl : litigationNew.getListPutusanPengadilan()) {
				lastSequencePutusanPengadilan += 1;
				dtl.setSequence(lastSequencePutusanPengadilan);
				dtl.setIsEditable(false);
				if(dtl.getJudgementWarning() != null) dtl.setSelectJudgementWarning(dtl.getJudgementWarning().getParameterDtlCode());
			}
		}
		
		litigationNew.setPrevDescription(litigationNew.getDescription());

		
		if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
			
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU);
			litigationNew.setPicDescPailitPkpu(litigationNew.getPicDescription());
			if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPailitPkpu(litigationNew.getCaseTeamHandler().getParameterDtlCode());
			
			listPihakPemohonPailitPkpu = litigationNew.getListPihakPenggugatPemohon();
			listPihakTermohonPailitPkpu = litigationNew.getListPihakTergugatTermohon();
			listPihakKuratorPailitPkpu = litigationNew.getListPihakKuratorTurutTergugat();
			listProgressPerkaraPailitPkpu = litigationNew.getListProgresPerkara();
			
			tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPemohonPailitPkpu);
			tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTermohonPailitPkpu);
			tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakKuratorPailitPkpu);
			tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPailitPkpu);
			
			if(litigationNew.getLitigationPailitPkpu().getCaseHandler() != null) litigationNew.getLitigationPailitPkpu().setSelectCaseHandler(litigationNew.getLitigationPailitPkpu().getCaseHandler().getParameterDtlCode());
			if(litigationNew.getLitigationPailitPkpu().getJudgementWarning() != null) litigationNew.getLitigationPailitPkpu().setSelectJudgementWarning(litigationNew.getLitigationPailitPkpu().getJudgementWarning().getParameterDtlCode());
			if(litigationNew.getLitigationPailitPkpu().getCommercialCourt() != null) litigationNew.getLitigationPailitPkpu().setSelectCommercialCourt(litigationNew.getLitigationPailitPkpu().getCommercialCourt().getParameterDtlCode());
			
			//helper edit litigasi
			litigationNew.setNoPerkaraOld(litigationNew.getLitigationPailitPkpu().getCaseNumber());
		
		}else if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA)) {
			
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA);
			
			if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
				//LITIGATION PERDATA PENGGUGAT
				
				litigationNew.setSelectCaseTypeDtlPerdata(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT);
				litigationNew.setPicDescPerdataPeng(litigationNew.getPicDescription());
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPerdataPeng(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				
				
				listPihakPenggugatPerdataPenggugat = litigationNew.getListPihakPenggugatPemohon();
				listPihakTergugatPerdataPenggugat = litigationNew.getListPihakTergugatTermohon();
				listPihakTurutTergugatPerdataPenggugat = litigationNew.getListPihakKuratorTurutTergugat();
				listProgressPerkaraPerdataPenggugat = litigationNew.getListProgresPerkara();
				
				tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPenggugatPerdataPenggugat);
				tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTergugatPerdataPenggugat);
				tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakTurutTergugatPerdataPenggugat);
				tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPerdataPenggugat);
				
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseHandler() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCaseHandler(litigationNew.getLitigationPerdataPenggugat().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCaseMainTopic() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCaseMainTopic(litigationNew.getLitigationPerdataPenggugat().getCaseMainTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCaseSubTopic() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCaseSubTopic(litigationNew.getLitigationPerdataPenggugat().getCaseSubTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCourtType() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCourtType(litigationNew.getLitigationPerdataPenggugat().getCourtType().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getForeignCurrencyType() != null)litigationNew.getLitigationPerdataPenggugat().setSelectForeignCurrency(litigationNew.getLitigationPerdataPenggugat().getForeignCurrencyType().getParameterDtlCode());
				
				//helper edit litigasi
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPerdataPenggugat().getCaseNumber());
				
				//Inspect Level Flag setups
				if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingBani() != null)
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingBani().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(false);
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPn() != null)
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(false);
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPt() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPt().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaKasasi() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaPk() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaPk().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(false);
				}
				
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedBani() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedBani().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPn() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(false);
				}
					
				if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPt() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPt().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(false);
				}
					
				if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaKasasi() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(false);
				}
					
				if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaPk() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaPk().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(true);
					}else {
						litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(false);
				}
					
				//Inspect Level Flag setups - END
					
				//LITIGATION PERDATA PENGGUGAT - END
			}else if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
				//LITIGATION PERDATA TERGUGAT
				
				litigationNew.setSelectCaseTypeDtlPerdata(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT);
				litigationNew.setPicDescPerdataTer(litigationNew.getPicDescription());
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPerdataTer(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				
				listPihakPenggugatPerdataTergugat = litigationNew.getListPihakPenggugatPemohon();
				listPihakTergugatPerdataTergugat = litigationNew.getListPihakTergugatTermohon();
				listPihakTurutTergugatPerdataTergugat = litigationNew.getListPihakKuratorTurutTergugat();
				listProgressPerkaraPerdataTergugat = litigationNew.getListProgresPerkara();
				
				tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPenggugatPerdataTergugat);
				tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTergugatPerdataTergugat);
				tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<>(listPihakTurutTergugatPerdataTergugat);
				tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPerdataTergugat);
				
				
				if(litigationNew.getLitigationPerdataTergugat().getCaseHandler() != null)litigationNew.getLitigationPerdataTergugat().setSelectCaseHandler(litigationNew.getLitigationPerdataTergugat().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getDecisionStatus() != null)litigationNew.getLitigationPerdataTergugat().setSelectDecisionStatus(litigationNew.getLitigationPerdataTergugat().getDecisionStatus().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCaseMainTopic() != null)litigationNew.getLitigationPerdataTergugat().setSelectCaseMainTopic(litigationNew.getLitigationPerdataTergugat().getCaseMainTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCaseSubTopic() != null)litigationNew.getLitigationPerdataTergugat().setSelectCaseSubTopic(litigationNew.getLitigationPerdataTergugat().getCaseSubTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCourtType() != null)litigationNew.getLitigationPerdataTergugat().setSelectCourtType(litigationNew.getLitigationPerdataTergugat().getCourtType().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getForeignCurrencyType() != null)litigationNew.getLitigationPerdataTergugat().setSelectForeignCurrency(litigationNew.getLitigationPerdataTergugat().getForeignCurrencyType().getParameterDtlCode());
				
				//helper edit litigasi
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPerdataTergugat().getCaseNumber());
				
				//Inspect Level Flag setups
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingBani() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingBani().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaKasasi() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaPk() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaPk().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPa() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPa().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPn() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPt() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPt().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtAgama() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtAgama().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtun() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtun().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedBani() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedBani().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaKasasi() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaPk() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaPk().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPa() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPa().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPaFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPaFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPaFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPn() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPt() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPt().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtAgama() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtAgama().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(false);
				}
				
				if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtun() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtun().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(true);
					}else {
						litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(false);
					}
				}else {
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(false);
				}
				//Inspect Level Flag setups - END
	
				//LITIGATION PERDATA TERGUGAT - END
			}
			
		}else if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)) {
			
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA);
			
			if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
				//LITIGATION PIDANA PELAPOR 
				
				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR);
				litigationNew.setPicDescPidanaPel(litigationNew.getPicDescription());
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPidanaPel(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				
				listPihakPelaporPidanaPelapor = litigationNew.getListPihakPenggugatPemohon();
				listPihakTerlaporPidanaPelapor = litigationNew.getListPihakTergugatTermohon();
				listProgressPerkaraPidanaPelapor = litigationNew.getListProgresPerkara();
				
				tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPelaporPidanaPelapor);
				tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTerlaporPidanaPelapor);
				tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPidanaPelapor);
				
				if(litigationNew.getLitigationPidanaPelapor().getCaseHandler() != null)litigationNew.getLitigationPidanaPelapor().setSelectCaseHandler(litigationNew.getLitigationPidanaPelapor().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPidanaPelapor().getForeignCurrencyType() != null)litigationNew.getLitigationPidanaPelapor().setSelectForeignCurrency(litigationNew.getLitigationPidanaPelapor().getForeignCurrencyType().getParameterDtlCode());
				
				//helper edit litigasi
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPidanaPelapor().getCaseNumber());
				
				if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPn() != null) {
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(true);
					}else {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(false);
				}
					
				if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPol() != null) {
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPol().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(true);
					}else {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(false);
				}
				
				if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelJaksa() != null) {
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelJaksa().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(true);
					}else {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(false);
				}
				
				if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelFinished() != null) {
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelFinished().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(true);
					}else {
						litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(false);
				}
				
				
				//LITIGATION PIDANA PELAPOR - END
			}else if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
				//LITIGATION PIDANA TERLAPOR
				
				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR);
				litigationNew.setPicDescPidanaTer(litigationNew.getPicDescription());
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPidanaTer(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				
				listPihakPelaporPidanaTerlapor = litigationNew.getListPihakPenggugatPemohon();
				listPihakTerlaporPidanaTerlapor = litigationNew.getListPihakTergugatTermohon();
				listProgressPerkaraPidanaTerlapor = litigationNew.getListProgresPerkara();
				
				tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<>(listPihakPelaporPidanaTerlapor);
				tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<>(listPihakTerlaporPidanaTerlapor);
				tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<>(listProgressPerkaraPidanaTerlapor);
				
				if(litigationNew.getLitigationPidanaTerlapor().getCaseHandler() != null)litigationNew.getLitigationPidanaTerlapor().setSelectCaseHandler(litigationNew.getLitigationPidanaTerlapor().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPidanaTerlapor().getForeignCurrencyType() != null)litigationNew.getLitigationPidanaTerlapor().setSelectForeignCurrency(litigationNew.getLitigationPidanaTerlapor().getForeignCurrencyType().getParameterDtlCode());
				
				//helper edit litigasi
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPidanaTerlapor().getCaseNumber());
				
				if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPn() != null) {
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPn().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(true);
					}else {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(false);
				}
				
				if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPol() != null) {
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPol().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(true);
					}else {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(false);
				}
				
				if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelJaksa() != null) {
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelJaksa().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(true);
					}else {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(false);
				}
				
				if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelFinished() != null) {
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelFinished().equals(CommonConstants.RECORD_FLAG_YES)) {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(true);
					}else {
						litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(false);
					}
				}else {
					litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(false);
				}
					
			}
				//LITIGATION PIDANA TERLAPOR - END
		}
				
	}
		
	public Boolean validate() {
		Boolean flag = false;
		
		if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) 
				&& StringUtils.isBlank(litigationNew.getSelectCaseTypeDtlPerdata())){			
			facesUtil.addErrMessage("Maybank Selaku " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;			
		}else if (litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)
							&& StringUtils.isBlank(litigationNew.getSelectCaseTypeDtl())){
			facesUtil.addErrMessage("Maybank Selaku " + facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if(actionMode.equals("EDIT")) {
			if((litigationNew.getDescription() == null || litigationNew.getDescription().trim().isEmpty()) ){
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formLitigationDataChangeHistory") 
						+ facesUtil.retrieveMessage("validateRequired"));
				flag = true;
			}
			
			if(litigationNew.getPrevDescription()!=null) {
				if(litigationNew.getPrevDescription().trim().equalsIgnoreCase(litigationNew.getDescription().trim()) ) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formLitigationDataChangeHistory") + " baru harus berbeda dengan sebelumnya");
					flag = true;
				}
			}
		}
		
		return flag;
	}
	
	/* 
	 * on Add PIC / on Delete PIC ajaxs for new Litigation - START
	 */
	
	//UNUSED
	public void onAddNewLitigationPic() {
		if(litigationNew.getLitigationPics() == null 
				|| litigationNew.getLitigationPics().size() == 0) {
			litigationNew.setLitigationPics(new ArrayList<>());
			
			lastSequenceLitigationPic = 0;
		}else {
			if(litigationNew.getLitigationPics().size() == 0) {
				lastSequenceLitigationPic = 0;
			}
		}
		
		LitigationPic rt = new LitigationPic();
		lastSequenceLitigationPic += 1;
		rt.setSequence(lastSequenceLitigationPic);
		rt.setIsEditable(true);
		litigationNew.getLitigationPics().add(rt);
		
		tableModelLitigationPic.setWrappedData(litigationNew.getLitigationPics());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowLitigationPic() {
		for(LitigationPic obj : selectedLitigationPic) {
			litigationNew.getLitigationPics().remove(obj);
		}
		
		if(litigationNew.getLitigationPics() == null 
				|| litigationNew.getLitigationPics().size() == 0) {
			lastSequenceLitigationPic = 0;
		}
		
		tableModelLitigationPic.setWrappedData(litigationNew.getLitigationPics());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	//UNUSED - END
	
	// PERDATA TERGUGAT TABLE DTL AJAX
	
	public void onAddNewPihakPenggugatPerdataTer() {
		if(listPihakPenggugatPerdataTergugat == null 
				|| listPihakPenggugatPerdataTergugat.size() == 0) {
			listPihakPenggugatPerdataTergugat = new ArrayList<>();
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(listPihakPenggugatPerdataTergugat.size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		listPihakPenggugatPerdataTergugat.add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPenggugatPerdataTergugat);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPenggugatPerdataTer() {
//		for (int i = 0; i < selectedLitigationPihakPenggugatPemohon.length; i++) {
//			litigationNew.getListPihakPenggugatPemohon().remove(selectedLitigationPihakPenggugatPemohon[i]);
//		}
		for(LitigationPihakPenggugatPemohon obj : selectedLitigationPihakPenggugatPemohon) {
			deletedListPihakPenggugatPemohon.add(obj);
			listPihakPenggugatPerdataTergugat.remove(obj);
		}
		
		if(listPihakPenggugatPerdataTergugat == null 
				|| listPihakPenggugatPerdataTergugat.size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPenggugatPerdataTergugat);
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(litigationNew.getListPihakPenggugatPemohon());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTergugatPerdataTer() {
		if(listPihakTergugatPerdataTergugat == null 
				|| listPihakTergugatPerdataTergugat.size() == 0) {
			listPihakTergugatPerdataTergugat = new ArrayList<>();
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(listPihakTergugatPerdataTergugat.size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		listPihakTergugatPerdataTergugat.add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTergugatPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTergugatPerdataTer() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			listPihakTergugatPerdataTergugat.remove(obj);
		}
		
		if(listPihakTergugatPerdataTergugat == null 
				|| listPihakTergugatPerdataTergugat.size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTergugatPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTurutTergugatPerdataTer() {
		if(listPihakTurutTergugatPerdataTergugat == null 
				|| listPihakTurutTergugatPerdataTergugat.size() == 0) {
			listPihakTurutTergugatPerdataTergugat = new ArrayList<>();
			
			lastSequencePihakKuratorTurutTergugat = 0;
		}else {
			if(listPihakTurutTergugatPerdataTergugat.size() == 0) {
				lastSequencePihakKuratorTurutTergugat = 0;
			}
		}
		
		LitigationPihakKuratorTurutTergugat rt = new LitigationPihakKuratorTurutTergugat();
		lastSequencePihakKuratorTurutTergugat += 1;
		rt.setSequence(lastSequencePihakKuratorTurutTergugat);
		rt.setIsEditable(true);
		listPihakTurutTergugatPerdataTergugat.add(rt);
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakTurutTergugatPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTurutTergugatPerdataTer() {
		for(LitigationPihakKuratorTurutTergugat obj : selectedLitigationPihakKuratorTurutTergugat) {
			deletedListPihakKuratorTurutTergugat.add(obj);
			listPihakTurutTergugatPerdataTergugat.remove(obj);
		}
		
		if(listPihakTurutTergugatPerdataTergugat == null 
				|| listPihakTurutTergugatPerdataTergugat.size() == 0) {
			lastSequencePihakKuratorTurutTergugat = 0;
		}
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakTurutTergugatPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewProgressPerkaraPerdataTer() {
		if(listProgressPerkaraPerdataTergugat == null 
				|| listProgressPerkaraPerdataTergugat.size() == 0) {
			listProgressPerkaraPerdataTergugat = new ArrayList<>();
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(listProgressPerkaraPerdataTergugat.size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		listProgressPerkaraPerdataTergugat.add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkaraPerdataTer() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			listProgressPerkaraPerdataTergugat.remove(obj);
		}
		
		if(listProgressPerkaraPerdataTergugat == null 
				|| listProgressPerkaraPerdataTergugat.size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPerdataTergugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	// PERDATA TERGUGAT TABLE DTL AJAX - END
	
	// PERDATA PENGGUGAT TABLE DTL AJAX 
	
	public void onAddNewPihakPenggugatPerdataPeng() {
		if(listPihakPenggugatPerdataPenggugat == null 
				|| listPihakPenggugatPerdataPenggugat.size() == 0) {
			listPihakPenggugatPerdataPenggugat = new ArrayList<>();
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(listPihakPenggugatPerdataPenggugat.size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		listPihakPenggugatPerdataPenggugat.add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPenggugatPerdataPenggugat);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPenggugatPerdataPeng() {
		for(LitigationPihakPenggugatPemohon obj : selectedLitigationPihakPenggugatPemohon) {
			deletedListPihakPenggugatPemohon.add(obj);
			listPihakPenggugatPerdataPenggugat.remove(obj);
		}
		
		if(listPihakPenggugatPerdataPenggugat == null 
				|| listPihakPenggugatPerdataPenggugat.size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPenggugatPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTergugatPerdataPeng() {
		if(listPihakTergugatPerdataPenggugat == null 
				|| listPihakTergugatPerdataPenggugat.size() == 0) {
			listPihakTergugatPerdataPenggugat = new ArrayList<>();
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(listPihakTergugatPerdataPenggugat.size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		listPihakTergugatPerdataPenggugat.add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTergugatPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTergugatPerdataPeng() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			listPihakTergugatPerdataPenggugat.remove(obj);
		}
		
		if(listPihakTergugatPerdataPenggugat == null 
				|| listPihakTergugatPerdataPenggugat.size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTergugatPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTurutTergugatPerdataPeng() {
		if(listPihakTurutTergugatPerdataPenggugat == null 
				|| listPihakTurutTergugatPerdataPenggugat.size() == 0) {
			listPihakTurutTergugatPerdataPenggugat = new ArrayList<>();
			
			lastSequencePihakKuratorTurutTergugat = 0;
		}else {
			if(listPihakTurutTergugatPerdataPenggugat.size() == 0) {
				lastSequencePihakKuratorTurutTergugat = 0;
			}
		}
		
		LitigationPihakKuratorTurutTergugat rt = new LitigationPihakKuratorTurutTergugat();
		lastSequencePihakKuratorTurutTergugat += 1;
		rt.setSequence(lastSequencePihakKuratorTurutTergugat);
		rt.setIsEditable(true);
		listPihakTurutTergugatPerdataPenggugat.add(rt);
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakTurutTergugatPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTurutTergugatPerdataPeng() {
		for(LitigationPihakKuratorTurutTergugat obj : selectedLitigationPihakKuratorTurutTergugat) {
			deletedListPihakKuratorTurutTergugat.add(obj);
			listPihakTurutTergugatPerdataPenggugat.remove(obj);
		}
		
		if(listPihakTurutTergugatPerdataPenggugat == null 
				|| listPihakTurutTergugatPerdataPenggugat.size() == 0) {
			lastSequencePihakKuratorTurutTergugat = 0;
		}
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakTurutTergugatPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewProgressPerkaraPerdataPeng() {
		if(listProgressPerkaraPerdataPenggugat == null 
				|| listProgressPerkaraPerdataPenggugat.size() == 0) {
			listProgressPerkaraPerdataPenggugat = new ArrayList<>();
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(listProgressPerkaraPerdataPenggugat.size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		listProgressPerkaraPerdataPenggugat.add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkaraPerdataPeng() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			listProgressPerkaraPerdataPenggugat.remove(obj);
		}
		
		if(listProgressPerkaraPerdataPenggugat == null 
				|| listProgressPerkaraPerdataPenggugat.size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPerdataPenggugat);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	// PERDATA PENGGUGAT TABLE DTL AJAX - END
	
	// PIDANA TERLAPOR TABLE DTL AJAX
	public void onAddNewPihakPelaporPidanaTer() {
		if(listPihakPelaporPidanaTerlapor == null 
				|| listPihakPelaporPidanaTerlapor.size() == 0) {
			listPihakPelaporPidanaTerlapor = new ArrayList<>();
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(listPihakPelaporPidanaTerlapor.size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		listPihakPelaporPidanaTerlapor.add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPelaporPidanaTerlapor);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPelaporPidanaTer() {
		for(LitigationPihakPenggugatPemohon obj : selectedLitigationPihakPenggugatPemohon) {
			deletedListPihakPenggugatPemohon.add(obj);
			listPihakPelaporPidanaTerlapor.remove(obj);
		}
		
		if(listPihakPelaporPidanaTerlapor == null 
				|| listPihakPelaporPidanaTerlapor.size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPelaporPidanaTerlapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTerlaporPidanaTer() {
		if(listPihakTerlaporPidanaTerlapor == null 
				|| listPihakTerlaporPidanaTerlapor.size() == 0) {
			listPihakTerlaporPidanaTerlapor = new ArrayList<>();
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(listPihakTerlaporPidanaTerlapor.size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		listPihakTerlaporPidanaTerlapor.add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTerlaporPidanaTerlapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTerlaporPidanaTer() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			listPihakTerlaporPidanaTerlapor.remove(obj);
		}
		
		if(listPihakTerlaporPidanaTerlapor == null 
				|| listPihakTerlaporPidanaTerlapor.size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTerlaporPidanaTerlapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewProgressPerkaraPidanaTer() {
		if(listProgressPerkaraPidanaTerlapor == null 
				|| listProgressPerkaraPidanaTerlapor.size() == 0) {
			listProgressPerkaraPidanaTerlapor = new ArrayList<>();
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(listProgressPerkaraPidanaTerlapor.size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		listProgressPerkaraPidanaTerlapor.add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPidanaTerlapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkaraPidanaTer() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			listProgressPerkaraPidanaTerlapor.remove(obj);
		}
		
		if(listProgressPerkaraPidanaTerlapor == null 
				|| listProgressPerkaraPidanaTerlapor.size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPidanaTerlapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	// PIDANA TERLAPOR TABLE DTL AJAX - END
	
	// PIDANA PELAPOR TABLE DTL AJAX
	
	public void onAddNewPihakPelaporPidanaPel() {
		if(listPihakPelaporPidanaPelapor == null 
				|| listPihakPelaporPidanaPelapor.size() == 0) {
			listPihakPelaporPidanaPelapor = new ArrayList<>();
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(listPihakPelaporPidanaPelapor.size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		listPihakPelaporPidanaPelapor.add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPelaporPidanaPelapor);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPelaporPidanaPel() {
		for(LitigationPihakPenggugatPemohon obj : selectedLitigationPihakPenggugatPemohon) {
			deletedListPihakPenggugatPemohon.add(obj);
			listPihakPelaporPidanaPelapor.remove(obj);
		}
		
		if(listPihakPelaporPidanaPelapor == null 
				|| listPihakPelaporPidanaPelapor.size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPelaporPidanaPelapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTerlaporPidanaPel() {
		if(listPihakTerlaporPidanaPelapor == null 
				|| listPihakTerlaporPidanaPelapor.size() == 0) {
			listPihakTerlaporPidanaPelapor = new ArrayList<>();
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(listPihakTerlaporPidanaPelapor.size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		listPihakTerlaporPidanaPelapor.add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTerlaporPidanaPelapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTerlaporPidanaPel() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			listPihakTerlaporPidanaPelapor.remove(obj);
		}
		
		if(listPihakTerlaporPidanaPelapor == null 
				|| listPihakTerlaporPidanaPelapor.size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTerlaporPidanaPelapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewProgressPerkaraPidanaPel() {
		if(listProgressPerkaraPidanaPelapor == null 
				|| listProgressPerkaraPidanaPelapor.size() == 0) {
			listProgressPerkaraPidanaPelapor = new ArrayList<>();
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(listProgressPerkaraPidanaPelapor.size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		listProgressPerkaraPidanaPelapor.add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPidanaPelapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkaraPidanaPel() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			listProgressPerkaraPidanaPelapor.remove(obj);
		}
		
		if(listProgressPerkaraPidanaPelapor == null 
				|| listProgressPerkaraPidanaPelapor.size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPidanaPelapor);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	// PIDANA PELAPOR TABLE DTL AJAX - END
	
	// PAILIT PKPU TABLE DTL AJAX 
	
	public void onAddNewPihakPemohonPailitPkpu() {
		if(listPihakPemohonPailitPkpu == null 
				|| listPihakPemohonPailitPkpu.size() == 0) {
			listPihakPemohonPailitPkpu = new ArrayList<>();
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(listPihakPemohonPailitPkpu.size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		listPihakPemohonPailitPkpu.add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPemohonPailitPkpu);
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPemohonPailitPkpu() {
		for(LitigationPihakPenggugatPemohon obj : selectedLitigationPihakPenggugatPemohon) {
			deletedListPihakPenggugatPemohon.add(obj);
			listPihakPemohonPailitPkpu.remove(obj);
		}
		
		if(listPihakPemohonPailitPkpu == null 
				|| listPihakPemohonPailitPkpu.size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(listPihakPemohonPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTermohonPailitPkpu() {
		if(listPihakTermohonPailitPkpu == null 
				|| listPihakTermohonPailitPkpu.size() == 0) {
			listPihakTermohonPailitPkpu = new ArrayList<>();
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(listPihakTermohonPailitPkpu.size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		listPihakTermohonPailitPkpu.add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTermohonPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTermohonPailitPkpu() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			listPihakTermohonPailitPkpu.remove(obj);
		}
		
		if(listPihakTermohonPailitPkpu == null 
				|| listPihakTermohonPailitPkpu.size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(listPihakTermohonPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewProgressPerkaraPailitPkpu() {
		if(listProgressPerkaraPailitPkpu == null 
				|| listProgressPerkaraPailitPkpu.size() == 0) {
			listProgressPerkaraPailitPkpu = new ArrayList<>();
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(listProgressPerkaraPailitPkpu.size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		listProgressPerkaraPailitPkpu.add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkaraPailitPkpu() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			listProgressPerkaraPailitPkpu.remove(obj);
		}
		
		if(listProgressPerkaraPailitPkpu == null 
				|| listProgressPerkaraPailitPkpu.size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(listProgressPerkaraPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPutusanPengadilan() {
		if(litigationNew.getListPutusanPengadilan() == null 
				|| litigationNew.getListPutusanPengadilan().size() == 0) {
			litigationNew.setListPutusanPengadilan(new ArrayList<>());
			
			lastSequencePutusanPengadilan = 0;
		}else {
			if(litigationNew.getListPutusanPengadilan().size() == 0) {
				lastSequencePutusanPengadilan = 0;
			}
		}
		
		LitigationPutusanPengadilan rt = new LitigationPutusanPengadilan();
		lastSequencePutusanPengadilan += 1;
		rt.setSequence(lastSequencePutusanPengadilan);
		rt.setIsEditable(true);
		litigationNew.getListPutusanPengadilan().add(rt);
		
		tableModelLitigationPutusanPengadilan.setWrappedData(litigationNew.getListPutusanPengadilan());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPutusanPengadilan() {
		for(LitigationPutusanPengadilan obj : selectedLitigationPutusanPengadilan) {
			deletedListPutusanPengadilan.add(obj);
			litigationNew.getListPutusanPengadilan().remove(obj);
		}
		
		if(litigationNew.getListPutusanPengadilan() == null 
				|| litigationNew.getListPutusanPengadilan().size() == 0) {
			lastSequencePutusanPengadilan = 0;
		}
		
		tableModelLitigationPutusanPengadilan.setWrappedData(litigationNew.getListPutusanPengadilan());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakKuratorPailitPkpu() {
		if(listPihakKuratorPailitPkpu == null 
				|| listPihakKuratorPailitPkpu.size() == 0) {
			litigationNew.setListPihakKuratorTurutTergugat(new ArrayList<>());
			
			lastSequencePihakKuratorTurutTergugat = 0;
		}else {
			if(listPihakKuratorPailitPkpu.size() == 0) {
				lastSequencePihakKuratorTurutTergugat = 0;
			}
		}
		
		LitigationPihakKuratorTurutTergugat rt = new LitigationPihakKuratorTurutTergugat();
		lastSequencePihakKuratorTurutTergugat += 1;
		rt.setSequence(lastSequencePihakKuratorTurutTergugat);
		rt.setIsEditable(true);
		listPihakKuratorPailitPkpu.add(rt);
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakKuratorPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakKuratorPailitPkpu() {
		for(LitigationPihakKuratorTurutTergugat obj : selectedLitigationPihakKuratorTurutTergugat) {
			deletedListPihakKuratorTurutTergugat.add(obj);
			listPihakKuratorPailitPkpu.remove(obj);
		}
		
		if(listPihakKuratorPailitPkpu == null 
				|| listPihakKuratorPailitPkpu.size() == 0) {
			lastSequencePihakKuratorTurutTergugat = 0;
		}
		
		tableModelLitigationPihakKuratorTurutTergugat.setWrappedData(listPihakKuratorPailitPkpu);
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	// PAILIT PKPU TABLE DTL AJAX - END
	
	//BASE CODE - UNUSED
	
	public void onAddNewProgressPerkara() {
		if(litigationNew.getListProgresPerkara() == null 
				|| litigationNew.getListProgresPerkara().size() == 0) {
			litigationNew.setListProgresPerkara(new ArrayList<>());
			
			lastSequenceLitigationProgressPerkara = 0;
		}else {
			if(litigationNew.getListProgresPerkara().size() == 0) {
				lastSequenceLitigationProgressPerkara = 0;
			}
		}
		
		LitigationProgressPerkara rt = new LitigationProgressPerkara();
		lastSequenceLitigationProgressPerkara += 1;
		rt.setSequence(lastSequenceLitigationProgressPerkara);
		rt.setIsEditable(true);
		litigationNew.getListProgresPerkara().add(rt);
		
		tableModelLitigationProgressPerkara.setWrappedData(litigationNew.getListProgresPerkara());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowProgressPerkara() {
		for(LitigationProgressPerkara obj : selectedLitigationProgressPerkara) {
			deletedListProgressPerkara.add(obj);
			litigationNew.getListProgresPerkara().remove(obj);
		}
		
		if(litigationNew.getListProgresPerkara() == null 
				|| litigationNew.getListProgresPerkara().size() == 0) {
			lastSequenceLitigationProgressPerkara = 0;
		}
		
		tableModelLitigationProgressPerkara.setWrappedData(litigationNew.getListProgresPerkara());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakPenggugatPemohon() {
		if(litigationNew.getListPihakPenggugatPemohon() == null 
				|| litigationNew.getListPihakPenggugatPemohon().size() == 0) {
			litigationNew.setListPihakPenggugatPemohon(new ArrayList<>());
			
			lastSequencePihakPenggugatPemohon = 0;
		}else {
			if(litigationNew.getListPihakPenggugatPemohon().size() == 0) {
				lastSequencePihakPenggugatPemohon = 0;
			}
		}
		
		LitigationPihakPenggugatPemohon rt = new LitigationPihakPenggugatPemohon();
		lastSequencePihakPenggugatPemohon += 1;
		rt.setSequence(lastSequencePihakPenggugatPemohon);
		rt.setIsEditable(true);
		litigationNew.getListPihakPenggugatPemohon().add(rt);
		
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(litigationNew.getListPihakPenggugatPemohon());
		
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakPenggugatPemohon() {
		for(int i = 0; i <selectedLitigationPihakPenggugatPemohon.length; i++) {
			deletedListPihakPenggugatPemohon.add(selectedLitigationPihakPenggugatPemohon[i]);
			litigationNew.getListPihakPenggugatPemohon().remove(selectedLitigationPihakPenggugatPemohon[i]);
		}
		
		if(litigationNew.getListPihakPenggugatPemohon() == null 
				|| litigationNew.getListPihakPenggugatPemohon().size() == 0) {
			lastSequencePihakPenggugatPemohon = 0;
		}
																			   
		tableModelLitigationPihakPenggugatPemohon.setWrappedData(litigationNew.getListPihakPenggugatPemohon());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onAddNewPihakTergugatTermohon() {
		if(litigationNew.getListPihakTergugatTermohon() == null 
				|| litigationNew.getListPihakTergugatTermohon().size() == 0) {
			litigationNew.setListPihakTergugatTermohon(new ArrayList<>());
			
			lastSequencePihakTergugatTermohon = 0;
		}else {
			if(litigationNew.getListPihakTergugatTermohon().size() == 0) {
				lastSequencePihakTergugatTermohon = 0;
			}
		}
		
		LitigationPihakTergugatTermohon rt = new LitigationPihakTergugatTermohon();
		lastSequencePihakTergugatTermohon += 1;
		rt.setSequence(lastSequencePihakTergugatTermohon);
		rt.setIsEditable(true);
		litigationNew.getListPihakTergugatTermohon().add(rt);
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(litigationNew.getListPihakTergugatTermohon());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public void onDeleteRowPihakTergugatTermohon() {
		for(LitigationPihakTergugatTermohon obj : selectedLitigationPihakTergugatTermohon) {
			deletedListPihakTergugatTermohon.add(obj);
			litigationNew.getListPihakTergugatTermohon().remove(obj);
		}
		
		if(litigationNew.getListPihakTergugatTermohon() == null 
				|| litigationNew.getListPihakTergugatTermohon().size() == 0) {
			lastSequencePihakTergugatTermohon = 0;
		}
		
		tableModelLitigationPihakTergugatTermohon.setWrappedData(litigationNew.getListPihakTergugatTermohon());
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	//BASE CODE - UNUSED
	
	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("picDialog", widgetVar) ||
				StringUtils.equals("picDialog2", widgetVar) ||
				StringUtils.equals("picDialog3", widgetVar) ||
				StringUtils.equals("picDialog4", widgetVar) ||
				StringUtils.equals("picDialog5", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			User userPic = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (userPic != null) {
				litigationNew.setPic(userPic);
				litigationNew.setSelectedPicName(userPic.getName());
			}else {
				litigationNew.setSelectedPicName("");
			}
		}
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	/* 
	 * on Add PIC / on Delete PIC ajaxs for new Litigation - END
	 */
	
	
	
	public void calculateTotalClaimValuePerdatTergugat() {
		Double materialIdr = litigationNew.getLitigationPerdataTergugat().getClaimsValueMaterialIdr() != null ? 
				litigationNew.getLitigationPerdataTergugat().getClaimsValueMaterialIdr() : Double.valueOf(0);
//		Double materialValas = litigationNew.getLitigationPerdataTergugat().getClaimsValueMaterialValas() != null ? 
//				litigationNew.getLitigationPerdataTergugat().getClaimsValueMaterialValas() : Double.valueOf(0);
		Double immaterialIdr = litigationNew.getLitigationPerdataTergugat().getClaimsValueImmaterialIdr() != null ? 
				litigationNew.getLitigationPerdataTergugat().getClaimsValueImmaterialIdr() : Double.valueOf(0);
		
		Double totalValue = materialIdr + immaterialIdr;
		litigationNew.getLitigationPerdataTergugat().setTotalClaimsValueIdr(totalValue);
		
	}
	
	
	public void calculateTotalClaimValuePerdatPenggugat() {
		Double materialIdr = litigationNew.getLitigationPerdataPenggugat().getClaimsValueMaterialIdr() != null ?
				litigationNew.getLitigationPerdataPenggugat().getClaimsValueMaterialIdr() : Double.valueOf(0);
//		Long materialValas = litigationNew.getLitigationPerdataPenggugat().getClaimsValueMaterialValas() != null ?
//				litigationNew.getLitigationPerdataPenggugat().getClaimsValueMaterialValas() : Long.valueOf(0);
		Double immaterialIdr = litigationNew.getLitigationPerdataPenggugat().getClaimsValueImmaterialIdr() != null ? 
				litigationNew.getLitigationPerdataPenggugat().getClaimsValueImmaterialIdr() : Double.valueOf(0);
		
		Double totalValue = materialIdr + immaterialIdr;
		litigationNew.getLitigationPerdataPenggugat().setTotalClaimsValueIdr(totalValue);
	}
	
	private String replace(String kalimat,String users,String jenisPerkara,String noPerkara) {
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		if (StringUtils.isNotBlank(jenisPerkara)) {
			kalimat = kalimat.replace("{jenis_perkara}", jenisPerkara);
		}
		if (StringUtils.isNotBlank(noPerkara)) {
			kalimat = kalimat.replace("{no_perkara}", noPerkara);
		}
		return kalimat;
	}
	
	private String replaceEdit(String kalimat, String jenisPerkaraOld, String noPerkaraOld, String jenisPerkaraNew, String noUrutNew, String noPerkaraNew, String users) {
		if (StringUtils.isNotBlank(jenisPerkaraOld)) {
			kalimat = kalimat.replace("{jenis_perkara_old}", jenisPerkaraOld);
		}
		if (StringUtils.isNotBlank(noPerkaraOld)) {
			kalimat = kalimat.replace("{no_perkara_old}", noPerkaraOld);
		}
		if (StringUtils.isNotBlank(jenisPerkaraNew)) {
			kalimat = kalimat.replace("{jenis_perkara_new}", jenisPerkaraNew);
		}
		if (StringUtils.isNotBlank(noUrutNew)) {
			kalimat = kalimat.replace("{no_urut_new}",noUrutNew);
		}
		if (StringUtils.isNotBlank(noPerkaraNew)) {
			kalimat = kalimat.replace("{no_perkara_new}",noPerkaraNew);
		}
		if (StringUtils.isNotBlank(users)) {
			kalimat = kalimat.replace("{user_name}", users);
		}
		return kalimat;
	}

	public void save() {
		try {
			if (!validate()) {
				
				if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA)) {
					
					if(litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
						
						litigationNew.setPicDescription(litigationNew.getPicDescPerdataPeng());
						litigationNew.setCaseTeamHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTeamHandlerPerdataPeng()));	
						
						litigationNew.getLitigationPerdataPenggugat().setCaseHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataPenggugat().getSelectCaseHandler()));
						
						litigationNew.getLitigationPerdataPenggugat().setCourtType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataPenggugat().getSelectCourtType()));
						
						litigationNew.getLitigationPerdataPenggugat().setForeignCurrencyType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataPenggugat().getSelectForeignCurrency()));
						
						litigationNew.getLitigationPerdataPenggugat().setCaseMainTopic(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataPenggugat().getSelectCaseMainTopic()));
						
						litigationNew.getLitigationPerdataPenggugat().setCaseSubTopic(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataPenggugat().getSelectCaseSubTopic()));
						

					}else if(litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)){
						
						litigationNew.setPicDescription(litigationNew.getPicDescPerdataTer());
						litigationNew.setCaseTeamHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTeamHandlerPerdataTer()));	
							
						litigationNew.getLitigationPerdataTergugat().setCaseHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectCaseHandler()));
						
						litigationNew.getLitigationPerdataTergugat().setDecisionStatus(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectDecisionStatus()));
						
						litigationNew.getLitigationPerdataTergugat().setCourtType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectCourtType()));
						
						litigationNew.getLitigationPerdataTergugat().setCaseMainTopic(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectCaseMainTopic()));
						
						litigationNew.getLitigationPerdataTergugat().setCaseSubTopic(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectCaseSubTopic()));
						
						litigationNew.getLitigationPerdataTergugat().setForeignCurrencyType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPerdataTergugat().getSelectForeignCurrency()));
						
					}
				} else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
					
					litigationNew.setPicDescription(litigationNew.getPicDescPailitPkpu());
					litigationNew.setCaseTeamHandler(
							parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTeamHandlerPailitPkpu()));	
					
					litigationNew.getLitigationPailitPkpu().setCaseHandler(
							parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPailitPkpu().getSelectCaseHandler()));
					
					litigationNew.getLitigationPailitPkpu().setJudgementWarning(
							parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPailitPkpu().getSelectJudgementWarning()));
					
					litigationNew.getLitigationPailitPkpu().setCommercialCourt(
							parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPailitPkpu().getSelectCommercialCourt()));
					
				} else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)) {
					
					if(litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
						
						litigationNew.setPicDescription(litigationNew.getPicDescPidanaTer());
						litigationNew.setCaseTeamHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTeamHandlerPidanaTer()));	
						
						litigationNew.getLitigationPidanaTerlapor().setForeignCurrencyType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPidanaTerlapor().getSelectForeignCurrency()));
						
						litigationNew.getLitigationPidanaTerlapor().setCaseHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPidanaTerlapor().getSelectCaseHandler()));
						
					}else if(litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
						
						litigationNew.setPicDescription(litigationNew.getPicDescPidanaPel());
						litigationNew.setCaseTeamHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTeamHandlerPidanaPel()));	
						
						litigationNew.getLitigationPidanaPelapor().setForeignCurrencyType(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPidanaPelapor().getSelectForeignCurrency()));
						
						litigationNew.getLitigationPidanaPelapor().setCaseHandler(
								parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getLitigationPidanaPelapor().getSelectCaseHandler()));
					}
					
				}
				
				if(actionMode.equalsIgnoreCase("ADD")) {
					String userNik = facesUtil.getUserLogin().getNik();
					
					litigationNew.setCaseType(parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseType()));
					EntityUtil.setCreationInfo(litigationNew, userNik);
					
					litigationNew.getLitigationPerdataPenggugat().setLitigation(litigationNew);
					litigationNew.getLitigationPerdataTergugat().setLitigation(litigationNew);
					litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setLitigationPerdata(litigationNew.getLitigationPerdataTergugat());
					litigationNew.getLitigationPailitPkpu().setLitigation(litigationNew);
					litigationNew.getLitigationPidanaPelapor().setLitigation(litigationNew);
					litigationNew.getLitigationPidanaTerlapor().setLitigation(litigationNew);
					
					EntityUtil.setCreationInfo(litigationNew.getLitigationPerdataTergugat(), userNik);
					EntityUtil.setCreationInfo(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel(), userNik);
					EntityUtil.setCreationInfo(litigationNew.getLitigationPerdataPenggugat(), userNik);
					EntityUtil.setCreationInfo(litigationNew.getLitigationPailitPkpu(), userNik);
					EntityUtil.setCreationInfo(litigationNew.getLitigationPidanaPelapor(), userNik);
					EntityUtil.setCreationInfo(litigationNew.getLitigationPidanaTerlapor(), userNik);
					
					if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
						
						litigationNew.setListPihakPenggugatPemohon(listPihakPemohonPailitPkpu);
						litigationNew.setListPihakTergugatTermohon(listPihakTermohonPailitPkpu);
						litigationNew.setListPihakKuratorTurutTergugat(listPihakKuratorPailitPkpu);
						litigationNew.setListProgresPerkara(listProgressPerkaraPailitPkpu);
						
					}else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA)) {
						
						if(litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
							
							litigationNew.setListPihakPenggugatPemohon(listPihakPenggugatPerdataTergugat);
							litigationNew.setListPihakTergugatTermohon(listPihakTergugatPerdataTergugat);
							litigationNew.setListPihakKuratorTurutTergugat(listPihakTurutTergugatPerdataTergugat);
							litigationNew.setListProgresPerkara(listProgressPerkaraPerdataTergugat);
							
						}else if(litigationNew.getSelectCaseTypeDtlPerdata().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
							
							litigationNew.setListPihakPenggugatPemohon(listPihakPenggugatPerdataPenggugat);
							litigationNew.setListPihakTergugatTermohon(listPihakTergugatPerdataPenggugat);
							litigationNew.setListPihakKuratorTurutTergugat(listPihakTurutTergugatPerdataPenggugat);
							litigationNew.setListProgresPerkara(listProgressPerkaraPerdataPenggugat);
							
						}
						
					}else if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)) {
						
						if(litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
							
							litigationNew.setListPihakPenggugatPemohon(listPihakPelaporPidanaTerlapor);
							litigationNew.setListPihakTergugatTermohon(listPihakTerlaporPidanaTerlapor);
							litigationNew.setListProgresPerkara(listProgressPerkaraPidanaTerlapor);
							
						}else if(litigationNew.getSelectCaseTypeDtl().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
							
							litigationNew.setListPihakPenggugatPemohon(listPihakPelaporPidanaPelapor);
							litigationNew.setListPihakTergugatTermohon(listPihakTerlaporPidanaPelapor);
							litigationNew.setListProgresPerkara(listProgressPerkaraPidanaPelapor);
							
						}
						
					}
					
					
					
					if(litigationNew.getListPihakPenggugatPemohon() != null && litigationNew.getListPihakPenggugatPemohon().size() > 0) {
						for(LitigationPihakPenggugatPemohon dtlPenggugat : litigationNew.getListPihakPenggugatPemohon()) {
							dtlPenggugat.setLitigation(litigationNew);
							EntityUtil.setCreationInfo(dtlPenggugat, userNik);
						}
					}
						
					if(litigationNew.getListPihakTergugatTermohon() != null && litigationNew.getListPihakTergugatTermohon().size() > 0) {
						for(LitigationPihakTergugatTermohon dtlTergugat : litigationNew.getListPihakTergugatTermohon()) {
							dtlTergugat.setLitigation(litigationNew);
							EntityUtil.setCreationInfo(dtlTergugat, userNik);
						}
					}
					
					if(litigationNew.getListPihakKuratorTurutTergugat() != null && litigationNew.getListPihakKuratorTurutTergugat().size() > 0) {
						for(LitigationPihakKuratorTurutTergugat dtlKurator : litigationNew.getListPihakKuratorTurutTergugat()) {
							dtlKurator.setLitigation(litigationNew);
							EntityUtil.setCreationInfo(dtlKurator, userNik);
						}
					}
						
					if(litigationNew.getListProgresPerkara() != null && litigationNew.getListProgresPerkara().size() > 0) {
						for(LitigationProgressPerkara dtlProgress : litigationNew.getListProgresPerkara()) {
							dtlProgress.setLitigation(litigationNew);
							EntityUtil.setCreationInfo(dtlProgress, userNik);
						}
					}
					
					if(litigationNew.getListPutusanPengadilan() != null && litigationNew.getListPutusanPengadilan().size() > 0) {
						for(LitigationPutusanPengadilan dtlPutusan : litigationNew.getListPutusanPengadilan()) {
							
							if(!dtlPutusan.getSelectJudgementWarning().isEmpty())dtlPutusan.setJudgementWarning(parameterDetailService.getParameterDetailByParamDtlCode(dtlPutusan.getSelectJudgementWarning()));
							
							dtlPutusan.setLitigation(litigationNew);
							EntityUtil.setCreationInfo(dtlPutusan, userNik);
						}
					}
						
					
					//add log activity litigasi
					LogActivity la = new LogActivity();
					if(actionMode == Constants.ACTION_ADD) {
						ParameterDetail activityDate = parameterDetailService
								.getParameterDetailByParamDtlCode("LOG_ACT_LITIGASI_ADD");
						List<ParameterDetail> activityType = parameterDetailService
								.getParameterDetailByParamCode("ACTIVITY_TYPE");
						ParameterDetail paramCaseType = litigationNew.getCaseType();
						//ParameterDetail paramCaseTypeDtl = parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTypeDtl());
						for (ParameterDetail data : activityType) {
							if (data.getParameterDtlCode().equals("ACTIVITY_TYPE_LITIGASI")) {
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replace(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),litigationNew.getLitigationPailitPkpu().getCaseNumber());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && 
										litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replace(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),litigationNew.getLitigationPerdataPenggugat().getCaseNumber());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && 
										litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replace(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),litigationNew.getLitigationPerdataTergugat().getCaseNumber());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && 
										litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replace(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),litigationNew.getLitigationPidanaPelapor().getCaseNumber());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && 
										litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replace(str, facesUtil.getUserLogin().getName(),paramCaseType.getNameIn(),litigationNew.getLitigationPidanaTerlapor().getCaseNumber());
									la.setActivityNote(hasil);
								}
							}
						}
					}
					
					if (StringUtils.isBlank(la.getCreatedBy())) {
						EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
					} else {
						EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
					}
					

					litigationNewService.save(litigationNew);
					
					logActivityService.save(la);
					

				}else if(actionMode.equalsIgnoreCase("EDIT")) {
					String userNik = facesUtil.getUserLogin().getNik();
					
					EntityUtil.setUpdateInfo(litigationNew, userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPerdataTergugat(), userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel(), userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPerdataPenggugat(), userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPailitPkpu(), userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPidanaPelapor(), userNik);
					EntityUtil.setUpdateInfo(litigationNew.getLitigationPidanaTerlapor(), userNik);
					
					if(litigationNew.getListPihakPenggugatPemohon() != null) {
						List<LitigationPihakPenggugatPemohon> newPihakPenggugatPemohonList = new ArrayList<>();
						
						if(listPihakPenggugatPerdataTergugat.size() > 0) {
							for (int i = 0; i < listPihakPenggugatPerdataTergugat.size(); i++) {
								LitigationPihakPenggugatPemohon data = listPihakPenggugatPerdataTergugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakPenggugatPemohonList.add(data);
							}
							litigationNew.getListPihakPenggugatPemohon().clear();
							litigationNew.setListPihakPenggugatPemohon(newPihakPenggugatPemohonList);
						}
						
						if(listPihakPenggugatPerdataPenggugat.size() > 0) {
							for (int i = 0; i < listPihakPenggugatPerdataPenggugat.size(); i++) {
								LitigationPihakPenggugatPemohon data = listPihakPenggugatPerdataPenggugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakPenggugatPemohonList.add(data);
							}
							litigationNew.getListPihakPenggugatPemohon().clear();
							litigationNew.setListPihakPenggugatPemohon(newPihakPenggugatPemohonList);
						}
						
						if(listPihakPemohonPailitPkpu.size() > 0) {
							for (int i = 0; i < listPihakPemohonPailitPkpu.size(); i++) {
								LitigationPihakPenggugatPemohon data = listPihakPemohonPailitPkpu.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakPenggugatPemohonList.add(data);
							}
							litigationNew.getListPihakPenggugatPemohon().clear();
							litigationNew.setListPihakPenggugatPemohon(newPihakPenggugatPemohonList);
						}
						
						if(listPihakPelaporPidanaTerlapor.size() > 0) {
							for (int i = 0; i < listPihakPelaporPidanaTerlapor.size(); i++) {
								LitigationPihakPenggugatPemohon data = listPihakPelaporPidanaTerlapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakPenggugatPemohonList.add(data);
							}
							litigationNew.getListPihakPenggugatPemohon().clear();
							litigationNew.setListPihakPenggugatPemohon(newPihakPenggugatPemohonList);
						}
						
						if(listPihakPelaporPidanaPelapor.size() > 0) {
							for (int i = 0; i < listPihakPelaporPidanaPelapor.size(); i++) {
								LitigationPihakPenggugatPemohon data = listPihakPelaporPidanaPelapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakPenggugatPemohonList.add(data);
							}
							litigationNew.getListPihakPenggugatPemohon().clear();
							litigationNew.setListPihakPenggugatPemohon(newPihakPenggugatPemohonList);
						}
						for(LitigationPihakPenggugatPemohon dtlPenggugat : litigationNew.getListPihakPenggugatPemohon()) {
							if(dtlPenggugat.getIsEditable()) {
								dtlPenggugat.setLitigation(litigationNew);
							}
						}	
					}
					
					if(litigationNew.getListPihakTergugatTermohon() != null) {
						List<LitigationPihakTergugatTermohon> newPihakTergugatTermohonList = new ArrayList<>();
						
						if(listPihakTergugatPerdataTergugat.size() > 0) {
							for (int i = 0; i < listPihakTergugatPerdataTergugat.size(); i++) {
								LitigationPihakTergugatTermohon data = listPihakTergugatPerdataTergugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakTergugatTermohonList.add(data);
							}
							litigationNew.getListPihakTergugatTermohon().clear();
							litigationNew.setListPihakTergugatTermohon(newPihakTergugatTermohonList);
						}
						
						if(listPihakTergugatPerdataPenggugat.size() > 0) {
							for (int i = 0; i < listPihakTergugatPerdataPenggugat.size(); i++) {
								LitigationPihakTergugatTermohon data = listPihakTergugatPerdataPenggugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakTergugatTermohonList.add(data);
							}
							litigationNew.getListPihakTergugatTermohon().clear();
							litigationNew.setListPihakTergugatTermohon(newPihakTergugatTermohonList);
						}
						
						if(listPihakTermohonPailitPkpu.size() > 0) {
							for (int i = 0; i < listPihakTermohonPailitPkpu.size(); i++) {
								LitigationPihakTergugatTermohon data = listPihakTermohonPailitPkpu.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakTergugatTermohonList.add(data);
							}
							litigationNew.getListPihakTergugatTermohon().clear();
							litigationNew.setListPihakTergugatTermohon(newPihakTergugatTermohonList);
						}
						
						if(listPihakTerlaporPidanaTerlapor.size() > 0) {
							for (int i = 0; i < listPihakTerlaporPidanaTerlapor.size(); i++) {
								LitigationPihakTergugatTermohon data = listPihakTerlaporPidanaTerlapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakTergugatTermohonList.add(data);
							}
							litigationNew.getListPihakTergugatTermohon().clear();
							litigationNew.setListPihakTergugatTermohon(newPihakTergugatTermohonList);
						}
						
						if(listPihakTerlaporPidanaPelapor.size() > 0) {
							for (int i = 0; i < listPihakTerlaporPidanaPelapor.size(); i++) {
								LitigationPihakTergugatTermohon data = listPihakTerlaporPidanaPelapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakTergugatTermohonList.add(data);
							}
							litigationNew.getListPihakTergugatTermohon().clear();
							litigationNew.setListPihakTergugatTermohon(newPihakTergugatTermohonList);
						}
						
						for(LitigationPihakTergugatTermohon dtlTergugat : litigationNew.getListPihakTergugatTermohon()) {
							if(dtlTergugat.getIsEditable()) {
								dtlTergugat.setLitigation(litigationNew);
							}
						}
					}
					
					if(litigationNew.getListPihakKuratorTurutTergugat() != null) {
						
						List<LitigationPihakKuratorTurutTergugat> newPihakKuratorTurutTergugatList = new ArrayList<>();
						
						if(listPihakTurutTergugatPerdataTergugat.size() > 0) {
							for (int i = 0; i < listPihakTurutTergugatPerdataTergugat.size(); i++) {
								LitigationPihakKuratorTurutTergugat data = listPihakTurutTergugatPerdataTergugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakKuratorTurutTergugatList.add(data);
							}
							litigationNew.getListPihakKuratorTurutTergugat().clear();
							litigationNew.setListPihakKuratorTurutTergugat(newPihakKuratorTurutTergugatList);
						}
						
						if(listPihakTurutTergugatPerdataPenggugat.size() > 0) {
							for (int i = 0; i < listPihakTurutTergugatPerdataPenggugat.size(); i++) {
								LitigationPihakKuratorTurutTergugat data = listPihakTurutTergugatPerdataPenggugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakKuratorTurutTergugatList.add(data);
							}
							litigationNew.getListPihakKuratorTurutTergugat().clear();
							litigationNew.setListPihakKuratorTurutTergugat(newPihakKuratorTurutTergugatList);
						}
						
						if(listPihakKuratorPailitPkpu.size() > 0) {
							for (int i = 0; i < listPihakKuratorPailitPkpu.size(); i++) {
								LitigationPihakKuratorTurutTergugat data = listPihakKuratorPailitPkpu.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newPihakKuratorTurutTergugatList.add(data);
							}
							litigationNew.getListPihakKuratorTurutTergugat().clear();
							litigationNew.setListPihakKuratorTurutTergugat(newPihakKuratorTurutTergugatList);
						}
						
						for(LitigationPihakKuratorTurutTergugat dtlKurator : litigationNew.getListPihakKuratorTurutTergugat()) {
							if(dtlKurator.getIsEditable()) {
								dtlKurator.setLitigation(litigationNew);
							}
						}
					}
					
					if(litigationNew.getListProgresPerkara() != null) {
						
						List<LitigationProgressPerkara> newProgressPerkaraList = new ArrayList<>();
						
						if(listProgressPerkaraPerdataTergugat.size() > 0) {
							for (int i = 0; i < listProgressPerkaraPerdataTergugat.size(); i++) {
								LitigationProgressPerkara data = listProgressPerkaraPerdataTergugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newProgressPerkaraList.add(data);
							}
							litigationNew.getListProgresPerkara().clear();
							litigationNew.setListProgresPerkara(newProgressPerkaraList);
						}
						
						if(listProgressPerkaraPerdataPenggugat.size() > 0) {
							for (int i = 0; i < listProgressPerkaraPerdataPenggugat.size(); i++) {
								LitigationProgressPerkara data = listProgressPerkaraPerdataPenggugat.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newProgressPerkaraList.add(data);
							}
							litigationNew.getListProgresPerkara().clear();
							litigationNew.setListProgresPerkara(newProgressPerkaraList);
						}
						
						if(listProgressPerkaraPailitPkpu.size() > 0) {
							for (int i = 0; i < listProgressPerkaraPailitPkpu.size(); i++) {
								LitigationProgressPerkara data = listProgressPerkaraPailitPkpu.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newProgressPerkaraList.add(data);
							}
							litigationNew.getListProgresPerkara().clear();
							litigationNew.setListProgresPerkara(newProgressPerkaraList);
						}
						
						if(listProgressPerkaraPidanaTerlapor.size() > 0) {
							for (int i = 0; i < listProgressPerkaraPidanaTerlapor.size(); i++) {
								LitigationProgressPerkara data = listProgressPerkaraPidanaTerlapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newProgressPerkaraList.add(data);
							}
							litigationNew.getListProgresPerkara().clear();
							litigationNew.setListProgresPerkara(newProgressPerkaraList);
						}
						
						if(listProgressPerkaraPidanaPelapor.size() > 0) {
							for (int i = 0; i < listProgressPerkaraPidanaPelapor.size(); i++) {
								LitigationProgressPerkara data = listProgressPerkaraPidanaPelapor.get(i);
								
								if (StringUtils.isEmpty(data.getCreatedBy())) {
									EntityUtil.setCreationInfo(data, userNik);
								} else {
									EntityUtil.setUpdateInfo(data, userNik);
								}
								newProgressPerkaraList.add(data);
							}
							litigationNew.getListProgresPerkara().clear();
							litigationNew.setListProgresPerkara(newProgressPerkaraList);
						}
						
						for(LitigationProgressPerkara dtlProgress : litigationNew.getListProgresPerkara()) {
							if(dtlProgress.getIsEditable()) {
								dtlProgress.setLitigation(litigationNew);
							}
						}
					}
					
					if(litigationNew.getListPutusanPengadilan() != null) {
						for(LitigationPutusanPengadilan dtlPutusan : litigationNew.getListPutusanPengadilan()) {
							
							if(!dtlPutusan.getSelectJudgementWarning().isEmpty())dtlPutusan.setJudgementWarning(parameterDetailService.getParameterDetailByParamDtlCode(dtlPutusan.getSelectJudgementWarning()));
							
							if(dtlPutusan.getIsEditable()) {
								dtlPutusan.setLitigation(litigationNew);
								EntityUtil.setCreationInfo(dtlPutusan, userNik);
							}else {
								EntityUtil.setUpdateInfo(dtlPutusan, userNik);
							}
						}
					}
						
					
					//edit log activity litigasi
					LogActivity la = new LogActivity();
					if(actionMode == Constants.ACTION_EDIT) {
						ParameterDetail activityDate = parameterDetailService
								.getParameterDetailByParamDtlCode("LOG_ACT_LITIGASI_EDIT");
						List<ParameterDetail> activityType = parameterDetailService
								.getParameterDetailByParamCode("ACTIVITY_TYPE");
						ParameterDetail paramCaseType = litigationNew.getCaseType();
						//ParameterDetail paramCaseTypeDtl = parameterDetailService.getParameterDetailByParamDtlCode(litigationNew.getSelectCaseTypeDtl());
						for (ParameterDetail data : activityType) {
							if (data.getParameterDtlCode().equals("ACTIVITY_TYPE_LITIGASI")) {
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replaceEdit(str,paramCaseType.getNameIn(),litigationNew.getNoUrutOld(),litigationNew.getNoPerkaraOld(),paramCaseType.getNameIn(),litigationNew.getLitigationPailitPkpu().getCaseNumber(),facesUtil.getUserLogin().getName());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replaceEdit(str,paramCaseType.getNameIn(),litigationNew.getNoUrutOld(),litigationNew.getNoPerkaraOld(),paramCaseType.getNameIn(),litigationNew.getLitigationPerdataPenggugat().getCaseNumber(),facesUtil.getUserLogin().getName());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA) && litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replaceEdit(str,paramCaseType.getNameIn(),litigationNew.getNoUrutOld(),litigationNew.getNoPerkaraOld(),paramCaseType.getNameIn(),litigationNew.getLitigationPerdataTergugat().getCaseNumber(),facesUtil.getUserLogin().getName());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replaceEdit(str,paramCaseType.getNameIn(),litigationNew.getNoUrutOld(),litigationNew.getNoPerkaraOld(),paramCaseType.getNameIn(),litigationNew.getLitigationPidanaPelapor().getCaseNumber(),facesUtil.getUserLogin().getName());
									la.setActivityNote(hasil);
								}
								if(litigationNew.getSelectCaseType().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA) && litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
									la.setUser(getUserLogin());
									la.setActivityType(data.getParameterDtlCode());
									la.setActivityDate(new Timestamp(new Date().getTime()));
									String str = activityDate.getNameIn();
									String hasil = replaceEdit(str,paramCaseType.getNameIn(),litigationNew.getNoUrutOld(),litigationNew.getNoPerkaraOld(),paramCaseType.getNameIn(),litigationNew.getLitigationPidanaTerlapor().getCaseNumber(),facesUtil.getUserLogin().getName());
									la.setActivityNote(hasil);
								}
							}
						}
					}
					
					if (StringUtils.isBlank(la.getCreatedBy())) {
						EntityUtil.setCreationInfo(la, facesUtil.retrieveUserLogin());
					} else {
						EntityUtil.setUpdateInfo(la, facesUtil.retrieveUserLogin());
					}
					
					logActivityService.save(la);
					
					//litigationNewService.updateLitiation(litigationNew);
					
					litigationNewService.udpateLitigation(litigationNew, deletedListPihakPenggugatPemohon, deletedListPihakTergugatTermohon, deletedListProgressPerkara, 
							deletedListPihakKuratorTurutTergugat, deletedListPutusanPengadilan);
				}
				
				facesUtil.redirect("/pages/litigation/litigation.faces");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public void onCheckPerdataTergugatInspectLevelCaseOngoing() {
		if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel() != null) {
			int counter = 0;
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingBaniFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBani(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBani(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPnFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPn(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPn(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPt(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPt(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtunFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtun(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtun(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPaFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPa(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPa(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtAgamaFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgama(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgama(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaKasasiFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasi(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasi(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaPkFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPk(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPk(CommonConstants.RECORD_FLAG_NO);
			}		
			
			litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setTotalOngoingCase(counter);
			PrimeFaces.current().executeScript("reInitSelect2();");
		}
	}
	
	public void onCheckPerdataTergugatInspectLevelCaseFinished() {
		if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel() != null) {
			int counter = 0;
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedBaniFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBani(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBani(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPnFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPn(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPn(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPt(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPt(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtunFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtun(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtun(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPaFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPa(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPa(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtAgamaFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgama(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgama(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaKasasiFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasi(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasi(CommonConstants.RECORD_FLAG_NO);
			}
			
			if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaPkFlag()) {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPk(CommonConstants.RECORD_FLAG_YES);
				
				counter +=1;
			}else {
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPk(CommonConstants.RECORD_FLAG_NO);
			}		
			
			litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setTotalFinishedCase(counter);
			PrimeFaces.current().executeScript("reInitSelect2();");
		}
	}
	
	public void onCheckPerdataPenggugatInspectLevelCaseOngoing() {
		int counter=0;
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingBaniFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBani(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBani(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPnFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPn(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPn(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPtFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPt(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPt(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaKasasiFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasi(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasi(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaPkFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPk(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPk(CommonConstants.RECORD_FLAG_NO);
		}
		
		litigationNew.getLitigationPerdataPenggugat().setTotalOngoingCase(counter);
	}
	
	public void onCheckPerdataPenggugatInspectLevelCaseFinished() {
		int counter = 0;
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedBaniFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBani(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBani(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPnFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPn(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPn(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPtFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPt(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPt(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaKasasiFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasi(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasi(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaPkFlag()) {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPk(CommonConstants.RECORD_FLAG_YES);
			
			counter+=1;
		}else {
			litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPk(CommonConstants.RECORD_FLAG_NO);
		}
		
		litigationNew.getLitigationPerdataPenggugat().setTotalFinishedCase(counter);
	}

	public void onCheckPidanaTerlaporInspectLevel() {
		if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPolFlag()) {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPol(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPol(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPnFlag()) {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPn(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPn(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelJaksaFlag()) {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksa(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksa(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelFinishedFlag()) {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinished(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinished(CommonConstants.RECORD_FLAG_NO);
		}
	}

	public void onCheckPidanaPelaporInspectLevel() {
		if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPolFlag()) {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelPol(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelPol(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPnFlag()) {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelPn(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelPn(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelJaksaFlag()) {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksa(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksa(CommonConstants.RECORD_FLAG_NO);
		}
		
		if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelFinishedFlag()) {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinished(CommonConstants.RECORD_FLAG_YES);
		}else {
			litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinished(CommonConstants.RECORD_FLAG_NO);
		}
	}
		
	
	public void cancel() {
		try {
			facesUtil.redirect("/pages/litigation/litigation.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSealitigationh() {
		return navigateSealitigationh;
	}

	public void setNavigateSealitigationh(String navigateSealitigationh) {
		this.navigateSealitigationh = navigateSealitigationh;
	}

	public LitigationNew getLitigationNew() {
		return litigationNew;
	}

	public void setLitigationNew(LitigationNew litigationNew) {
		this.litigationNew = litigationNew;
	}

	public LitigationNewService getLitigationNewService() {
		return litigationNewService;
	}

	public void setLitigationNewService(LitigationNewService litigationNewService) {
		this.litigationNewService = litigationNewService;
	}

	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}

	public String getEditedId() {
		return editedId;
	}

	public void setEditedId(String editedId) {
		this.editedId = editedId;
	}


	public boolean isCheckAll() {
		return checkAll;
	}

	public void setCheckAll(boolean checkAll) {
		this.checkAll = checkAll;
	}


	public Integer getLastSequenceOfDtl() {
		return lastSequenceOfDtl;
	}

	public void setLastSequenceOfDtl(Integer lastSequenceOfDtl) {
		this.lastSequenceOfDtl = lastSequenceOfDtl;
	}

	public FileUtil getFileUtil() {
		return fileUtil;
	}

	public void setFileUtil(FileUtil fileUtil) {
		this.fileUtil = fileUtil;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		LitigationEditBean.logger = logger;
	}

	public List<SelectItem> getCaseTypeList() {
		return caseTypeList;
	}

	public void setCaseTypeList(List<SelectItem> caseTypeList) {
		this.caseTypeList = caseTypeList;
	}

	public List<SelectItem> getCourtTypeList() {
		return courtTypeList;
	}

	public void setCourtTypeList(List<SelectItem> courtTypeList) {
		this.courtTypeList = courtTypeList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getIndexDtlPic() {
		return indexDtlPic;
	}

	public void setIndexDtlPic(Integer indexDtlPic) {
		this.indexDtlPic = indexDtlPic;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public List<SelectItem> getCaseTypePerdataDtlList() {
		return caseTypePerdataDtlList;
	}

	public void setCaseTypePerdataDtlList(List<SelectItem> caseTypePerdataDtlList) {
		this.caseTypePerdataDtlList = caseTypePerdataDtlList;
	}

	public List<SelectItem> getCaseTypePidanaDtlList() {
		return caseTypePidanaDtlList;
	}

	public void setCaseTypePidanaDtlList(List<SelectItem> caseTypePidanaDtlList) {
		this.caseTypePidanaDtlList = caseTypePidanaDtlList;
	}

	public List<SelectItem> getCaseHandlerList() {
		return caseHandlerList;
	}

	public void setCaseHandlerList(List<SelectItem> caseHandlerList) {
		this.caseHandlerList = caseHandlerList;
	}

	public LitigationPicTableModel<LitigationPic> getTableModelLitigationPic() {
		return tableModelLitigationPic;
	}

	public void setTableModelLitigationPic(LitigationPicTableModel<LitigationPic> tableModelLitigationPic) {
		this.tableModelLitigationPic = tableModelLitigationPic;
	}

	public LitigationPic[] getSelectedLitigationPic() {
		return selectedLitigationPic;
	}

	public void setSelectedLitigationPic(LitigationPic[] selectedLitigationPic) {
		this.selectedLitigationPic = selectedLitigationPic;
	}

	public Integer getLastSequenceLitigationPic() {
		return lastSequenceLitigationPic;
	}

	public void setLastSequenceLitigationPic(Integer lastSequenceLitigationPic) {
		this.lastSequenceLitigationPic = lastSequenceLitigationPic;
	}

	public SelectorInfo getSelectorPic() {
		return selectorPic;
	}

	public void setSelectorPic(SelectorInfo selectorPic) {
		this.selectorPic = selectorPic;
	}

	public List<SelectItem> getDecisionStatusList() {
		return decisionStatusList;
	}

	public void setDecisionStatusList(List<SelectItem> decisionStatusList) {
		this.decisionStatusList = decisionStatusList;
	}

	public List<SelectItem> getJudgementWarningList() {
		return judgementWarningList;
	}

	public void setJudgementWarningList(List<SelectItem> judgementWarningList) {
		this.judgementWarningList = judgementWarningList;
	}

	public LogActivity getLogActivity() {
		return logActivity;
	}

	public void setLogActivity(LogActivity logActivity) {
		this.logActivity = logActivity;
	}

	public LogActivityService getLogActivityService() {
		return logActivityService;
	}

	public void setLogActivityService(LogActivityService logActivityService) {
		this.logActivityService = logActivityService;
	}

	public HashMap<String, String> getRecordCaseTypeDtl() {
		return recordCaseTypeDtl;
	}

	public void setRecordCaseTypeDtl(HashMap<String, String> recordCaseTypeDtl) {
		this.recordCaseTypeDtl = recordCaseTypeDtl;
	}

	public List<SelectItem> getCountryCourtList() {
		return countryCourtList;
	}

	public void setCountryCourtList(List<SelectItem> countryCourtList) {
		this.countryCourtList = countryCourtList;
	}

	public List<SelectItem> getCaseTeamHandlerList() {
		return caseTeamHandlerList;
	}

	public void setCaseTeamHandlerList(List<SelectItem> caseTeamHandlerList) {
		this.caseTeamHandlerList = caseTeamHandlerList;
	}

	public List<SelectItem> getCaseMainTopicList() {
		return caseMainTopicList;
	}

	public void setCaseMainTopicList(List<SelectItem> caseMainTopicList) {
		this.caseMainTopicList = caseMainTopicList;
	}

	public List<SelectItem> getCaseSubTopicList() {
		return caseSubTopicList;
	}

	public void setCaseSubTopicList(List<SelectItem> caseSubTopicList) {
		this.caseSubTopicList = caseSubTopicList;
	}

	public List<SelectItem> getForeignCurrencyList() {
		return foreignCurrencyList;
	}

	public void setForeignCurrencyList(List<SelectItem> foreignCurrencyList) {
		this.foreignCurrencyList = foreignCurrencyList;
	}

	public LitigationProgressPerkaraTableModel<LitigationProgressPerkara> getTableModelLitigationProgressPerkara() {
		return tableModelLitigationProgressPerkara;
	}

	public void setTableModelLitigationProgressPerkara(
			LitigationProgressPerkaraTableModel<LitigationProgressPerkara> tableModelLitigationProgressPerkara) {
		this.tableModelLitigationProgressPerkara = tableModelLitigationProgressPerkara;
	}

	public Integer getIndexDtlProgressPerkara() {
		return indexDtlProgressPerkara;
	}

	public void setIndexDtlProgressPerkara(Integer indexDtlProgressPerkara) {
		this.indexDtlProgressPerkara = indexDtlProgressPerkara;
	}

	public Integer getLastSequenceLitigationProgressPerkara() {
		return lastSequenceLitigationProgressPerkara;
	}

	public void setLastSequenceLitigationProgressPerkara(Integer lastSequenceLitigationProgressPerkara) {
		this.lastSequenceLitigationProgressPerkara = lastSequenceLitigationProgressPerkara;
	}

	public LitigationProgressPerkara[] getSelectedLitigationProgressPerkara() {
		return selectedLitigationProgressPerkara;
	}

	public void setSelectedLitigationProgressPerkara(LitigationProgressPerkara[] selectedLitigationProgressPerkara) {
		this.selectedLitigationProgressPerkara = selectedLitigationProgressPerkara;
	}

	public Integer getIndexDtlPihakPenggugatPemohon() {
		return indexDtlPihakPenggugatPemohon;
	}

	public void setIndexDtlPihakPenggugatPemohon(Integer indexDtlPihakPenggugatPemohon) {
		this.indexDtlPihakPenggugatPemohon = indexDtlPihakPenggugatPemohon;
	}

	public Integer getLastSequencePihakPenggugatPemohon() {
		return lastSequencePihakPenggugatPemohon;
	}

	public void setLastSequencePihakPenggugatPemohon(Integer lastSequencePihakPenggugatPemohon) {
		this.lastSequencePihakPenggugatPemohon = lastSequencePihakPenggugatPemohon;
	}

	public LitigationPihakPenggugatPemohon[] getSelectedLitigationPihakPenggugatPemohon() {
		return selectedLitigationPihakPenggugatPemohon;
	}

	public void setSelectedLitigationPihakPenggugatPemohon(
			LitigationPihakPenggugatPemohon[] selectedLitigationPihakPenggugatPemohon) {
		this.selectedLitigationPihakPenggugatPemohon = selectedLitigationPihakPenggugatPemohon;
	}

	public LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> getTableModelLitigationPihakPenggugatPemohon() {
		return tableModelLitigationPihakPenggugatPemohon;
	}

	public void setTableModelLitigationPihakPenggugatPemohon(
			LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> tableModelLitigationPihakPenggugatPemohon) {
		this.tableModelLitigationPihakPenggugatPemohon = tableModelLitigationPihakPenggugatPemohon;
	}

	public Integer getIndexDtlPihakTergugatTermohon() {
		return indexDtlPihakTergugatTermohon;
	}

	public void setIndexDtlPihakTergugatTermohon(Integer indexDtlPihakTergugatTermohon) {
		this.indexDtlPihakTergugatTermohon = indexDtlPihakTergugatTermohon;
	}

	public Integer getLastSequencePihakTergugatTermohon() {
		return lastSequencePihakTergugatTermohon;
	}

	public void setLastSequencePihakTergugatTermohon(Integer lastSequencePihakTergugatTermohon) {
		this.lastSequencePihakTergugatTermohon = lastSequencePihakTergugatTermohon;
	}

	public LitigationPihakTergugatTermohon[] getSelectedLitigationPihakTergugatTermohon() {
		return selectedLitigationPihakTergugatTermohon;
	}

	public void setSelectedLitigationPihakTergugatTermohon(
			LitigationPihakTergugatTermohon[] selectedLitigationPihakTergugatTermohon) {
		this.selectedLitigationPihakTergugatTermohon = selectedLitigationPihakTergugatTermohon;
	}

	public LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> getTableModelLitigationPihakTergugatTermohon() {
		return tableModelLitigationPihakTergugatTermohon;
	}

	public void setTableModelLitigationPihakTergugatTermohon(
			LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> tableModelLitigationPihakTergugatTermohon) {
		this.tableModelLitigationPihakTergugatTermohon = tableModelLitigationPihakTergugatTermohon;
	}

	public Integer getIndexDtlPihakKuratorTurutTergugat() {
		return indexDtlPihakKuratorTurutTergugat;
	}

	public void setIndexDtlPihakKuratorTurutTergugat(Integer indexDtlPihakKuratorTurutTergugat) {
		this.indexDtlPihakKuratorTurutTergugat = indexDtlPihakKuratorTurutTergugat;
	}

	public Integer getLastSequencePihakKuratorTurutTergugat() {
		return lastSequencePihakKuratorTurutTergugat;
	}

	public void setLastSequencePihakKuratorTurutTergugat(Integer lastSequencePihakKuratorTurutTergugat) {
		this.lastSequencePihakKuratorTurutTergugat = lastSequencePihakKuratorTurutTergugat;
	}

	public LitigationPihakKuratorTurutTergugat[] getSelectedLitigationPihakKuratorTurutTergugat() {
		return selectedLitigationPihakKuratorTurutTergugat;
	}

	public void setSelectedLitigationPihakKuratorTurutTergugat(
			LitigationPihakKuratorTurutTergugat[] selectedLitigationPihakKuratorTurutTergugat) {
		this.selectedLitigationPihakKuratorTurutTergugat = selectedLitigationPihakKuratorTurutTergugat;
	}

	public LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> getTableModelLitigationPihakKuratorTurutTergugat() {
		return tableModelLitigationPihakKuratorTurutTergugat;
	}

	public void setTableModelLitigationPihakKuratorTurutTergugat(
			LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> tableModelLitigationPihakKuratorTurutTergugat) {
		this.tableModelLitigationPihakKuratorTurutTergugat = tableModelLitigationPihakKuratorTurutTergugat;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public List<SelectItem> getCommercialCourtList() {
		return commercialCourtList;
	}

	public void setCommercialCourtList(List<SelectItem> commercialCourtList) {
		this.commercialCourtList = commercialCourtList;
	}

	public Integer getIndexDtlPutusanPengadilan() {
		return indexDtlPutusanPengadilan;
	}

	public void setIndexDtlPutusanPengadilan(Integer indexDtlPutusanPengadilan) {
		this.indexDtlPutusanPengadilan = indexDtlPutusanPengadilan;
	}

	public Integer getLastSequencePutusanPengadilan() {
		return lastSequencePutusanPengadilan;
	}

	public void setLastSequencePutusanPengadilan(Integer lastSequencePutusanPengadilan) {
		this.lastSequencePutusanPengadilan = lastSequencePutusanPengadilan;
	}

	public LitigationPutusanPengadilan[] getSelectedLitigationPutusanPengadilan() {
		return selectedLitigationPutusanPengadilan;
	}

	public void setSelectedLitigationPutusanPengadilan(LitigationPutusanPengadilan[] selectedLitigationPutusanPengadilan) {
		this.selectedLitigationPutusanPengadilan = selectedLitigationPutusanPengadilan;
	}

	public LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> getTableModelLitigationPutusanPengadilan() {
		return tableModelLitigationPutusanPengadilan;
	}

	public void setTableModelLitigationPutusanPengadilan(
			LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> tableModelLitigationPutusanPengadilan) {
		this.tableModelLitigationPutusanPengadilan = tableModelLitigationPutusanPengadilan;
	}

	public List<LitigationPihakPenggugatPemohon> getDeletedListPihakPenggugatPemohon() {
		return deletedListPihakPenggugatPemohon;
	}

	public void setDeletedListPihakPenggugatPemohon(
			List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon) {
		this.deletedListPihakPenggugatPemohon = deletedListPihakPenggugatPemohon;
	}

	public List<LitigationPihakTergugatTermohon> getDeletedListPihakTergugatTermohon() {
		return deletedListPihakTergugatTermohon;
	}

	public void setDeletedListPihakTergugatTermohon(
			List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon) {
		this.deletedListPihakTergugatTermohon = deletedListPihakTergugatTermohon;
	}

	public List<LitigationProgressPerkara> getDeletedListProgressPerkara() {
		return deletedListProgressPerkara;
	}

	public void setDeletedListProgressPerkara(List<LitigationProgressPerkara> deletedListProgressPerkara) {
		this.deletedListProgressPerkara = deletedListProgressPerkara;
	}

	public List<LitigationPihakKuratorTurutTergugat> getDeletedListPihakKuratorTurutTergugat() {
		return deletedListPihakKuratorTurutTergugat;
	}

	public void setDeletedListPihakKuratorTurutTergugat(
			List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat) {
		this.deletedListPihakKuratorTurutTergugat = deletedListPihakKuratorTurutTergugat;
	}

	public List<LitigationPutusanPengadilan> getDeletedListPutusanPengadilan() {
		return deletedListPutusanPengadilan;
	}

	public void setDeletedListPutusanPengadilan(List<LitigationPutusanPengadilan> deletedListPutusanPengadilan) {
		this.deletedListPutusanPengadilan = deletedListPutusanPengadilan;
	}
	
	
}