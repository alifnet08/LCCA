package com.wo.module.DiscussionFE.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.wo.module.DiscussionFE.constant.DiscussionFEConstant;
import com.wo.module.DiscussionFE.service.DiscussionFEService;
import com.wo.module.DiscussionFE.vo.DiscussionPostFEVo;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.constant.Constants;
import com.wo.module.discussion.model.Discussion;
import com.wo.module.discussion.model.DiscussionPost;
import com.wo.module.discussion.service.DiscussionPostService;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.user.service.UserService;

public class DiscussionFEEditBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -3370561154371759360L;
	private static final Logger logger = Logger.getLogger(DiscussionFEEditBean.class);
	private static final String NAVIGATE_BACK = DiscussionFEConstant.NAVIGATE_DISCUSSION;
	
	private DiscussionFEService discussionFEService;
	private UserService userService;
	private DiscussionPostService discussionPostService;
	
	private Discussion discussion;
	
	private String editId;
	private String komentar;
	
	private Integer rating;
	
	private List<DiscussionPostFEVo> discussionPostLists;
	
	private FacesUtil facesUtil;

	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

	private Integer testFirst;
	
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
		if (facesUtil.retrieveRequestParam("first") != null) {
			testFirst = Integer.parseInt(facesUtil.retrieveRequestParam("first"));
			facesUtil.setSessionAttribute("FIRST_DISC_FE", testFirst);
			facesUtil.setSessionAttribute("BACK_SESSION", false);
		}
		checkNewOrEdit();
	}
	
	private void checkNewOrEdit() {
		this.editId = facesUtil.retrieveRequestParam("id");
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		
		if (StringUtils.isBlank(editId)) {
			// do nothing
		} else {
			handleEdit(editId);
		}
	}

	private void handleEdit(String editId) {
		Long editIdLong = Long.parseLong(editId);
		discussionPostLists = new ArrayList<DiscussionPostFEVo>();
		discussion = discussionFEService.findById(editIdLong);
		
		if(discussion.getThreadRating()!=null){
		rating = Integer.valueOf(discussion.getThreadRating());
		}
		
		discussionPostLists = discussionFEService.getDiscussionPostDataById(discussion.getDiscussionId()) != null ? 
				discussionFEService.getDiscussionPostDataById(discussion.getDiscussionId()) : new ArrayList<DiscussionPostFEVo>();
		
		PrimeFaces.current().executeScript("remove();");
	}
	
	private boolean isValidate() {
		boolean flag = true;
		
		return flag;
	}
	
	public void save() {
		try {
			if (isValidate()) {
				if (discussion.getDiscussionId() != null) {
					
					if (discussion.getDiscussionPosts() == null || discussion.getDiscussionPosts().size() <= 0) {
						discussion.setDiscussionPosts(new ArrayList<DiscussionPost>());
					}
					discussion.getDiscussionPosts().clear();
					
					for (int i = 0; i < discussionPostLists.size(); i++) {
						DiscussionPostFEVo dpFE = (DiscussionPostFEVo) discussionPostLists.get(i);
						DiscussionPost dp = discussionPostService.findById(dpFE.getDiscussionPostId());
						if (dp != null) {
							discussion.getDiscussionPosts().add(dp);							
						}
					}
					
					DiscussionPost dp = new DiscussionPost();
					dp.setDiscussion(discussion);
					if(rating!= null){
					dp.setPostedRating(rating.doubleValue());
					}
					dp.setPostedComment(komentar);
					dp.setPostedDate(new Date());
					dp.setPostedBy(facesUtil.getUserLogin());
					dp.setCreatedBy(facesUtil.retrieveUserLogin());
					dp.setCreationDate(new Timestamp(new Date().getTime()));
					dp.setEnabledFlag(CommonConstants.RECORD_FLAG_YES);
					dp.setDelId(new Long(0));
					discussion.getDiscussionPosts().add(dp);
					
					Long initRating = discussion.getThreadRating() != null ?
							Long.parseLong(discussion.getThreadRating()) : new Long(0);
					if (initRating > 0) {
						if(rating == null){rating = 0;}
						Long r = initRating + rating;
						Double calRating = (double) (r / discussion.getDiscussionPosts().size());
						discussion.setThreadRating(Double.toString(calRating));
					} else {
						if(rating == null){rating = 0;}
						Double calRating = (double) (rating / discussion.getDiscussionPosts().size());
						discussion.setThreadRating(Double.toString(calRating));
						
					}
					
					discussionFEService.update(discussion);
					facesUtil.redirect("/pages/discussionFE/discussionFE.faces");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		try {
			if (facesUtil.retrieveRequestParam("first") != null) {
				facesUtil.setSessionAttribute("BACK_SESSION", true);
			}
			facesUtil.redirect("/pages/discussionFE/discussionFE.faces");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public DiscussionFEService getDiscussionFEService() {
		return discussionFEService;
	}

	public void setDiscussionFEService(DiscussionFEService discussionFEService) {
		this.discussionFEService = discussionFEService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public Discussion getDiscussion() {
		return discussion;
	}

	public void setDiscussion(Discussion discussion) {
		this.discussion = discussion;
	}

	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static String getNavigateBack() {
		return NAVIGATE_BACK;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public List<DiscussionPostFEVo> getDiscussionPostLists() {
		return discussionPostLists;
	}

	public void setDiscussionPostLists(List<DiscussionPostFEVo> discussionPostLists) {
		this.discussionPostLists = discussionPostLists;
	}

	public String getKomentar() {
		return komentar;
	}

	public void setKomentar(String komentar) {
		this.komentar = komentar;
	}

	public DiscussionPostService getDiscussionPostService() {
		return discussionPostService;
	}

	public void setDiscussionPostService(DiscussionPostService discussionPostService) {
		this.discussionPostService = discussionPostService;
	}

	public Integer getTestFirst() {
		return testFirst;
	}

	public void setTestFirst(Integer testFirst) {
		this.testFirst = testFirst;
	}
}
