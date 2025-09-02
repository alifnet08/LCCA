/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigation.constant.LitigationConstants;
import com.wo.module.litigation.model.Litigation;
import com.wo.module.litigationView.constant.LitigationViewConstant;

/**
 *
 * @author hendra
 */
@Repository("litigationDao")
public class LitigationDaoImpl extends GenericDAOHibernate<Litigation, Long> implements LitigationDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(LitigationDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
//						sb.append(" and case_type LIKE :caseType ");
//					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_DEBITUR, col)) {
//						sb.append(" and lawsuit LIKE :lawsuit ");
//					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_NO_PERKARA, col)) {
//						sb.append(" and litigation_no LIKE :litigationNo ");
//					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						sb.append(" AND l.CASE_TYPE = :jenisPerkara ");
					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_DEBITUR, col)) {
//						sb.append(" AND l.DEBITUR LIKE :debitur ");
//					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_NO_PERKARA, col)) {
						sb.append(" AND l.LITIGATION_NO LIKE :noPerkara ");
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_PROGRESS, col)) {
						sb.append(" AND ld.PROGRESS LIKE :progress ");
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_PUTUSAN, col)) {
						sb.append(" AND ld.SENTENCE = :putusan ");
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_UPAYA_HUKUM, col)) {
						sb.append(" AND ld.LEGAL_EFFORT = :upayaHukum ");
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = :userId)) ");
					}
					
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
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
//						query.setParameter("caseType", "%" + val + "%");
//					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_DEBITUR, col)) {
//						query.setParameter("lawsuit", "%" + val + "%");
//					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_NO_PERKARA, col)) {
//						query.setParameter("litigationNo", "%" + val + "%");
//					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_JENIS_PERKARA, col)) {
						query.setParameter("jenisPerkara", val);
					}
//					if (StringUtils.equals(LitigationConstants.SEARCH_BY_DEBITUR, col)) {
//						String debitur = "%"+val+"%";
//						query.setParameter("debitur", debitur);
//					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_NO_PERKARA, col)) {
						String noPerkara = "%"+val+"%";
						query.setParameter("noPerkara", noPerkara);
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_PROGRESS, col)) {
						String progress = "%"+val+"%";
						query.setParameter("progress", progress);
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_PUTUSAN, col)) {
						query.setParameter("putusan", val);
					}
					if (StringUtils.equals(LitigationConstants.SEARCH_BY_UPAYA_HUKUM, col)) {
						query.setParameter("upayaHukum", val);
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
					}
					
				}
			}
		}
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
//		sb.append(" select count(1) ");
//		sb.append(" from wo_mst_litigation ct ");
//		sb.append(" where 1=1 ");
//		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" SELECT COUNT(DISTINCT ld.LITIGATION_ID) COUNT ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append("     INNER JOIN WO_MST_LITIGATION_DTL ld ");
		sb.append("         ON ld.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCaseType ");
		sb.append("         ON pdCaseType.PARAMETER_DTL_CODE = l.CASE_TYPE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdBranchOffice ");
		sb.append("         ON pdBranchOffice.PARAMETER_DTL_CODE = l.BRANCH_OFFICE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdLitigationType ");
		sb.append("         ON pdLitigationType.PARAMETER_DTL_CODE = l.LITIGATION_PLACE ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = l.CREATED_BY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND l.ENABLED_FLAG <> 'N' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Litigation> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Litigation> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Litigation> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT DISTINCT l.LITIGATION_ID LIGATION_ID ");
		sb.append("       ,pdCaseType.NAME_IN JENIS_PERKARA_IN ");
		sb.append("       ,pdCaseType.NAME_EN JENIS_PERKARA_EN ");
		sb.append("       ,l.DEBITUR DEBITUR ");
		sb.append("       ,pdBranchOffice.NAME_IN KANTOR_CABANG_IN ");
		sb.append("       ,pdBranchOffice.NAME_EN KANTOR_CABANG_EN ");
		sb.append("       ,l.LITIGATION_NO NOMOR_PERKARA ");
		sb.append("       ,l.LAWSUIT JENIS_GUGATAN ");
		sb.append("       ,pdLitigationType.NAME_IN DAERAH_PERKARA_IN ");
		sb.append("       ,pdLitigationType.NAME_EN DAERAH_PERKARA_EN ");
		sb.append("       ,l.MATERIAL_CHARGES MATERIAL ");
		sb.append("       ,l.IMMATERIAL_CHARGES IMMATERIAL ");
		sb.append(" FROM WO_MST_LITIGATION l ");
		sb.append("     INNER JOIN WO_MST_LITIGATION_DTL ld ");
		sb.append("         ON ld.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCaseType ");
		sb.append("         ON pdCaseType.PARAMETER_DTL_CODE = l.CASE_TYPE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdBranchOffice ");
		sb.append("         ON pdBranchOffice.PARAMETER_DTL_CODE = l.BRANCH_OFFICE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdLitigationType ");
		sb.append("         ON pdLitigationType.PARAMETER_DTL_CODE = l.LITIGATION_PLACE ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = l.CREATED_BY ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND l.ENABLED_FLAG <> 'N' ");

		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY l.LITIGATION_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Litigation> vo = new ArrayList<Litigation>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Litigation data = new Litigation();

//				Long litigationId = MathUtil.returnIdObjectToLong(obj[0]);
//				data = findById(litigationId);
//				data.setLitigationId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
//				data.setJenisPerkaraIn(obj[1] != null ? (String) obj[1] : null);
//				data.setJenisPerkaraEn(obj[2] != null ? (String) obj[2] : null);
//				data.setDebitur(obj[3] != null ? (String) obj[3] : null);
//				data.setKantorCabangIn(obj[4] != null ? (String) obj[4] : null);
//				data.setKantorCabangEn(obj[5] != null ? (String) obj[5] : null);
//				data.setNoPerkara(obj[6] != null ? (String) obj[6] : null);
//				data.setJenisGugatan(obj[7] != null ? (String) obj[7] : null);
//				data.setDaerahPerkaraIn(obj[8] != null ? (String) obj[8] : null);
//				data.setDaerahPerkaraEn(obj[9] != null ? (String) obj[9] : null);
//				data.setMaterial(obj[10] != null ? MathUtil.returnIdObjectToLong(obj[10]) : null);
//				data.setImmaterial(obj[11] != null ? MathUtil.returnIdObjectToLong(obj[11]) : null);
				
				
				vo.add(data);
			}
		}

//		query.setFirstResult(first);
//		query.setMaxResults(pageSize);

		return vo;
	}

	

}
