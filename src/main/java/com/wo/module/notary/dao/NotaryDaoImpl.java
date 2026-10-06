/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.notary.dao;

import java.sql.Timestamp;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.notary.constant.NotaryConstants;
import com.wo.module.notary.model.Notary;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

/**
 *
 * @author hendra
 */
@Repository("notaryDao")
public class NotaryDaoImpl extends GenericDAOHibernate<Notary, Long> implements NotaryDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(NotaryDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA, col)) {
						
							sb.append(" and UPPER(area) like UPPER(:area) ");
					
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_NOTARY_NAME, col)) {
							sb.append(" and UPPER(notary_name) like UPPER(:notaryName) ");
						
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA_CODE, col)) {
						sb.append(" and UPPER(area_code) like UPPER(:areaCode) ");
					
				}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_CATEGORY, col)) {
						sb.append(" and UPPER(dtl.NAME_IN) like UPPER(:categoryName) ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_STATUS, col)) {
						sb.append(" and ct.STATUS = :status ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_USER_PENGAJU, col)) {
						sb.append(" and UPPER(ct.USER_PENGAJU) = UPPER(:userPengaju) ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_MAKER_TASK, col)) {
						sb.append(" and ct.STATUS in ('revision', 'waiting approval CDU Checker', ");
						sb.append("'waiting approval Legal', 'waiting approval SPV Legal', 'rejected') ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_INBOX_OR_REJECTED, col)) {
						sb.append(" and (ct.STATUS = :inboxStatus or ct.STATUS = '");
						sb.append(NotaryConstants.STATUS_REJECTED);
						sb.append("') ");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_LEGAL_TASK, col)) {
						sb.append(" and (ct.STATUS = '");
						sb.append(NotaryConstants.STATUS_WAITING_APPROVAL_LEGAL);
						sb.append("' or ct.STATUS = '");
						sb.append(NotaryConstants.STATUS_REJECTED);
						sb.append("' or (ct.STATUS = '");
						sb.append(NotaryConstants.STATUS_REVISION);
						sb.append("' and exists (select 1 from WO_MST_NOTARY_HISTORY h ");
						sb.append(" where h.NOTARY_ID = ct.NOTARY_ID and h.ENABLED_FLAG = 'Y' ");
						sb.append(" and h.STATUS = '");
						sb.append(NotaryConstants.HISTORY_REVISION_SPV_TO_LEGAL);
						sb.append("' and h.NOTARY_HISTORY_ID = (select max(h2.NOTARY_HISTORY_ID) ");
						sb.append(" from WO_MST_NOTARY_HISTORY h2 where h2.NOTARY_ID = ct.NOTARY_ID ");
						sb.append(" and h2.ENABLED_FLAG = 'Y')))) ");
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
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA, col)) {
						query.setParameter("area", "%" + val + "%");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_NOTARY_NAME, col)) {
						query.setParameter("notaryName", "%" + val + "%");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_AREA_CODE, col)) {
						query.setParameter("areaCode", "%" + val + "%");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_CATEGORY, col)) {
						query.setParameter("categoryName", "%" + val + "%");
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_STATUS, col)) {
						query.setParameter("status", val);
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_USER_PENGAJU, col)) {
						query.setParameter("userPengaju", val);
					}
					if (StringUtils.equals(NotaryConstants.SEARCH_BY_INBOX_OR_REJECTED, col)) {
						query.setParameter("inboxStatus", val);
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
		sb.append(" select count(1) ");
		sb.append(" from wo_mst_notary ct ");
		sb.append(" inner join wo_mst_parameter_dtl dtl on ct.notary_category = dtl.parameter_dtl_code ");
		sb.append(" and dtl.parameter_code = :notaryCategoryHeader ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("notaryCategoryHeader", ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<Notary> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<Notary> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<Notary> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ct.NOTARY_ID, ct.NOTARY_CATEGORY, dtl.NAME_IN NOTARY_CATEGORY_NAME, ");
		sb.append("		   ct.AREA, ct.NOTARY_NAME, ct.ADDRESS, ct.AREA_CODE, ct.PHONE_NO, ct.FAX_NO, ");
		sb.append("        ct.EMAIL, ct.MOBILE_NO, ct.WORK_AREA, ct.NOTE, ct.CREATED_BY, ct.CREATION_DATE, ");
		sb.append("        ct.LAST_UPDATE_BY, ct.LAST_UPDATE_DATE, ct.ENABLED_FLAG, ");
		sb.append("        ct.STATUS, ct.TANGGAL_PENSIUN, ct.TANGGAL_BERAKHIR_PKS, ct.JENIS_PENGAJUAN, ");
		sb.append("        ct.NO_PENGAJUAN, ct.USER_PENGAJU, ct.TANGGAL_PENGAJUAN, ");
		sb.append("        (SELECT h.CATATAN_REVISI FROM WO_MST_NOTARY_HISTORY h ");
		sb.append("          WHERE h.NOTARY_HISTORY_ID = (SELECT MAX(h2.NOTARY_HISTORY_ID) ");
		sb.append("            FROM WO_MST_NOTARY_HISTORY h2 ");
		sb.append("           WHERE h2.NOTARY_ID = ct.NOTARY_ID AND h2.ENABLED_FLAG = 'Y' ");
		sb.append("             AND h2.CATATAN_REVISI IS NOT NULL)), ");
		sb.append("        (SELECT h.STATUS FROM WO_MST_NOTARY_HISTORY h ");
		sb.append("          WHERE h.NOTARY_HISTORY_ID = (SELECT MAX(h2.NOTARY_HISTORY_ID) ");
		sb.append("            FROM WO_MST_NOTARY_HISTORY h2 ");
		sb.append("           WHERE h2.NOTARY_ID = ct.NOTARY_ID AND h2.ENABLED_FLAG = 'Y')) ");
		sb.append("   FROM WO_MST_NOTARY ct ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL dtl ON ct.NOTARY_CATEGORY = dtl.PARAMETER_DTL_CODE ");
		sb.append("        AND dtl.PARAMETER_CODE = :notaryCategoryHeader ");
		sb.append("  WHERE 1=1 ");
		sb.append("        AND ct.ENABLED_FLAG = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY NOTARY_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("notaryCategoryHeader", ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);
		query.setFirstResult(first);
		if (pageSize > 0 && pageSize < Integer.MAX_VALUE) {
			query.setMaxResults(pageSize);
		}

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<Notary> vo = new ArrayList<Notary>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Notary data = new Notary();

				data.setNotaryId(obj[0] !=null?MathUtil.returnIdObjectToLong(obj[0]):null);
				ParameterDetail paramCategory = new ParameterDetail();
				paramCategory.setParameterDtlCode(obj[1] !=null?(String)obj[1]:null);
				paramCategory.setNameIn(obj[2] !=null?(String)obj[2]:null);
				data.setNotaryCategory(paramCategory);
				data.setArea(obj[3] !=null?(String)obj[3]:null);
				data.setNotaryName(obj[4] !=null?(String)obj[4]:null);
				data.setAddress(obj[5] !=null?(String)obj[5]:null);
				data.setAreaCode(obj[6] !=null?(String)obj[6]:null);
				data.setPhoneNo(obj[7] !=null?(String)obj[7]:null);
				data.setFaxNo(obj[8] !=null?(String)obj[8]:null);
				data.setEmail(obj[9] !=null?(String)obj[9]:null);
				data.setMobileNo(obj[10] !=null?(String)obj[10]:null);
				data.setWorkArea(obj[11] !=null?(String)obj[11]:null);
				data.setNote(obj[12] !=null?(String)obj[12]:null);
			    data.setCreatedBy(obj[13] !=null?(String)obj[13]:null);
				data.setCreationDate(obj[14] !=null?(Timestamp)obj[14]:null);
				data.setLastUpdateBy(obj[15] !=null?(String)obj[15]:null);
				data.setLastUpdateDate(obj[16] !=null?(Timestamp)obj[16]:null);
				data.setEnabledFlag(obj[17] !=null?(String)obj[17]:null);
				data.setStatus(obj[18] !=null?(String)obj[18]:null);
				if (obj[19] != null) {
					data.setTanggalPensiun((Date) obj[19]);
				}
				if (obj[20] != null) {
					data.setTanggalBerakhirPks((Date) obj[20]);
				}
				data.setJenisPengajuan(obj[21] !=null?(String)obj[21]:null);
				data.setNotaryNo(obj[22] !=null?(String)obj[22]:null);
				data.setUserPengaju(obj[23] !=null?(String)obj[23]:null);
				if (obj[24] != null) {
					data.setTanggalPengajuan((Date) obj[24]);
				}
				data.setCatatanRevisi(obj[25] != null ? obj[25].toString() : null);
				if (obj.length > 26 && obj[26] != null) {
					data.setLatestHistoryStatus(obj[26].toString().trim());
				}
				
				//Long notaryId = MathUtil.returnIdObjectToLong(obj[0]);
				//data = findById(notaryId);
				
				vo.add(data);
			}
		}

		return vo;
	}

	@Override
	public String getLastNoPengajuan(String prefixLike) throws Exception {
		Query query = getSession().createSQLQuery(
				"SELECT MAX(ct.NO_PENGAJUAN) FROM WO_MST_NOTARY ct WHERE ct.NO_PENGAJUAN LIKE :prefixLike ");
		query.setParameter("prefixLike", prefixLike);
		Object result = query.getSingleResult();
		return result != null ? result.toString() : null;
	}

}
