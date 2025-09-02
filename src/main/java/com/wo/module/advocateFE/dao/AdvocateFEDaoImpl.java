/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.advocateFE.dao;

//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.advocate.model.Advocate;
import com.wo.module.advocate.model.AdvocateInfo;
import com.wo.module.advocateFE.constant.AdvocateFEConstants;
import com.wo.module.advocateFE.vo.AdvocateFEVO;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

/**
 *
 * @author hendra
 */
@Repository("advocateFEDao")
public class AdvocateFEDaoImpl extends GenericDAOHibernate<Advocate, Long> implements AdvocateFEDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AdvocateFEDaoImpl.class);
	

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
//							sb.append(" and (UPPER(ct.region) LIKE UPPER(:textVal) ");
//							sb.append(" or UPPER(ct.branch) LIKE UPPER(:textVal) ");
//							sb.append(" or UPPER(ct.advocate_name) LIKE UPPER(:textVal)) ");
//					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" and upper(ct.advocate_name) like upper (:advocateName) ");
					}
					if (StringUtils.equals(AdvocateFEConstants.SEARCH_CABANG, col)) {
						sb.append(" and upper(ct.branch) like upper (:cabang) ");
					}
					if (StringUtils.equals(AdvocateFEConstants.SEARCH_PARTNER, col)) {
						sb.append("     AND exists ( ");
						sb.append("                     select 1 ");
						sb.append("                     from WO_MST_ADVOCATE_PARTNERS partner ");
						sb.append("                     where 1 = 1 ");
						sb.append("                         AND partner.ADVOCATE_ID = ct.ADVOCATE_ID ");
						sb.append("                         AND upper(partner.PARTNERS) like upper(:partner) ");
						sb.append("                ) ");
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
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
//						query.setParameter("textVal", "%" + val + "%");
//					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("advocateName", "%" + val + "%");
					}
					if (StringUtils.equals(AdvocateFEConstants.SEARCH_CABANG, col)) {
						query.setParameter("cabang", "%" + val + "%");
					}
					if (StringUtils.equals(AdvocateFEConstants.SEARCH_PARTNER, col)) {
						query.setParameter("partner", "%" + val + "%");
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
		sb.append(" from wo_mst_advocate ct ");
		sb.append("	       LEFT JOIN WO_MST_ADVOCATE_INFO info	 ");
		sb.append("	          ON ct.ADVOCATE_ID = info.ADVOCATE_ID	 ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");
		//sb.append("  AND info.ADVOCATE_INFO_ID IN (SELECT MIN(i.ADVOCATE_INFO_ID) FROM WO_MST_ADVOCATE_INFO i) ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<AdvocateFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {

		List<AdvocateFEVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<AdvocateFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT ");
		sb.append(" ct.ADVOCATE_ID, ");
		sb.append("	ct.REGION, ");
		sb.append("	ct.BRANCH, ");
		sb.append("	ct.NO, ");
		sb.append("	ct.ADVOCATE_NAME, ");
		sb.append("	(SELECT "
				+ "		LISTAGG(partners,', ') "
				+ "		WITHIN GROUP "
				+ "		(ORDER BY partners) item_name_list "
				+ "	 FROM WO_MST_ADVOCATE_PARTNERS "
				+ "		WHERE advocate_id = ct.advocate_id) PARTNERS, ");
		sb.append("	ct.WEBSITE, ");
		sb.append("	ct.NOTE ");
		sb.append(" FROM WO_MST_ADVOCATE ct ");
		//sb.append("LEFT JOIN WO_MST_ADVOCATE_INFO info	ON ct.ADVOCATE_ID = info.ADVOCATE_ID ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" AND ct.enabled_flag = 'Y' ");
		//sb.append("  		AND info.ADVOCATE_INFO_ID IN (SELECT MIN(i.ADVOCATE_INFO_ID) FROM WO_MST_ADVOCATE_INFO i) ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY ct.ADVOCATE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<AdvocateFEVO> vo = new ArrayList<AdvocateFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				AdvocateFEVO data = new AdvocateFEVO();

				Long advId = MathUtil.returnIdObjectToLong(obj[0]);
				data.setAdvocateId(advId);
				data.setRegion(obj[1]!=null?(String) obj[1]:null);
				data.setBranch(obj[2]!=null?(String) obj[2]:null);
				
				Long no = MathUtil.returnIdObjectToLong(obj[3]);
				data.setNo(no.toString());
				
				data.setAdvocateName(obj[4]!=null?(String) obj[4]:null);
				data.setPartners(obj[5]!=null?(String) obj[5]:null);
				data.setWebsite(obj[6]!=null?(String) obj[6]:null);
				
				//WHY THEY ADD ADVOCATE INFO IF THEYRE NOT USING IT ON SEARCH PAGE????
//				AdvocateInfo inf = new AdvocateInfo();
//				Long infId = MathUtil.returnIdObjectToLong(obj[7]);
//				inf.setAdvocateInfoId(infId);
//				inf.setPhoneNo(obj[8]!=null?(String) obj[8]:null);
//				inf.setEmail(obj[9]!=null?(String) obj[9]:null);
//			    
//				data.setNote(obj[10] != null ? (String) obj[10] : null);
				
				data.setNote(obj[7] != null ? (String) obj[7] : null);
				
//				data.setInfo(inf);
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

	

}
