/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpRmd.dao;

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
import com.wo.module.regulationSocialization.vo.StatusConfirmationVO;
import com.wo.module.tmpRmd.constant.TmpRmdConstants;
import com.wo.module.tmpRmd.model.TmpRmd;
//import com.wo.module.tmpRmdApproval.constant.TmpRmdApprovalConstants;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;

/**
 *
 * @author hendra
 */
@Repository("tmpRmdDao")
public class TmpRmdDaoImpl extends GenericDAOHibernate<TmpRmd, Long> implements TmpRmdDao {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(TmpRmdDaoImpl.class);

	@Autowired
	@Qualifier("tmpRmdRegulationDao")
	private TmpRmdRegulationDao tmpRmdRegulationDao;
	
	public TmpRmdRegulationDao getTmpRmdRegulationDao() {
		return tmpRmdRegulationDao;
	}

	public void setTmpRmdRegulationDao(TmpRmdRegulationDao tmpRmdRegulationDao) {
		this.tmpRmdRegulationDao = tmpRmdRegulationDao;
	}

	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValueAsString();
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_TYPE, col)) {
						sb.append(" and rt.report_type_id = :reportType ");
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and LOWER(r1.report_name_en) LIKE :reportName ");
						} else {
							sb.append(" and LOWER(r1.report_name_in) LIKE :reportName ");
						}
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_STATUS, col)) {
						sb.append(" and r1.status = :status ");
					}
					
					
					/*
					 * if (StringUtils.equals(RmdConstants.SEARCH_BY_NAME, col)) { if (locale !=
					 * null && locale.equals(locale.ENGLISH)) { sb.append(" and name LIKE :name ");
					 * } else { sb.append(" and name LIKE :name "); } }
					 */
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

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_TYPE, col)) {
						query.setParameter("reportType", val);
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_NAME, col)) {
						query.setParameter("reportName", "%" + val + "%");
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_STATUS, col)) {					
						query.setParameter("status", "" + val + "");						
					}
				}
			}
		}
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
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" left join wo_mst_report_type rt on rt.report_type_id = r1.report_type_id ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");		
		sb.append(" left join wo_trc_rmd r2 on r2.rmd_id = r1.rmd_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");		
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValue(query, searchCriteria);

		return (Number) query.getSingleResult();
	}

	@SuppressWarnings("rawtypes")
	public List<TmpRmd> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField,
			SortOrder sortOrder) throws Exception {

		List<TmpRmd> vo = searchDataCriteria(searchCriteria, first, pageSize);

		return vo;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<TmpRmd> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first, int pageSize) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select r1.rmd_id, r1.report_name_en,  r1.report_name_in,  ");
		sb.append(" rt.report_type_en,  rt.report_type_in, pic1.nik||'-'||pic1.name as PIC1_NAME, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" picConfirmation.name as PIC_CONFIRMATION_NAME, r2.confirmation_date, " );
		sb.append(" r2.followup_date, r2.followup_note, r1.status, "); //r2.followup_status  ");
		sb.append(" case when (select count(1) from wo_trc_rmd_pic_followup rpf where rpf.rmd_id = r1.rmd_id and rpf.followup_date is not null) = 0 then null else 'ADA ISI' end followup_Status ");
		sb.append(" ,pic2.name as PIC2_NAME, pic3.name as PIC3_NAME " );
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" left join wo_mst_report_type rt on rt.report_type_id = r1.report_type_id ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");		
		sb.append(" left join wo_trc_rmd r2 on r2.rmd_id = r1.rmd_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = r2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append(" left join wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = r2.followup_by_id ");
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");

		this.getQueryWhereString(sb, searchCriteria);
		sb.append(" ORDER BY r1.rmd_id DESC ");
		

		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
        query.setMaxResults(pageSize);
        

		this.getQuerySetValue(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpRmd> vo = new ArrayList<TmpRmd>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpRmd data = new TmpRmd();
				//data.setRmdId(((BigInteger) obj[0]).longValue());
				data.setRmdId(obj[0]!=null?MathUtil.returnIdObjectToLong(obj[0]):null);
				data.setReportNameEn(obj[1]!=null?(String) obj[1]:null);
				data.setReportNameIn(obj[2]!=null?(String) obj[2]:null);
				data.setReportTypeNameEn(obj[3]!=null?(String) obj[3]:null);
				data.setReportTypeNameIn(obj[4]!=null?(String) obj[4]:null);
				data.setPic1(obj[5]!=null?(String) obj[5]:null);
				data.setStatusNameEn(obj[6]!=null?(String) obj[6]:null);
				data.setStatusNameIn(obj[7]!=null?(String) obj[7]:null);
				data.setFollowupStatusNameEn(obj[8]!=null?(String) obj[8]:null);
				data.setFollowupStatusNameIn(obj[9]!=null?(String) obj[9]:null);
				data.setPicConfirmationName(obj[10]!=null?(String) obj[10]:null);

				if (obj[11] != null) {
					data.setConfirmationDate((Date) obj[11]);
				}

				if (obj[12] != null) {
					data.setFollowupDate((Date) obj[12]);
				}

				data.setFollowupNote(obj[13]!=null?(String) obj[13]:null);
				data.setStatusCode(obj[14]!=null?(String) obj[14]:null);
				data.setFollowupStatusCode(obj[15]!=null?(String) obj[15]:null);
				data.setPic2(obj[16] != null ? (String) obj[16] : null);
				data.setPic3(obj[17] != null ? (String) obj[17] : null);
				
				//data.setStatusList(getDataConfirmStatusByRmdId(data.getRmdId()));
				
				/*try {
					data.setRegulationDetails(tmpRmdRegulationDao.getTmpRmdRegulationByRmdId(data.getRmdId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Query targetSql = getSession().createSQLQuery("select distinct(target_date) from wo_tmp_rmd_pic_followup_email where rmd_id = :rmdId order by target_date");
				targetSql.setParameter("rmdId", data.getRmdId());
				List<Date> targetList = targetSql.getResultList();
				if(targetList!= null) {
					if (data.getPicFollowupDetails() == null)
						data.setPicFollowupDetails(new ArrayList<TrcRmdPicFollowup>());
					for (Date targetDate : targetList) {
						TrcRmdPicFollowup entity = new TrcRmdPicFollowup();
						entity.setTargetDate(targetDate);
						data.getPicFollowupDetails().add(entity);
						if (targetDate.compareTo(new Date()) > 0) {
							break;
						}
					}
				}
				
				String hql = " FROM TrcRmdPicFollowup where trcRmd.rmdId = :rmdId and enabledFlag = 'Y' order by trcRmd.targetDate asc";
				Query result = getSession().createQuery(hql);
				result.setParameter("rmdId", data.getRmdId());
				List<TrcRmdPicFollowup> picFollowupDetails = (List<TrcRmdPicFollowup>)result.getResultList();
				
				if(picFollowupDetails!= null) {
					if (data.getPicFollowupDetails() == null)
						data.setPicFollowupDetails(new ArrayList<TrcRmdPicFollowup>() );
					data.getPicFollowupDetails().addAll(picFollowupDetails);
					
				}*/
				
				
				vo.add(data);
			}
		}

		//query.setFirstResult(first);
		//query.setMaxResults(pageSize);

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public List<StatusConfirmationVO> getDataConfirmStatusByRmdId(Long rmdId) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select pic1.nik ||'-'|| pic1.name as PIC1_NAME, pic2.name as PIC2_NAME, pic3.name as PIC3_NAME "
				+ "	from wo_tmp_rmd r1 "
				+ "		left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 "
				+ "		left join wo_mst_user pic2 on pic2.user_id = r1.user_id_2 "
				+ " 	left join wo_mst_user pic3 on pic3.user_id = r1.user_id_3 "
				+ "where r1.rmd_id = :rmdId ");

		sb.append(" ORDER BY r1.rmd_id ASC ");

		Query result = getSession().createSQLQuery(sb.toString());
		result.setParameter("rmdId", rmdId);
		List resultList = result.getResultList();

		List<StatusConfirmationVO> vo = new ArrayList<StatusConfirmationVO>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				StatusConfirmationVO data = new StatusConfirmationVO();
				data.setPic1(obj[0] != null ? (String) obj[0] : null);
				data.setPic2(obj[1] != null ? (String) obj[1] : null);
				data.setPic3(obj[2] != null ? (String) obj[2] : null);
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	public Boolean isDataDuplicate(TmpRmd entity) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ct.rmd_id ");
		sb.append(" from wo_tmp_rmd ct ");
		sb.append(" where 1=1 ");
		sb.append(" and ct.enabled_flag = 'Y' ");
		sb.append(" and ct.report_type_id = :reportTypeId ");
		sb.append(" and ct.report_name_in = :reportNameIn ");

		if (entity.getRmdId() != null) {
			sb.append(" and ct.rmd_id <> :rmdId ");
		}

		Query query = getSession().createSQLQuery(sb.toString());

		query.setParameter("reportTypeId", entity.getReportType().getReportTypeId());
		query.setParameter("reportNameIn", entity.getReportTypeNameIn());

		if (entity.getRmdId() != null) {
			query.setParameter("rmdId", entity.getRmdId());
		}

		List resultList = query.getResultList();

		if (resultList != null && resultList.size() > 0) {
			return true;
		}

		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public List<TmpRmd> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception {

		List<TmpRmd> vo = searchDataCriteriaXls(searchCriteria);

		return vo;
	}

	@SuppressWarnings("rawtypes")
	private List<TmpRmd> searchDataCriteriaXls(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct r1.rmd_id, r1.report_name_en,  r1.report_name_in,  ");
		sb.append(" rt.report_type_en,  rt.report_type_in, pic1.name as PIC1_NAME, ");
		sb.append(" pd1.name_en as STATUS_NAME_EN, pd1.name_in as STATUS_NAME_IN, ");
		sb.append(" pd2.name_en as FOLLOWUP_STATUS_NAME_EN, pd2.name_in as FOLLOWUP_STATUS_NAME_IN, ");
		sb.append(" picConfirmation.name as PIC_CONFIRMATION_NAME, rf2.confirmation_date, ");
		sb.append(" rf2.followup_date, rf2.followup_note, r1.status, rf2.followup_status  ");
		sb.append(" ,pic2.name as PIC2_NAME,pic3.name as PIC3_NAME ");
		sb.append(" ,get_unit_name_by_rmd_id(r1.rmd_id) as UNIT1, '' as UNIT2, '' as UNIT3 ");
		//sb.append("	,(SELECT GROUP_CONCAT(DISTINCT unit.name SEPARATOR ',') FROM wo_tmp_rmd_supporting_unit rsu, wo_mst_user unit WHERE unit.user_id = rsu.email_cc_id_1 and rsu.rmd_id = r1.rmd_id) UNIT1 ");
		//sb.append("	,(SELECT GROUP_CONCAT(DISTINCT unit.name SEPARATOR ',') FROM wo_tmp_rmd_supporting_unit rsu, wo_mst_user unit WHERE unit.user_id = rsu.email_cc_id_2 and rsu.rmd_id = r1.rmd_id) UNIT2 ");
		//sb.append("	,(SELECT GROUP_CONCAT(DISTINCT unit.name SEPARATOR ',') FROM wo_tmp_rmd_supporting_unit rsu, wo_mst_user unit WHERE unit.user_id = rsu.email_cc_id_3 and rsu.rmd_id = r1.rmd_id) UNIT3 ");
		sb.append(" ,r1.dedicated_to, r1.sanctions ");
//		sb.append(" ,TO_CHAR(rf2.target_date, 'dd-Mon-yyyy') target_date1 ");
		sb.append(" ,TO_CHAR(coalesce(rf2.target_date, ema.target_date), 'dd-Mon-yyyy') target_date1 ");
		sb.append(" ,TO_CHAR(rf2.confirmation_date, 'dd-Mon-yyyy') confirmation_date2 ");
		sb.append(" ,TO_CHAR(rf2.followup_date, 'dd-Mon-yyyy') followup_date2 ");
		sb.append(" ,r1.note ");
		sb.append(" from wo_tmp_rmd r1 ");
		sb.append(" left join wo_mst_report_type rt on rt.report_type_id = r1.report_type_id ");
		sb.append(" left join wo_mst_parameter_dtl pd1 on pd1.parameter_dtl_code = r1.status ");		
		sb.append(" left join wo_trc_rmd r2 on r2.rmd_id = r1.rmd_id ");
		sb.append(" left join wo_trc_rmd_pic_followup rf2 on r2.rmd_id = rf2.rmd_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = rf2.followup_status ");
		sb.append(" left join wo_mst_user pic1 on pic1.user_id = r1.user_id_1 ");
		sb.append(" left join wo_mst_user pic2 on pic2.user_id = r1.user_id_2 ");
		sb.append(" left join wo_mst_user pic3 on pic3.user_id = r1.user_id_3 ");
		sb.append(" left join wo_mst_user picConfirmation on picConfirmation.user_id = rf2.followup_by_id ");
//		sb.append(" left join wo_tmp_rmd_supporting_unit rsu on rsu.rmd_id = r1.rmd_id ");
//		sb.append(" left join wo_mst_user unit1 on unit1.user_id = rsu.email_cc_id_1 ");
//		sb.append(" left join wo_mst_user unit2 on unit2.user_id = rsu.email_cc_id_2 ");
//		sb.append(" left join wo_mst_user unit3 on unit3.user_id = rsu.email_cc_id_3 ");
		
		sb.append(" left join (select distinct target_date, rmd_id from wo_tmp_rmd_pic_followup_email) ema ");
		sb.append(" on ema.rmd_id = r1.rmd_id and ((ema.target_date != rf2.target_date) or (ema.target_date is not null and rf2.target_date is null)) ");
		
		sb.append(" where 1=1 ");
		sb.append(" and r1.enabled_flag = 'Y' ");

		this.getQueryWhereStringXls(sb, searchCriteria);
		sb.append(" ORDER BY r1.rmd_id DESC ");

		Query query = getSession().createSQLQuery(sb.toString());

		this.getQuerySetValueXls(query, searchCriteria);

		List resultList = query.getResultList();

		List<TmpRmd> vo = new ArrayList<TmpRmd>();

		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				TmpRmd data = new TmpRmd();
				data.setRmdId(obj[0]!=null?MathUtil.returnIdObjectToLong(obj[0]):null);
				data.setReportNameEn(obj[1]!=null?(String) obj[1]:null);
				data.setReportNameIn(obj[2]!=null?(String) obj[2]:null);
				data.setReportTypeNameEn(obj[3]!=null?(String) obj[3]:null);
				data.setReportTypeNameIn(obj[4]!=null?(String) obj[4]:null);
				data.setPic1(obj[5]!=null?(String) obj[5]:null);
				data.setStatusNameEn(obj[6]!=null?(String) obj[6]:null);
				data.setStatusNameIn(obj[7]!=null?(String) obj[7]:null);
				data.setFollowupStatusNameEn(obj[8]!=null?(String) obj[8]:null);
				data.setFollowupStatusNameIn(obj[9]!=null?(String) obj[9]:null);
				data.setPicConfirmationName(obj[10]!=null?(String) obj[10]:null);

				if (obj[11] != null) {
					data.setConfirmationDate((Date) obj[11]);
				}

				if (obj[12] != null) {
					data.setFollowupDate((Date) obj[12]);
				}

				data.setFollowupNote(obj[13]!=null?(String) obj[13]:null);
				data.setStatusCode(obj[14]!=null?(String) obj[14]:null);
				data.setFollowupStatusCode(obj[15]!=null?(String) obj[15]:null);
				
				data.setSupportingUnitName(obj[18] != null ? (String) obj[18] : null);
				/*if(obj[18] != null) {
					data.setSupportingUnitName(data.getSupportingUnitName());
				}
				if(obj[19] != null) {
					data.setSupportingUnitName(data.getSupportingUnitName() +", "+ (String) obj[19]);
				}
				if(obj[20] != null) {
					data.setSupportingUnitName(data.getSupportingUnitName() +", "+ (String) obj[20]);
				}*/
				data.setDedicatedTo(obj[21] != null ? (String) obj[21] : null);
				data.setSanctions(obj[22] != null ? (String) obj[22] : null);
				data.setNote(obj[26] != null ? (String) obj[26] : null);
				
				data.setStatusList(new ArrayList<>());
				StatusConfirmationVO dataConfirm = new StatusConfirmationVO();
				dataConfirm.setNamePic(obj[5] != null ? (String) obj[5] : null);
				if(obj[5] != null ) {
					dataConfirm.setNamePic(dataConfirm.getNamePic());
				}
				if(obj[16] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() +", "+ (String) obj[16]);
				}
				if(obj[17] != null) {
					dataConfirm.setNamePic(dataConfirm.getNamePic() +", "+ (String) obj[17]);
				}
				dataConfirm.setFollowupNote(obj[13] != null ? (String) obj[13] : null); 
				dataConfirm.setFollowupStatusEn(obj[8] != null ? (String) obj[8] : null);
				dataConfirm.setFollowupStatusIn(obj[9] != null ? (String) obj[9] : null);
				dataConfirm.setFollowupBy(obj[10] != null ? (String) obj[10] : null);
				dataConfirm.setConfirmationDate(obj[24] != null ? (String) obj[24] : null);
				dataConfirm.setFollowupDate(obj[25] != null ? (String) obj[25] : null);
				dataConfirm.setTargetDate(obj[23] != null ? (String) obj[23] : null );
				
				data.getStatusList().add(dataConfirm);
				
				
				try {
					data.setRegulationDetails(tmpRmdRegulationDao.getTmpRmdRegulationByRmdId(data.getRmdId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				vo.add(data);
			}
		}

		return vo;
	}
	
	@SuppressWarnings({ "rawtypes", "static-access" })
	private void getQueryWhereStringXls(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_TYPE, col)) {
						sb.append(" and rt.report_type_id = :reportType ");
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_NAME, col)) {
						if (locale != null && locale.equals(locale.ENGLISH)) {
							sb.append(" and LOWER(r1.report_name_en) LIKE :reportName ");
						} else {
							sb.append(" and LOWER(r1.report_name_in) LIKE :reportName ");
						}
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_STATUS, col)) {
						sb.append(" and r1.status = :status ");
					}
					
					
					/*
					 * if (StringUtils.equals(RmdConstants.SEARCH_BY_NAME, col)) { if (locale !=
					 * null && locale.equals(locale.ENGLISH)) { sb.append(" and name LIKE :name ");
					 * } else { sb.append(" and name LIKE :name "); } }
					 */
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void getQuerySetValueXls(Query query, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";

				if (!StringUtils.isBlank(val)) {
					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_TYPE, col)) {
						query.setParameter("reportType", val);
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_REPORT_NAME, col)) {
						query.setParameter("reportName", "%" + val + "%");
					}

					if (StringUtils.equals(TmpRmdConstants.SEARCH_BY_STATUS, col)) {					
						query.setParameter("status", "" + val + "");						
					}
				}
			}
		}
	}

	@Override
	public Integer getTmpRmdByNameIn(String reportNameIn, Long reportTypeId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1) ");
		sb.append("		FROM wo_tmp_rmd ");
		sb.append("		WHERE report_name_in = '"+ reportNameIn +"' ");
		sb.append("			AND report_type_id = " + reportTypeId);
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue() ;
	}

	@Override
	public Integer getTmpRmdByIdAndNameIn(Long id, String reportNameIn, Long reportTypeId) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT COUNT(1)"
				+ "		FROM wo_tmp_rmd ");
		sb.append("		WHERE rmd_id <> '" + id + "'");
		sb.append("			AND report_name_in = '" + reportNameIn + "' ");
		sb.append("			AND report_type_id = " + reportTypeId);
		sb.append("			AND enabled_flag = 'Y' ");
		
		Query result = getSession().createSQLQuery(sb.toString());
		
		Number count = (Number) result.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		return (Integer) count.intValue();
	}
	
	@SuppressWarnings("rawtypes")
	public List<TrcRmdPicFollowup> getRmdPicFollowupByRmdId(Long rmdId) {
		String hql = " FROM TrcRmdPicFollowup where trcRmd.rmdId = :rmdId and enabledFlag = 'Y' order by trcRmd.targetDate asc";
		Query result = getSession().createQuery(hql);
		result.setParameter("rmdId", rmdId);
		List<TrcRmdPicFollowup> picFollowupDetails = (List<TrcRmdPicFollowup>)result.getResultList();
		return picFollowupDetails;
	}

}