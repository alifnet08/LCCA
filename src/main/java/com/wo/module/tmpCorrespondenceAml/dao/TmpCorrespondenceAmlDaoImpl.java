/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpCorrespondenceAml.dao;

import java.sql.Clob;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.tmpCorrespondence.model.TmpCorrespondence;
import com.wo.module.tmpCorrespondence.vo.TmpCorrespondenceSearchVo;
import com.wo.module.tmpCorrespondenceAml.constant.TmpCorrespondenceAmlConstants;

@Repository("tmpCorrespondenceAmlDao")
public class TmpCorrespondenceAmlDaoImpl extends GenericDAOHibernate<TmpCorrespondence, Long> implements TmpCorrespondenceAmlDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TmpCorrespondenceAmlDaoImpl.class);

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and r1.sender_code = :searchPengirim ");
					}

					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and r1.letter_no LIKE :searchNoSurat ");
					}

					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) >= TO_DATE(:searchTanggalTerimaSuratFrom, 'yyyy-MM-dd') ");
					}

					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) <= TO_DATE(:searchTanggalTerimaSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_date) >= TO_DATE(:searchTanggalSuratFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_date) <= TO_DATE(:searchTanggalSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r1.perihal_en LIKE :searchPerihal ");
						} else {
							sb.append(" and r1.perihal_in LIKE :searchPerihal ");
						}
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(r1.target_date) >= TO_DATE(:searchTargetDateFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(r1.target_date) <= TO_DATE(:searchTargetDateTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_STATUS, col)) {
						sb.append(" and r1.status = :searchStatus ");
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private void getQuerySetValue(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				Object valReal = searchVal.getSearchValue();

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PENGIRIM, col)) {
						query.setParameter("searchPengirim",  val);
					}

					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_NO_SURAT, col)) {
						query.setParameter("searchNoSurat", "%" + val + "%");
					}

