package com.wo.module.internalRegulationObsolete.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.common.exception.CustomAPIException;
import com.wo.module.common.util.MathUtil;
import com.wo.module.common.utility.CallApiManager;
import com.wo.module.common.vo.SendEmailVo;
import com.wo.module.counterType.service.CounterTypeService;
import com.wo.module.division.service.DivisionService;
import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.emailTemplate.service.EmailTemplateService;
import com.wo.module.holiday.service.HolidayService;
import com.wo.module.internalRegulationObsolete.constant.InternalRegulationObsoleteConstants;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsoleteEmailTmp;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsoletePic;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsoletePicTableModel;
import com.wo.module.internalRegulationObsolete.service.InternalRegulationObsoleteService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.lov.bean.SelectorListener;
import com.wo.module.lov.bean.SelectorModel.SelectorInfo;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.user.model.Division;
import com.wo.module.user.model.User;
import com.wo.module.user.service.UserService;

public class InternalRegulationObsoleteEditBean extends CommonBean implements SelectorListener<Object>, Serializable {

	private static final long serialVersionUID = 2677660747617423084L;

	static Logger logger = Logger.getLogger(InternalRegulationObsoleteEditBean.class);

	private Boolean isViewOnly; 

	private Integer lastSequenceOfPickonversi; 
	private Integer indexDtlPicKonversi;
	private Integer indexDtlFollowupIrg;

	private List<SelectItem> typeRegObsoletes;
	private List<SelectItem> counterTypes;
	private List<SelectItem> unitKerjas;
	private List<SelectItem> openCloseRegulationObsoletes;

	private InternalRegulationObsolete regulationObsolete;
	
	private Long typeRegObsoleteDtlId;
	private Long publisherDivisionId;
	private Long openCloseRegObsoleteId;

	private InternalRegulationObsoletePic[] selectedDataPickonversi;
	private InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic> tableModelPickonversi;

	public FacesUtil facesUtil;

	private String navigateSearch = InternalRegulationObsoleteConstants.NAVIGATE_SEARCH;
	private String actionMode;

	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

	private SelectorInfo selectorUser1;
	private SelectorInfo selectorUser2;
	private SelectorInfo selectorUser3;
	private SelectorInfo selectorIrg2;

	private CounterTypeService counterTypeService;
	private UserService userService;
	private InternalRegulationObsoleteService internalRegulationObsoleteService;
	private DivisionService divisionService;
	private EmailTemplateService emailTemplateService;
	private HolidayService holidayService;

	private String picIrgName1;
	
	public String getPicIrgName1() {
		return picIrgName1;
	}

	public void setPicIrgName1(String picIrgName1) {
		this.picIrgName1 = picIrgName1;
	}

	private String textWarningUpload;

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
		initSelectItemList();

		selectorUser1 = InternalRegulationObsoleteConstants.buildSelectorUser();
		selectorUser2 = InternalRegulationObsoleteConstants.buildSelectorUser();
		selectorUser3 = InternalRegulationObsoleteConstants.buildSelectorUser();
		selectorIrg2 = InternalRegulationObsoleteConstants.buildSelectorPICIrg(facesUtil);

		checkNewOrEdit();

