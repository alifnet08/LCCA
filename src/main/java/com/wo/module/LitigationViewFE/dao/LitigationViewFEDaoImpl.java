package com.wo.module.LitigationViewFE.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.LitigationViewFE.vo.LitigationAttachmentViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationDetailViewFEVo;
import com.wo.module.LitigationViewFE.vo.LitigationViewFEVo;
import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigation.model.Litigation;

@Repository("litigationViewFEDao")
public class LitigationViewFEDaoImpl extends GenericDAOHibernate<Litigation, Long>
	implements LitigationViewFEDao, Serializable{

	private static final long serialVersionUID = 4054234960466940078L;

	@SuppressWarnings("rawtypes")
	private void setQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
//						sb.append(" AND l.CASE_TYPE = :jenisPekara ");
//					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						sb.append(" AND UPPER(DEBITUR) LIKE UPPER(:debitur) ");
					}
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
//						sb.append(" and (u.DIVISION_NAME = (select DIVISION_NAME from wo_mst_user where user_id = :userId) "
//								+ "OR ("
//								+ "(select USER_ID from wo_mst_user where user_id = :userId) IN "
//								+ "(select USER_ID from WO_MST_LITIGATION_VIEWER where LITIGATION_ID = l.litigation_id)"
//								+ ")) ");
//					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void setQuerySetString(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				
				if (!StringUtils.isBlank(val)) {
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_COMBO_BOX, col)) {
//						query.setParameter("jenisPekara", val);
//					}
					if (StringUtils.equals(CommonConstants.SEARCH_BY_TEXT_BOX, col)) {
						String debitur = "%"+val+"%";
						query.setParameter("debitur", debitur);
					}
//					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
//						query.setParameter("userId", val);
//					}
					
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationViewFEVo> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize,
			String sortField, SortOrder sortOrder) throws Exception {
		List<LitigationViewFEVo> vo = searchDataCriteria(searchCriteria, first, pageSize);
		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<LitigationViewFEVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize){
		List<LitigationViewFEVo> voList = new ArrayList<>();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT "
				+ " lpt.CASE_NUMBER, "
				+ " lpt.DEBITUR ");
		sb.append(" FROM WO_MST_LITI_PERDATA_TERGUGAT lpt ");
		sb.append(" INNER JOIN WO_MST_LITIGATION l ON lpt.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND lpt.ENABLED_FLAG <> 'N' ");
		sb.append(" AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PERDATA_TERGUGAT' ");
		
		this.setQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY lpt.CREATION_DATE DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		this.setQuerySetString(query, searchCriteria);
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List resultList = query.getResultList();
		
		for(int i=0; i< resultList.size(); i++) {
			Object[] obj = (Object[]) resultList.get(i);
			LitigationViewFEVo vo = new LitigationViewFEVo();
			
			vo.setNoPerkara(obj[0] != null ? obj[0].toString() : "");
			vo.setDebitur(obj[1] != null ? obj[1].toString() : "");
			
			voList.add(vo);
		}
		
		return voList;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		Number result = searchCountDataCriteria(searchCriteria);
		
		if (result == null) {
			result = 0;
		}
		
		return result.longValue();
	}
	
	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT COUNT(*) ");
		sb.append(" FROM WO_MST_LITI_PERDATA_TERGUGAT lpt ");
		sb.append(" INNER JOIN WO_MST_LITIGATION l ON lpt.LITIGATION_ID = l.LITIGATION_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND lpt.ENABLED_FLAG <> 'N' ");
		sb.append(" AND l.CASE_TYPE_DTL = 'CASE_TYPE_DTL_PERDATA_TERGUGAT' ");
		sb.append(" ORDER BY lpt.CREATION_DATE DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		Number result = (Number) query.getSingleResult();
		
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationDetailViewFEVo> getLitigationDtlListsByLitigationId(Long litigationId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT ld.LITIGATION_DTL_ID DETAIL_ID ");
		sb.append("       ,ld.LITIGATION_ID HEADER_ID ");
		sb.append("       ,ld.COURT_TYPE COURT_CODE ");
		sb.append("       ,pdCourtType.NAME_IN COURT_IN ");
		sb.append("       ,pdCourtType.NAME_EN COURT_EN ");
		sb.append("       ,ld.COURT_NAME NAME_CODE ");
		sb.append("       ,pdCourtName.NAME_IN NAME_IN ");
		sb.append("       ,pdCourtName.NAME_EN NAME_EN ");
		sb.append("       ,ld.COURT_DATE ");
		sb.append("       ,TO_CHAR(ld.COURT_DATE, 'dd FMMonth yyyy', 'nls_date_language=indonesian') COURT_DATE_STR ");
		sb.append("       ,ld.PROGRESS ");
		sb.append("       ,ld.SENTENCE SENTENCE_CODE ");
		sb.append("       ,pdSentence.NAME_IN SENTENCE_IN ");
		sb.append("       ,pdSentence.NAME_EN SENTENCE_EN ");
		sb.append("       ,ld.NOTE ");
		sb.append("       ,ld.LEGAL_EFFORT EFFORT_CODE ");
		sb.append("       ,pdEffort.NAME_IN EFFORT_IN ");
		sb.append("       ,pdEffort.NAME_EN EFFORT_EN ");
		sb.append(" FROM WO_MST_LITIGATION_DTL ld ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCourtType ");
		sb.append("         ON pdCourtType.PARAMETER_DTL_CODE = ld.COURT_TYPE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdCourtName ");
		sb.append("         ON pdCourtName.PARAMETER_DTL_CODE = ld.COURT_NAME ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdSentence ");
		sb.append("         ON pdSentence.PARAMETER_DTL_CODE = ld.SENTENCE ");
		sb.append("     LEFT JOIN WO_MST_PARAMETER_DTL pdEffort ");
		sb.append("         ON pdEffort.PARAMETER_DTL_CODE = ld.LEGAL_EFFORT ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND ld.LITIGATION_ID = :litigationId ");
		sb.append("     AND ld.ENABLED_FLAG = 'Y' ");
		sb.append("     AND ld.COURT_DATE IS NOT NULL ");
		sb.append(" ORDER BY ld.LITIGATION_DTL_ID ASC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		
		List result = query.getResultList();
		List<LitigationDetailViewFEVo> vo = new ArrayList<LitigationDetailViewFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationDetailViewFEVo data = new LitigationDetailViewFEVo();
				
				data.setLitigationDtlId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setLitigationId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setCourtTypeCode(obj[2] != null ? (String) obj[2] : null);
				data.setCourtTypeIn(obj[3] != null ? (String) obj[3] : null);
				data.setCourtTypeEn(obj[4] != null ? (String) obj[4] : null);
				data.setCourtNameCode(obj[5] != null ? (String) obj[5] : null);
				data.setCourtNameIn(obj[6] != null ? (String) obj[6] : null);
				data.setCourtNameEn(obj[7] != null ? (String) obj[7] : null);
				data.setCourtDate(obj[8] != null ? (Date) obj[8] : null);
				data.setCourtDateStr(obj[9] != null ? (String) obj[9] : null);
				data.setProgress(obj[10] != null ? (String) obj[10] : null);
				data.setSentenceCode(obj[11] != null ? (String) obj[11] : null);
				data.setSentenceIn(obj[12] != null ? (String) obj[12] : null);
				data.setSentenceEn(obj[13] != null ? (String) obj[13] : null);
				data.setNote(obj[14] != null ? (String) obj[14] : null);
				data.setLegalEffortCode(obj[15] != null ? (String) obj[15] : null);
				data.setLegalEffortIn(obj[16] != null ? (String) obj[16] : null);
				data.setLegalEffortEn(obj[17] != null ? (String) obj[17] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationAttachmentViewFEVo> getLitigationAttachListsByLitigationIdAndCourtType(Long litigationId,
			String courtType) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT la.LITIGATION_ATTACHMENT_ID DETAIL_ID ");
		sb.append("       ,la.LITIGATION_ID HEADER_ID ");
		sb.append("       ,la.ATTACHMENT_CODE ");
		sb.append("       ,la.FILE_ID ");
		sb.append("       ,la.FILE_SIZE ");
		sb.append("       ,la.ATTACHMENT_FILE ");
		sb.append(" FROM WO_MST_LITIGATION_ATTACHMENT la ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND la.LITIGATION_ID = :litigationId ");
		sb.append("     AND la.ATTACHMENT_CODE = :attachmentCode ");
		sb.append("     AND la.ENABLED_FLAG = 'Y' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("litigationId", litigationId);
		query.setParameter("attachmentCode", courtType);
		
		List result = query.getResultList();
		List<LitigationAttachmentViewFEVo> vo = new ArrayList<LitigationAttachmentViewFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationAttachmentViewFEVo data = new LitigationAttachmentViewFEVo();
				
				data.setLitigationAttachmentId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setLitigationId(obj[1] != null ? MathUtil.returnIdObjectToLong(obj[1]) : null);
				data.setAttachmentCode(obj[2] != null ? (String) obj[2] : null);
				data.setFileId(obj[3] != null ? (String) obj[3] : null);
				data.setFileSize(obj[4] != null ? ((BigDecimal) obj[4]).longValue() : null);
				data.setAttachmentFile(obj[5] != null ? (String) obj[5] : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