//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
//						query.setParameter("searchTanggalTerimaSuratFrom", (Date) valReal );
//					}
//
//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
//						query.setParameter("searchTanggalTerimaSuratTo", (Date) valReal);
//					}
//					
//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
//						query.setParameter("searchTanggalSuratFrom", (Date) valReal);
//					}
//					
//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
//						query.setParameter("searchTanggalSuratTo", (Date) valReal);
//					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						query.setParameter("searchTanggalTerimaSuratFrom", val );
					}

					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						query.setParameter("searchTanggalTerimaSuratTo", val );
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						query.setParameter("searchTanggalSuratFrom", val );
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						query.setParameter("searchTanggalSuratTo",  val );
					}
					
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PERIHAL, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
//						query.setParameter("searchTargetDateFrom", (Date) valReal);
//					}
//					
//					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
//						query.setParameter("searchTargetDateTo", (Date) valReal);
//					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
						query.setParameter("searchTargetDateFrom", val );
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
						query.setParameter("searchTargetDateTo",  val );
					}
					
					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_STATUS, col)) {
						query.setParameter("searchStatus", val);
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
		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER_AML') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_trc_correspondence r2 on r2.correspondence_id = r1.correspondence_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");
		sb.append(" left join wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_code = r2.compliance_status ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		//sb.append(" and r1.status = 'DATA_NEW' ");
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<TmpCorrespondenceSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<TmpCorrespondenceSearchVo> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpCorrespondenceSearchVo> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.correspondence_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" picConfirmation.name as PIC_CONFIRMATION_NAME, r2.confirmation_date, ");
		sb.append(" r2.followup_date, r2.followup_note, ");
		sb.append(" r2.compliance_date, ");
		sb.append(" pdCompliance.name_en as COMPLIANCE_STATUS_NAME_EN, pdCompliance.name_in as COMPLIANCE_STATUS_NAME_IN, ");
		sb.append(" r1.status, r2.followup_status, TO_CHAR(r1.letter_received_date, 'dd-Mon-yyyy') letter_received_date1, TO_CHAR(r1.letter_date, 'dd-Mon-yyyy') letter_date1, ");
		sb.append("	TO_CHAR(r2.confirmation_date, 'dd-Mon-yyyy') confirmation_date1, TO_CHAR(r2.followup_date, 'dd-Mon-yyyy') followup_date1, TO_CHAR(r2.compliance_date, 'dd-Mon-yyyy') compliance_date1, ");
		sb.append("	r1.letter_date, TO_CHAR(r2.target_date, 'dd-Mon-yyyy') target_date1" );
		sb.append(" ,r1.perihal_en, r1.perihal_in ");
		sb.append(" from wo_tmp_correspondence r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER_AML')  pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_trc_correspondence r2 on r2.correspondence_id = r1.correspondence_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");
		sb.append(" left join wo_mst_parameter_dtl pdCompliance on pdCompliance.parameter_dtl_code = r2.compliance_status ");
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		//sb.append(" and r1.status = 'DATA_NEW' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.correspondence_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpCorrespondenceSearchVo> vo = new ArrayList<TmpCorrespondenceSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpCorrespondenceSearchVo data = new TmpCorrespondenceSearchVo();
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
				data.setLetterReceivedDateStr(obj[18] != null ? (String)obj[18] : null);
				data.setLetterDateStr(obj[19] != null ? (String)obj[19] : null);
				data.setFollowDateStr(obj[20] != null ? (String)obj[20] : null);
				data.setConfirmationDateStr(obj[21] != null ? (String)obj[21] : null);
				data.setFullfillmentDateStr(obj[22] != null ? (String)obj[22] : null);
				data.setLetterDate(obj[23] != null ? (Date) obj[23] : null);
				data.setTargetDateStr(obj[24] != null ? (String)obj[24] : null );
				data.setPerihalEn(obj[25] != null ? FacesUtil.convertClobToString((Clob) obj[25]) : null);
				data.setPerihalIn(obj[26] != null ? FacesUtil.convertClobToString((Clob) obj[26]) : null);
				data.setStatusList(getDataConfirmStatusByCorrespondenceId(data.getCorrespondenceId()));
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getDataConfirmStatusByCorrespondenceId(Long correspondenceId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select u1.name pic1, u2.name pic2,u3.name pic3, "
				+ "TO_CHAR(c.confirmation_date, 'dd-Mon-yyyy')confirmation_date, "
				+ "TO_CHAR(c.followup_date, 'dd-Mon-yyyy')followup_date, "
				+ "c.followup_note, TO_CHAR(c.compliance_date, 'dd-Mon-yyyy') compliance_date, "
				+ "d.name_in compliance_statusIn,d.name_en compliance_statusEn, "
				+ "compliance_note, "
				+ "u4.name followupBy,d2.name_in,d2.name_en "
				+ ",u5.name as picHadir "
				+ "from wo_trc_correspondence c "
				+ "left join wo_mst_parameter_dtl d on c.compliance_status = d.parameter_dtl_code "
				+ "left join wo_mst_user u1 on u1.user_id = c.user_id_1 "
				+ "left join wo_mst_user u2 on u2.user_id = c.user_id_2 "
				+ "left join wo_mst_user u3 on u3.user_id = c.user_id_3 "
				+ "left join wo_mst_user u4 on u4.user_id = c.followup_by_id  "
				+ "left join wo_mst_parameter_dtl d2 on c.followup_status = d2.parameter_dtl_code "
				+ "left join wo_trc_crpdc_pic_fp_atdee ca on ca.correspondence_id = c.correspondence_id "
				+ "left join wo_mst_user u5 on u5.user_id = ca.user_id "
				+ "where c.correspondence_id = :correspondenceId ");

		sb.append(" ORDER BY c.correspondence_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("correspondenceId", correspondenceId);
		List resultList = result.getResultList();

		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				data.setConfirmationDate(obj[3] != null ? (String) obj[3] : null);
				data.setFollowupDate(obj[4] != null ? (String) obj[4] : null);
				data.setFollowupNote(obj[5] != null ? (String) obj[5] : null);
				data.setComplianceDate(obj[6] != null ? (String) obj[6] : null);
				data.setComplianceStatusIn(obj[7] != null ? (String) obj[7] : null);
				data.setComplianceStatusEn(obj[8] != null ? (String) obj[8] : null);
				data.setComplianceNote(obj[9] != null ? (String) obj[9] : null);
				data.setFollowupBy(obj[10] != null ? (String) obj[10] : null);
				data.setFollowupStatusIn(obj[11] != null ? (String) obj[11] : null);
				data.setFollowupStatusEn(obj[12] != null ? (String) obj[12] : null);
				data.setPicNameHadir(obj[13] != null ? (String) obj[13] : null);
				vo.add(data);
			}
		}

		return vo;
	}
	
	public Boolean hasReachedMaximumReschedule(Long correspondenceId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_tmp_crpdc_pic_fp_rschdl  ");
		sb.append(" where 1=1 ");
		sb.append(" and correspondence_id =  "+correspondenceId);
		Query query = getSession().createSQLQuery(sb.toString());

		//query.setParameter("correspondenceId", correspondenceId);
		
		Number count = (Number) query.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		StringBuilder sb2 = new StringBuilder();
		sb2.append(" SELECT name_in FROM wo_mst_parameter_dtl where parameter_dtl_code = 'MAX_RESCHEDULE_DATE' ");
	
		Query query2 = getSession().createSQLQuery(sb2.toString());

		Long maxRescheduleDate = new Long(((String) query2.getSingleResult()) );
		
		if( count.longValue() >= maxRescheduleDate.longValue() ) {
			return true;
		} else {
			return false;
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<TmpCorrespondenceSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append("	select 	c.correspondence_id, ");
		sb.append("			TO_CHAR(c.letter_received_date, 'dd-Mon-yyyy') letter_received_date, c.letter_no, ");
		sb.append("			TO_CHAR(c.letter_date, 'dd-Mon-yyyy') letter_date, ");
		sb.append("			c.perihal_in, c.perihal_en, c.letter_summary, ");
		sb.append("			c.follow_up, ");
		sb.append("			TO_CHAR(c2.target_date, 'dd-Mon-yyyy') target_date, picName1.name as picName1, picName2.name as picName2, picName3.name as picName3, ");
		sb.append("			(SELECT GROUP_CONCAT(DISTINCT supportingUnitName.name SEPARATOR ',') FROM wo_tmp_crpdc_sup_unit c3, wo_mst_user supportingUnitName WHERE supportingUnitName.user_id = c3.email_cc_id_1 and c3.correspondence_id = c.correspondence_id) supportingUnitName1, ");
		sb.append("			(SELECT GROUP_CONCAT(DISTINCT supportingUnitName.name SEPARATOR ',') FROM wo_tmp_crpdc_sup_unit c3, wo_mst_user supportingUnitName WHERE supportingUnitName.user_id = c3.email_cc_id_2 and c3.correspondence_id = c.correspondence_id) supportingUnitName2, ");
		sb.append("			(SELECT GROUP_CONCAT(DISTINCT supportingUnitName.name SEPARATOR ',') FROM wo_tmp_crpdc_sup_unit c3, wo_mst_user supportingUnitName WHERE supportingUnitName.user_id = c3.email_cc_id_3 and c3.correspondence_id = c.correspondence_id) supportingUnitName3, ");
//		sb.append("			supportingUnitName1.name as supportingUnitName1, supportingUnitName2.name as supportingUnitName2, supportingUnitName3.name as supportingUnitName3, pd3.name_en, pd3.name_in, ");
		sb.append("			pd3.name_en, pd3.name_in, ");
		sb.append("			picConfirmation.name as picConfirmation, ");
		sb.append("			TO_CHAR(c2.confirmation_date, 'dd-Mon-yyyy') confirmation_date, ");
		sb.append("			TO_CHAR(c2.followup_date, 'dd-Mon-yyyy') followup_date, c2.followup_note, ");
		sb.append("			c.status, u4.name followupBy, ");
		sb.append("			pd.name_en as SENDER_NAME_EN, pd.name_in as SENDER_NAME_IN, ");
		sb.append("			pd4.name_in as compilanceStatusIn, pd4.name_en as compilanceStatusEn, ");
		sb.append("			pd2.name_en as STATUS_NAME_EN, pd2.name_in as STATUS_NAME_IN, ");
		sb.append("			TO_CHAR(c2.compliance_date, 'dd-Mon-yyyy') compliance_date2 ");
		sb.append("			,( SELECT GROUP_CONCAT(u5.`name` SEPARATOR ',') ");
		sb.append("				from wo_trc_crpdc_pic_fp_atdee ca ");
		sb.append("					inner join wo_mst_user u5 ");
		sb.append("					on u5.user_id = ca.user_id where ca.correspondence_id = c.correspondence_id ");
		sb.append("				) nama ");
		sb.append("	from wo_tmp_correspondence c ");
		sb.append("		left join wo_mst_parameter_dtl pd on pd.parameter_dtl_code = c.SENDER_CODE ");
		sb.append("		left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = c.status ");
		sb.append("		left join wo_trc_correspondence c2 on c2.correspondence_id = c.correspondence_id ");
		sb.append("		left join wo_mst_parameter_dtl pd3 on pd3.parameter_dtl_code = c2.followup_status ");
		sb.append("		left join wo_mst_user picConfirmation on picConfirmation.user_id = c2.followup_by_id ");
		sb.append("		left join wo_mst_user u2 on u2.user_id = c2.followup_by_id ");
		sb.append("		left join wo_mst_parameter_dtl pd4 on pd4.parameter_dtl_code = c2.compliance_status ");
		sb.append("		left join wo_mst_user picName1 on picName1.user_id = c2.user_id_1 ");
		sb.append("		left join wo_mst_user picName2 on picName2.user_id = c2.user_id_2 ");
		sb.append("		left join wo_mst_user picName3 on picName3.user_id = c2.user_id_3 ");
		sb.append("		left join wo_mst_user u4 on u4.user_id = c2.followup_by_id ");
//		sb.append("		left join wo_tmp_crpdc_sup_unit c3 on c3.correspondence_id = c.correspondence_id ");
//		sb.append("		left join wo_mst_user supportingUnitName1 on supportingUnitName1.user_id = c3.email_cc_id_1 ");
//		sb.append("		left join wo_mst_user supportingUnitName2 on supportingUnitName2.user_id = c3.email_cc_id_2 ");
//		sb.append("		left join wo_mst_user supportingUnitName3 on supportingUnitName3.user_id = c3.email_cc_id_3 ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER_AML') t on t.parameter_dtl_code = c.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" 	and c.enabled_flag = 'Y' ");
		sb.append(" and t.parameter_dtl_code is not null ");
		
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append(" ORDER BY c.correspondence_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());
		List resultList = query.getResultList();

		List<TmpCorrespondenceSearchVo> vo = new ArrayList<TmpCorrespondenceSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpCorrespondenceSearchVo data = new TmpCorrespondenceSearchVo();
				//data.setCorrespondenceId(((BigInteger) obj[0]).longValue());
				data.setCorrespondenceId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterReceivedDateStr(obj[1] != null ? (String) obj[1] : null);
				data.setLetterNo(obj[2] != null ? (String) obj[2] : null);
				data.setLetterDateStr(obj[3] != null ? (String) obj[3] : null);
				data.setPerihalIn(obj[4] != null ? FacesUtil.convertClobToString((Clob) obj[4]) : null);
				data.setPerihalEn(obj[5] != null ? FacesUtil.convertClobToString((Clob) obj[5]) : null);
				data.setLetterSummary(obj[6] != null ? FacesUtil.convertClobToString((Clob) obj[6]) : null);
				data.setFollowUp(obj[7] != null ? (String) obj[7] : null);
				data.setTargetDateStr(obj[8] != null ? (String) obj[8] : null);
				data.setSupportingUnitName(obj[12] != null ? (String) obj[12] : null);
				if(obj[12] != null) {
					data.setSupportingUnitName((String) obj[12]);
				}
				if(obj[13] != null) {
					data.setSupportingUnitName(data.getSupportingUnitName() + ", " + (String) obj[13]);
				}
				if(obj[14] != null) {
					data.setSupportingUnitName(data.getSupportingUnitName() + ", " + (String) obj[14]);
				}
				data.setPicConfirmationName(obj[17] != null ? (String) obj[17] : null);
				data.setStatusCode(obj[21] != null ? (String) obj[21] : null);
				data.setSenderNameEn(obj[23] != null ? (String) obj[23] : null);
				data.setSenderNameIn(obj[24] != null ? (String) obj[24] : null);
				data.setStatusNameEn(obj[27] != null ? (String) obj[27] : null);
				data.setStatusNameIn(obj[28] != null ? (String) obj[28] : null);
				data.setFullfillmentDateStr(obj[29] != null ? (String) obj[29] : null);
				data.setPicAttendeeName(obj[30] != null ? (String) obj[30] : null);
				
				data.setStatusList(new ArrayList<>());
				StatusConfirmationVO dataConfirm = new StatusConfirmationVO();
				dataConfirm.setNamePic(obj[9] != null ? (String) obj[9] : null);
				
				if(obj[9] != null) {
					dataConfirm.setNamePic((String) obj[9]);
				}
				if(obj[10] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() + ", " + (String) obj[10]);
				}
				if(obj[11] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() + ", " + (String) obj[11]);
				}
				
				dataConfirm.setFollowupStatusEn(obj[15] != null ? (String) obj[15] : null);
				dataConfirm.setFollowupStatusIn(obj[16] != null ? (String) obj[16] : null);
				dataConfirm.setConfirmationDate(obj[18] != null ? (String) obj[18] : null);
				dataConfirm.setFollowupDate(obj[19] != null ? (String) obj[19] : null);
				dataConfirm.setFollowupNote(obj[20] != null ? (String) obj[20] : null);
				dataConfirm.setFollowupBy(obj[22] != null ? (String) obj[22] : null);
				dataConfirm.setComplianceStatusIn(obj[25] != null ? (String) obj[25] : null);
				dataConfirm.setComplianceStatusEn(obj[26] != null ? (String) obj[26] : null);
				
				data.getStatusList().add(dataConfirm);
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private StringBuilder getQueryWhereXLSString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
  		if (searchCriteria != null) {
  			for (SearchObject searchVal : searchCriteria) {
  				String col = searchVal.getSearchColumn();
  				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
  				
  				if(!StringUtils.isBlank(val)) {
  					if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and c.sender_code = '" + val + "' ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and c.letter_no LIKE '%" + val + "%' ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(c.letter_received_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(c.letter_received_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(c.letter_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(c.letter_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and c.perihal_en LIKE '%" + val + "%' ");
						} else {
							sb.append(" and c.perihal_in LIKE '%" + val + "%' ");
						}
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(c.target_date) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(c.target_date) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}else if (StringUtils.equals(TmpCorrespondenceAmlConstants.SEARCH_STATUS, col)) {
						sb.append(" and c.status = '" + val + "' ");
					}
  				}
  			}
  		}
  		return sb;
	}
}
