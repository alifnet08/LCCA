package com.wo.module.economySector.dao;

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
import com.wo.module.economySector.constant.EconomySectorConstants;
import com.wo.module.economySector.model.EconomySector;
import com.wo.module.economySector.vo.EconomySectorVO;

@Repository("economySectorDao")
public class EconomySectorDaoImpl extends GenericDAOHibernate<EconomySector, Long> 
implements EconomySectorDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5702249341530593593L;
	static Logger logger = Logger.getLogger(EconomySectorDaoImpl.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<EconomySectorVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		
		List<EconomySectorVO> economySectorVOList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return economySectorVOList;
	}

	@SuppressWarnings("rawtypes")
	private List<EconomySectorVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fes.economy_sector_id, "
				+ "fes.economy_sector_name, fes.risk_rating ");
		sb.append("FROM wo_mst_fcc_economy_sector fes ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fes.enabled_flag = 'Y' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append("ORDER BY fes.economy_sector_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		this.getQuerySetValue(query, searchCriteria);
		
		List resultList = query.getResultList();
		
		List<EconomySectorVO> economySectorVOList = new ArrayList<EconomySectorVO>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				EconomySectorVO economySectorVO = new EconomySectorVO();
				
				economySectorVO.setEconomySectorId(MathUtil.returnIdObjectToLong(obj[0]));
				//economySectorVO.setEconomySectorCode(obj[1].toString());
				economySectorVO.setEconomySector(obj[1].toString());
				economySectorVO.setRiskRating(obj[2].toString());
				
				economySectorVOList.add(economySectorVO);
			}
		}
		
		return economySectorVOList;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					
					//if (StringUtils.equals(EconomySectorConstants.SEARCH_BY_CODE, col)) {
					//	sb.append(" AND UPPER(fes.economy_sector_code) LIKE :code ");	
					//}
					if (StringUtils.equals(EconomySectorConstants.SEARCH_BY_NAME, col)) {
						sb.append(" AND UPPER(fes.economy_sector_name) LIKE :name ");	
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
					//if (StringUtils.equals(EconomySectorConstants.SEARCH_BY_CODE, col)) {
					//	query.setParameter("code", "%" + val.toUpperCase() + "%");
					//}
					
					if (StringUtils.equals(EconomySectorConstants.SEARCH_BY_NAME, col)) {
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
		sb.append(" FROM wo_mst_fcc_economy_sector fes ");
		sb.append(" where 1=1 ");
		sb.append(" and fes.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<EconomySector> findAll() throws ParseException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fes.economy_sector_id, "
				+ "fes.economy_sector_name, fes.risk_rating, "
				+ "fes.created_by, fes.creation_date, fes.enabled_flag ");
		sb.append("FROM wo_mst_fcc_economy_sector fes ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fes.enabled_flag = 'Y' ");
		sb.append("ORDER BY fes.economy_sector_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = null;
		resultList = query.getResultList();
		
		List<EconomySector> economySectorList = new ArrayList<EconomySector>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				EconomySector economySector = new EconomySector();
				
				economySector.setEconomySectorId(MathUtil.returnIdObjectToLong(obj[0]));
				//economySector.setEconomySectorCode(obj[1].toString());
				economySector.setEconomySectorName(obj[1].toString());
				economySector.setRiskRating(obj[2].toString());
				
				economySector.setCreatedBy(obj[3].toString());
				Date creationDate = DateUtil.stringToDateFromYYYYMMDD(obj[4].toString());
				economySector.setCreationDate(DateUtil.dateToSqlTimestamp(creationDate));
				economySector.setEnabledFlag(obj[5].toString());
				
				economySectorList.add(economySector);
			}
		}
		
		return economySectorList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getFileName() {
		try {
			String fileName = "";
			StringBuilder sb = new StringBuilder();
			
			//need 'SELECT UPLOAD_DATE' so 'ORDER BY UPLOAD_DATE' could work
			sb.append("SELECT DISTINCT fes.FILE_NAME,fes.UPLOAD_DATE ");
			sb.append("FROM WO_MST_FCC_ECONOMY_SECTOR fes ");
			sb.append("WHERE 1=1 ");
			sb.append("AND fes.ENABLED_FLAG = 'Y' ");
			sb.append("AND TRUNC(fes.UPLOAD_DATE) <= CURRENT_TIMESTAMP ");
			sb.append("ORDER BY fes.UPLOAD_DATE DESC");
			
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
				return "FCC_Economy_Sector_Template";
			}
			
		//IF there is problem, still return template file name
		}catch(Exception ex) {
			ex.printStackTrace();
			return "FCC_Economy_Sector_Template";
		}
		
	}
	
	public void deleteAll() {
		String queryStr = "DELETE FROM WO_MST_FCC_ECONOMY_SECTOR fes WHERE fes.ENABLED_FLAG = 'Y'";
		
		Query query = getSession().createSQLQuery(queryStr);
		
		query.executeUpdate();
	}
	
}
