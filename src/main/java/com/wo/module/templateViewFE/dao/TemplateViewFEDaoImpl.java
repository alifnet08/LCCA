package com.wo.module.templateViewFE.dao;

import java.io.Serializable;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.template.model.Template;
import com.wo.module.templateViewFE.vo.TemplateDocumentViewFEVo;
import com.wo.module.templateViewFE.vo.TemplateViewFEVo;

@Repository("templateViewFEDao")
public class TemplateViewFEDaoImpl extends GenericDAOHibernate<Template, Long>
	implements TemplateViewFEDao, Serializable{

	private static final long serialVersionUID = 311622776990758404L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" AND t.TEMPLATE_CATEGORY = :comboBox ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND UPPER(t.PERIHAL_IN) LIKE UPPER(:textBox) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query,List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("comboBox", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String textBox = "%"+val+"%";
						query.setParameter("textBox", textBox);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TemplateViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<TemplateViewFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<TemplateViewFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT t.TEMPLATE_ID ");
		sb.append("       ,t.TEMPLATE_CATEGORY ");
		sb.append("       ,pdCat.NAME_IN CAT_IN ");
		sb.append("       ,pdCat.NAME_EN CAT_EN ");
		sb.append("       ,t.TEMPLATE_SUBCATEGORY ");
		sb.append("       ,pdSubCat.NAME_IN SUB_CAT_IN ");
		sb.append("       ,pdSubCat.NAME_EN SUB_CAT_EN ");
		sb.append("       ,t.PERIHAL_IN ");
		sb.append("       ,t.PERIHAL_EN ");
		sb.append(" FROM WO_MST_TEMPLATE t ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append("         ON pdCat.PARAMETER_DTL_CODE = t.TEMPLATE_CATEGORY ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdSubCat ");
		sb.append("         ON pdSubCat.PARAMETER_DTL_CODE = t.TEMPLATE_SUBCATEGORY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND t.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY t.TEMPLATE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<TemplateViewFEVo> vo = new ArrayList<TemplateViewFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				TemplateViewFEVo data = new TemplateViewFEVo();
				
				data.setTemplateId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setTemplateCategoryCode(obj[1] != null ? (String) obj[1] : null);
				data.setTemplateCategoryIn(obj[2] != null ? (String) obj[2] : null);
				data.setTemplateCategoryEn(obj[3] != null ? (String) obj[3] : null);
				data.setTemplateSubCategoryCode(obj[4] != null ? (String) obj[4] : null);
				data.setTemplateSubCategoryIn(obj[5] != null ? (String) obj[5] : null);
				data.setTemplateSubCategoryEn(obj[6] != null ? (String) obj[6] : null);
				data.setPerihalIn(obj[7] != null ? FacesUtil.convertClobToString((Clob)obj[7]) : null);
				data.setPerihalEn(obj[8] != null ? FacesUtil.convertClobToString((Clob)obj[8]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		Number result = searchCountDataCriteria(searchCriteria);
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_TEMPLATE t ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append("         ON pdCat.PARAMETER_DTL_CODE = t.TEMPLATE_CATEGORY ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdSubCat ");
		sb.append("         ON pdSubCat.PARAMETER_DTL_CODE = t.TEMPLATE_SUBCATEGORY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND t.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TemplateDocumentViewFEVo> getTemplateDocByType(Long id, String documentType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT td.TEMPLATE_DOCUMENT_ID DETAIL_ID ");
		sb.append("       ,td.TEMPLATE_ID HEADER_ID ");
		sb.append("       ,td.DOCUMENT_TYPE ");
		sb.append("       ,td.ATTACHMENT_FILE ");
		sb.append("       ,td.FILE_ID ");
		sb.append("       ,td.FILE_SIZE ");
		sb.append(" FROM WO_MST_TEMPLATE_DOCUMENT td ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND td.TEMPLATE_ID = :templateId ");
		sb.append("     AND td.DOCUMENT_TYPE = :documentType ");
		sb.append("     AND td.ENABLED_FLAG = 'Y' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("templateId", id);
		query.setParameter("documentType", documentType);
		
		List result = query.getResultList();
		List<TemplateDocumentViewFEVo> vo = new ArrayList<TemplateDocumentViewFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				TemplateDocumentViewFEVo data = new TemplateDocumentViewFEVo();
				
				data.setTemplateDocumentId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setTemplateId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setDocumentType(obj[2] != null ? (String) obj[2] : null);
				data.setAttachmentFile(obj[3] != null ? (String) obj[3] : null);
				data.setFileId(obj[4] != null ? (String) obj[4] : null);
				data.setFileSize(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
