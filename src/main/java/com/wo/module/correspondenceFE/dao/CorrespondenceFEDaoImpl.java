package com.wo.module.correspondenceFE.dao;


import java.util.ArrayList;
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
import com.wo.module.correspondenceFE.vo.CorrespondenceFEVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;

@Repository("correspondenceFEDao")
public class CorrespondenceFEDaoImpl extends GenericDAOHibernate<TrcCorrespondence, Long> 
    implements CorrespondenceFEDao {

	private void getQueryWhereString(StringBuilder sb, @SuppressWarnings("rawtypes") List<? extends SearchObject> searchCriteria) {
			if (searchCriteria != null) {
			for (@SuppressWarnings("rawtypes") SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
							sb.append(" and UPPER(r1.letter_no) LIKE UPPER(:searchPerihal) ");
						
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						sb.append(" and ((((r1.CORRESPONDENCE_TYPE = 'NONINVITATION' and :searchStatus  <> 'PIC_DONE') or r1.followup_status = 'PIC_DONE') ");
						sb.append(" and ((r1.CORRESPONDENCE_TYPE = 'NONINVITATION' and :searchStatus  = 'PIC_DONE') or (r1.followup_status is null OR r1.followup_status <> 'PIC_DONE'))) ");
						
						sb.append(" or( ((r1.CORRESPONDENCE_TYPE = 'INVITATION' and :searchStatus  <> 'PIC_DONE') or (r1.followup_status is not null and r1.pic_followup_status is not null)) ");
						sb.append(" and ((r1.CORRESPONDENCE_TYPE = 'INVITATION' and :searchStatus  = 'PIC_DONE') or (r1.followup_status is null or r1.pic_followup_status is null)))) ");
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						//sb.append(" and (r1.user_id_1 = :userId or r1.user_id_2 = :userId or r1.user_id_3 = :userId ) ");
						sb.append(" and wtcpc.user_id_1 = :userId ");
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
//				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
						query.setParameter("searchStatus", val);
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {	
						query.setParameter("userId", val );						
					}

				}
			}
		}
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
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(1) ");
		sb.append("   FROM wo_trc_correspondence r1 ");
		sb.append("        INNER JOIN (SELECT d.* ");
		sb.append("                      FROM wo_mst_parameter p ");
		sb.append("                           INNER JOIN wo_mst_parameter_dtl d ON d.parameter_code = p.parameter_code ");
		sb.append("                                  AND p.parameter_code = 'SENDER') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append("        INNER JOIN wo_trc_crpdc_pic_confirm wtcpc ON wtcpc.correspondence_id = r1.correspondence_id ");
		sb.append("        INNER JOIN wo_mst_user pic1 ON pic1.user_id = wtcpc.user_id_1 ");
		sb.append("        LEFT JOIN wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append("        LEFT JOIN wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_id = wtcpc.STATUS_PIC_ID ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd3 on pd3.parameter_dtl_code = r1.correspondence_type ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_id = wtcpc.COMPLIANCE_STATUS_ID ");		
		sb.append("        AND r1.enabled_flag = 'Y' ");
		sb.append("        AND r1.follow_up = 'Y' ");
		
		
		/*sb.append(" and (r1.pic_followup_status is null) ");
		sb.append(" and (r1.CORRESPONDENCE_TYPE = 'NONINVITATION' OR r1.pic_followup_status is null) ");*/
		
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<CorrespondenceFEVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<CorrespondenceFEVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<CorrespondenceFEVO> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT distinct r1.correspondence_id, r1.letter_no, ");
		sb.append("        pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append("        r1.letter_received_date, pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append("        pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append("        r1.confirmation_date, r1.followup_date, r1.followup_note, r1.compliance_date, ");
		sb.append("        pdCompliance.name_en as COMPLIANCE_STATUS_NAME_EN, pdCompliance.name_in as COMPLIANCE_STATUS_NAME_IN, ");
		sb.append("        r1.status, r1.followup_status, pic1.name as PIC_NAME_1, ");
		sb.append("        pic2.name as PIC_NAME_2, pic3.name as PIC_NAME_3, ");
		sb.append("        TO_CHAR(wtcpc.target_date, 'dd FMMonth yyyy', 'nls_date_language=indonesian') targetDate, ");
		sb.append("        r1.letter_date, r1.compliance_note, r1.correspondence_type, pdCompliance.parameter_dtl_code compliance_status, ");
		sb.append("        r1.pic_followup_status, pd3.name_in correspndence_type_name, wtcpc.CRPDC_PIC_CONFIRM_ID ");
		sb.append("   FROM wo_trc_correspondence r1 ");
		sb.append("        INNER JOIN (SELECT d.* ");
		sb.append("                      FROM wo_mst_parameter p ");
		sb.append("                           INNER JOIN wo_mst_parameter_dtl d ON d.parameter_code = p.parameter_code ");
		sb.append("                                  AND p.parameter_code = 'SENDER') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append("        INNER JOIN wo_trc_crpdc_pic_confirm wtcpc ON wtcpc.correspondence_id = r1.correspondence_id ");
		sb.append("        INNER JOIN wo_mst_user pic1 ON pic1.user_id = wtcpc.user_id_1 ");
		sb.append("        LEFT JOIN wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append("        LEFT JOIN wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_id = wtcpc.STATUS_PIC_ID ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pd3 on pd3.parameter_dtl_code = r1.correspondence_type ");
		sb.append("        LEFT JOIN wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_id = wtcpc.COMPLIANCE_STATUS_ID ");		
        sb.append("  WHERE 1=1 ");
 		sb.append("        AND r1.enabled_flag = 'Y' ");
 		sb.append("        AND r1.follow_up = 'Y' "); 
 				
		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.correspondence_id DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.getQuerySetValue(query, searchCriteria);
		
		query.setFirstResult(first);
        query.setMaxResults(pageSize);	

		List resultList = query.getResultList();

		List<CorrespondenceFEVO> vo = new ArrayList<CorrespondenceFEVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				CorrespondenceFEVO data = new CorrespondenceFEVO();
				//data.setCorrespondenceId(((BigInteger) obj[0]).longValue());
				data.setCorrespondenceId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterNo((String) obj[1]);
				data.setSenderNameEn((String) obj[2]);
				data.setSenderNameIn((String) obj[3]);
				data.setLetterReceivedDate(obj[4] != null ? (Date) obj[4] : null);
				data.setStatusNameEn((String) obj[5]);
				data.setStatusNameIn((String) obj[6]);
				data.setFollowupStatusNameEn((String) obj[7]);
				data.setFollowupStatusNameIn((String) obj[8]);
				data.setConfirmationDate(obj[9] !=null ?(Date) obj[9] : null);
				data.setFollowupDate(obj[10] !=null ? (Date) obj[10] : null);
				data.setFollowupNote((String) obj[11]);
				data.setFulfillmentFollowupDate(obj[12] !=null ? (Date) obj[12] : null);
				data.setComplianceCheckerStatusEn((String) obj[13]);
				data.setComplianceCheckerStatusIn((String) obj[14]);
				data.setStatusCode((String) obj[15]);
				data.setFollowupStatusCode((String) obj[16]);
				data.setPicName1((String) obj[17]);
				data.setPicName2((String) obj[18]);
				data.setPicName3((String) obj[19]);				
				data.setTargetDateStr(obj[20] != null ? (String) obj[20] : null);
				data.setLetterDate(obj[21] != null ? (Date) obj[21] : null);
				data.setComplianceNote((String) obj[22]);
				data.setCorrespondenceType((String) obj[23]);
				data.setComplianceStatusCode(obj[24]!=null?(String) obj[24]:null);
				data.setPicFollowupStatusCode(obj[25]!=null?(String) obj[25]:null);
				data.setCorrespondenceTypeName(obj[26]!=null?(String) obj[26]:null);
				data.setCrpdcPicConfirmId(MathUtil.returnIdObjectToLong(obj[27]));
				
				vo.add(data);				
			}
		}


		return vo;
	}
	

}
