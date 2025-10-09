package com.wo.module.announcementViewFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcementViewFE.vo.AnnouncementViewFEVo;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

@Repository("announcementViewFEDao")
public class AnnouncementViewFEDaoImpl extends GenericDAOHibernate<Announcement, Long>
	implements AnnouncementViewFEDao, Serializable{

	private static final long serialVersionUID = -395370951917793816L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND a.ANNOUNCEMENT_TITLE LIKE :textBox ");
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
	public List<AnnouncementViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<AnnouncementViewFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<AnnouncementViewFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT a.ANNOUNCEMENT_ID ");
		sb.append("       ,a.ANNOUNCEMENT_TITLE ");
		sb.append("       ,a.PERIOD_START ");
		sb.append("       ,TO_CHAR(a.PERIOD_START, 'dd FMMonth yyyy', 'nls_date_language=indonesian') PERIOD_START_STR ");
		sb.append("       ,a.PERIOD_END ");
		sb.append("       ,TO_CHAR(a.PERIOD_END, 'dd FMMonth yyyy', 'nls_date_language=indonesian') PERIOD_END_STR ");
		sb.append("       ,a.ANNOUNCEMENT_CONTENT ");
		sb.append(" FROM WO_MST_ANNOUNCEMENT a ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY a.ANNOUNCEMENT_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<AnnouncementViewFEVo> vo = new ArrayList<AnnouncementViewFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				AnnouncementViewFEVo data = new AnnouncementViewFEVo();
				
				data.setAnnouncementId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setAnnouncementTitle(obj[1] != null ? (String) obj[1] : null);
				data.setPeriodStart(obj[2] != null ? (Date) obj[2] : null);
				data.setPeriodStartStr(obj[3] != null ? (String) obj[3] : null);
				data.setPeriodEnd(obj[4] != null ? (Date) obj[4] : null);
				data.setPeriodEndStr(obj[5] != null ? (String) obj[5] : null);
				data.setAnnouncementContent(obj[6] != null ? (String) obj[6] : null);
				
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
	public Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_ANNOUNCEMENT a ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.ENABLED_FLAG = 'Y' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}
}
