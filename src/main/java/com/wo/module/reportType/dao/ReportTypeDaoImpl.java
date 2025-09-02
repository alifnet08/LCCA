/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.reportType.dao;


//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
//import com.wo.module.emailTemplate.model.EmailTemplate;
import com.wo.module.reportType.model.ReportType;

/**
 *
 * @author hendra
 */

@Repository("reportTypeDao")
public class ReportTypeDaoImpl extends GenericDAOHibernate<ReportType, Long> 
    implements ReportTypeDao {
	
	@SuppressWarnings({ "rawtypes", "static-access" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(SearchObject.ALL_COLUMNS, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(report_type_en) LIKE UPPER('%"+val+"%') ");
						} else {
							sb.append(" and UPPER(report_type_in) LIKE UPPER('%"+val+"%') ");
						}
					}
				}
			}
		}
		
		return sb;
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
		sb.append(" FROM wo_mst_report_type rt ");
		sb.append(" WHERE 1=1 and rt.enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<ReportType> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<ReportType> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<ReportType> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" select report_type_id, report_type_in,report_type_en, report_type_description, due_day, due_date,due_month,due_year ");
		sb.append(" FROM wo_mst_report_type rt ");
		sb.append(" WHERE 1=1 and rt.enabled_flag = 'Y' ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        sb.append(" ORDER BY report_type_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<ReportType> vo = new ArrayList<ReportType>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                ReportType data = new ReportType();
                //data.setReportTypeId(((BigInteger)obj[0]).longValue());
                data.setReportTypeId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setReportTypeIn((String)obj[1]);
                data.setReportTypeEn((String)obj[2]);
                data.setReportTypeDescription((String)obj[3]);
                data.setDueDay((String)obj[4]);
                data.setDueDate((String)obj[5]);
                data.setDueMonth((String)obj[6]);
                data.setDueYear((String)obj[7]);
                
                vo.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return vo;
    }
    
    @SuppressWarnings("rawtypes")
	public List<ReportType> getAllReportType() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select report_type_id, report_type_in,report_type_en, report_type_description, due_day, due_date,due_month,due_year ");
		sb.append(" FROM wo_mst_report_type rt ");
		sb.append(" WHERE 1=1 and rt.enabled_flag = 'Y' ");

		sb.append(" ORDER BY report_type_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());

		List resultList = query.getResultList();

		List<ReportType> vo = new ArrayList<ReportType>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
                ReportType data = new ReportType();
                //data.setReportTypeId(((BigInteger)obj[0]).longValue());
                data.setReportTypeId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setReportTypeIn((String)obj[1]);
                data.setReportTypeEn((String)obj[2]);
                data.setReportTypeDescription((String)obj[3]);
                data.setDueDay((String)obj[4]);
                data.setDueDate((String)obj[5]);
                data.setDueMonth((String)obj[6]);
                data.setDueYear((String)obj[7]);
				vo.add(data);
			}
		}

		return vo;
    }

    @SuppressWarnings("rawtypes")
	@Override
    public Boolean isReportTypeDuplicate(ReportType reportype) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select report_type_in ");
		sb.append(" from wo_mst_report_type rt ");
		sb.append(" where 1=1 ");
		sb.append(" and rt.enabled_flag = 'Y' ");
		sb.append(" and rt.report_type_in = :reportTypeIn ");

		if(reportype.getReportTypeId() != null ) {
			sb.append(" and rt.report_type_id  <> :id ");
		}	

		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("reportTypeIn", reportype.getReportTypeIn());
		
		if(reportype.getReportTypeId() != null ) {
			query.setParameter("id", reportype.getReportTypeId());
		}

		List resultList = query.getResultList();

		if (resultList != null &&  resultList.size() > 0) {
			return true;
		}

		return false;
	}
    
    @SuppressWarnings("rawtypes")
	public Boolean isUsedInTransaction(Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select rt.report_type_id ");
		sb.append(" from wo_mst_report_type rt ");
		sb.append(" where 1=1 ");
		sb.append(" and rt.report_type_id = :reportTypeId ");
		sb.append(" and ( exists (select rmd.report_type_id from wo_tmp_rmd rmd ");
		sb.append("                where rmd.report_type_id = :reportTypeId and rmd.enabled_flag = 'Y') ");
		sb.append("    ) ");
		
		Query query = getSession().createSQLQuery(sb.toString().trim());

		query.setParameter("reportTypeId", id);		

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
	
    
}
