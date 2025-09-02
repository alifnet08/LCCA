package com.wo.module.notaryFE.dao;

import java.io.Serializable;
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
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.notaryFE.constant.NotaryFEConstant;
import com.wo.module.notaryFE.vo.NotaryFEVo;
import com.wo.module.qaFE.constant.QAFEConstant;

@Repository("notaryFEDao")
public class NotaryFEDaoImpl extends GenericDAOHibernate<Notary, Long>
	implements NotaryFEDao, Serializable{

	private static final long serialVersionUID = -8336192214997728397L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
//						sb.append(" and (UPPER(notary_name) like UPPER(:notaryName) OR UPPER(area) like UPPER(:notaryName) OR UPPER(WORK_AREA) like UPPER(:notaryName) ) ");
						sb.append(" and upper(n.NOTARY_NAME) like upper(:notaryName) ");
					}
					if (StringUtils.equals(QAFEConstant.SEARCH_BY_CATEGORY, col)) {
						sb.append(" and n.NOTARY_CATEGORY = :category ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA, col)) {
						sb.append(" and upper(n.AREA) like upper(:area) ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("notaryName", "%" + val + "%");
					}
					if (StringUtils.equals(QAFEConstant.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("category",val);
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA, col)) {
						query.setParameter("area", "%" + val + "%");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<NotaryFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<NotaryFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<NotaryFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT n.NOTARY_ID ");
		sb.append("       ,n.NOTARY_CATEGORY ");
		sb.append("       ,pdCat.NAME_IN CAT_IN ");
		sb.append("       ,pdCat.NAME_EN CAR_EN ");
		sb.append("       ,n.AREA ");
		sb.append("       ,n.ADDRESS ");
		sb.append("       ,n.AREA_CODE ");
		sb.append("       ,n.PHONE_NO ");
		sb.append("       ,n.FAX_NO ");
		sb.append("       ,n.EMAIL ");
		sb.append("       ,n.MOBILE_NO ");
		sb.append("       ,n.NOTARY_NAME ");
		sb.append("       ,n.NOTE ");
		sb.append(" FROM WO_MST_NOTARY n ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append(" 		ON pdCat.PARAMETER_DTL_CODE = n.NOTARY_CATEGORY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND n.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY n.NOTARY_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<NotaryFEVo> vo = new ArrayList<NotaryFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				NotaryFEVo data = new NotaryFEVo();
				
				data.setNotaryId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setNotaryCategoryCode(obj[1] != null ? (String) obj[1] : null);
				data.setNotaryCategoryIn(obj[2] != null ? (String) obj[2] : null);
				data.setNotaryCategoryEn(obj[3] != null ? (String) obj[3] : null);
				data.setArea(obj[4] != null ? (String) obj[4] : null);
				data.setAddress(obj[5] != null ? (String) obj[5] : null);
				data.setAreaCode(obj[6] != null ? (String) obj[6] : null);
				data.setPhoneNo(obj[7] != null ? (String) obj[7] : null);
				data.setFaxNo(obj[8] != null ? (String) obj[8] : null);
				data.setEmail(obj[9] != null ? (String) obj[9] : null);
				data.setMobileNo(obj[10] != null ? (String) obj[10] : null);
				data.setNotaryName(obj[11] != null ? (String) obj[11] : null);
				data.setNote(obj[12] != null ? (String) obj[12] : null);
				
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
		
		sb.append(" SELECT COUNT(1)");
		sb.append(" FROM WO_MST_NOTARY n ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL pdCat ");
		sb.append(" 		ON pdCat.PARAMETER_DTL_CODE = n.NOTARY_CATEGORY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND n.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

}
