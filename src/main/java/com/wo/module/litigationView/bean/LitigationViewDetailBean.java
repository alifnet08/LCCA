package com.wo.module.litigationView.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.litigation.model.LitigationNew;
import com.wo.module.litigation.model.LitigationPic;
import com.wo.module.litigation.model.LitigationPicTableModel;
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
import com.wo.module.litigationView.service.LitigationViewService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;
import com.wo.module.rc.model.RC;
import com.wo.module.rc.service.RCService;

public class LitigationViewDetailBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = 7702030897432099476L;
	private static final Logger logger = Logger.getLogger(LitigationViewDetailBean.class);
	
	private LitigationViewService litigationViewService;
	private LitigationNewService litigationNewService;
	private RCService rcService;
	
	private LitigationNew litigationNew;
	
	private String viewId;
	
	private FacesUtil facesUtil;
	
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
	
	private HashMap<String, String> recordCaseTypeDtl = new HashMap<String, String>();
	
	private LitigationProgressPerkaraTableModel<LitigationProgressPerkara> tableModelLitigationProgressPerkara;
	private LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> tableModelLitigationPihakPenggugatPemohon;
	private LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> tableModelLitigationPihakTergugatTermohon;
	private LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> tableModelLitigationPihakKuratorTurutTergugat;
	private LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> tableModelLitigationPutusanPengadilan;
	
	@PostConstruct
	public void init() {
		super.init();
		initList();
		checkView();
		
		tableModelLitigationProgressPerkara = new LitigationProgressPerkaraTableModel<LitigationProgressPerkara>(litigationNew.getListProgresPerkara());
		tableModelLitigationPihakPenggugatPemohon = new LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon>(
				litigationNew.getListPihakPenggugatPemohon());
		tableModelLitigationPihakTergugatTermohon = new LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon>(
				litigationNew.getListPihakTergugatTermohon());
		tableModelLitigationPihakKuratorTurutTergugat = new LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat>(
				litigationNew.getListPihakKuratorTurutTergugat());
		tableModelLitigationPutusanPengadilan = new LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan>(
				litigationNew.getListPutusanPengadilan());
	}
	
	public void initList(){
		caseTypePidanaDtlList = new ArrayList<>();
		caseHandlerList = new ArrayList<>();
		decisionStatusList = new ArrayList<>();
		judgementWarningList = new ArrayList<>();
		countryCourtList = new ArrayList<>();
		caseTypePerdataDtlList = new ArrayList<>();
		caseTypeList = new ArrayList<>();
		courtTypeList = new ArrayList<>();
		caseTeamHandlerList = new ArrayList<>();
		caseMainTopicList = new ArrayList<>();
		caseSubTopicList = new ArrayList<>();
		foreignCurrencyList = new ArrayList<>();
		rcList = new ArrayList<>();
		commercialCourtList = new ArrayList<>();
		
		
		try {
			List<ParameterDetail> listCaseType = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE);
			for (ParameterDetail vo : listCaseType) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypeList.add(si);
				
			}
		
			List<ParameterDetail> listCaseTypeDtlPerdata = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PERDATA);
			for (ParameterDetail vo : listCaseTypeDtlPerdata) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypePerdataDtlList.add(si);
				recordCaseTypeDtl.put(vo.getParameterDtlCode(), vo.getName());
			}
		
			List<ParameterDetail> listCaseTypeDtlPidana = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TYPE_DTL_PIDANA);
			for (ParameterDetail vo : listCaseTypeDtlPidana) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTypePidanaDtlList.add(si);
			}
		
			List<ParameterDetail> listCaseHandler = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_HANDLER);
			for(ParameterDetail vo : listCaseHandler) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseHandlerList.add(si);
			}
		
			List<ParameterDetail> listDecisionStatus = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_DECISION_STATUS);
			for(ParameterDetail vo : listDecisionStatus) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				decisionStatusList.add(si);
			}
		
			List<ParameterDetail> listJudgementWarning = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_JUDGEMENT_WARNING);
			for(ParameterDetail vo : listJudgementWarning) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				judgementWarningList.add(si);
			}
			
			List<ParameterDetail> listCountryCourt = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_COUNTRY_COURT);
			for(ParameterDetail vo : listCountryCourt) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				countryCourtList.add(si);
			}
			
			List<ParameterDetail> listCaseTeamHandler = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_TEAM_HANDLER);
			for(ParameterDetail vo : listCaseTeamHandler) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseTeamHandlerList.add(si);
			}
			
			List<ParameterDetail> listForeignCurrency = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_FOREIGN_CURRENCY_TYPE);
			for(ParameterDetail vo : listForeignCurrency) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				foreignCurrencyList.add(si);
			}
			
			List<ParameterDetail> listMainTopic = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_MAIN_TOPIC);
			for(ParameterDetail vo : listMainTopic) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseMainTopicList.add(si);
			}
			
			List<ParameterDetail> listSubTopic = parameterDetailService.getParameterDetailByParamCode(ParameterHeader.PARAM_HEAD_LITIGATION_CASE_SUB_TOPIC);
			for(ParameterDetail vo : listSubTopic) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getName());
				si.setValue(vo.getParameterDtlCode());
				caseSubTopicList.add(si);
			}
			
			List<RC> listRC = rcService.getRCList();
			for(RC vo : listRC) {
				SelectItem si = new SelectItem();
				si.setLabel(vo.getRegion());
				si.setValue(vo.getRcId());
				rcList.add(si);
			}
			
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
		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	
	@SuppressWarnings("unused")
	private void checkView() {
		this.viewId = facesUtil.retrieveRequestParam("viewId");
		String token = facesUtil.retrieveRequestParam("token");
		
		if (StringUtils.isNotBlank(viewId)) {
			handleView(viewId);
		}
		
	}
	
	
	private void handleView(String viewId) {
		Long idLong = Long.parseLong(viewId);
		
		litigationNew = litigationNewService.findById(idLong);
		if(litigationNew.getPic() != null) litigationNew.setSelectedPicName(litigationNew.getPic().getName());
		
		if(litigationNew.getListPutusanPengadilan() != null) {
			for(LitigationPutusanPengadilan dtl : litigationNew.getListPutusanPengadilan()) {
				if(dtl.getJudgementWarning() != null) dtl.setSelectJudgementWarning(dtl.getJudgementWarning().getParameterDtlCode());
			}
		}
		
		
		if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU)) {
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PAILIT_PKPU);
			litigationNew.setNoPerkaraOld(litigationNew.getLitigationPailitPkpu().getCaseNumber());
			
			if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPailitPkpu(litigationNew.getCaseTeamHandler().getParameterDtlCode());
			litigationNew.setPicDescPailitPkpu(litigationNew.getPicDescription());
			
			if(litigationNew.getLitigationPailitPkpu().getCaseHandler() != null) litigationNew.getLitigationPailitPkpu().setSelectCaseHandler(litigationNew.getLitigationPailitPkpu().getCaseHandler().getParameterDtlCode());
			if(litigationNew.getLitigationPailitPkpu().getJudgementWarning() != null) litigationNew.getLitigationPailitPkpu().setSelectJudgementWarning(litigationNew.getLitigationPailitPkpu().getJudgementWarning().getParameterDtlCode());
			if(litigationNew.getLitigationPailitPkpu().getCommercialCourt() != null) litigationNew.getLitigationPailitPkpu().setSelectCommercialCourt(litigationNew.getLitigationPailitPkpu().getCommercialCourt().getParameterDtlCode());
		
			
		}  else if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA)) {
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PERDATA);
			
			if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT)) {
//				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT);
				litigationNew.setSelectCaseTypeDtlPerdata(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_PENGGUGAT);
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPerdataPeng(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				litigationNew.setPicDescPerdataTer(litigationNew.getPicDescription());
				
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPerdataPenggugat().getCaseNumber());
				litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(false);
				litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(false);
				
				if(litigationNew.getLitigationPerdataPenggugat().getCaseHandler() != null) litigationNew.getLitigationPerdataPenggugat().setSelectCaseHandler(litigationNew.getLitigationPerdataPenggugat().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCaseMainTopic() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCaseMainTopic(litigationNew.getLitigationPerdataPenggugat().getCaseMainTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCaseSubTopic() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCaseSubTopic(litigationNew.getLitigationPerdataPenggugat().getCaseSubTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getCourtType() != null)litigationNew.getLitigationPerdataPenggugat().setSelectCourtType(litigationNew.getLitigationPerdataPenggugat().getCourtType().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataPenggugat().getForeignCurrencyType() != null)litigationNew.getLitigationPerdataPenggugat().setSelectForeignCurrency(litigationNew.getLitigationPerdataPenggugat().getForeignCurrencyType().getParameterDtlCode());
				
				if(litigationNew.getLitigationPerdataPenggugat() != null) {
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingBani() != null && litigationNew.getLitigationPerdataPenggugat().getCaseOngoingBani().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseOngoingBaniFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPn() != null && litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPnFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPt() != null && litigationNew.getLitigationPerdataPenggugat().getCaseOngoingPt().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseOngoingPtFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaKasasi() != null && litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaKasasiFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaPk() != null && litigationNew.getLitigationPerdataPenggugat().getCaseOngoingMaPk().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseOngoingMaPkFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedBani() != null && litigationNew.getLitigationPerdataPenggugat().getCaseFinishedBani().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseFinishedBaniFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPn() != null && litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPnFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPt() != null && litigationNew.getLitigationPerdataPenggugat().getCaseFinishedPt().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseFinishedPtFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaKasasi() != null && litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaKasasiFlag(true);
					if(litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaPk() != null && litigationNew.getLitigationPerdataPenggugat().getCaseFinishedMaPk().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataPenggugat().setCaseFinishedMaPkFlag(true);
				}
				
			} else if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT)) {
//				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT);
				litigationNew.setSelectCaseTypeDtlPerdata(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PERDATA_TERGUGAT);
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPerdataTer(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				litigationNew.setPicDescPerdataPeng(litigationNew.getPicDescription());
				
				if(litigationNew.getLitigationPerdataTergugat().getCaseHandler() != null) litigationNew.getLitigationPerdataTergugat().setSelectCaseHandler(litigationNew.getLitigationPerdataTergugat().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getDecisionStatus() != null) litigationNew.getLitigationPerdataTergugat().setSelectDecisionStatus(litigationNew.getLitigationPerdataTergugat().getDecisionStatus().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCaseMainTopic() != null)litigationNew.getLitigationPerdataTergugat().setSelectCaseMainTopic(litigationNew.getLitigationPerdataTergugat().getCaseMainTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCaseSubTopic() != null)litigationNew.getLitigationPerdataTergugat().setSelectCaseSubTopic(litigationNew.getLitigationPerdataTergugat().getCaseSubTopic().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getCourtType() != null)litigationNew.getLitigationPerdataTergugat().setSelectCourtType(litigationNew.getLitigationPerdataTergugat().getCourtType().getParameterDtlCode());
				if(litigationNew.getLitigationPerdataTergugat().getForeignCurrencyType() != null)litigationNew.getLitigationPerdataTergugat().setSelectForeignCurrency(litigationNew.getLitigationPerdataTergugat().getForeignCurrencyType().getParameterDtlCode());
				
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPerdataTergugat().getCaseNumber());
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(false);
				litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(false);
				
				if (litigationNew.getLitigationPerdataTergugat() != null) {
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingBani() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingBani().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingBaniFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaKasasi() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaKasasiFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaPk() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingMaPk().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingMaPkFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPa() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPa().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPaFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPn() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPnFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPt() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPt().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtAgama() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtAgama().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtAgamaFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtun() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseOngoingPtun().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseOngoingPtunFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedBani() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedBani().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedBaniFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaKasasi() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaKasasi().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaKasasiFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaPk() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedMaPk().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedMaPkFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPa() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPa().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPaFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPn() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPnFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPt() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPt().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtAgama() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtAgama().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtAgamaFlag(true);
					if(litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtun() != null && litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().getCaseFinishedPtun().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPerdataTergugat().getLitigationPerdataInspectLevel().setCaseFinishedPtunFlag(true);
				}
			}
		} else if(litigationNew.getCaseType().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA)) {
			litigationNew.setSelectCaseType(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_PIDANA);
			
			if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR)) {
				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_PELAPOR);
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPidanaPel(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				litigationNew.setPicDescPidanaPel(litigationNew.getPicDescription());
				
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPidanaPelapor().getCaseNumber());
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(false);
				litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(false);
				
				if(litigationNew.getLitigationPidanaPelapor().getCaseHandler() != null)litigationNew.getLitigationPidanaPelapor().setSelectCaseHandler(litigationNew.getLitigationPidanaPelapor().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPidanaPelapor().getForeignCurrencyType() != null)litigationNew.getLitigationPidanaPelapor().setSelectForeignCurrency(litigationNew.getLitigationPidanaPelapor().getForeignCurrencyType().getParameterDtlCode());
				
				if (litigationNew.getLitigationPidanaPelapor() != null) {
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPn() != null && litigationNew.getLitigationPidanaPelapor().getInspectionLevelPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaPelapor().setInspectionLevelPnFlag(true);
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelPol() != null && litigationNew.getLitigationPidanaPelapor().getInspectionLevelPol().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaPelapor().setInspectionLevelPolFlag(true);
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelJaksa() != null && litigationNew.getLitigationPidanaPelapor().getInspectionLevelJaksa().equals(CommonConstants.RECORD_FLAG_YES))litigationNew.getLitigationPidanaPelapor().setInspectionLevelJaksaFlag(true);
					if(litigationNew.getLitigationPidanaPelapor().getInspectionLevelFinished() != null && litigationNew.getLitigationPidanaPelapor().getInspectionLevelFinished().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaPelapor().setInspectionLevelFinishedFlag(true);
				}
				
			} else if(litigationNew.getCaseTypeDtl().getParameterDtlCode().equals(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR)) {
				litigationNew.setSelectCaseTypeDtl(ParameterDetail.PARAM_DET_CODE_LITIGATION_CASE_TYPE_DTL_PIDANA_TERLAPOR);
				if(litigationNew.getCaseTeamHandler() != null) litigationNew.setSelectCaseTeamHandlerPidanaTer(litigationNew.getCaseTeamHandler().getParameterDtlCode());
				litigationNew.setPicDescPidanaTer(litigationNew.getPicDescription());
				
				litigationNew.setNoPerkaraOld(litigationNew.getLitigationPidanaTerlapor().getCaseNumber());
				litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(false);
				litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(false);
				litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(false);
				litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(false);
				
				if(litigationNew.getLitigationPidanaTerlapor().getCaseHandler() != null)litigationNew.getLitigationPidanaTerlapor().setSelectCaseHandler(litigationNew.getLitigationPidanaTerlapor().getCaseHandler().getParameterDtlCode());
				if(litigationNew.getLitigationPidanaTerlapor().getForeignCurrencyType() != null)litigationNew.getLitigationPidanaTerlapor().setSelectForeignCurrency(litigationNew.getLitigationPidanaTerlapor().getForeignCurrencyType().getParameterDtlCode());
				
				if (litigationNew.getLitigationPidanaTerlapor() != null) {
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPn() != null && litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPn().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPnFlag(true);
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPol() != null && litigationNew.getLitigationPidanaTerlapor().getInspectionLevelPol().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaTerlapor().setInspectionLevelPolFlag(true);
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelJaksa() != null && litigationNew.getLitigationPidanaTerlapor().getInspectionLevelJaksa().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaTerlapor().setInspectionLevelJaksaFlag(true);
					if(litigationNew.getLitigationPidanaTerlapor().getInspectionLevelFinished() != null && litigationNew.getLitigationPidanaTerlapor().getInspectionLevelFinished().equals(CommonConstants.RECORD_FLAG_YES)) litigationNew.getLitigationPidanaTerlapor().setInspectionLevelFinishedFlag(true);
				}
			}
		}
		PrimeFaces.current().executeScript("disableEnable();");	
	}

	public void downloadFile(String fileId, String fileName, byte[] content) throws Exception{
		CallApiManager.downloadFile(fileId, fileName, content, parameterDetailService);
	}
	
	
	public void back() {
		try {
			facesUtil.redirect("/pages/litigationView/litigationView.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LitigationViewService getLitigationViewService() {
		return litigationViewService;
	}

	public void setLitigationViewService(LitigationViewService litigationViewService) {
		this.litigationViewService = litigationViewService;
	}
	
	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
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

	public List<SelectItem> getCountryCourtList() {
		return countryCourtList;
	}

	public void setCountryCourtList(List<SelectItem> countryCourtList) {
		this.countryCourtList = countryCourtList;
	}

	public List<SelectItem> getCaseTypeList() {
		return caseTypeList;
	}

	public void setCaseTypeList(List<SelectItem> caseTypeList) {
		this.caseTypeList = caseTypeList;
	}

	public HashMap<String, String> getRecordCaseTypeDtl() {
		return recordCaseTypeDtl;
	}

	public void setRecordCaseTypeDtl(HashMap<String, String> recordCaseTypeDtl) {
		this.recordCaseTypeDtl = recordCaseTypeDtl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<SelectItem> getCourtTypeList() {
		return courtTypeList;
	}

	public void setCourtTypeList(List<SelectItem> courtTypeList) {
		this.courtTypeList = courtTypeList;
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

	public List<SelectItem> getRcList() {
		return rcList;
	}

	public void setRcList(List<SelectItem> rcList) {
		this.rcList = rcList;
	}

	public RCService getRcService() {
		return rcService;
	}

	public void setRcService(RCService rcService) {
		this.rcService = rcService;
	}

	public LitigationProgressPerkaraTableModel<LitigationProgressPerkara> getTableModelLitigationProgressPerkara() {
		return tableModelLitigationProgressPerkara;
	}

	public void setTableModelLitigationProgressPerkara(
			LitigationProgressPerkaraTableModel<LitigationProgressPerkara> tableModelLitigationProgressPerkara) {
		this.tableModelLitigationProgressPerkara = tableModelLitigationProgressPerkara;
	}

	public LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> getTableModelLitigationPihakPenggugatPemohon() {
		return tableModelLitigationPihakPenggugatPemohon;
	}

	public void setTableModelLitigationPihakPenggugatPemohon(
			LitigationPihakPenggugatPemohonTableModel<LitigationPihakPenggugatPemohon> tableModelLitigationPihakPenggugatPemohon) {
		this.tableModelLitigationPihakPenggugatPemohon = tableModelLitigationPihakPenggugatPemohon;
	}

	public LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> getTableModelLitigationPihakTergugatTermohon() {
		return tableModelLitigationPihakTergugatTermohon;
	}

	public void setTableModelLitigationPihakTergugatTermohon(
			LitigationPihakTergugatTermohonTableModel<LitigationPihakTergugatTermohon> tableModelLitigationPihakTergugatTermohon) {
		this.tableModelLitigationPihakTergugatTermohon = tableModelLitigationPihakTergugatTermohon;
	}

	public LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> getTableModelLitigationPihakKuratorTurutTergugat() {
		return tableModelLitigationPihakKuratorTurutTergugat;
	}

	public void setTableModelLitigationPihakKuratorTurutTergugat(
			LitigationPihakKuratorTurutTergugatTableModel<LitigationPihakKuratorTurutTergugat> tableModelLitigationPihakKuratorTurutTergugat) {
		this.tableModelLitigationPihakKuratorTurutTergugat = tableModelLitigationPihakKuratorTurutTergugat;
	}

	public List<SelectItem> getCommercialCourtList() {
		return commercialCourtList;
	}

	public void setCommercialCourtList(List<SelectItem> commercialCourtList) {
		this.commercialCourtList = commercialCourtList;
	}

	public LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> getTableModelLitigationPutusanPengadilan() {
		return tableModelLitigationPutusanPengadilan;
	}

	public void setTableModelLitigationPutusanPengadilan(
			LitigationPutusanPengadilanTableModel<LitigationPutusanPengadilan> tableModelLitigationPutusanPengadilan) {
		this.tableModelLitigationPutusanPengadilan = tableModelLitigationPutusanPengadilan;
	}
	
	
	
	

}
