package com.wo.module.outgoingLetterView.dao;

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
import com.wo.module.outgoingLetter.constant.OutgoingLetterConstants;
import com.wo.module.outgoingLetterView.model.OutgoingLetterView;

@Repository("outgoingLetterViewDao")
public class OutgoingLetterViewDaoImpl extends GenericDAOHibernate<OutgoingLetterView, Long> 
	implements OutgoingLetterViewDao{

	@Autowired
	@Qualifier("outgoingLetterAttachmentViewDao")
	private OutgoingLetterAttachmentViewDao outgoingLetterAttachmentViewDao;
	
	public OutgoingLetterAttachmentViewDao getOutgoingLetterAttachmentViewDao() {
		return outgoingLetterAttachmentViewDao;
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(OutgoingLetterAttachmentViewDaoImpl.class);
	
	public void setOutgoingLetterAttachmentViewDao(OutgoingLetterAttachmentViewDao outgoingLetterAttachmentViewDao) {
		this.outgoingLetterAttachmentViewDao = outgoingLetterAttachmentViewDao;
	}

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
							sb.append(" and ol.letter_purpose_en like '%" + val + "%' ");
						} else {
							sb.append(" and ol.letter_purpose_in like '%" + val + "%' ");
						}
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_LETTER_NO, col)) {
						sb.append(" and ol.letter_no like '%" + val + "%' ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and ol.perihal_en like '%" + val + "%' ");
						} else {
							sb.append(" and ol.perihal_in like '%" + val + "%' ");
						}
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_DELIVERED_TO, col)) {
						sb.append(" and ol.delivered_to like '%" + val + "%' ");
					} else if (StringUtils.equals(OutgoingLetterConstants.WHERE_TEMBUSAN, col)) {
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
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<OutgoingLetterView> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<OutgoingLetterView> voList = searchDataCriteria(searchCriteria,first,pageSize);
		
		return voList;
	}

	@SuppressWarnings("rawtypes")
	private List<OutgoingLetterView> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("	select ol.outgoing_letter_id"
				+ "		,ol.letter_purpose_in, ol.letter_purpose_en"
				+ "		,ol.letter_no "
				+ "		,ol.perihal_in, ol.perihal_en"
				+ "		,ol.delivered_to, ol.tembusan "
				+ "		,ol.letter_date "
				+ "		,TO_CHAR(ol.letter_date, 'dd-Mon-yyyy') letter_date1"
				+ "	from wo_mst_outgoing_letter ol "
				+ "	where 1 = 1 and ol.enabled_flag = 'Y' ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		sb.append("	order by ol.letter_date desc,ol.letter_no desc");
		//sb.append("	limit " + first + ", " + pageSize + " ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		result.setFirstResult(first);
		result.setMaxResults(pageSize);
		
		List resultList = result.getResultList();
		
		List<OutgoingLetterView> vo = new ArrayList<OutgoingLetterView>();
		
		if(resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				OutgoingLetterView data = new OutgoingLetterView();
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
					data.setOutgoingLetterAttachmentView(outgoingLetterAttachmentViewDao.getOutgoingLetterAttachmentViewByOutgoingLetterId(data.getOutgoingLetterId()));
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
				+ "	where 1=1 and ol.enabled_flag = 'Y' ");
		
		sb = getQueryWhereString(sb, searchCriteria);
		Query result = getSession().createSQLQuery(sb.toString());
		
		return (Number) result.getSingleResult();
	}

}
