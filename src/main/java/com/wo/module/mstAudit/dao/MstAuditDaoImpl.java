package com.wo.module.mstAudit.dao;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.mstAudit.constant.MstAuditConstant;
import com.wo.module.mstAudit.model.MstAudit;
import com.wo.module.mstAudit.vo.MstAuditVO;

/**
 *
 * @author Neir Kate
 * 
 */

@Repository("mstAuditDao")
public class MstAuditDaoImpl extends GenericDAOHibernate<MstAudit, Long> implements MstAuditDao, MstAuditConstant {

	@SuppressWarnings({ "static-access", "rawtypes" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(WHERE_AUDIT_FOLLOWUP, col)) {
						sb.append(" and ta.auditor = '" + val + "' ");
					} else if (StringUtils.equals(WHERE_AUDIT_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and ta.audit_template_name_en like '%" + val + "%' ");
						} else {
							sb.append(" and ta.audit_template_name_in like '%" + val + "%' ");
						}
					} else if (StringUtils.equals(WHERE_AUDIT_SCOPE, col)) {
						sb.append(" and ta.scope like '%" + val + "%' ");
					} else if (StringUtils.equals(WHERE_AUDIT_PERIOD_FROM, col)) {
						sb.append(" and TRUNC(ta.audit_date_from) >= TO_DATE('" +  val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(WHERE_AUDIT_PERIOD_TO, col)) {
						sb.append(" and TRUNC(ta.audit_date_to) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}
				}
			}
		}

		return sb;
	}

	@SuppressWarnings("rawtypes")
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {

		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append(" FROM wo_mst_audit ta ");
		sb.append(" WHERE ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);

		Query result = getSession().createSQLQuery(sb.toString());

		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<MstAuditVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<MstAuditVO> voList = searchDataCriteria(searchCriteria, first, pageSize);

		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<MstAuditVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {

		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"	ta.mst_audit_id auditId, " + 
				"	pd1.name_in auditorIn, " + 
				"	pd1.name_en auditorEn," + 
				"	ta.audit_template_name_in auditTemplateNameIn," + 
				"	ta.audit_template_name_en auditTemplateNameEn," +
				"	TO_CHAR(ta.audit_date_from, 'dd-Mon-yyyy') auditDateFrom," + 
				"	TO_CHAR(ta.audit_date_to, 'dd-Mon-yyyy') auditDateTo," +
				"	ta.scope" +
				" FROM wo_mst_audit ta" + 
				" LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" +
				" WHERE 1=1 and ta.enabled_flag = 'Y' ");

		sb = getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY mst_audit_id DESC ");
		

		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();

		List<MstAuditVO> vo = new ArrayList<MstAuditVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				MstAuditVO data = new MstAuditVO();
				// data.setHolidayId(((BigInteger) obj[0]).longValue());
				data.setMstAuditId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setAuditorIn((String) obj[1]);
				data.setAuditorEn((String) obj[2]);
				data.setAuditTemplateNameIn((String) obj[3]);
				data.setAuditTemplateNameEn((String) obj[4]);
				data.setAuditDateFrom((String) obj[5]);
				data.setAuditDateTo((String) obj[6]);
				data.setScope(obj[7] != null ?  FacesUtil.convertClobToString((Clob)obj[7]) : null);

				vo.add(data);
			}
		}

		result.setFirstResult(first);
		result.setMaxResults(pageSize);

		return vo;
	}

	@Override
	public Integer countSameData(String tempalteNameIn) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) "
				+ "	from wo_mst_audit ta "
				+ "	where ta.audit_template_name_in = '" + tempalteNameIn + "'"
				+ " 	and ta.enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if (count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	@Override
	public Integer countSameDataById(Long id, String tempalteNameIn) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) "
				+ "	from wo_mst_audit ta "
				+ "	where ta.mst_audit_id <> '" + id + "'"
				+ "		and ta.audit_template_name_in = '" + tempalteNameIn + "'"
				+ " 	and ta.enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if (count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MstAudit> getAllMstAuditData() {
		String hql = "FROM MstAudit where enabled_flag = 'Y' ";
		Query result = getSession().createQuery(hql);

		return result.getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public MstAuditVO getSingleData(Long mstAuditId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT " + 
				"	ta.mst_audit_id auditId, " + 
				"	pd1.name_in auditorIn, " + 
				"	pd1.name_en auditorEn," + 
				"	ta.audit_template_name_in auditTemplateNameIn," + 
				"	ta.audit_template_name_en auditTemplateNameEn," +
				"	TO_CHAR(ta.audit_date_from, 'dd-Mon-yyyy') auditDateFrom," + 
				"	TO_CHAR(ta.audit_date_to, 'dd-Mon-yyyy') auditDateTo," +
				"	ta.scope, " +
				"	ta.auditor " +
				" FROM wo_mst_audit ta" + 
				" 	LEFT JOIN wo_mst_parameter_dtl pd1 ON pd1.parameter_dtl_code = ta.auditor" +
				" WHERE 1=1 and ta.enabled_flag = 'Y' "+
				"	AND ta.mst_audit_id = '" + mstAuditId + "' ");
		
		sb.append(" ORDER BY mst_audit_id DESC ");

		Query result = getSession().createSQLQuery(sb.toString());
		List resultList = result.getResultList();
		
		MstAuditVO vo = new MstAuditVO();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				// data.setHolidayId(((BigInteger) obj[0]).longValue());
				vo.setMstAuditId(MathUtil.returnIdObjectToLong(obj[0]));
				vo.setAuditorIn((String) obj[1]);
				vo.setAuditorEn((String) obj[2]);
				vo.setAuditTemplateNameIn((String) obj[3]);
				vo.setAuditTemplateNameEn((String) obj[4]);
				vo.setAuditDateFrom((String) obj[5]);
				vo.setAuditDateTo((String) obj[6]);
				vo.setScope(obj[7] != null ?  FacesUtil.convertClobToString((Clob)obj[7]) : null);
				vo.setAuditorCode(obj[8] != null ? (String) obj[8] : null);

			}
		}

		return vo;
		
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	public Boolean isUsedInTransaction(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select audit_id ");
		sb.append(" from wo_tmp_audit ");
		sb.append(" where 1=1 ");
		sb.append(" and mst_audit_id = :mstAuditId ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("mstAuditId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
}
