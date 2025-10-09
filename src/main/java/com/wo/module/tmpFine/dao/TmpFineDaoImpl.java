/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpFine.dao;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.dao.ParameterDetailDao;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.tmpFine.constant.TmpFineConstants;
import com.wo.module.tmpFine.model.TmpFine;
import com.wo.module.tmpFine.model.TmpFineApproval;
import com.wo.module.tmpFine.vo.TmpFineSearchVo;
import com.wo.module.trcFineApproval.dao.TrcFineApprovalDao;
import com.wo.module.user.dao.UserDao;
import com.wo.module.user.model.User;

@Repository("tmpFineDao")
public class TmpFineDaoImpl extends GenericDAOHibernate<TmpFine, Long> implements TmpFineDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TmpFineDaoImpl.class);
	
	@Autowired
	@Qualifier("trcFineApprovalDao")
	private TrcFineApprovalDao trcFineApprovalDao;
	
	@Autowired
	@Qualifier("userDao")
	private UserDao userDao;
	
	@Autowired
	@Qualifier("parameterDetailDao")
	private ParameterDetailDao parameterDetailDao;
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpFineConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and r1.sender_code = :searchPengirim ");
					}

					if (StringUtils.equals(TmpFineConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and UPPER(r1.letter_no) LIKE UPPER(:searchNoSurat) ");
					}

					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) >= TO_DATE(:searchTanggalTerimaSuratFrom,'yyyy-MM-dd') ");
					}

					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_received_date) <= TO_DATE(:searchTanggalTerimaSuratTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(r1.letter_date) >= TO_DATE(:searchTanggalSuratFrom,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(r1.letter_date) <= TO_DATE(:searchTanggalSuratTo,'yyyy-MM-dd') ");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and UPPER(r1.perihal_en) LIKE UPPER(:searchPerihal) ");
						} else {
							sb.append(" and UPPER(r1.perihal_in) LIKE UPPER(:searchPerihal) ");
						}
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and target_date >= TO_DATE(:searchTargetDateFrom,'yyyy-MM-dd') ) ");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and EXISTS (select fine_pic_followup_id from WO_TRC_FINE_PIC_FOLLOWUP where FINE_ID = r1.fine_id and target_date <= TO_DATE(:searchTargetDateTo,'yyyy-MM-dd')   ) ");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_STATUS, col)) {
						sb.append(" and r1.status = :searchStatus ");
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						sb.append(" and (u.DIVISION_NAME in (select DIVISION_NAME from wo_mst_user where user_id = :userId)"
								+ " 	or exists (select 1 from WO_TRC_FINE_PIC_COMPLIANCE wtspc where wtspc.fine_id = r1.fine_id and wtspc.user_id = :userId) OR exists (select 1 from wo_mst_user u2 inner join wo_mst_responsibility r2 on u2.responsibility_id = r2.responsibility_id and u2.user_id = :userId and UPPER(r2.name) = UPPER('Super Administrator'))) ");
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
					if (StringUtils.equals(TmpFineConstants.SEARCH_PENGIRIM, col)) {
						query.setParameter("searchPengirim",  val);
					}

					if (StringUtils.equals(TmpFineConstants.SEARCH_NO_SURAT, col)) {
						query.setParameter("searchNoSurat", "%" + val + "%");
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						query.setParameter("searchTanggalTerimaSuratFrom", val );
					}

					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						query.setParameter("searchTanggalTerimaSuratTo", val );
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						query.setParameter("searchTanggalSuratFrom", val );
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						query.setParameter("searchTanggalSuratTo",  val );
					}
					
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_PERIHAL, col)) {
						query.setParameter("searchPerihal", "%" + val + "%");
					}
					
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_FROM, col)) {
						query.setParameter("searchTargetDateFrom", val );
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_TO, col)) {
						query.setParameter("searchTargetDateTo",  val );
					}
					
					if (StringUtils.equals(TmpFineConstants.SEARCH_STATUS, col)) {
						query.setParameter("searchStatus", val);
					}
					
					if (StringUtils.equals(CommonConstants.SEARCH_BY_USER_LOGIN, col)) {
						query.setParameter("userId", val);
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
		sb.append(" from wo_tmp_fine r1 ");
		sb.append(" left join wo_mst_parameter_dtl pdSender on pdSender.parameter_dtl_code = r1.SENDER_CODE ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_trc_fine r2 on r2.fine_id = r1.fine_id ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') t on t.parameter_dtl_code = r1.sender_code ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = r1.CREATED_BY ");
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
	public List<TmpFineSearchVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {

		List<TmpFineSearchVo> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpFineSearchVo> searchDataCriteria(List<? extends SearchObject> searchCriteria,
			int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.fine_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" r1.status, ");
		sb.append("	r1.letter_date," );
		sb.append(" r1.perihal_en, r1.perihal_in ,pd1.name_in,pd1.name_en");
		sb.append(" from wo_tmp_fine r1 ");
		sb.append(" left join wo_mst_parameter_dtl pdSender on pdSender.parameter_dtl_code = r1.SENDER_CODE ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_trc_fine r2 on r2.fine_id = r1.fine_id ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') t on t.parameter_dtl_code = r1.sender_code ");
		sb.append("     INNER JOIN WO_MST_USER u ");
		sb.append("         ON u.nik = r1.CREATED_BY ");
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

		List<TmpFineSearchVo> vo = new ArrayList<TmpFineSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpFineSearchVo data = new TmpFineSearchVo();
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
	
	
	
	public Boolean hasReachedMaximumReschedule(Long fineId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM wo_tmp_crpdc_pic_fp_rschdl  ");
		sb.append(" where 1=1 ");
		sb.append(" and fine_id =  "+fineId);
		Query query = getSession().createSQLQuery(sb.toString());

		//query.setParameter("fineId", fineId);
		
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
	public List<TmpFineSearchVo> searchDataXLS(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.fine_id, ");
		sb.append(" r1.letter_no, ");
		sb.append(" pdSender.name_en as SENDER_NAME_EN, pdSender.name_in as SENDER_NAME_IN, ");
		sb.append(" r1.letter_received_date, ");
		sb.append(" r1.status, ");
		sb.append("	r1.letter_date," );
		sb.append(" r1.perihal_en, r1.perihal_in ,pd1.name_in,pd1.name_en,r1.follow_up");
		sb.append(" from wo_tmp_fine r1 ");
		sb.append(" left join wo_mst_parameter_dtl pdSender on pdSender.parameter_dtl_code = r1.SENDER_CODE ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");
		sb.append(" left join wo_trc_fine r2 on r2.fine_id = r1.fine_id ");
		sb.append(" left join (SELECT " + 
				"    d.parameter_dtl_code " + 
				"  FROM wo_mst_parameter p " + 
				"    INNER JOIN wo_mst_parameter_dtl d " + 
				"      ON d.parameter_code = p.parameter_code " + 
				"      AND p.parameter_code = 'SENDER') t on t.parameter_dtl_code = r1.sender_code ");
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");
		sb.append(" and t.parameter_dtl_code is not null ");
		sb = getQueryWhereXLSString(sb, searchCriteria);
		sb.append(" ORDER BY r1.fine_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());
		List resultList = query.getResultList();

		List<TmpFineSearchVo> vo = new ArrayList<TmpFineSearchVo>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpFineSearchVo data = new TmpFineSearchVo();
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
				data.setFollowUp((String) obj[11]);
				data.setTrcFine(trcFineApprovalDao.findById(data.getFineId()));
				
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
  					if (StringUtils.equals(TmpFineConstants.SEARCH_PENGIRIM, col)) {
						sb.append(" and c.sender_code = '" + val + "' ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_NO_SURAT, col)) {
						sb.append(" and c.letter_no LIKE '%" + val + "%' ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_FROM, col)) {
						sb.append(" and TRUNC(c.letter_received_date) >= TO_DATE('" + val + "','yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_TERIMA_SURAT_TO, col)) {
						sb.append(" and TRUNC(c.letter_received_date) <= TO_DATE('" + val + "','yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_FROM, col)) {
						sb.append(" and TRUNC(c.letter_date) >= TO_DATE('" + val + "','yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TANGGAL_SURAT_TO, col)) {
						sb.append(" and TRUNC(c.letter_date) <= TO_DATE('" + val + "','yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_PERIHAL, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and c.perihal_en LIKE '%" + val + "%' ");
						} else {
							sb.append(" and c.perihal_in LIKE '%" + val + "%' ");
						}
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_FROM, col)) {
						sb.append(" and TRUNC(c.target_date) >= TO_DATE('" + val + "','yyyy-MM-dd') ");
					} else if (StringUtils.equals(TmpFineConstants.SEARCH_TARGET_DATE_TO, col)) {
						sb.append(" and TRUNC(c.target_date) <= TO_DATE('" + val + "','yyyy-MM-dd') ");
					}else if (StringUtils.equals(TmpFineConstants.SEARCH_STATUS, col)) {
						sb.append(" and c.status = '" + val + "' ");
					}
  				}
  			}
  		}
  		return sb;
	}

	@Override
	public List<TmpFineApproval> getDataApprovalByFineId(Long fineId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" 	SELECT AP.FINE_APPROVAL_ID,	");
		sb.append(" 	       AP.USER_ID,	");
		sb.append(" 	       AP.APPROVAL_STATUS,	");
		sb.append(" 	       AP.APPROVAL_DATE,	");
		sb.append(" 	       AP.APPROVAL_NOTE	");
		sb.append(" 	  FROM WO_TMP_FINE_APPROVAL AP	");
		sb.append(" 	       LEFT JOIN WO_TRC_FINE F ON AP.FINE_ID = F.FINE_ID	");
		sb.append("  	WHERE 1=1 AND F.FINE_ID = '" + fineId + "' ");
		sb.append("            AND AP.ENABLED_FLAG = 'Y' ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		@SuppressWarnings("rawtypes")
		List resultList = query.getResultList();
		
		List<TmpFineApproval> appr = new ArrayList<TmpFineApproval>();
		
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpFineApproval data = new TmpFineApproval();

				try {
					data.setFineApprovalId(MathUtil.returnIdObjectToLong(obj[0]));
					
					Long userId = MathUtil.returnIdObjectToLong(obj[1]);
					
					User userAppr = userDao.findById(userId);
					
					if (userAppr != null) {
						data.setUser(userAppr);
					}
					
					String apprStatus = (String) obj[2];
					
					ParameterDetail param = parameterDetailDao.getParameterDetailByParamDtlCode(apprStatus);
					if (apprStatus != null) {
						data.setApprovalStatus(param);
					}

					data.setApprovalDate(obj[3] != null ? (Date) obj[3] : null);
					data.setApprovalNote((String) obj[4]);
					
					appr.add(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		
		return appr;
	}
	
	public TrcFineApprovalDao getTrcFineApprovalDao() {
		return trcFineApprovalDao;
	}

	public void setTrcFineApprovalDao(TrcFineApprovalDao trcFineApprovalDao) {
		this.trcFineApprovalDao = trcFineApprovalDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		TmpFineDaoImpl.logger = logger;
	}

	public ParameterDetailDao getParameterDetailDao() {
		return parameterDetailDao;
	}

	public void setParameterDetailDao(ParameterDetailDao parameterDetailDao) {
		this.parameterDetailDao = parameterDetailDao;
	}	
}
