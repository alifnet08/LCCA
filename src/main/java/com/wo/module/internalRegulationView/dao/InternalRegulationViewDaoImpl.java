package com.wo.module.internalRegulationView.dao;

//import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.externalRegulation.dao.RegulationAttachmentMstDao;
import com.wo.module.externalRegulation.dao.RegulationTrackRecordMstDao;
import com.wo.module.internalRegulation.constant.InternalRegulationConstants;
import com.wo.module.internalRegulationView.model.InternalRegulationView;


@Repository("internalRegulationViewDao")
public class InternalRegulationViewDaoImpl extends GenericDAOHibernate<InternalRegulationView, Long> 
    implements InternalRegulationViewDao {
	
	@Autowired
    @Qualifier("regulationTrackRecordMstDao")
    private RegulationTrackRecordMstDao regulationTrackRecordMstDao;
	
	@Autowired
    @Qualifier("regulationAttachmentMstDao")
    private RegulationAttachmentMstDao regulationAttachmentMstDao;
	
	public RegulationTrackRecordMstDao getRegulationTrackRecordMstDao() {
		return regulationTrackRecordMstDao;
	}

	public void setRegulationTrackRecordMstDao(RegulationTrackRecordMstDao regulationTrackRecordMstDao) {
		this.regulationTrackRecordMstDao = regulationTrackRecordMstDao;
	}

	public RegulationAttachmentMstDao getRegulationAttachmentMstDao() {
		return regulationAttachmentMstDao;
	}

	public void setRegulationAttachmentMstDao(RegulationAttachmentMstDao regulationAttachmentMstDao) {
		this.regulationAttachmentMstDao = regulationAttachmentMstDao;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
    private StringBuilder getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue()!=null?searchVal.getSearchValueAsString():"";
				if (!StringUtils.isBlank(val)) {					
					if (StringUtils.equals(InternalRegulationConstants.WHERE_JENIS_KETENTUAN, col)) {
						sb.append(" and r.jenis_ketentuan = '" + val + "'");
					}					
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_TYPE, col)) {
						sb.append(" and dt.document_type_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_CATEGORY, col)) {
						sb.append(" and dc.document_category_id = " + val + " ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_PUBLISHER_UNIT, col)) {
						sb.append(" and UPPER(r.publisher_unit) like UPPER('%" + val + "%') ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_DOC_NO, col)) {
						sb.append(" and UPPER(r.document_no) LIKE UPPER('%" + val + "%') ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(r.name_en) LIKE UPPER('%" + val + "%') ");
						} else {
							sb.append(" and UPPER(r.name_in) LIKE UPPER('%" + val + "%') ");
						}
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_PUBLISHED_DATE_START, col)) {
						try {
							sb.append(" and TRUNC(r.published_date) >= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_PUBLISHED_DATE_END, col)) {
						try {
							sb.append(" and TRUNC(r.published_date) <= TO_DATE('" + sdf2.format(sdf.parse(val)) + "','yyyy-MM-dd') ");
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_REKAM_JEJAK, col)) {
						sb.append(" and ( EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"            wo_mst_regulation_track_record rtr" + 
								"        WHERE" + 
								"            rtr.regulation_id = r.regulation_id" + 
								"                AND rtr.track_code = '" + val + "')" + 
								"        OR EXISTS( SELECT " + 
								"            1" + 
								"        FROM" + 
								"			wo_mst_regulation tr," + 
								"            wo_mst_regulation_track_record rtr" + 
								"        WHERE" + 
								"			rtr.regulation_id = tr.regulation_id AND tr.enabled_flag = 'Y' AND" + 
								"            rtr.regulation_link_id = r.regulation_id" + 
								"                AND rtr.track_code = '" + val + "') ) ");
					}
					else if (StringUtils.equals(InternalRegulationConstants.WHERE_STATUS, col)) {
						sb.append(" and status = '" + val + "' ");
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
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append("        left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
		//sb.append(" left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
		//sb.append(" left join wo_tmp_regulation_track_record tr on tr.regulation_id = r.regulation_id  ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append(" WHERE 1=1 and r.enabled_flag = 'Y' ");
       
        sb = getQueryWhereString(sb, searchCriteria);
        
        Query result = getSession().createSQLQuery(sb.toString());
       
        
        return (Number) result.getSingleResult();
    }
    
    @SuppressWarnings("rawtypes")
    public List<InternalRegulationView> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder) 
            throws Exception {
        
        List<InternalRegulationView> voList = searchDataCriteria(searchCriteria, first, pageSize);

        return voList;
    }
    
    @SuppressWarnings("rawtypes")
    private List<InternalRegulationView> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
        
        StringBuilder sb = new StringBuilder();
		sb.append(" select dt.document_type_in, dt.document_type_en, dc.document_category_in, dc.document_category_en, ");
		sb.append("        '' document_topic_in, '' document_topic_en, r.document_no, r.name_in, r.name_en, ");
		sb.append("        TO_CHAR(r.published_date, 'dd-Mon-yyyy') published_date, TO_CHAR(r.expired_date, 'dd-Mon-yyyy') expired_date, ");
		sb.append("        r.publisher_unit, r.status, r.regulation_id, pd.name_in status_name_in, pd.name_en status_name_en ");
		sb.append("   FROM wo_mst_regulation r  ");
		sb.append("        left join wo_mst_document_type dt on r.document_type_id = dt.document_type_id  ");
		sb.append(" 	   left join wo_mst_document_category dc on dc.document_category_id = r.document_category_id  ");
	//	sb.append("		   left join wo_mst_document_topic dp on dp.document_topic_id = r.document_topic_id  ");
	//	sb.append("		   left join wo_tmp_regulation_track_record tr on tr.regulation_id = r.regulation_id ");
		sb.append("        left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = r.status ");
		sb.append("  WHERE 1=1 ");
		sb.append("        and r.enabled_flag = 'Y'  ");
        
        sb = getQueryWhereString(sb, searchCriteria);
        
        sb.append(" ORDER BY r.published_date DESC, r.document_no asc ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        List resultList = result.getResultList();
        
        List<InternalRegulationView> internalRegulationList = new ArrayList<InternalRegulationView>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationView data = new InternalRegulationView();
                data.setDocumentTypeNameIn(obj[0]!=null?(String)obj[0]:null);
                data.setDocumentTypeNameEn(obj[1]!=null?(String)obj[1]:null);
                data.setDocumentCategoryNameIn(obj[2]!=null?(String)obj[2]:null);
                data.setDocumentCategoryNameEn(obj[3]!=null?(String)obj[3]:null);
                data.setDocumentTopicNameIn(obj[4]!=null?(String)obj[4]:null);
                data.setDocumentTopicNameEn(obj[5]!=null?(String)obj[5]:null);
                data.setDocumentNo(obj[6]!=null?(String)obj[6]:null);
                data.setNameIn(obj[7]!=null?(String)obj[7]:null);
                data.setNameEn(obj[8]!=null?(String)obj[8]:null);
                data.setPublishedDateStr(obj[9]!=null?(String)obj[9]:null);
                data.setExpiredDateStr(obj[10]!=null?(String)obj[10]:null);
                data.setPublisherUnit(obj[11]!=null?(String)obj[11]:null);
                data.setStatus(obj[12]!=null?(String)obj[12]:null);
                //data.setRegulationId(obj[13]!=null?((BigInteger)obj[13]).longValue():null);
                data.setRegulationId(obj[13]!=null?(MathUtil.returnIdObjectToLong(obj[13])):null);
                data.setStatusNameIn(obj[14]!=null?(String)obj[14]:null);
                data.setStatusNameEn(obj[15]!=null?(String)obj[15]:null);
                
                try {
					data.setRegulationTrackRecords(regulationTrackRecordMstDao.getRegulationTrackRecordByRegulationId(data.getRegulationId()));
					data.setRegulationAttachments(regulationAttachmentMstDao.getRegulationAttachmentByRegulationId(data.getRegulationId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
                
                internalRegulationList.add(data);
            }
        }

        result.setFirstResult(first);
        result.setMaxResults(pageSize);
        
        return internalRegulationList;
    }
    
}
