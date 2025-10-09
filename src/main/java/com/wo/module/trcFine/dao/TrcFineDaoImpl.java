/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcFine.dao;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.trcFine.constant.TrcFineConstants;
import com.wo.module.trcFine.vo.TrcFineSearchVo;
import com.wo.module.trcFineApproval.constant.TrcFineApprovalConstants;
import com.wo.module.trcFineApproval.dao.TrcFineApprovalDao;
import com.wo.module.trcFineApproval.model.TrcFine;

@Repository("trcFineDao")
public class TrcFineDaoImpl extends GenericDAOHibernate<TrcFine, Long> implements TrcFineDao, TrcFineConstants {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TrcFineDaoImpl.class);

	@Autowired
	@Qualifier("trcFineApprovalDao")
	private TrcFineApprovalDao trcFineApprovalDao;
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TrcFineConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and r1.sender_code = :searchPengirim ");
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and r1.letter_no LIKE :searchNoSurat ");
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) >= TO_DATE(:searchTanggalTerimaSuratFrom, 'yyyy-MM-dd') ");
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) <= TO_DATE(:searchTanggalTerimaSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_date) >= TO_DATE(:searchTanggalSuratFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_date) <= TO_DATE(:searchTanggalSuratTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r1.perihal_en LIKE :searchPerihal ");
						} else {
							sb.append(" and r1.perihal_in LIKE :searchPerihal ");
						}
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(r1.target_date) >= TO_DATE(:searchTargetDateFrom, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(r1.target_date) <= TO_DATE(:searchTargetDateTo, 'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_STATUS, col)) {
						sb.append(" and r1.status = :searchStatus ");
					}
					
					if (StringUtils.equals(TrcFineApprovalConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and (user_id_1 = :userId or user_id_2 = :userId or user_id_3 = :userId )) ");
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
					if (StringUtils.equals(TrcFineConstants.SEARCH_PENGIRIM, col)) {
						query.setParameter("searchPengirim",  val);
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_NO_SURAT, col)) {
						query.setParameter("searchNoSurat", "%" + val + "%");
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						query.setParameter("searchTanggalTerimaSuratFrom", (Date) valReal );
					}

					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						query.setParameter("searchTanggalTerimaSuratTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						query.setParameter("searchTanggalSuratFrom", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						query.setParameter("searchTanggalSuratTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_PERIHAL, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TARGET_DATE_FROM, col)) {
						query.setParameter("searchTargetDateFrom", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_TARGET_DATE_TO, col)) {
						query.setParameter("searchTargetDateTo", (Date) valReal);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_STATUS, col)) {
						query.setParameter("searchStatus", val);
					}
					
					if (StringUtils.equals(TrcFineConstants.SEARCH_BY_USER_LOGIN, col)) {	
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
		sb.append(" from wo_trc_fine r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r1.reminder_status ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and (followup_status <> 'PIC_DONE' or followup_status is null) ) ");
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<TrcFineSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<TrcFineSearchVo> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TrcFineSearchVo> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.fine_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" r1.status, r1.reminder_status,r1.letter_date ");
		sb.append(" from wo_trc_fine r1 ");
		sb.append(" inner join (SELECT " + 
				"    d.* " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') pdSender on pdSender.parameter_dtl_code = r1.sender_code ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r1.reminder_status ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and (followup_status <> 'PIC_DONE' or followup_status is null) ) ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.fine_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TrcFineSearchVo> vo = new ArrayList<TrcFineSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				
				TrcFineSearchVo data = new TrcFineSearchVo();
				data.setFineId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterNo((String) obj[1]);
				data.setSenderNameEn((String) obj[2]);
				data.setSenderNameIn((String) obj[3]);
				data.setLetterReceivedDate(obj[4] != null ? (Date) obj[4] : null);
				data.setStatusNameEn((String) obj[5]);
				data.setStatusNameIn((String) obj[6]);
				data.setFollowupStatusNameEn((String) obj[7]);
				data.setFollowupStatusNameIn((String) obj[8]);
				data.setStatusCode((String) obj[9]);
				data.setFollowupStatusCode((String) obj[10]);
				data.setLetterDate(obj[11] != null ? (Date) obj[11] : null);
				
				data.setTrcFine(trcFineApprovalDao.findById(data.getFineId()));
				
				vo.add(data);				
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}

}
