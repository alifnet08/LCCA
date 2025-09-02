package com.wo.module.country.dao;

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
import com.wo.module.country.constant.CountryConstants;
import com.wo.module.country.model.Country;
import com.wo.module.country.vo.CountryVO;

@Repository("countryDao")
public class CountryDaoImpl extends GenericDAOHibernate<Country, Long> 
implements CountryDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5702249341530593593L;
	static Logger logger = Logger.getLogger(CountryDaoImpl.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<CountryVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		
		List<CountryVO> countryVOList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return countryVOList;
	}

	@SuppressWarnings("rawtypes")
	private List<CountryVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fc.country_id, fc.country_name, fc.risk_rating ");
		sb.append("FROM wo_mst_fcc_country fc ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fc.enabled_flag = 'Y' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append("ORDER BY fc.country_name ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		this.getQuerySetValue(query, searchCriteria);
		
		List resultList = query.getResultList();
		
		List<CountryVO> countryVOList = new ArrayList<CountryVO>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				CountryVO countryVO = new CountryVO();
				
				countryVO.setNegaraId(MathUtil.returnIdObjectToLong(obj[0]));
				countryVO.setNegara(obj[1].toString());
				countryVO.setRiskRating(obj[2].toString());
				
				countryVOList.add(countryVO);
			}
		}
		
		return countryVOList;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CountryConstants.SEARCH_BY_NAME, col)) {
							sb.append(" AND UPPER(fc.country_name) LIKE :name ");	
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
					if (StringUtils.equals(CountryConstants.SEARCH_BY_NAME, col)) {
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
		sb.append(" FROM wo_mst_fcc_country fc ");
		sb.append(" where 1=1 ");
		sb.append(" and fc.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Country> findAll() throws ParseException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fc.country_id, fc.country_name, fc.risk_rating, "
				+ "fc.created_by, fc.creation_date, fc.enabled_flag ");
		sb.append("FROM wo_mst_fcc_country fc ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fc.enabled_flag = 'Y' ");
		sb.append("ORDER BY fc.country_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = null;
		resultList = query.getResultList();
		
		List<Country> countryList = new ArrayList<Country>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				Country country = new Country();
				
				country.setCountryId(MathUtil.returnIdObjectToLong(obj[0]));
				country.setCountryName(obj[1].toString());
				country.setRiskRating(obj[2].toString());
				
				country.setCreatedBy(obj[3].toString());
				Date creationDate = DateUtil.stringToDateFromYYYYMMDD(obj[4].toString());
				country.setCreationDate(DateUtil.dateToSqlTimestamp(creationDate));
				country.setEnabledFlag(obj[5].toString());
				
				countryList.add(country);
			}
		}
		
		return countryList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getFileName() {
		try {
			String fileName = "";
			StringBuilder sb = new StringBuilder();
			
			//need 'SELECT UPLOAD_DATE' so 'ORDER BY UPLOAD_DATE' could work
			sb.append("SELECT DISTINCT fc.FILE_NAME,fc.UPLOAD_DATE ");
			sb.append("FROM WO_MST_FCC_COUNTRY fc ");
			sb.append("WHERE 1=1 ");
			sb.append("AND fc.ENABLED_FLAG = 'Y' ");
			sb.append("AND TRUNC(fc.UPLOAD_DATE) <= CURRENT_TIMESTAMP ");
			sb.append("ORDER BY fc.UPLOAD_DATE DESC");
			
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
				return "FCC_Country_Template";
			}
				
		//IF there is problem, still return template file name
		}catch (Exception ex) {
			ex.printStackTrace();
			return "FCC_Country_Template";
		}
	}

	@Override
	public void deleteAll() {
		String queryStr = "DELETE FROM WO_MST_FCC_COUNTRY fc WHERE fc.ENABLED_FLAG = 'Y'";
		
		Query query = getSession().createSQLQuery(queryStr);
		
		query.executeUpdate();
	}
}
