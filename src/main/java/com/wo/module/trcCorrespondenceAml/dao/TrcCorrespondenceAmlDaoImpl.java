package com.wo.module.trcCorrespondenceAml.dao;

import java.util.ArrayList;
import java.util.Date;
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
import com.wo.module.trcCorrespondence.model.TrcCorrespondence;
import com.wo.module.trcCorrespondence.vo.TrcCorrespondenceSearchVo;
import com.wo.module.trcCorrespondenceAml.constant.TrcCorrespondenceAmlConstants;

@Repository("trcCorrespondenceAmlDao")
public class TrcCorrespondenceAmlDaoImpl extends GenericDAOHibernate<TrcCorrespondence, Long> implements TrcCorrespondenceAmlDao, TrcCorrespondenceAmlConstants{

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and r1.sender_code = :searchPengirim ");
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and r1.letter_no LIKE :searchNoSurat ");
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) >= TO_DATE(:searchTanggalTerimaSuratFrom, 'yyyy-MM-dd') ");
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) <= TO_DATE(:searchTanggalTerimaSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_date) >= TO_DATE(:searchTanggalSuratFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_date) <= TO_DATE(:searchTanggalSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r1.perihal_en LIKE :searchPerihal ");
						} else {
							sb.append(" and r1.perihal_in LIKE :searchPerihal ");
						}
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(r1.target_date) >= TO_DATE(:searchTargetDateFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(r1.target_date) <= TO_DATE(:searchTargetDateTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_STATUS, col)) {
						sb.append(" and r1.status = :searchStatus ");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (r1.user_id_1 = :userId or r1.user_id_2 = :userId or r1.user_id_3 = :userId ) ");
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
				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_PENGIRIM, col)) {
						query.setParameter("searchPengirim",  val);
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_NO_SURAT, col)) {
						query.setParameter("searchNoSurat", "%" + val + "%");
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						query.setParameter("searchTanggalTerimaSuratFrom", (Date) valReal );
					}

					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						query.setParameter("searchTanggalTerimaSuratTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						query.setParameter("searchTanggalSuratFrom", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						query.setParameter("searchTanggalSuratTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_PERIHAL, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
						query.setParameter("searchTargetDateFrom", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
						query.setParameter("searchTargetDateTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_STATUS, col)) {
						query.setParameter("searchStatus", val);
					}
					
					if (StringUtils.equals(TrcCorrespondenceAmlConstants.SEARCH_BY_USER_LOGIN, col)) {	
						query.setParameter("userId", val );						
					}

				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<TrcCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		
		List<TrcCorrespondenceSearchVo> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TrcCorrespondenceSearchVo> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.correspondence_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" picConfirmation.name as PIC_CONFIRMATION_NAME, r1.confirmation_date, ");
		sb.append(" r1.followup_date, r1.followup_note, ");
		sb.append(" r1.compliance_date, ");
		sb.append(" pdCompliance.name_en as COMPLIANCE_STATUS_NAME_EN, pdCompliance.name_in as COMPLIANCE_STATUS_NAME_IN, ");
		sb.append(" r1.status, r1.followup_status, ");
		sb.append(" pic1.name as PIC_NAME_1 , pic2.name as PIC_NAME_2,  pic3.name as PIC_NAME_3, ");
		sb.append(" r1.target_date, r1.letter_date, r1.compliance_note ");
		sb.append(" from wo_trc_correspondence r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER_AML')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r1.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r1.followup_by_id ");
		sb.append(" left join wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_code = r1.compliance_status ");
		sb.append(" left join wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append(" left join wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");
		sb.append(" left join wo_trc_crpdc_pic_fp_atdee corAttendee on corAttendee.correspondence_id = r1.correspondence_id ");
		sb.append(" left join wo_mst_user picAttendee on picAttendee.user_id = corAttendee.user_id ");

		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.follow_up = 'Y' ");
		sb.append(" and (r1.followup_status <> 'PIC_DONE' or r1.followup_status is null) ");
		sb.append(" and (r1.followup_status <> 'I_ATTEND' or r1.followup_status is null) ");
		sb.append(" and (r1.followup_status <> 'NOT_ATTEND' or r1.followup_status is null) ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.correspondence_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TrcCorrespondenceSearchVo> vo = new ArrayList<TrcCorrespondenceSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				TrcCorrespondenceSearchVo data = new TrcCorrespondenceSearchVo();
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
				data.setPicConfirmationName((String) obj[9]);

				if (obj[10] != null) {
					data.setConfirmationDate((Date) obj[10]);
				}

				if (obj[11] != null) {
					data.setFollowupDate((Date) obj[11]);
				}

				data.setFollowupNote((String) obj[12]);
				
				if (obj[13] != null) {
					data.setFulfillmentFollowupDate((Date) obj[13]);
				}

				data.setComplianceCheckerStatusEn((String) obj[14]);
				data.setComplianceCheckerStatusIn((String) obj[15]);
				
				data.setStatusCode((String) obj[16]);
				data.setFollowupStatusCode((String) obj[17]);
				data.setPicName1((String) obj[18]);
				data.setPicName2((String) obj[19]);
				data.setPicName3((String) obj[20]);
				data.setTargetDate(obj[21] != null ? (Date) obj[21] : null);
				data.setLetterDate(obj[22] != null ? (Date) obj[22] : null);
				data.setComplianceNote((String) obj[23]);
				
				vo.add(data);				
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
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
		sb.append(" from wo_trc_correspondence r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER_AML')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r1.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r1.followup_by_id ");
		sb.append(" left join wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_code = r1.compliance_status ");
		sb.append(" left join wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append(" left join wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");

		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and r1.follow_up = 'Y' ");
		sb.append(" and (r1.followup_status <> 'PIC_DONE' or r1.followup_status is null) ");
		sb.append(" and (r1.followup_status <> 'I_ATTEND' or r1.followup_status is null) ");
		sb.append(" and (r1.followup_status <> 'NOT_ATTEND' or r1.followup_status is null) ");
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}
}
