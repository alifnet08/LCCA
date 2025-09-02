package com.wo.module.occupation.dao;

import java.io.Serializable;
import java.text.ParseException;
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
import com.wo.module.common.util.DateUtil;
import com.wo.module.common.util.MathUtil;
import com.wo.module.occupation.constant.OccupationConstants;
import com.wo.module.occupation.model.Occupation;
import com.wo.module.occupation.vo.OccupationVO;

@Repository("occupationDao")
public class OccupationDaoImpl extends GenericDAOHibernate<Occupation, Long> 
implements OccupationDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5702249341530593593L;
	static Logger logger = Logger.getLogger(OccupationDaoImpl.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<OccupationVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		
		List<OccupationVO> occupationVOList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return occupationVOList;
	}

	@SuppressWarnings("rawtypes")
	private List<OccupationVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fo.occupation_id, fo.occupation_name, fo.risk_rating ");
		sb.append("FROM wo_mst_fcc_occupation fo ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fo.enabled_flag = 'Y' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append("ORDER BY fo.occupation_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		this.getQuerySetValue(query, searchCriteria);
		
		List resultList = query.getResultList();
		
		List<OccupationVO> occupationVOList = new ArrayList<OccupationVO>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				OccupationVO occupationVO = new OccupationVO();
				
				occupationVO.setOccupationId(MathUtil.returnIdObjectToLong(obj[0]));
				occupationVO.setOccupation(obj[1].toString());
				occupationVO.setRiskRating(obj[2].toString());
				
				occupationVOList.add(occupationVO);
			}
		}
		
		return occupationVOList;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(OccupationConstants.SEARCH_BY_NAME, col)) {
							sb.append(" AND UPPER(occupation_name) LIKE :name ");	
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
					if (StringUtils.equals(OccupationConstants.SEARCH_BY_NAME, col)) {
						query.setParameter("name", "%" + val.toUpperCase() + "%");
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		
		Number result = searchCountDataCriteria(searchCriteria);
		if(result == null) {
			result = 0;
		}
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_mst_fcc_occupation fo ");
		sb.append(" where 1=1 ");
		sb.append(" and fo.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Occupation> findAll() throws ParseException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fo.occupation_id, fo.occupation_name, fo.risk_rating, "
				+ "fo.created_by, fo.creation_date, fo.enabled_flag ");
		sb.append("FROM wo_mst_fcc_occupation fo ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fo.enabled_flag = 'Y' ");
		sb.append("ORDER BY fo.occupation_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = null;
		resultList = query.getResultList();
		
		List<Occupation> occupationList = new ArrayList<Occupation>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Occupation occupation = new Occupation();
				
				occupation.setOccupationId(MathUtil.returnIdObjectToLong(obj[0]));
				occupation.setOccupationName(obj[1].toString());
				occupation.setRiskRating(obj[2].toString());
				
				occupation.setCreatedBy(obj[3].toString());
				Date creationDate = DateUtil.stringToDateFromYYYYMMDD(obj[4].toString());
				occupation.setCreationDate(DateUtil.dateToSqlTimestamp(creationDate));
				occupation.setEnabledFlag(obj[5].toString());
				
				occupationList.add(occupation);
			}
		}
		
		return occupationList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getFileName() {
		try {
			String fileName="";
			StringBuilder sb = new StringBuilder();
			
			//need 'SELECT UPLOAD_DATE' so 'ORDER BY UPLOAD_DATE' could work
			sb.append("SELECT DISTINCT fo.FILE_NAME,fo.UPLOAD_DATE ");
			sb.append("FROM WO_MST_FCC_OCCUPATION fo ");
			sb.append("WHERE 1=1 ");
			sb.append("AND fo.ENABLED_FLAG = 'Y' ");
			sb.append("AND TRUNC(fo.UPLOAD_DATE) <= CURRENT_TIMESTAMP ");
			sb.append("ORDER BY fo.UPLOAD_DATE DESC");
			
			Query query = getSession().createSQLQuery(sb.toString());
			List resultList = query.getResultList();
			
			//IF there are results
			if(resultList.size() > 0) {
				//get first row since last upload date is on first row
				Object[] firstResult = (Object[]) resultList.get(0);
				
				fileName = firstResult[0].toString();
				
				return fileName;
			//IF there are no results
			}else {
				return "FCC_Occupation_Template";
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
			return "FCC_Occupation_Template";
		}
	}

	@Override
	public void deleteAll() {
		String queryStr = "DELETE FROM WO_MST_FCC_OCCUPATION fo WHERE fo.ENABLED_FLAG = 'Y'";
		
		Query query = getSession().createSQLQuery(queryStr);
		
		query.executeUpdate();
	}
	
}
