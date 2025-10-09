package com.wo.module.trcFineView.dao;

import java.sql.Clob;
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
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.trcFineApproval.dao.TrcFineApprovalDao;
import com.wo.module.trcFineApproval.model.TrcFine;
import com.wo.module.trcFineView.constants.TrcFineViewConstants;
import com.wo.module.trcFineView.vo.TrcFineViewSearchVO;

@Repository("trcFineViewDAO")
public class TrcFineViewDAOImpl extends GenericDAOHibernate<TrcFine, Long>
	implements TrcFineViewDAO {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TrcFineViewDAOImpl.class);
	
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
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and r1.sender_code = :searchPengirim ");
					}

					if (StringUtils.equals(TrcFineViewConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and r1.letter_no LIKE :searchNoSurat ");
					}

					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and r1.letter_received_date >= TO_DATE(:searchTanggalTerimaSuratFrom,'yyyy-MM-dd') ");
					}

					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) <= TO_DATE(:searchTanggalTerimaSuratTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_date) >= TO_DATE(:searchTanggalSuratFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_date) <= TO_DATE(:searchTanggalSuratTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and r1.perihal_en LIKE :searchPerihal ");
						} else {
							sb.append(" and r1.perihal_in LIKE :searchPerihal ");
						}
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and target_date >= TO_DATE(:searchTargetDateFrom,'yyyy-MM-dd') ) ");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and target_date <= TO_DATE(:searchTargetDateTo,'yyyy-MM-dd')   ) ");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_STATUS, col)) {
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
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_PENGIRIM, col)) {
						query.setParameter("searchPengirim",  val);
					}

					if (StringUtils.equals(TrcFineViewConstants.SEARCH_NO_SURAT, col)) {
						query.setParameter("searchNoSurat", "%" + val + "%");
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						query.setParameter("searchTanggalTerimaSuratFrom", val );
					}

					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						query.setParameter("searchTanggalTerimaSuratTo", val );
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						query.setParameter("searchTanggalSuratFrom", val );
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						query.setParameter("searchTanggalSuratTo",  val );
					}
					
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_PERIHAL, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TARGET_DATE_FROM, col)) {
						query.setParameter("searchTargetDateFrom", val );
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_TARGET_DATE_TO, col)) {
						query.setParameter("searchTargetDateTo",  val );
					}
					
					if (StringUtils.equals(TrcFineViewConstants.SEARCH_STATUS, col)) {
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
		sb.append(" from WO_TRC_FINE r1 ");
		sb.append(" left join wo_mst_parameter_dtl pdSender on pdSender.parameter_dtl_code = r1.SENDER_CODE ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') t on t.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and t.parameter_dtl_code is not null ");
		//sb.append(" and r1.status = 'DATA_NEW' ");
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List<TrcFineViewSearchVO> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<TrcFineViewSearchVO> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TrcFineViewSearchVO> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.fine_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" r1.status, ");
		sb.append("	r1.letter_date," );
		sb.append(" r1.perihal_en, r1.perihal_in ,pd1.name_in,pd1.name_en");
		sb.append(" from WO_TRC_FINE r1 ");
		sb.append(" left join wo_mst_parameter_dtl pdSender on pdSender.parameter_dtl_code = r1.SENDER_CODE ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') t on t.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and t.parameter_dtl_code is not null ");
		//sb.append(" and r1.status = 'DATA_NEW' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.fine_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TrcFineViewSearchVO> vo = new ArrayList<TrcFineViewSearchVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TrcFineViewSearchVO data = new TrcFineViewSearchVO();
				data.setFineId(MathUtil.returnIdObjectToLong(obj[0]));
				data.setLetterNo((String) obj[1]);
				data.setSenderNameEn((String) obj[2]);
				data.setSenderNameIn((String) obj[3]);
				data.setLetterReceivedDate(obj[4] != null ? (Date) obj[4] : null);
				data.setStatusCode((String) obj[5]);
				data.setLetterDate(obj[6] != null ? (Date) obj[6] : null);
				data.setPerihalEn(obj[7] != null ?  FacesUtil.convertClobToString((Clob)obj[7]) : null);
				data.setPerihalIn(obj[8] != null ?  FacesUtil.convertClobToString((Clob)obj[8]) : null);
				data.setStatusNameIn((String) obj[9]);
				data.setStatusNameEn((String) obj[10]);
				
				data.setTrcFine(trcFineApprovalDao.findById(data.getFineId()));
				vo.add(data);
			}
		}

		query.setFirstResult(first);
		query.setMaxResults(pageSize);

		return vo;
	}
	
	public TrcFineApprovalDao getTrcFineApprovalDao() {
		return trcFineApprovalDao;
	}

	public void setTrcFineApprovalDao(TrcFineApprovalDao trcFineApprovalDao) {
		this.trcFineApprovalDao = trcFineApprovalDao;
	}
	
}