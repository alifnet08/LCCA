package com.wo.module.searchAllFE.dao;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.correspondenceFE.constant.CorrespondenceFEConstants;
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.searchAllFE.vo.SearchAllFEVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

@Repository("searchAllFEDao")
public class SearchAllFEDaoImpl extends GenericDAOHibernate<TrcCorrespondence, Long> 
    implements SearchAllFEDao {

	private String getSearchVal(@SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
		String searchVal = null;
		if (searchCriteria != null) {
			for (@SuppressWarnings("rawtypes") SearchObject data : searchCriteria) {
				String col = data.getSearchColumn();
				String val = data.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					searchVal = val;
					break;
				}
			}
		}
		return searchVal;
	}
	
	private void getQueryWhereString(StringBuilder sb, @SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
			if (searchCriteria != null) {
			for (@SuppressWarnings("rawtypes") SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" and type_code = :typeCode");
					}
					

				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private String getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		String searchValValue = null;
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					
					val = val.replace(":and", "&");
					val = val.replace(":percent", "%");
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("searchVal", "%" + val + "%");
						searchValValue = val;
					}
					
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("typeCode", val);
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userLoginId", val);
						searchValValue = val;
					}

				}
			}
		}
		
		return searchValValue;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {

		Number results = searchCountDataCriteria(searchCriteria);
		if (results == null) {
			results = 0;
		}

		return results.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		
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
		
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) from ( ");
		sb.append(" SELECT r.regulation_id internalid,document_no no,r.name_in name, 'Ketentuan Internal' type, 'SEARCH_CATEGORY_PERATURAN_INTERNAL' type_code ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		//sb.append(" and r.status = 'DATA_ACTIVE' ");
		//sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) ");

		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		
		sb.append(" UNION ALL ");
		sb.append(" SELECT r.regulation_id,document_no,r.name_in, 'Ketentuan Eksternal', 'SEARCH_CATEGORY_PERATURAN_EKSTERNAL' ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
		//sb.append(" and r.status = 'DATA_ACTIVE' ");
		//sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) ");
		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		sb.append(" UNION ALL  ");
		sb.append(" SELECT d.DISCUSSION_ID,'',d.THREAD_NAME_IN,'Grup Diskusi', 'SEARCH_CATEGORY_DISKUSI_GRUP' ");
		sb.append(" FROM WO_MST_DISCUSSION d ");
		sb.append(" INNER JOIN WO_MST_USER uThreadStart ON uThreadStart.USER_ID = d.THREAD_USER_ID ");
		sb.append(" LEFT JOIN WO_MST_DISCUSSION_POST dp ON dp.DISCUSSION_ID = d.DISCUSSION_ID ");
		sb.append(" LEFT JOIN WO_MST_USER uPostBy ON uPostBy.USER_ID = dp.POSTED_BY_ID  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND dp.DISCUSSION_POST_ID IN(SELECT MAX(dp1.DISCUSSION_POST_ID) FROM WO_MST_DISCUSSION_POST dp1)  ");
		sb.append(" AND d.ENABLED_FLAG = 'Y'  ");
		sb.append(" and UPPER(d.THREAD_NAME_IN) like UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" select faq_id, question_in,TO_CHAR(answer_in),'FAQ', 'SEARCH_CATEGORY_FAQ' ");
		sb.append(" from wo_mst_faq ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and (UPPER(ANSWER_IN) like UPPER(:searchVal) OR UPPER(QUESTION_IN) like UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT n.NOTARY_ID,'',n.NOTARY_NAME,'Notaris', 'SEARCH_CATEGORY_NOTARIS' ");
		sb.append(" FROM WO_MST_NOTARY n ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pdCat ON pdCat.PARAMETER_DTL_CODE = n.NOTARY_CATEGORY ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND n.ENABLED_FLAG = 'Y' ");
		sb.append(" and UPPER(n.NOTARY_NAME) like UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT c.CPSA_ID,c.LETTER_NO,c.LETTER_ABOUT,'CPSA', 'SEARCH_CATEGORY_CPSA' ");
		sb.append(" FROM WO_MST_CPSA c ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pdCpsa ");
		sb.append(" ON pdCpsa.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append(" LEFT JOIN WO_MST_CPSA_PIC cPic ");
		sb.append(" ON cPic.CPSA_ID = c.CPSA_ID ");
		sb.append(" LEFT JOIN WO_MST_USER uCPic ");
		sb.append(" ON uCPic.USER_ID = cPic.USER_ID_1 ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" AND c.ENABLED_FLAG = 'Y' ");
		sb.append(" AND cPic.USER_ID_1 = :userLoginId ");
		sb.append(" AND UPPER(c.LETTER_ABOUT) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
//		sb.append(" select ct.ADVOCATE_ID,TO_CHAR(ct.NO),ct.ADVOCATE_NAME,'Kantor Hukum', 'SEARCH_CATEGORY_KANTOR_HUKUM' ");
//		sb.append(" FROM wo_mst_advocate ct ");
//		sb.append(" LEFT JOIN WO_MST_ADVOCATE_INFO info ON ct.ADVOCATE_ID = info.ADVOCATE_ID ");
//		sb.append(" WHERE 1 = 1 AND ct.enabled_flag = 'Y' ");
//		sb.append(" AND info.ADVOCATE_INFO_ID IN (SELECT MIN(i.ADVOCATE_INFO_ID) FROM WO_MST_ADVOCATE_INFO i)  ");
//		sb.append(" and (UPPER(ct.ADVOCATE_NAME) like UPPER(:searchVal) OR UPPER(TO_CHAR(ct.NO)) like UPPER(:searchVal)) ");
		sb.append(" select ct.ADVOCATE_ID,TO_CHAR(ct.NO),ct.ADVOCATE_NAME,'Kantor Hukum', 'SEARCH_CATEGORY_KANTOR_HUKUM' ");
		sb.append(" FROM wo_mst_advocate ct ");
		sb.append(" WHERE 1 = 1 AND ct.enabled_flag = 'Y' ");
		sb.append("     AND ");
		sb.append("     (   UPPER(ct.ADVOCATE_NAME) like UPPER(:searchVal) ");
		sb.append("         OR UPPER(TO_CHAR(ct.NO)) like UPPER(:searchVal) ");
		sb.append("         OR exists ( ");
		sb.append("                     select 1 from WO_MST_ADVOCATE_PARTNERS partner ");
		sb.append("                     WHERE 1 = 1 ");
		sb.append("                         AND ct.ADVOCATE_ID = partner.ADVOCATE_ID ");
		sb.append("                         AND partner.ENABLED_FLAG = 'Y' ");
		sb.append("                         AND UPPER(partner.PARTNERS) like UPPER(:searchVal) ");
		sb.append("                   ) ");
		sb.append("         OR exists ( ");
		sb.append("                 select 1 from WO_MST_ADVOCATE_INFO info ");
		sb.append("                 WHERE 1 = 1 ");
		sb.append("                     AND ct.ADVOCATE_ID = info.ADVOCATE_ID ");
		sb.append("                     AND info.ENABLED_FLAG = 'Y' ");
		sb.append("                     AND UPPER(info.OFFICE_ADDRESS) like UPPER(:searchVal) ");
		sb.append("                  ) ");
		sb.append("     ) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT ct.ARTICLE_ID,'',ct.ARTICLE_TITLE_IN,'Artikel', 'SEARCH_CATEGORY_ARTIKEL' ");
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdArtType ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE  ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdStatus ON pdStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS  ");
		//sb.append(" INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ON ct.ARTICLE_ID = appr.ARTICLE_ID  ");
		//sb.append(" AND appr.APPROVAL_STATUS = 'STATUS_APPROVED'  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND ct.enabled_flag = 'Y'  ");
		sb.append(" AND ct.ACTIVE_STATUS = 'DATA_ACTIVE'  ");
		sb.append(" AND ct.ARTICLE_TYPE <> 'LEGAL_OPINION' AND ct.ARTICLE_TYPE <> 'COMPLIANCE_OPINION' ");
		sb.append(" and (UPPER(ct.content_in) LIKE UPPER(:searchVal) or UPPER(ct.ARTICLE_TITLE_IN) LIKE UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT ct.ARTICLE_ID,'',ct.ARTICLE_TITLE_IN,'Opini', 'SEARCH_CATEGORY_OPINI' ");
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdArtType ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE  ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdStatus ON pdStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS  ");
		//sb.append(" INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ON ct.ARTICLE_ID = appr.ARTICLE_ID  ");
		//sb.append(" AND appr.APPROVAL_STATUS = 'STATUS_APPROVED'  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND ct.enabled_flag = 'Y'  ");
		sb.append(" AND ct.ACTIVE_STATUS = 'DATA_ACTIVE'  ");
		sb.append(" AND (ct.ARTICLE_TYPE = 'LEGAL_OPINION' OR ct.ARTICLE_TYPE = 'COMPLIANCE_OPINION') ");
		sb.append(" and (UPPER(ct.content_in) LIKE UPPER(:searchVal) or UPPER(ct.ARTICLE_TITLE_IN) LIKE UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" select qna_id, question,answer,'QA', 'SEARCH_CATEGORY_QA' ");
		sb.append(" from WO_MST_QNA ct ");
		sb.append(" where ct.enabled_flag = 'Y' and from_qna_id is null and ((UPPER(ANSWER) like UPPER(:searchVal) OR UPPER(QUESTION) like UPPER(:searchVal)) OR EXISTS(select 1 from WO_MST_QNA q2 where q2.enabled_flag = 'Y' and q2.from_qna_id = ct.qna_id and (UPPER(q2.ANSWER) like UPPER(:searchVal) OR UPPER(q2.QUESTION) like UPPER(:searchVal))  )) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fc.COUNTRY_ID, fc.RISK_RATING, fc.COUNTRY_NAME, 'FCC - Country','SEARCH_CATEGORY_FCC' ");
		sb.append(" FROM WO_MST_FCC_COUNTRY fc ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fc.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fc.COUNTRY_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fes.ECONOMY_SECTOR_ID, fes.RISK_RATING, fes.ECONOMY_SECTOR_NAME, 'FCC - Economy Sector','SEARCH_CATEGORY_FCC' ");
		sb.append(" FROM WO_MST_FCC_ECONOMY_SECTOR fes ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fes.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fes.ECONOMY_SECTOR_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fo.OCCUPATION_ID, fo.RISK_RATING, fo.OCCUPATION_NAME, 'FCC - Occupation','SEARCH_CATEGORY_FCC' ");
		sb.append(" FROM WO_MST_FCC_OCCUPATION fo ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fo.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fo.OCCUPATION_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" ) ref WHERE 1 = 1   ");
		
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		String searchVal = this.getQuerySetValue(query, searchCriteria);
		
		if(searchVal == null){
			query.setParameter("searchVal", "%" );
		}

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<SearchAllFEVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<SearchAllFEVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<SearchAllFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		
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
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * from ( ");
		sb.append(" with queryKetentuanInternal as(SELECT r.regulation_id internalid,document_no no,r.name_in name, 'Ketentuan Internal' type, 'SEARCH_CATEGORY_PERATURAN_INTERNAL' type_code, r.published_date CREATION_DATE, 1 as ORDER_ID,r.status STATUS  ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) order by r.published_date desc), ");
		sb.append(" queryKetentuanExternal as(SELECT r.regulation_id internalid,document_no no,r.name_in name, 'Ketentuan Internal' type, 'SEARCH_CATEGORY_PERATURAN_INTERNAL' type_code, r.published_date CREATION_DATE, 1 as ORDER_ID ,r.status STATUS  ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
		sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) order by r.published_date desc) ");
		
		sb.append(" SELECT * from  queryKetentuanInternal ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT r.regulation_id internalid,document_no no,r.name_in name, 'Ketentuan Internal' type, 'SEARCH_CATEGORY_PERATURAN_INTERNAL' type_code, r.published_date CREATION_DATE, 2 as ORDER_ID,r.status STATUS   ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' ");
		sb.append(" and r.regulation_id not in (select internalid from queryKetentuanInternal) ");
		//sb.append(" and r.status = 'DATA_ACTIVE' ");
		//sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) ");
		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		sb.append(" UNION ALL ");
		sb.append(" SELECT * from  queryKetentuanExternal ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT r.regulation_id,document_no,r.name_in, 'Ketentuan Eksternal', 'SEARCH_CATEGORY_PERATURAN_EKSTERNAL', r.published_date CREATION_DATE, 2 as ORDER_ID,r.status STATUS  ");
		sb.append(" FROM wo_mst_regulation r ");
		sb.append(" WHERE 1=1 ");
		sb.append(" and r.enabled_flag = 'Y' ");
		sb.append(" and r.JENIS_KETENTUAN = 'KETENTUAN_EKSTERNAL' ");
		sb.append(" and r.regulation_id not in (select internalid from queryKetentuanExternal) ");
		//sb.append(" and r.status = 'DATA_ACTIVE' ");
		//sb.append(" and (UPPER(r.name_in) like UPPER(:searchVal) or UPPER(r.document_no) like UPPER(:searchVal)) ");
		if(list!=null && !list.isEmpty()) {
			if(list.size()==1) {
				sb.append(" and (UPPER(r.name_in) like UPPER('%"+valueSearch+"%') or UPPER(r.document_no) like UPPER('%"+valueSearch+"%')) ");
			}else {
				for(int i=0;i<list.size();i++) {
					String val = list.get(i);
					if(i==0) {
						sb.append(" and (");
						sb.append(" UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}else if(i==list.size()-1) {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
						sb.append(" )");
					}else {
						sb.append(" OR UPPER(r.name_in) like UPPER('%"+val+"%') or UPPER(r.document_no) like UPPER('%"+val+"%') ");
					}
				}
			}
		}
		sb.append(" UNION ALL  ");
		sb.append(" SELECT d.DISCUSSION_ID,'',d.THREAD_NAME_IN,'Grup Diskusi', 'SEARCH_CATEGORY_DISKUSI_GRUP', d.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS  ");
		sb.append(" FROM WO_MST_DISCUSSION d ");
		sb.append(" INNER JOIN WO_MST_USER uThreadStart ON uThreadStart.USER_ID = d.THREAD_USER_ID ");
		sb.append(" LEFT JOIN WO_MST_DISCUSSION_POST dp ON dp.DISCUSSION_ID = d.DISCUSSION_ID ");
		sb.append(" LEFT JOIN WO_MST_USER uPostBy ON uPostBy.USER_ID = dp.POSTED_BY_ID  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND dp.DISCUSSION_POST_ID IN(SELECT MAX(dp1.DISCUSSION_POST_ID) FROM WO_MST_DISCUSSION_POST dp1)  ");
		sb.append(" AND d.ENABLED_FLAG = 'Y'  ");
		sb.append(" and UPPER(d.THREAD_NAME_IN) like UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" select faq_id, question_in,TO_CHAR(answer_in),'FAQ', 'SEARCH_CATEGORY_FAQ', ct.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" from wo_mst_faq ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' and (UPPER(ANSWER_IN) like UPPER(:searchVal) OR UPPER(QUESTION_IN) like UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT n.NOTARY_ID,'',n.NOTARY_NAME,'Notaris', 'SEARCH_CATEGORY_NOTARIS', n.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_NOTARY n ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pdCat ON pdCat.PARAMETER_DTL_CODE = n.NOTARY_CATEGORY ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND n.ENABLED_FLAG = 'Y' ");
		sb.append(" and UPPER(n.NOTARY_NAME) like UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT c.CPSA_ID,c.LETTER_NO,c.LETTER_ABOUT,'CPSA', 'SEARCH_CATEGORY_CPSA', c.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_CPSA c ");
		sb.append(" LEFT JOIN WO_MST_PARAMETER_DTL pdCpsa ");
		sb.append(" ON pdCpsa.PARAMETER_DTL_CODE = c.CPSA_TYPE ");
		sb.append(" LEFT JOIN WO_MST_CPSA_PIC cPic ");
		sb.append(" ON cPic.CPSA_ID = c.CPSA_ID ");
		sb.append(" LEFT JOIN WO_MST_USER uCPic ");
		sb.append(" ON uCPic.USER_ID = cPic.USER_ID_1 ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" AND c.ENABLED_FLAG = 'Y' ");
		sb.append(" AND cPic.USER_ID_1 = :userLoginId ");
		sb.append(" AND UPPER(c.LETTER_ABOUT) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
//		sb.append(" select ct.ADVOCATE_ID,TO_CHAR(ct.NO),ct.ADVOCATE_NAME,'Kantor Hukum', 'SEARCH_CATEGORY_KANTOR_HUKUM' ");
//		sb.append(" FROM wo_mst_advocate ct ");
//		sb.append(" LEFT JOIN WO_MST_ADVOCATE_INFO info ON ct.ADVOCATE_ID = info.ADVOCATE_ID ");
//		sb.append(" WHERE 1 = 1 AND ct.enabled_flag = 'Y' ");
//		sb.append(" AND info.ADVOCATE_INFO_ID IN (SELECT MIN(i.ADVOCATE_INFO_ID) FROM WO_MST_ADVOCATE_INFO i)  ");
//		sb.append(" and (UPPER(ct.ADVOCATE_NAME) like UPPER(:searchVal) OR UPPER(TO_CHAR(ct.NO)) like UPPER(:searchVal)) ");
		sb.append(" select ct.ADVOCATE_ID,TO_CHAR(ct.NO),ct.ADVOCATE_NAME,'Kantor Hukum', 'SEARCH_CATEGORY_KANTOR_HUKUM', ct.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM wo_mst_advocate ct ");
		sb.append(" WHERE 1 = 1 AND ct.enabled_flag = 'Y' ");
		sb.append("     AND ");
		sb.append("     (   UPPER(ct.ADVOCATE_NAME) like UPPER(:searchVal) ");
		sb.append("         OR UPPER(TO_CHAR(ct.NO)) like UPPER(:searchVal) ");
		sb.append("         OR exists ( ");
		sb.append("                     select 1 from WO_MST_ADVOCATE_PARTNERS partner ");
		sb.append("                     WHERE 1 = 1 ");
		sb.append("                         AND ct.ADVOCATE_ID = partner.ADVOCATE_ID ");
		sb.append("                         AND partner.ENABLED_FLAG = 'Y' ");
		sb.append("                         AND UPPER(partner.PARTNERS) like UPPER(:searchVal) ");
		sb.append("                   ) ");
		sb.append("         OR exists ( ");
		sb.append("                 select 1 from WO_MST_ADVOCATE_INFO info ");
		sb.append("                 WHERE 1 = 1 ");
		sb.append("                     AND ct.ADVOCATE_ID = info.ADVOCATE_ID ");
		sb.append("                     AND info.ENABLED_FLAG = 'Y' ");
		sb.append("                     AND UPPER(info.OFFICE_ADDRESS) like UPPER(:searchVal) ");
		sb.append("                  ) ");
		sb.append("     ) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT ct.ARTICLE_ID,'',ct.ARTICLE_TITLE_IN,'Artikel', 'SEARCH_CATEGORY_ARTIKEL', ct.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdArtType ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE  ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdStatus ON pdStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS  ");
		//sb.append(" INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ON ct.ARTICLE_ID = appr.ARTICLE_ID  ");
		//sb.append(" AND appr.APPROVAL_STATUS = 'STATUS_APPROVED'  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND ct.enabled_flag = 'Y'  ");
		sb.append(" AND ct.ACTIVE_STATUS = 'DATA_ACTIVE'  ");
		sb.append(" AND ct.ARTICLE_TYPE <> 'LEGAL_OPINION' AND ct.ARTICLE_TYPE <> 'COMPLIANCE_OPINION' ");
		sb.append(" and (UPPER(ct.content_in) LIKE UPPER(:searchVal) or UPPER(ct.ARTICLE_TITLE_IN) LIKE UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT ct.ARTICLE_ID,'',ct.ARTICLE_TITLE_IN,'Opini', 'SEARCH_CATEGORY_OPINI', ct.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_ARTICLE ct ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdArtType ON pdArtType.PARAMETER_DTL_CODE = ct.ARTICLE_TYPE  ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL pdStatus ON pdStatus.PARAMETER_DTL_CODE = ct.ACTIVE_STATUS  ");
		//sb.append(" INNER JOIN WO_TMP_ARTICLE_APPROVAL appr ON ct.ARTICLE_ID = appr.ARTICLE_ID  ");
		//sb.append(" AND appr.APPROVAL_STATUS = 'STATUS_APPROVED'  ");
		sb.append(" WHERE 1 = 1  ");
		sb.append(" AND ct.enabled_flag = 'Y'  ");
		sb.append(" AND ct.ACTIVE_STATUS = 'DATA_ACTIVE'  ");
		sb.append(" AND (ct.ARTICLE_TYPE = 'LEGAL_OPINION' OR ct.ARTICLE_TYPE = 'COMPLIANCE_OPINION') ");
		sb.append(" and (UPPER(ct.content_in) LIKE UPPER(:searchVal) or UPPER(ct.ARTICLE_TITLE_IN) LIKE UPPER(:searchVal)) ");
		sb.append(" UNION ALL ");
		sb.append(" select qna_id, ticket_no,category_type,'QA', 'SEARCH_CATEGORY_QA', ct.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" from WO_MST_QNA ct ");
		sb.append(" where ct.enabled_flag = 'Y' and from_qna_id is null and ((UPPER(ANSWER) like UPPER(:searchVal) OR UPPER(QUESTION) like UPPER(:searchVal)) OR EXISTS(select 1 from WO_MST_QNA q2 where q2.enabled_flag = 'Y' and q2.from_qna_id = ct.qna_id and (UPPER(q2.ANSWER) like UPPER(:searchVal) OR UPPER(q2.QUESTION) like UPPER(:searchVal))  )) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fc.COUNTRY_ID, 'COUNTRY', fc.COUNTRY_NAME, 'FCC','SEARCH_CATEGORY_FCC', fc.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_FCC_COUNTRY fc ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fc.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fc.COUNTRY_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fes.ECONOMY_SECTOR_ID, 'ECONOMY_SECTOR', fes.ECONOMY_SECTOR_NAME, 'FCC','SEARCH_CATEGORY_FCC', fes.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_FCC_ECONOMY_SECTOR fes ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fes.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fes.ECONOMY_SECTOR_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" UNION ALL ");
		sb.append(" SELECT fo.OCCUPATION_ID, 'OCCUPATION', fo.OCCUPATION_NAME, 'FCC','SEARCH_CATEGORY_FCC', fo.CREATION_DATE, 2 as ORDER_ID,'DATA_ACTIVE' as STATUS ");
		sb.append(" FROM WO_MST_FCC_OCCUPATION fo ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND fo.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(fo.OCCUPATION_NAME) LIKE UPPER(:searchVal) ");
		sb.append(" )  ref WHERE 1 = 1  ");
		

		this.getQueryWhereString(sb, searchCriteria);
		
		
		sb.append(" order by ORDER_ID ASC,STATUS ASC,CREATION_DATE DESC");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

        String searchVal = this.getQuerySetValue(query, searchCriteria);
		
		if(searchVal == null){
			query.setParameter("searchVal", "%" );
		}

		List resultList = query.getResultList();

		List<SearchAllFEVO> vo = new ArrayList<SearchAllFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				SearchAllFEVO data = new SearchAllFEVO();
				data.setInternalId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setNo((String) obj[1]);
				data.setName((String) obj[2]);
				data.setType((String) obj[3]);
				data.setTypeCode((String) obj[4]);
				data.setStatus(obj[7]!=null?(String) obj[7]:null);
				
				vo.add(data);				
			}
		}

		//query.setFirstResult(first);
		//query.setMaxResults(pageSize);

		return vo;
	}
	

}
