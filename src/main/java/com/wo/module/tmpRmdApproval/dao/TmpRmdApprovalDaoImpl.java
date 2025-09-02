/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmdApproval.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
//import com.wo.module.tmpRmd.constant.TmpRmdConstants;
import com.wo.module.tmpRmdApproval.constant.TmpRmdApprovalConstants;
import com.wo.module.tmpRmdApproval.vo.TmpRmdApprovalSearchVo;
import com.wo.module.user.dao.UserDao;

/**
 *
 * @author hendra
 */
@Repository("tmpRmdApprovalDao")
public class TmpRmdApprovalDaoImpl extends GenericDAOHibernate<TmpRmdApprovalSearchVo, Long> implements TmpRmdApprovalDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TmpRmdApprovalDaoImpl.class);

	@Autowired
    @Qualifier("userDao")
    private UserDao userDao;
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_REPORT_TYPE, col)) {
						sb.append(" and rt.report_type_id = :reportType ");
					}

					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_REPORT_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r1.report_name_en LIKE :reportName ");
						} else {
							sb.append(" and r1.report_name_in LIKE :reportName ");
						}
					}

					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_STATUS, col)) {
						sb.append(" and r1.status = :status ");
					}
					
					

					/*
					 * if (StringUtils.equals(RmdConstants.SEARCH_BY_NAME, col)) { if (locale !=
					 * null && locale.equals(locale.ENGLISH)) { sb.append(" and name LIKE :name ");
					 * } else { sb.append(" and name LIKE :name "); } }
					 */
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_REPORT_TYPE, col)) {
						query.setParameter("reportType", val);
					}

					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_REPORT_NAME, col)) {
						query.setParameter("reportName", "%" + val + "%");
					}

					if (StringUtils.equals(TmpRmdApprovalConstants.SEARCH_BY_STATUS, col)) {					
						query.setParameter("status", "" + val + "");						
					}
					
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
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
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" left join wo_mst_report_type rt on rt.report_type_id = r1.report_type_id ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");		
		sb.append(" left join wo_trc_rmd r2 on r2.rmd_id = r1.rmd_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");		
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<TmpRmdApprovalSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {

		List<TmpRmdApprovalSearchVo> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpRmdApprovalSearchVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.rmd_id, r1.report_name_en,  r1.report_name_in,  ");
		sb.append(" rt.report_type_en,  rt.report_type_in, pic1.name as PIC1_NAME, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" picConfirmation.name as PIC_CONFIRMATION_NAME, r2.confirmation_date, r2.followup_date, r2.followup_note, r1.status  ");
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" left join wo_mst_report_type rt on rt.report_type_id = r1.report_type_id ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");		
		sb.append(" left join wo_trc_rmd r2 on r2.rmd_id = r1.rmd_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.status = 'DATA_NEW' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.rmd_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpRmdApprovalSearchVo> vo = new ArrayList<TmpRmdApprovalSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpRmdApprovalSearchVo data = new TmpRmdApprovalSearchVo();
				//data.setRmdId(((BigInteger) obj[0]).longValue());
				data.setRmdId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setReportNameEn((String) obj[1]);
				data.setReportNameIn((String) obj[2]);
				data.setReportTypeNameEn((String) obj[3]);
				data.setReportTypeNameIn((String) obj[4]);
				data.setPic1((String) obj[5]);
				data.setStatusNameEn((String) obj[6]);
				data.setStatusNameIn((String) obj[7]);
				data.setFollowupStatusNameEn((String) obj[8]);
				data.setFollowupStatusNameIn((String) obj[9]);
				data.setPicConfirmationName((String) obj[10]);

				if (obj[11] != null) {
					data.setConfirmationDate((Date) obj[11]);
				}

				if (obj[12] != null) {
					data.setFollowupDate((Date) obj[12]);
				}

				data.setFollowupNote((String) obj[13]);
				data.setStatusCode((String) obj[14]);
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	
	
}