		try {
			ParameterDetail getText = parameterDetailService.getParameterDetailByParamDtlCode("UPLOAD_WARNING_TEXT");
			textWarningUpload = getText.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initSelectItemList() {
		typeRegObsoletes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pd = parameterDetailService.getParameterDetailByParamCode("TYPE_REGULATION_OBSOLETE");
			for (int i = 0; i < pd.size(); i++) {
				SelectItem si = new SelectItem();
				si.setLabel(((ParameterDetail) pd.get(i)).getName());
				si.setValue(((ParameterDetail) pd.get(i)).getParameterDtlId());
				typeRegObsoletes.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//UNUSED YET - incase IRG team want to use "Tipe Pengingat"
//		counterTypes = new ArrayList<SelectItem>();
//		try {
//			counterTypes = counterTypeService.getAllCounterTypeLabelValue();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		unitKerjas = new ArrayList<SelectItem>();
		try {
			List<Division> pd = userService.getAllDivision();
			for (Division division : pd) {
				SelectItem si = new SelectItem();
				si.setLabel(division.getDivisionName());
				si.setValue(division.getDivisionId());
				unitKerjas.add(si);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		openCloseRegulationObsoletes = new ArrayList<SelectItem>();
		try {
			List<ParameterDetail> pdList = parameterDetailService.getParameterDetailByParamCode("OPEN_CLOSE_REGULATION_OBSOLETE");
			for(ParameterDetail pd : pdList) {
				SelectItem si = new SelectItem();
				si.setLabel(pd.getNameIn());
				si.setValue(pd.getParameterDtlId());
				openCloseRegulationObsoletes.add(si);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}
	
	public List<String> completeObsoleteTitle(String query) {
		List<String> obsoleteTitles = internalRegulationObsoleteService.getDataObsoleteTitle(query);
		return obsoleteTitles;
	}

	public void checkNewOrEdit() {
		try {
			String editId = facesUtil.retrieveRequestParam("editedId");
			String viewId = facesUtil.retrieveRequestParam("viewId");

			isViewOnly = false;
			if (viewId != null && !viewId.isEmpty()) {
				if (viewId.trim().equalsIgnoreCase("true")) {
					isViewOnly = true;
				}
			}
			if (StringUtils.isBlank(editId)) {
				handleNew();
			} else {
				handleEdit(editId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleNew() {
		actionMode = Constants.ACTION_ADD;
		
		regulationObsolete = new InternalRegulationObsolete();
		
		tableModelPickonversi = new InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic>(
				regulationObsolete.getInternalRegulationObsoletePicKonversis());
		
		lastSequenceOfPickonversi = 0;

		typeRegObsoleteDtlId = Long.valueOf(0);
		publisherDivisionId = Long.valueOf(0);
		openCloseRegObsoleteId = Long.valueOf(0);
		
		regulationObsolete.setIsEditable(true);
		
		User userLogin = facesUtil.getUserLogin();
		User userAtasan = userService.getUserByNik(userLogin.getPukNik());
		regulationObsolete.setPicIrg1(userLogin);
		regulationObsolete.setPicIrg2(userAtasan);

		facesUtil.setSessionAttribute("token", null);
	}

	public void handleEdit(String editId) {
		actionMode = Constants.ACTION_EDIT;
		
		String token = facesUtil.retrieveRequestParam("token");
		if (StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));

		Long idLong = Long.parseLong(editId);

		regulationObsolete = internalRegulationObsoleteService.findById(idLong);
		
		typeRegObsoleteDtlId = regulationObsolete.getTipeRegulasiObsolete().getParameterDtlId();
		publisherDivisionId = regulationObsolete.getPublisherDivision().getDivisionId();
		openCloseRegObsoleteId = regulationObsolete.getOpenCloseRegulationObsolete().getParameterDtlId();

		regulationObsolete.setIsEditable(false);
		
		if (regulationObsolete.getInternalRegulationObsoletePicKonversis() != null) {
			lastSequenceOfPickonversi = regulationObsolete.getInternalRegulationObsoletePicKonversis().size();

			for (int i = 0; i < regulationObsolete.getInternalRegulationObsoletePicKonversis().size(); i++) {
				InternalRegulationObsoletePic picKonv = regulationObsolete.getInternalRegulationObsoletePicKonversis()
						.get(i);

				lastSequenceOfPickonversi += 1;
				picKonv.setSequence(lastSequenceOfPickonversi);
				picKonv.setDivisionId(picKonv.getDivision().getDivisionId());
				picKonv.setIsEditable(false);
				
				regulationObsolete.getInternalRegulationObsoletePicKonversis().set(i, picKonv);
			}

			tableModelPickonversi = new InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic>(
					regulationObsolete.getInternalRegulationObsoletePicKonversis());
		} else {
			tableModelPickonversi = new InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic>(
					regulationObsolete.getInternalRegulationObsoletePicKonversis());

			lastSequenceOfPickonversi = 0;
		}

	}

	@Override
	public void itemSelected(String clientId, String widgetVar, Object selectedItem) {
		if (StringUtils.equals("pic1Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user1 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user1 != null) {

				regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi).setUser1(user1);

				User user2 = userService.getUserByNik(user1.getPukNik());
				if (user2 != null) {
					regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi)
							.setUser2(user2);

//					User user3 = userService.getUserByNik(user2.getPukNik());
//					if (user3 != null) {
//						regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi)
//								.setUser3(user3);
//					}
				}

			}
			tableModelPickonversi.setWrappedData(regulationObsolete.getInternalRegulationObsoletePicKonversis());
			
			PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName1");
			PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName2");
			//PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName3");
		}

		if (StringUtils.equals("pic2Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user2 != null) {
				regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi).setUser2(user2);

//				User user3 = userService.getUserByNik(user2.getPukNik());
//				if (user3 != null) {
//					regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi)
//							.setUser3(user3);
//				}
			}

			tableModelPickonversi.setWrappedData(regulationObsolete.getInternalRegulationObsoletePicKonversis());
			
			PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName2");
			//PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName3");
		}

		if (StringUtils.equals("pic3Dialog", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;

			User user3 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (user3 != null) {
				regulationObsolete.getInternalRegulationObsoletePicKonversis().get(indexDtlPicKonversi).setUser3(user3);
			}
			tableModelPickonversi.setWrappedData(regulationObsolete.getInternalRegulationObsoletePicKonversis());

			PrimeFaces.current().ajax().update("form:dataTablePickonversi:"+indexDtlPicKonversi+":userName3");
		}
		
		if(StringUtils.equals("pic2DialogIrg", widgetVar)) {
			Object[] objects = (Object[]) selectedItem;
			
			User irg2 = userService.findById(MathUtil.returnIdObjectToLong(objects[0]));
			if (irg2 != null) {
				regulationObsolete.setPicIrg2(irg2);
			}
			
			
			PrimeFaces.current().ajax().update("form:dataTablePicIrg:");
		}

		PrimeFaces.current().executeScript("reInitSelect2();");
	}

	public void onChangeUnitKerjaPenerbit() {
		String directorateData = userService.getDirectorateByDivisionId(publisherDivisionId);
		regulationObsolete.setPublisherDirectorateName(directorateData);

		PrimeFaces.current().ajax().update("form:internalRegulasiObsoleteDirPenerbit");
	}
	
	public void onAddNewPickonversi() {
		if (regulationObsolete.getInternalRegulationObsoletePicKonversis() == null
				|| regulationObsolete.getInternalRegulationObsoletePicKonversis().size() == 0) {

			regulationObsolete
					.setInternalRegulationObsoletePicKonversis(new ArrayList<InternalRegulationObsoletePic>());

			lastSequenceOfPickonversi = 0;
		} else {
			if (regulationObsolete.getInternalRegulationObsoletePicKonversis().size() == 0) {
				lastSequenceOfPickonversi = 0;
			}
		}

		InternalRegulationObsoletePic rt = new InternalRegulationObsoletePic();
		lastSequenceOfPickonversi = lastSequenceOfPickonversi + 1;
		rt.setSequence(lastSequenceOfPickonversi);
		rt.setIsEditable(true);

		regulationObsolete.getInternalRegulationObsoletePicKonversis().add(rt);

		tableModelPickonversi.setWrappedData(regulationObsolete.getInternalRegulationObsoletePicKonversis());

		PrimeFaces.current().executeScript("reInitSelect2();");
		// RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onDeleteRowPickonversi() {
		for (int i = 0; i < selectedDataPickonversi.length; i++) {
			regulationObsolete.getInternalRegulationObsoletePicKonversis().remove(selectedDataPickonversi[i]);
		}

		if (regulationObsolete.getInternalRegulationObsoletePicKonversis() == null
				|| regulationObsolete.getInternalRegulationObsoletePicKonversis().size() == 0) {
			lastSequenceOfPickonversi = 0;
		}

		tableModelPickonversi.setWrappedData(regulationObsolete.getInternalRegulationObsoletePicKonversis());
		
		
		PrimeFaces.current().executeScript("reInitSelect2();");
		// RequestContext.getCurrentInstance().execute("reInitSelect2();");
	}

	public void onChangeUnitKerja(int statIndex) {
		InternalRegulationObsoletePic data = regulationObsolete.getInternalRegulationObsoletePicKonversis().get(statIndex);
		data.setUser1(null);
		data.setUser2(null);
		data.setUser3(null);
		data.setTargetDate(null);
		
		
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public void clearPicDetail2(int statIndex) {
		regulationObsolete.getInternalRegulationObsoletePicKonversis().get(statIndex).setUser2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public void clearPicDetail3(int statIndex) {
		regulationObsolete.getInternalRegulationObsoletePicKonversis().get(statIndex).setUser3(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}
	
	public void clearPicIrgDetail2() {	
		regulationObsolete.setPicIrg2(null);
		PrimeFaces.current().executeScript("initSelect2();");
	}

	public void onTargetDateChange() {

	}
	
	//unused (for reference only)
	public void sendEmail() {
		try {
			List<InternalRegulationObsoletePic> picKonvEdit = new ArrayList<InternalRegulationObsoletePic>();
			List<SendEmailVo> sendEmailList = new ArrayList<SendEmailVo>();
			
			EmailTemplate emailTemplate = emailTemplateService.getEmailTemplateByEmailTemplateCode("EMAIL_IRG_OBSOLETE");
			
			if(actionMode.equalsIgnoreCase(Constants.ACTION_EDIT)) {
				if(!regulationObsolete.getInternalRegulationObsoletePicKonversis().isEmpty()) {
					for(InternalRegulationObsoletePic picKonv : regulationObsolete.getInternalRegulationObsoletePicKonversis()) {
						if(picKonv.getIsEditable()) {
							picKonvEdit.add(picKonv);
						}
					}
				}
			}
			
			String emailSubject = emailTemplate.getEmailSubject().replaceAll("no_random", regulationObsolete.getNomorObsolete());
			String emailContent = emailTemplate.getEmailContent();
			
			if(actionMode.equalsIgnoreCase(Constants.ACTION_ADD)) {
				if(!regulationObsolete.getInternalRegulationObsoletePicKonversis().isEmpty()) {
					for(InternalRegulationObsoletePic picKonv : regulationObsolete.getInternalRegulationObsoletePicKonversis()) {
						if(picKonv.getIsEditable()) {
							SendEmailVo sendEmail = new SendEmailVo();
							
							String emailTo = "", emailCc = "", emailCc1 = "", emailCc2 = "";
							
							emailContent = emailContent.replaceAll("nama_pic", picKonv.getUser1().getName());
							
							emailTo = picKonv.getUser1() != null ? picKonv.getUser1().getEmail() : null;
							emailCc = picKonv.getUser2() != null ? picKonv.getUser2().getEmail() : null;
							emailCc2 = picKonv.getUser3() != null ? picKonv.getUser3().getEmail() : null;
							
							if(StringUtils.isNotEmpty(emailCc1)) {
								emailCc = emailCc.concat(emailCc1);
							}
							if(StringUtils.isNotEmpty(emailCc2)) {
								emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2) : emailCc.concat(emailCc2);
							}
							
							sendEmail.setEmailTo(emailTo);
							sendEmail.setEmailCc(emailCc);
							sendEmail.setSubject(emailSubject);
							sendEmail.setContent(emailContent);
							
							emailContent = emailTemplate.getEmailContent();
							
							sendEmailList.add(sendEmail);
						}
					}
				}
			}else {
				if(!picKonvEdit.isEmpty()) {
					for(InternalRegulationObsoletePic picKonv : picKonvEdit) {
						SendEmailVo sendEmail = new SendEmailVo();
						
						String emailTo = "";
						String emailCc = "";
						String emailCc1 = "";
						String emailCc2 = "";
						
						emailContent = emailContent.replaceAll("nama_pic", picKonv.getUser1().getName());
						
						emailTo = picKonv.getUser1() != null ? picKonv.getUser1().getEmail() : null;
						emailCc = picKonv.getUser2() != null ? picKonv.getUser2().getEmail() : null;
						emailCc2 = picKonv.getUser3() != null ? picKonv.getUser3().getEmail() : null;
						
						if(StringUtils.isNotEmpty(emailCc1)) {
							emailCc = emailCc.concat(emailCc1);
						}
						if(StringUtils.isNotEmpty(emailCc2)) {
							emailCc = StringUtils.isNotEmpty(emailCc) ? emailCc.concat(",").concat(emailCc2) : emailCc.concat(emailCc2);
						}
						
						sendEmail.setEmailTo(emailTo);
						sendEmail.setEmailCc(emailCc);
						sendEmail.setSubject(emailSubject);
						sendEmail.setContent(emailContent);
						
						emailContent = emailTemplate.getEmailContent();
						
						sendEmailList.add(sendEmail);
					}
				}
			}
			
			if(!sendEmailList.isEmpty()) {
				for(SendEmailVo sendEmail : sendEmailList) {
					final String subject = sendEmail.getSubject();
					final String content = sendEmail.getContent();
					final String to = sendEmail.getEmailTo();
					final String cc = sendEmail.getEmailCc();
					
					if(StringUtils.isNotBlank(to)) {
						CallApiManager.sendEmailAPI(to, cc, subject, content, "EMAIL_IRG_OBSOLETE", "true", parameterDetailService);
					}
				}
			}
			
			
		} catch(Exception ex) {
			ex.printStackTrace();
			throw new CustomAPIException(ex.getMessage()); 
		}
	}
 
	public void cancel() {
		try {
			facesUtil.redirect("/pages/internalRegulationObsolete/internalRegulationObsolete.faces");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isBlank(regulationObsolete.getJudulObsolete())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteObsoleteTitle") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		//check if user is editing a existing irg obsolete
		if(regulationObsolete.getIsEditable() == true){	
			if (internalRegulationObsoleteService.checkIRGObsoleteByObsoleteNum(regulationObsolete.getNomorObsolete())) {
				facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteObsoleteNumber") + " "
						+ facesUtil.retrieveMessage("errorDuplicate"));
				flag = true;
			}
		}
		
		
		if (Objects.equals(typeRegObsoleteDtlId, Long.valueOf(0))) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteRegulationType") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isBlank(regulationObsolete.getNomorObsolete())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteObsoleteNumber") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (StringUtils.isBlank(regulationObsolete.getInfoObsolete())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteObsoleteInfo") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (publisherDivisionId == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoletePublisherDivision") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}
		
		if (openCloseRegObsoleteId == null) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoleteOpenClose") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		if (regulationObsolete.getInternalRegulationObsoletePicKonversis() != null) {
			for (int i = 0; i < regulationObsolete.getInternalRegulationObsoletePicKonversis().size(); i++) {
				InternalRegulationObsoletePic pic = regulationObsolete.getInternalRegulationObsoletePicKonversis()
						.get(i);

				if (pic.getDivisionId() == null) {
					facesUtil.addErrMessage(
							facesUtil.retrieveMessage("formInternalRegulationObsoleteConversionPICDivision") + " "
									+ facesUtil.retrieveMessage("validateRequired"));

					flag = true;
					break;
				}

				if (pic.getUser2() == null) {
					facesUtil.addErrMessage(facesUtil.retrieveMessage("formInternalRegulationObsoletePIC2") + " "
							+ facesUtil.retrieveMessage("validateRequired"));

					flag = true;
					break;
				}
				
				if (pic.getTargetDate() == null) {
					facesUtil.addErrMessage(
							facesUtil.retrieveMessage("formInternalRegulationObsoleteConversionTargetDate") + " "
							+ facesUtil.retrieveMessage("validateRequired"));
					
					flag = true;
					break;
				}
			}
		}

		return flag;
	}

	public void save() {
		try {
			if (!validate()) {
				
				regulationObsolete.setTipeRegulasiObsolete(
						parameterDetailService.findById(typeRegObsoleteDtlId));
				regulationObsolete.setPublisherDivision(divisionService.findById(publisherDivisionId));
				
				regulationObsolete.setOpenCloseRegulationObsolete(
						parameterDetailService.findById(openCloseRegObsoleteId));
				
				if (regulationObsolete.getInternalRegulationObsoletePicKonversis() != null) {
					for (int i=0; i<regulationObsolete.getInternalRegulationObsoletePicKonversis().size(); i++) {
						InternalRegulationObsoletePic pic = 
								regulationObsolete.getInternalRegulationObsoletePicKonversis().get(i);
						
						pic.setDivision(divisionService.findById(pic.getDivisionId()));
						pic.setInternalRegulasiObsolete(regulationObsolete);

						if (StringUtils.isEmpty(pic.getCreatedBy())) {
							pic.setCreatedBy(facesUtil.retrieveUserLogin());
							pic.setCreationDate(new Timestamp(System.currentTimeMillis()));
							pic.setEnabledFlag("Y");
							pic.setDelId(Long.valueOf(0));
						}else {
							pic.setLastUpdateBy(facesUtil.retrieveUserLogin());
							pic.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
						}	
						
						if(pic.getInternalRegObsoletePicKonvId() == null) { //ADD NEW IRG OBSOLETE
							pic.setRegulationObsoleteEmailTmps(new ArrayList<InternalRegulationObsoleteEmailTmp>());
							
							InternalRegulationObsoleteEmailTmp picEmail = new InternalRegulationObsoleteEmailTmp();
							picEmail.setIrgObsoletePic(pic);
							picEmail.setEmailDate(pic.getTargetDate());
							
							//uncomment if counter type/ tipe pengingat is used
//							picEmail.setSlaType(null);
//							picEmail.setSla(null);
							
							picEmail.setCreatedBy(facesUtil.retrieveUserLogin());
							picEmail.setCreationDate(new Timestamp(System.currentTimeMillis()));
							picEmail.setEnabledFlag(Constants.CONSTANT_YES);
							picEmail.setDelId(Long.valueOf(0));
							picEmail.setSlaType(InternalRegulationObsoleteConstants.REMINDER_H_PLUS_0);
							
							pic.getRegulationObsoleteEmailTmps().add(picEmail);
							//ADD a new list for IRG Obsolete Email Tmps for D-2
							InternalRegulationObsoleteEmailTmp picEmailDayMinus2 = new InternalRegulationObsoleteEmailTmp();
							
							picEmailDayMinus2.setIrgObsoletePic(pic);
							
							//check if d-2 is a holiday
							Calendar calendar = Calendar.getInstance();
							Date targetDateTmpMinus2 = pic.getTargetDate();
							int counterDate = 0;
							calendar.setTime(targetDateTmpMinus2);
							
							while (counterDate < 2) {
								
								calendar.setTime(targetDateTmpMinus2);
								calendar.add(Calendar.DAY_OF_MONTH, -1);
								targetDateTmpMinus2 = calendar.getTime();
								
								int day = calendar.get(Calendar.DAY_OF_WEEK);
								//System.out.println("Calendar Day: "+day);
								
								if (day == 1 || day == 7) {
									// do nothing
								} else {
									if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmpMinus2))) {
										counterDate++;
										//System.out.println("Counter Date: " + counterDate);
									}
								}
								
							}
							
							picEmailDayMinus2.setEmailDate(new Timestamp(targetDateTmpMinus2.getTime()));
							
							picEmailDayMinus2.setCreatedBy(facesUtil.retrieveUserLogin());
							picEmailDayMinus2.setCreationDate(new Timestamp(System.currentTimeMillis()));
							picEmailDayMinus2.setEnabledFlag(Constants.CONSTANT_YES);
							picEmailDayMinus2.setDelId(Long.valueOf(0));;
							picEmailDayMinus2.setSlaType(InternalRegulationObsoleteConstants.REMINDER_H_MINUS_2);
							
							pic.getRegulationObsoleteEmailTmps().add(picEmailDayMinus2);
						}else { //UPDATE EXISTING IRG OBSOLETE
							if(pic.getIsEditable()) { //ADD NEW EMAIL WITH NEW PIC
								InternalRegulationObsoleteEmailTmp picEmail = new InternalRegulationObsoleteEmailTmp();
								
								picEmail.setIrgObsoletePic(pic);
								picEmail.setEmailDate(pic.getTargetDate());
								
								
								picEmail.setCreatedBy(facesUtil.retrieveUserLogin());
								picEmail.setCreationDate(new Timestamp(System.currentTimeMillis()));
								picEmail.setEnabledFlag(Constants.CONSTANT_YES);
								picEmail.setDelId(Long.valueOf(0));
								
								pic.getRegulationObsoleteEmailTmps().add(picEmail);
								
								//ADD a new list for IRG Obsolete Email Tmps for D-2
								InternalRegulationObsoleteEmailTmp picEmailDayMinus2 = new InternalRegulationObsoleteEmailTmp();
								
								picEmailDayMinus2.setIrgObsoletePic(pic);
								
								//check if d-2 is a holiday
								Calendar calendar = Calendar.getInstance();
								Date targetDateTmpMinus2 = pic.getTargetDate();
								int counterDate = 0;
								calendar.setTime(targetDateTmpMinus2);
								
								while (counterDate < 2) {
									
									calendar.setTime(targetDateTmpMinus2);
									calendar.add(Calendar.DAY_OF_MONTH, -1);
									targetDateTmpMinus2 = calendar.getTime();
									
									int day = calendar.get(Calendar.DAY_OF_WEEK);
									//System.out.println("Calendar Day: "+day);
									
									if (day == 1 || day == 7) {
										// do nothing
									} else {
										if (Boolean.TRUE.equals(holidayService.isAvailableDate(targetDateTmpMinus2))) {
											counterDate++;
											//System.out.println("Counter Date: " + counterDate);
										}
									}
									
								}
								
								picEmailDayMinus2.setEmailDate(new Timestamp(targetDateTmpMinus2.getTime()));
								
								picEmailDayMinus2.setCreatedBy(facesUtil.retrieveUserLogin());
								picEmailDayMinus2.setCreationDate(new Timestamp(System.currentTimeMillis()));
								picEmailDayMinus2.setEnabledFlag(Constants.CONSTANT_YES);
								picEmailDayMinus2.setDelId(Long.valueOf(0));;
								picEmailDayMinus2.setSlaType(InternalRegulationObsoleteConstants.REMINDER_H_MINUS_2);
								
								pic.getRegulationObsoleteEmailTmps().add(picEmailDayMinus2);
								
							}
						}
					}
				}
				
				if (regulationObsolete.getInternalRegulasiObsoleteId() != null) {
					regulationObsolete.setLastUpdateBy(facesUtil.retrieveUserLogin());
					regulationObsolete.setLastUpdateDate(new Timestamp(System.currentTimeMillis()));
					internalRegulationObsoleteService.update(regulationObsolete);
				} else {
					regulationObsolete.setCreatedBy(facesUtil.retrieveUserLogin());
					regulationObsolete.setCreationDate(new Timestamp(System.currentTimeMillis()));
					regulationObsolete.setDelId(Long.valueOf(0));
					regulationObsolete.setEnabledFlag("Y");
					internalRegulationObsoleteService.save(regulationObsolete);
				}

				facesUtil.redirect("/pages/internalRegulationObsolete/internalRegulationObsolete.faces");
			}
		} catch (Exception e) {
			e.printStackTrace();
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + e.getMessage(), "");
		}
	}

	// GETTER SETTER
	public String getActionMode() {
		return actionMode;
	}

	public void setActionMode(String actionMode) {
		this.actionMode = actionMode;
	}
	
	public Boolean getIsViewOnly() {
		return isViewOnly;
	}

	public void setIsViewOnly(Boolean isViewOnly) {
		this.isViewOnly = isViewOnly;
	}

	public Integer getLastSequenceOfPickonversi() {
		return lastSequenceOfPickonversi;
	}

	public void setLastSequenceOfPickonversi(Integer lastSequenceOfPickonversi) {
		this.lastSequenceOfPickonversi = lastSequenceOfPickonversi;
	}

	public Long getTypeRegObsoleteDtlId() {
		return typeRegObsoleteDtlId;
	}

	public void setTypeRegObsoleteDtlId(Long typeRegObsoleteDtlId) {
		this.typeRegObsoleteDtlId = typeRegObsoleteDtlId;
	}

	public Long getPublisherDivisionId() {
		return publisherDivisionId;
	}

	public void setPublisherDivisionId(Long publisherDivisionId) {
		this.publisherDivisionId = publisherDivisionId;
	}

	public List<SelectItem> getTypeRegObsoletes() {
		return typeRegObsoletes;
	}

	public void setTypeRegObsoletes(List<SelectItem> typeRegObsoletes) {
		this.typeRegObsoletes = typeRegObsoletes;
	}

	public List<SelectItem> getCounterTypes() {
		return counterTypes;
	}

	public void setCounterTypes(List<SelectItem> counterTypes) {
		this.counterTypes = counterTypes;
	}

	public InternalRegulationObsolete getRegulationObsolete() {
		return regulationObsolete;
	}

	public void setRegulationObsolete(InternalRegulationObsolete regulationObsolete) {
		this.regulationObsolete = regulationObsolete;
	}

	public InternalRegulationObsoletePic[] getSelectedDataPickonversi() {
		return selectedDataPickonversi;
	}

	public void setSelectedDataPickonversi(InternalRegulationObsoletePic[] selectedDataPickonversi) {
		this.selectedDataPickonversi = selectedDataPickonversi;
	}

	public InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic> getTableModelPickonversi() {
		return tableModelPickonversi;
	}

	public void setTableModelPickonversi(
			InternalRegulationObsoletePicTableModel<InternalRegulationObsoletePic> tableModelPickonversi) {
		this.tableModelPickonversi = tableModelPickonversi;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public CounterTypeService getCounterTypeService() {
		return counterTypeService;
	}

	public void setCounterTypeService(CounterTypeService counterTypeService) {
		this.counterTypeService = counterTypeService;
	}

	public List<SelectItem> getUnitKerjas() {
		return unitKerjas;
	}

	public void setUnitKerjas(List<SelectItem> unitKerjas) {
		this.unitKerjas = unitKerjas;
	}

	public List<SelectItem> getOpenCloseRegulationObsoletes() {
		return openCloseRegulationObsoletes;
	}

	public void setOpenCloseRegulationObsoletes(List<SelectItem> openCloseRegulationObsoletes) {
		this.openCloseRegulationObsoletes = openCloseRegulationObsoletes;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public DivisionService getDivisionService() {
		return divisionService;
	}

	public void setDivisionService(DivisionService divisionService) {
		this.divisionService = divisionService;
	}

	public EmailTemplateService getEmailTemplateService() {
		return emailTemplateService;
	}

	public void setEmailTemplateService(EmailTemplateService emailTemplateService) {
		this.emailTemplateService = emailTemplateService;
	}

	public InternalRegulationObsoleteService getInternalRegulationObsoleteService() {
		return internalRegulationObsoleteService;
	}

	public void setInternalRegulationObsoleteService(
			InternalRegulationObsoleteService internalRegulationObsoleteService) {
		this.internalRegulationObsoleteService = internalRegulationObsoleteService;
	}

	public SelectorInfo getSelectorUser1() {
		return selectorUser1;
	}

	public void setSelectorUser1(SelectorInfo selectorUser1) {
		this.selectorUser1 = selectorUser1;
	}

	public SelectorInfo getSelectorUser2() {
		return selectorUser2;
	}

	public void setSelectorUser2(SelectorInfo selectorUser2) {
		this.selectorUser2 = selectorUser2;
	}

	public SelectorInfo getSelectorUser3() {
		return selectorUser3;
	}

	public void setSelectorUser3(SelectorInfo selectorUser3) {
		this.selectorUser3 = selectorUser3;
	}

	public SelectorInfo getSelectorIrg2() {
		return selectorIrg2;
	}

	public void setSelectorIrg2(SelectorInfo selectorIrg2) {
		this.selectorIrg2 = selectorIrg2;
	}

	public String getTextWarningUpload() {
		return textWarningUpload;
	}

	public void setTextWarningUpload(String textWarningUpload) {
		this.textWarningUpload = textWarningUpload;
	}

	public Integer getIndexDtlPicKonversi() {
		return indexDtlPicKonversi;
	}

	public void setIndexDtlPicKonversi(Integer indexDtlPicKonversi) {
		this.indexDtlPicKonversi = indexDtlPicKonversi;
	}

	public Integer getIndexDtlFollowupIrg() {
		return indexDtlFollowupIrg;
	}

	public void setIndexDtlFollowupIrg(Integer indexDtlFollowupIrg) {
		this.indexDtlFollowupIrg = indexDtlFollowupIrg;
	}

	public Long getOpenCloseRegObsoleteId() {
		return openCloseRegObsoleteId;
	}

	public void setOpenCloseRegObsoleteId(Long openCloseRegObsoleteId) {
		this.openCloseRegObsoleteId = openCloseRegObsoleteId;
	}

	public HolidayService getHolidayService() {
		return holidayService;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}
	
	
}