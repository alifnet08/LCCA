package com.wo.module.aboutUsFE.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

import com.wo.module.aboutUsFE.service.AboutUsFEService;
import com.wo.module.aboutUsFE.vo.AboutUsFEVo;
import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.staticPage.model.StaticPage;
import com.wo.module.staticPage.service.StaticPageService;

public class AboutUsFEBean extends CommonBean implements Serializable{

	private static final long serialVersionUID = -4074185265736565826L;
	private static final Logger logger = Logger.getLogger(AboutUsFEBean.class);

	private AboutUsFEService aboutUsFEService;
	private StaticPageService staticPageService;
	
	private OrganigramNode rootNode;
	private OrganigramNode selection;
	
	private String filePath;
	
	private String content;
	
	private boolean zoom = false;
	private int leafNodeConnectorHeight = 0;
	private boolean autoScrollToSelection = false;
	
	private FacesUtil facesUtil;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void init() {
		super.init();
		
		StaticPage staticPage = staticPageService.getStaticPageByCategory("ABOUT_US");
		
		if(staticPage != null){
			this.content = staticPage.getContentIn();
		}
		
		initComponent();
		List<AboutUsFEVo> aboutUsLists = aboutUsFEService.getAllAboutUsFEVoData();
		
		selection = new DefaultOrganigramNode(null, "Tentang LCCA", null);
		
		rootNode = new DefaultOrganigramNode(null, "Tentang LCCA", null);
		rootNode.setCollapsible(false);
		rootNode.setDroppable(false);
		
		Map<Long, AboutUsFEVo> root = new HashMap<Long, AboutUsFEVo>();
		List<Long> idLongList = new ArrayList<Long>();
		List<AboutUsFEVo> result = new ArrayList<AboutUsFEVo>();
		
		for (int i = 0; i < aboutUsLists.size(); i++) {
			AboutUsFEVo data = (AboutUsFEVo) aboutUsLists.get(i);
			addNode(rootNode, data);
			//AboutUsFEVo parent = (AboutUsFEVo) root.get(data.getAboutUsId());
			
			/*if (parent != null) {
				if (parent.getChild() == null) {
					List<AboutUsFEVo> parentList = new ArrayList<AboutUsFEVo>();
					parentList.add(data);
					parent.setChild(parentList);
				} else {
					parent.getChild().add(data);
				}
			} else {
				if (data.getAboutUsId() != null) {
					AboutUsFEVo newData = new AboutUsFEVo();
					newData.setAboutUsId(data.getAboutUsId());
					newData.setOurName(data.getOurName());
					newData.setOurJob(data.getOurJob());
					newData.setPhotoFile(data.getPhotoFile());
					newData.setEmail(data.getEmail());
					newData.setExt(data.getExt());
					newData.setFileId(data.getFileId());
					newData.setFileSize(data.getFileSize());
					newData.setPukId(data.getPukId());
					
					root.put(newData.getAboutUsId(), newData);
					idLongList.add(newData.getAboutUsId());
				}
			}*/
			
//			if(data.getPukId() != null) {
//				AboutUsFEVo parent = (AboutUsFEVo) root.get(data.getPukId());
//				root.put(data.getAboutUsId(), data);
//				if(parent.getChild() == null) {
//					List list = new ArrayList<AboutUsFEVo>();
//					list.add(data);
//					parent.setChild(list);
//				}else {
//					parent.getChild().add(data);
//				}
//			}else {
//				root.put(data.getAboutUsId(), data);
//				idLongList.add(data.getAboutUsId());
//			}
		}
		
//		for (int i = 0; i < idLongList.size(); i++) {
//			result.add(root.get(idLongList.get(i)));
//		}
//		
//		for (int i = 0; i < result.size(); i++) {
//			addNode(rootNode, result.get(i));
//		}
		 
	}

	private void addNode(OrganigramNode parent, AboutUsFEVo data) {
		DefaultOrganigramNode parentNode = new DefaultOrganigramNode("employee", data, parent);
		
		if (data.getChild() != null && data.getChild().size() > 0) {
			for (int i = 0; i < data.getChild().size(); i++) {
				addNode(parentNode, data.getChild().get(i));
			}
		}
	}
	
	public String encryptFilePath(String filePath){
		return Constants.encryptString(filePath);
	}
	
	private void initComponent() {
		try {
			ParameterDetail getFilePath = parameterDetailService.getParameterDetailByParamDtlCode(ParameterDetail.PARAM_DET_CODE_ATTACHMENT_FILE_PATH);
			
			if (getFilePath != null) {
				this.filePath = getFilePath.getName();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FacesUtil getFacesUtil() {
		return facesUtil;
	}

	public void setFacesUtil(FacesUtil facesUtil) {
		this.facesUtil = facesUtil;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	public OrganigramNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(OrganigramNode rootNode) {
		this.rootNode = rootNode;
	}

	public OrganigramNode getSelection() {
		return selection;
	}

	public void setSelection(OrganigramNode selection) {
		this.selection = selection;
	}

	public AboutUsFEService getAboutUsFEService() {
		return aboutUsFEService;
	}

	public void setAboutUsFEService(AboutUsFEService aboutUsFEService) {
		this.aboutUsFEService = aboutUsFEService;
	}

	public boolean isZoom() {
		return zoom;
	}

	public void setZoom(boolean zoom) {
		this.zoom = zoom;
	}

	public int getLeafNodeConnectorHeight() {
		return leafNodeConnectorHeight;
	}

	public void setLeafNodeConnectorHeight(int leafNodeConnectorHeight) {
		this.leafNodeConnectorHeight = leafNodeConnectorHeight;
	}

	public boolean isAutoScrollToSelection() {
		return autoScrollToSelection;
	}

	public void setAutoScrollToSelection(boolean autoScrollToSelection) {
		this.autoScrollToSelection = autoScrollToSelection;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public StaticPageService getStaticPageService() {
		return staticPageService;
	}

	public void setStaticPageService(StaticPageService staticPageService) {
		this.staticPageService = staticPageService;
	}
	
	
}
