package com.wo.module.occupationFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.economySector.constant.EconomySectorConstants;
import com.wo.module.occupation.model.Occupation;
import com.wo.module.occupationFE.vo.OccupationFEVO;

@Repository("occupationFEDao")
public class OccupationFEDaoImpl extends GenericDAOHibernate<Occupation, Long> 
	implements OccupationFEDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5043291159792883785L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<OccupationFEVO> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<OccupationFEVO> voList = searchDataCriteria(searchCriteria, first, pageSize);
		return voList;
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
	private List<OccupationFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		List<String> list = getSearchValList(searchCriteria);
		
		//match logic query
//		sb.append("SELECT fo.occupation_id, fo.occupation_name, fo.risk_rating ");
//		sb.append("FROM wo_mst_fcc_occupation fo ");
//		sb.append("WHERE 1=1 ");
//		sb.append("AND fo.enabled_flag = 'Y' ");
//		
//		this.getQueryWhereString(sb, searchCriteria);
		
		//fuzzy logic query
		sb.append("SELECT fo.occupation_id, fo.occupation_name, fo.risk_rating ");
		sb.append("FROM wo_mst_fcc_occupation fo ");
		sb.append("WHERE 1=1 ");
		sb.append("AND fo.enabled_flag = 'Y' ");
		if(list!=null && !list.isEmpty()) {
			//if list only contain 1 string
			if(list.size()==1) {
				sb.append("AND (UPPER(fo.occupation_name) like UPPER('%"+getSearchVal(searchCriteria)+"%') ) ");
			//if not
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					//first item on list
					if(i==0) {
						sb.append(" AND (");
						sb.append(" UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
					//last item on list
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
						sb.append(" )");
					//items between first and last
					}else {
						sb.append(" OR UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		
		sb.append("ORDER BY fo.occupation_name ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
//		this.getQuerySetValue(query, searchCriteria);
		
		List resultList = query.getResultList();
		List<OccupationFEVO> voList = new ArrayList<OccupationFEVO>();
		
		if(resultList != null) {
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				OccupationFEVO vo = new OccupationFEVO();
				
				vo.setOccupationId(MathUtil.returnIdObjectToLong(obj[0]));
				vo.setOccupationName(obj[1].toString());
				vo.setRiskRating(obj[2].toString());
				
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		List<String> list = getSearchValList(searchCriteria);
		
		//match logic query
//		sb.append(" SELECT COUNT(1) ");
//		sb.append(" FROM wo_mst_fcc_occupation fo ");
//		sb.append(" where 1=1 ");
//		sb.append(" and fo.enabled_flag = 'Y' ");
//		this.getQueryWhereString(sb, searchCriteria);
		
		//fuzzy logic query
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_mst_fcc_occupation fo ");
		sb.append(" where 1=1 ");
		sb.append(" and fo.enabled_flag = 'Y' ");
		if(list!=null && !list.isEmpty()) {
			//if list only contain 1 string
			if(list.size()==1) {
				sb.append("AND (UPPER(fo.occupation_name) like UPPER('%"+getSearchVal(searchCriteria)+"%') ) ");
			//if not
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					//first item on list
					if(i==0) {
						sb.append(" AND (");
						sb.append(" UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
					//last item on list
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
						sb.append(" )");
					//items between first and last
					}else {
						sb.append(" OR UPPER(fo.occupation_name) like UPPER('%"+val+"%') ");
					}
				}
			}
		}

		

		Query query = getSession().createSQLQuery(sb.toString());

//		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("name", "%" + val + "%");
					}
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
							sb.append(" AND UPPER(fo.occupation_name) LIKE UPPER(:name) ");	
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private List<String> getSearchValList(List<? extends SearchObject> searchCriteria) {
		String valueSearch = getSearchVal(searchCriteria);
		List<String> list = new ArrayList<String>();
		
		if(valueSearch!=null && !valueSearch.isEmpty()) {
			valueSearch = valueSearch.replace(":and", "&");
			valueSearch = valueSearch.replace(":percent", "%");
			
			String[] newStr = valueSearch.split(" ");
			String data = "";
			for (int i = 0; i < newStr.length; i++) {
				if(i==0) {
					list.add(newStr[i]);
					data = newStr[i];
				}else {
					list.add(newStr[i]);
					data = data.concat(" ").concat(newStr[i]);
					list.add(data);
				}
	        }
			
			Collections.sort(list, new Comparator<String>() {

				@Override
	            public int compare(String str1, String str2) {
	                return str2.length() - str1.length();
	            }
	        });
			
			System.out.println(list);
		}
		
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	private String getSearchVal(List<? extends SearchObject> searchCriteria) {
		String searchVal = null;
		if (searchCriteria != null) {
			for (SearchObject data : searchCriteria) {
				String col = data.getSearchColumn();
				String val = data.getSearchValueAsString();
				
				if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
					searchVal = val;
					break;
				}
//				else if (!StringUtils.isBlank(val)) {	
//					searchVal = val;
//					break;
//				}
			}
		}
		return searchVal;
	}

}
