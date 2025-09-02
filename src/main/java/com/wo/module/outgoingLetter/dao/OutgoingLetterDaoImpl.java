package com.wo.module.outgoingLetter.dao;

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

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetter.model.OutgoingLetter;

@Repository("outgoingLetterDao")
public class OutgoingLetterDaoImpl extends GenericDAOHibernate<OutgoingLetter, Long>
	implements OutgoingLetterDao{

	@Autowired
	@Qualifier("outgoingLetterAttachmentDao")
	private OutgoingLetterAttachmentDao outgoingLetterAttachmentDao; 
	
	public OutgoingLetterAttachmentDao getOutgoingLetterAttachmentDao() {
		return outgoingLetterAttachmentDao;
	}

	public void setOutgoingLetterAttachmentDao(OutgoingLetterAttachmentDao outgoingLetterAttachmentDao) {
		this.outgoingLetterAttachmentDao = outgoingLetterAttachmentDao;
	}

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(OutgoingLetterDaoImpl.class);
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_PURPOSE, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(ol.letter_purpose_en) like UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(ol.letter_purpose_in) like UPPER('%" + val + "%') ");
						}
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_NO, col)) {
						sb.append(" and UPPER(ol.letter_no) like UPPER('%" + val + "%') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(ol.perihal_en) like UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(ol.perihal_in) like UPPER('%" + val + "%') ");
						}
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_DELIVERED_TO, col)) {
						sb.append(" and UPPER(ol.delivered_to) like UPPER('%" + val + "%') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_TEMBUSAN, col)) {
						sb.append(" and UPPER(ol.tembusan) like UPPER('%" + val + "%') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_DATE_FROM, col)) {
						sb.append(" and TRUNC(ol.letter_date) >=  TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_DATE_TO, col)) {
						sb.append(" and TRUNC(ol.letter_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_DIVISION, col)) {
						sb.append(" and ol.user_division_id = '"+ val +"' ");
					}else if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = '"+ val +"')) ");
					}
				}
			}
		}
		
		return sb;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<OutgoingLetter> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		
		List<OutgoingLetter> voList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<OutgoingLetter> searchDataCriteria(List<? extends SearchObject> searchCriteria
			,int first, int pageSize){
		
		StringBuilder sb = new StringBuilder();
		sb.append("	select ol.outgoing_letter_id"
				+ "		,ol.letter_purpose_in, ol.letter_purpose_en"
				+ "		,ol.letter_no "
				+ "		,ol.perihal_in, ol.perihal_en"
				+ "		,ol.delivered_to, ol.tembusan "
				+ "		,ol.letter_date "
				+ "		,TO_CHAR(ol.letter_date, 'dd-Mon-yyyy') letter_date1"
				+ "	from wo_mst_outgoing_letter ol "
				+"     INNER JOIN WO_MST_USER u "
				+"         ON u.nik = ol.CREATED_BY "
				+ "	where 1 = 1 and ol.enabled_flag = 'Y' ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		sb.append("	order by ol.letter_date desc,ol.letter_no desc ");
		//sb.append("	limit " + first + ", " + pageSize + " ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		List resultList = result.getResultList();
		
		List<OutgoingLetter> vo = new ArrayList<OutgoingLetter>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				OutgoingLetter data = new OutgoingLetter();
				data.setOutgoingLetterId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterPurposeIn(obj[1] != null ? (String) obj[1] : null);
				data.setLetterPurposeEn(obj[2] != null ? (String) obj[2] : null);
				data.setLetterNo(obj[3] != null ? (String) obj[3] : null);
				data.setPerihalIn(obj[4] != null ? (String) obj[4] : null);
				data.setPerihalEn(obj[5] != null ? (String) obj[5] : null);
				data.setDeliveredTo(obj[6] != null ? (String) obj[6] : null);
				data.setTembusan(obj[7] != null ? (String) obj[7] : null);
				data.setLetterDate(obj[8] != null ? (Date) obj[8] : null);
				data.setLetterDateStr(obj[9] != null ? (String) obj[9] : null);
				
				try {
					data.setOutgoingLetterAttachments(outgoingLetterAttachmentDao.getOutgoingLetterAttachmentByOutgoingLetterId(data.getOutgoingLetterId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				vo.add(data);
			}
		}
		
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		
		Number results = searchCountDataCriteria(searchCriteria);
		if(results == null) {
			results = 0;
		}
		return results.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	select count(1) "
				+ "	from wo_mst_outgoing_letter ol "
				+"     INNER JOIN WO_MST_USER u "
				+"         ON u.nik = ol.CREATED_BY "
				+ "	where 1=1 and ol.enabled_flag = 'Y' ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		Query result = getSession().createSQLQuery(sb.toString());
		
		return (Number) result.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<OutgoingLetter> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("	select ol.outgoing_letter_id "
				+ "		,ol.letter_purpose_in, ol.letter_purpose_en"
				+ "		,ol.letter_no "
				+ "		,ol.perihal_in, ol.perihal_en"
				+ "		,ol.delivered_to, ol.tembusan "
				+ "		,ol.letter_date "
				+ "		,TO_CHAR(ol.letter_date, 'dd-Mon-yyyy') letter_date1"
				+ " from wo_mst_outgoing_letter ol "
				+ "	where 1=1 and ol.enabled_flag = 'Y' ");
		
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append("	order by ol.outgoing_letter_id desc ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		List resulList = result.getResultList();
		
		List<OutgoingLetter> vo = new ArrayList<OutgoingLetter>();
		
		if (resulList != null) {
			for (int i = 0; i < resulList.size(); i++) {
				Object[] obj = (Object[]) resulList.get(i);
				OutgoingLetter data = new OutgoingLetter();
				data.setOutgoingLetterId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterPurposeIn(obj[1] != null ? (String) obj[1] : null);
				data.setLetterPurposeEn(obj[2] != null ? (String) obj[2] : null);
				data.setLetterNo(obj[3] != null ? (String) obj[3] : null);
				data.setPerihalIn(obj[4] != null ? (String) obj[4] : null);
				data.setPerihalEn(obj[5] != null ? (String) obj[5] : null);
				data.setDeliveredTo(obj[6] != null ? (String) obj[6] : null);
				data.setTembusan(obj[7] != null ? (String) obj[7] : null);
				data.setLetterDate(obj[8] != null ? (Date) obj[8] : null);
				data.setLetterDateStr(obj[9] != null ? (String) obj[9] : null);
				
				vo.add(data);
			}
		}
		return vo;
	}
	
	@SuppressWarnings({"rawtypes", "static-access" })
	private StringBuilder getQueryWhereXLSString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		
		if(searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString() != null ? searchVal.getSearchValueAsString() : "";
				
				if(!StringUtils.isBlank(val)) {
					if(StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_PURPOSE, val)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and ol.letter_purpose_en like '%" + val + "%' ");
						} else {
							sb.append(" and ol.letter_purpose_in like '%" + val + "%' ");
						}
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_NO, val)) {
						sb.append(" and ol.letter_no like '%" + val + "%' ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_PERIHAL, val)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and ol.perihal_en like '%" + val + "%' ");
						} else {
							sb.append(" and ol.perihal_in like '%" + val + "%' ");
						}
//					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_DELIVERED_TO, val)) {
//						sb.append(" and ol.delivered_to like '%" + val + "%' ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_TEMBUSAN, val)) {
						sb.append(" and ol.tembusan like '%" + val + "%' ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_DATE_FROM, col)) {
						sb.append(" and TRUNC(ol.letter_date) >=  TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_DATE_TO, col)) {
						sb.append(" and TRUNC(ol.letter_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_DIVISION, col)) {
						sb.append(" and ol.user_division_id = '"+ val +"' ");
					}
				}
			}
		}
		
		return sb;
	}

	@Override
	public Integer getOutgoingLetterByLetterInAndNo(String letterIn, String letterNo) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_mst_outgoing_letter ");
		sb.append("		WHERE letter_purpose_in = '"+ letterIn +"' ");
		sb.append("			AND letter_no = '" + letterNo +"' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue() ;
	}

	@Override
	public Integer getOutgoingLetterByLetterInAndNo(Long id, String letterIn, String letterNo) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)"
				+ "		FROM wo_mst_outgoing_letter ");
		sb.append("		WHERE outgoing_letter_id <> '" + id + "'");
		sb.append("			AND letter_purpose_in = '" +letterIn+ "' ");
		sb.append("			AND letter_no = '" +letterNo+ "' ");
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}
	
}