package com.wo.module.responsibility.bean;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.wo.module.common.bean.CommonBean;
import com.wo.module.common.constant.Constants;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.menu.model.Menu;
import com.wo.module.menu.service.MenuService;
import com.wo.module.responsibility.constant.ResponsibilityConstants;
import com.wo.module.responsibility.model.Responsibility;
import com.wo.module.responsibility.model.ResponsibilityDetail;
import com.wo.module.responsibility.service.ResponsibilityService;
import com.wo.module.responsibility.vo.ResponsibilityDtlVO;

public class ResponsibilityEditBean extends CommonBean implements Serializable {

	private static final long serialVersionUID = 1L;
	static Logger logger = Logger.getLogger(ResponsibilityEditBean.class);

	private Responsibility responsibility;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	private List<ResponsibilityDtlVO> responsibilityMenuList;

	private Boolean isViewOnly;

	private String actionMode;

	private String editedId;

	private ResponsibilityService responsibilityService;

	private MenuService menuService;

	public FacesUtil facesUtil;

	public int indexCounter;
	public int parentIndex;

	private String navigateSearch = ResponsibilityConstants.NAVIGATE_SEARCH;

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
		checkNewOrEdit();
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
				searchResponsibilityMenuAndPopulateTree(null);
			} else {
				this.handleEdit(editId);

				searchResponsibilityMenuAndPopulateTree(new Long(editId));
			}
		} catch (Exception e) {

		}

	}

	public void searchResponsibilityMenuAndPopulateTree(Long responsibility_id) throws ClassCastException, Exception {
		try {
			ResponsibilityDtlVO responsibilityMenuVO = new ResponsibilityDtlVO();

			responsibilityMenuVO.setResponsibility_id(responsibility_id);

			responsibilityMenuList = responsibilityService.searchResponsibilityMenuAllMenu(responsibilityMenuVO);

			responsibilityMenuList = setRealChildListSize(responsibilityMenuList);

			indexCounter = 0;
			root = new DefaultTreeNode("root", null);
			root.setExpanded(true);
			populateTree(responsibilityMenuList, root);

		} catch (Exception ex) {
			FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Operation Failed : " + ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, errorMessage);
			logger.error(ex.getMessage(), ex);
			throw ex;
		}
	}

	private void populateTree(List<ResponsibilityDtlVO> responsibilityMenuListRaw, TreeNode rootNode) throws Exception {
		try {

			for (int index = 0; (responsibilityMenuListRaw != null
					&& index < responsibilityMenuListRaw.size()); index++) {
				ResponsibilityDtlVO responsibilityMenuVO = (ResponsibilityDtlVO) responsibilityMenuListRaw.get(index);

				TreeNode treeNode = new DefaultTreeNode(responsibilityMenuVO, rootNode);
				treeNode.setExpanded(true);
				if (responsibilityMenuVO.getCheck()) {
					treeNode.setSelected(true);
				} else {
					/*
					 * if (StringUtils.isNotBlank(responsibilityMenuVO.getMenu_action()) &&
					 * responsibilityMenuVO.getMenu_action().equals(LoginConstants.COMPLETE_HOME_URL
					 * )) { treeNode.setSelected(true); }
					 */
				}
				populateTree(responsibilityMenuVO.getChildResponsibilityMenuList(), treeNode);
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	private List<ResponsibilityDtlVO> setRealChildListSize(List<ResponsibilityDtlVO> initialList) {
		if (initialList != null) {
			for (int a = 0; a < initialList.size(); a++) {
				ResponsibilityDtlVO aVo = (ResponsibilityDtlVO) initialList.get(a);
				List<ResponsibilityDtlVO> aNextList = aVo.getChildResponsibilityMenuList();
				int aSize = 0;
				if (aNextList != null) {
					aSize += aNextList.size();
					for (int b = 0; b < aNextList.size(); b++) {
						ResponsibilityDtlVO bVo = (ResponsibilityDtlVO) aNextList.get(b);
						List<ResponsibilityDtlVO> bNextList = bVo.getChildResponsibilityMenuList();
						int bSize = 0;
						if (bNextList != null) {
							aSize += bNextList.size();
							bSize = bNextList.size();
							for (int c = 0; c < bNextList.size(); c++) {
								ResponsibilityDtlVO cVo = (ResponsibilityDtlVO) bNextList.get(c);
								List<ResponsibilityDtlVO> cNextList = cVo.getChildResponsibilityMenuList();
								int cSize = 0;
								if (cNextList != null) {
									aSize += cNextList.size();
									bSize += cNextList.size();
									cSize = cNextList.size();
									for (int d = 0; d < cNextList.size(); d++) {
										ResponsibilityDtlVO dVo = (ResponsibilityDtlVO) cNextList.get(d);
										List<ResponsibilityDtlVO> dNextList = dVo.getChildResponsibilityMenuList();
										int dSize = 0;
										if (dNextList != null) {
											aSize += dNextList.size();
											bSize += dNextList.size();
											cSize += dNextList.size();
											dSize = dNextList.size();
											for (int e = 0; e < dNextList.size(); e++) {
												ResponsibilityDtlVO eVo = (ResponsibilityDtlVO) dNextList.get(e);
												List<ResponsibilityDtlVO> eNextList = eVo
														.getChildResponsibilityMenuList();
												int eSize = 0;
												if (eNextList != null) {
													aSize += eNextList.size();
													bSize += eNextList.size();
													cSize += eNextList.size();
													dSize += eNextList.size();
													eSize = eNextList.size();
												}
												eVo.setChildResponsibilityMenuListSize(eSize);
												dNextList.set(e, eVo);
											}
										}
										dVo.setChildResponsibilityMenuListSize(dSize);
										cNextList.set(d, dVo);
									}
								}
								cVo.setChildResponsibilityMenuListSize(cSize);
								bNextList.set(c, cVo);
							}
						}
						bVo.setChildResponsibilityMenuListSize(bSize);
						aNextList.set(b, bVo);
					}
				}
				aVo.setChildResponsibilityMenuListSize(aSize);
				initialList.set(a, aVo);
			}
		}
		return initialList;
	}

	private void handleNew() {
		responsibility = new Responsibility();
		actionMode = Constants.ACTION_ADD;
		facesUtil.setSessionAttribute("token", null);
	}

	private void handleEdit(String editId) {
		String token = facesUtil.retrieveRequestParam("token");
		if(StringUtils.isNotEmpty(token)) {
			editId = Constants.decryptString(token);
		}
		facesUtil.setSessionAttribute("token", Constants.encryptString(editId));
		actionMode = Constants.ACTION_EDIT;
		Long idLong = Long.parseLong(editId);
		responsibility = responsibilityService.findById(idLong);
	}

	public Boolean validate() {
		Boolean flag = false;

		if (StringUtils.isEmpty(responsibility.getName())) {
			facesUtil.addErrMessage(facesUtil.retrieveMessage("formResponsibilityResponsibility") + " "
					+ facesUtil.retrieveMessage("validateRequired"));
			flag = true;
		}

		return flag;
	}

	private TreeNode[] addElement(TreeNode[] org, TreeNode added) {
		TreeNode[] result = Arrays.copyOf(org, org.length + 1);
		result[org.length] = added;
		return result;
	}

	private void extractTreeSelectable(TreeNode rootNode, int i) throws Exception {
		try {
			if (i == 0)
				selectedNodes = new TreeNode[0];
			for (int index = 0; (rootNode != null && index < rootNode.getChildCount()); index++) {
				TreeNode responsibilityMenuTree = (TreeNode) rootNode.getChildren().get(index);

				if (responsibilityMenuTree.isSelected())
					selectedNodes = addElement(selectedNodes, responsibilityMenuTree);
				extractTreeSelectable(responsibilityMenuTree, 1);
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	public void save() {
		try {
			if (!validate()) {
				saveResponsibilityMenu();

				facesUtil.redirect("/pages/responsibility/responsibility.faces");
			}

		} catch (Exception ex) {
			facesUtil.addFacesMsg(FacesMessage.SEVERITY_ERROR, null, "Operation Failed : " + ex.getMessage(), "");
		}

	}

	public String saveResponsibilityMenu() throws Exception {
		String result = null;
		try {
			logger.debug("ResponseMenuEditBean saveResponsibilityMenu");
			List<ResponsibilityDtlVO> newResponsibilityMenuList = new ArrayList<ResponsibilityDtlVO>();
			// newResponsibilityMenuList = extractTree(newResponsibilityMenuList,getRoot());
			extractTreeSelectable(getRoot(), 0);
			if (getSelectedNodes() != null) {
				for (TreeNode tn : getSelectedNodes()) {
					ResponsibilityDtlVO responsibilityMenuVO = (ResponsibilityDtlVO) tn.getData();
					newResponsibilityMenuList.add(responsibilityMenuVO);
				}
			}

			ResponsibilityDtlVO vo = new ResponsibilityDtlVO();
			vo.setResponsibility_id(responsibility.getResponsibilityId());
			vo.setChildResponsibilityMenuList(newResponsibilityMenuList);

			responsibilityService.deleteInsertResponsibilityMenu(vo, facesUtil.retrieveUserLogin());

			Set<Long> requiredParentId = new HashSet<Long>();
			Set<Long> existingParentId = new HashSet<Long>();
			if (newResponsibilityMenuList != null && newResponsibilityMenuList.size() > 0) {
				for (ResponsibilityDtlVO dtlVO : newResponsibilityMenuList) {
					Menu menu = menuService.findById(dtlVO.getMenu_id());

					if (menu.getParentId() != null) {
						requiredParentId.add(menu.getParentId());							
					}

					if (menu.getMenuLevel() != null && menu.getMenuLevel().longValue() == 1) {
						existingParentId.add(menu.getMenuId());
					}
				}
			}

			if (existingParentId != null && existingParentId.size() > 0) {
				for (Long id : existingParentId) {
					requiredParentId.remove(id);
				}
			}

			if (newResponsibilityMenuList != null && newResponsibilityMenuList.size() > 0) {

				for (ResponsibilityDtlVO dtlVO : newResponsibilityMenuList) {

					Menu menu = menuService.findById(dtlVO.getMenu_id());
					ResponsibilityDetail newRespDet = new ResponsibilityDetail();
					newRespDet.setMenu(menu);
					newRespDet.setResponsibility(responsibility);
					newRespDet.setCreatedBy(facesUtil.retrieveUserLogin());
					newRespDet.setCreationDate(new Timestamp(new Date().getTime()));
					newRespDet.setLastUpdateBy(facesUtil.retrieveUserLogin());
					newRespDet.setLastUpdateDate(new Timestamp(new Date().getTime()));
					newRespDet.setEnabledFlag(Constants.CONSTANT_YES);
					newRespDet.setDelId(new Long(0));

					responsibility.getDetails().add(newRespDet);

				}
			}

			for (Long id : requiredParentId) {
				Menu menu = menuService.findById(id);
				ResponsibilityDetail newRespDet = new ResponsibilityDetail();
				newRespDet.setMenu(menu);
				newRespDet.setResponsibility(responsibility);
				newRespDet.setCreatedBy(facesUtil.retrieveUserLogin());
				newRespDet.setCreationDate(new Timestamp(new Date().getTime()));
				newRespDet.setLastUpdateBy(facesUtil.retrieveUserLogin());
				newRespDet.setLastUpdateDate(new Timestamp(new Date().getTime()));
				newRespDet.setEnabledFlag(Constants.CONSTANT_YES);
				newRespDet.setDelId(new Long(0));

				responsibility.getDetails().add(newRespDet);
			}

			// flush();

			if (responsibility.getResponsibilityId() != null) {
				responsibility.setLastUpdateBy(facesUtil.retrieveUserLogin());
				responsibility.setLastUpdateDate(new Timestamp(new Date().getTime()));
				responsibility.setDelId(new Long(0));
				responsibility.setEnabledFlag(Constants.CONSTANT_YES);
				responsibilityService.update(responsibility);
			} else {
				responsibility.setCreatedBy(facesUtil.retrieveUserLogin());
				responsibility.setCreationDate(new Timestamp(new Date().getTime()));
				responsibility.setDelId(new Long(0));
				responsibility.setEnabledFlag(Constants.CONSTANT_YES);
				responsibilityService.save(responsibility);
			}

		} catch (

		Exception ex) {
			FacesMessage errorMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Operation Failed : " + ex.getMessage(), ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, errorMessage);
			logger.error(ex.getMessage(), ex);
		}

		return result;
	}

	private void selectUnselectTreeNode(TreeNode rootNode, TreeNode paramNode, boolean selected) {
		try {
			for (int index = 0; (rootNode != null && index < rootNode.getChildCount()); index++) {
				TreeNode responsibilityMenuTree = (TreeNode) rootNode.getChildren().get(index);
				if (responsibilityMenuTree.equals(paramNode)) {
					responsibilityMenuTree.setSelected(selected);
					if (responsibilityMenuTree.getChildren() != null && responsibilityMenuTree.getChildCount() > 0)
						setAllChild(responsibilityMenuTree, selected);
					break;
				}

				selectUnselectTreeNode(responsibilityMenuTree, paramNode, selected);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void setAllChild(TreeNode theNode, boolean selected) throws Exception {
		for (int index = 0; (theNode != null && index < theNode.getChildCount()); index++) {
			TreeNode childNode = (TreeNode) theNode.getChildren().get(index);
			childNode.setSelected(selected);
			setAllChild(childNode, selected);
		}
	}

	public void onNodeSelect(NodeSelectEvent event) {
		selectUnselectTreeNode(getRoot(), event.getTreeNode(), true);
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		selectUnselectTreeNode(getRoot(), event.getTreeNode(), false);
	}

	public void cancel() {
		try {
			facesUtil.redirect("/pages/responsibility/responsibility.faces");
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

	public ResponsibilityService getResponsibilityService() {
		return responsibilityService;
	}

	public void setResponsibilityService(ResponsibilityService responsibilityService) {
		this.responsibilityService = responsibilityService;
	}

	public String getNavigateSearch() {
		return navigateSearch;
	}

	public void setNavigateSearch(String navigateSearch) {
		this.navigateSearch = navigateSearch;
	}

	public Responsibility getResponsibility() {
		return responsibility;
	}

	public void setResponsibility(Responsibility responsibility) {
		this.responsibility = responsibility;
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

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public List<ResponsibilityDtlVO> getResponsibilityMenuList() {
		return responsibilityMenuList;
	}

	public void setResponsibilityMenuList(List<ResponsibilityDtlVO> responsibilityMenuList) {
		this.responsibilityMenuList = responsibilityMenuList;
	}

	public int getIndexCounter() {
		return indexCounter;
	}

	public void setIndexCounter(int indexCounter) {
		this.indexCounter = indexCounter;
	}

	public int getParentIndex() {
		return parentIndex;
	}

	public void setParentIndex(int parentIndex) {
		this.parentIndex = parentIndex;
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

}