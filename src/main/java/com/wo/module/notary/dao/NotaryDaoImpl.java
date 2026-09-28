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
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

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
		sb.append("        ct.STATUS, ct.TANGGAL_PENSIUN, ct.TANGGAL_BERAKHIR_PKS, ct.JENIS_PENGAJUAN ");
		sb.append("   FROM WO_MST_NOTARY ct ");
		sb.append("        INNER JOIN WO_MST_PARAMETER_DTL dtl ON ct.NOTARY_CATEGORY = dtl.PARAMETER_DTL_CODE ");
		sb.append("  WHERE 1=1 ");
		sb.append("        AND ct.ENABLED_FLAG = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY NOTARY_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

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
				
				//Long notaryId = MathUtil.returnIdObjectToLong(obj[0]);
				//data = findById(notaryId);
				
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
