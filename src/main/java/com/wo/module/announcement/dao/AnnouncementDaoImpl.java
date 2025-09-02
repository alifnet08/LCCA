package com.wo.module.announcement.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.announcement.constant.AnnouncementConstant;
import com.wo.module.announcement.model.Announcement;
import com.wo.module.announcement.vo.AnnouncementVo;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;

@Repository("announcementDao")
public class AnnouncementDaoImpl extends GenericDAOHibernate<Announcement, Long>
	implements AnnouncementDao, Serializable{

	private static final long serialVersionUID = 53606887720545782L;

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_ANNOUNCEMENT_TITLE, col)) {
						sb.append(" AND a.ANNOUNCEMENT_TITLE LIKE :announcementTitle ");
					}
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_PERIOD_START, col)) {
//						sb.append(" AND TO_DATE(a.PERIOD_START, 'yyyy-mm-dd') >= TO_DATE(:periodStart, 'yyyy-mm-dd') ");
						sb.append(" AND :periodStart <= TO_CHAR(a.PERIOD_START, 'yyyy-mm-dd') ");
					}
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_PERIOD_END, col)) {
//						sb.append(" AND TO_DATE(a.PERIOD_END, 'yyyy-mm-dd') <= TO_DATE(:periodEnd, 'yyyy-mm-dd') ");
						sb.append(" AND :periodEnd >= TO_CHAR(a.PERIOD_END, 'yyyy-mm-dd') ");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_ANNOUNCEMENT_TITLE, col)) {
						String announcementTitle = "%"+val+"%";
						query.setParameter("announcementTitle", announcementTitle);
					}
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_PERIOD_START, col)) {
						query.setParameter("periodStart", val);
					}
					if (StringUtils.equals(AnnouncementConstant.SEARCH_BY_PERIOD_END, col)) {
						query.setParameter("periodEnd", val);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<AnnouncementVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<AnnouncementVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<AnnouncementVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT a.ANNOUNCEMENT_ID ANNOUNCEMENT_ID ");
		sb.append("       ,a.ANNOUNCEMENT_TITLE ANNOUNCEMENT_TITLE ");
		sb.append("       ,a.PERIOD_START PERIOD_START ");
		sb.append("       ,TO_CHAR(a.PERIOD_START, 'DD MON YYYY') PERIOD_START_STR ");
		sb.append("       ,a.PERIOD_END PERIOD_END ");
		sb.append("       ,TO_CHAR(a.PERIOD_END, 'DD MON YYYY') PERIOD_END_STR ");
		sb.append("       ,a.ANNOUNCEMENT_CONTENT ANNOUNCEMENT_CONTENT ");
		sb.append(" FROM WO_MST_ANNOUNCEMENT a ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.ENABLED_FLAG <> 'N' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY a.ANNOUNCEMENT_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List result = query.getResultList();
		List<AnnouncementVo> vo = new ArrayList<AnnouncementVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				AnnouncementVo data = new AnnouncementVo();
				
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
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_MST_ANNOUNCEMENT a ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND a.ENABLED_FLAG <> 'N' ");
		this.getQueryWhereString(sb, searchCriteria);
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetString(query, searchCriteria);
		
		return (Number) query.getSingleResult();
	}
	
}
