package com.wo.module.internalRegulationObsolete.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.common.util.MathUtil;
import com.wo.module.internalRegulationObsolete.constant.InternalRegulationObsoleteConstants;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationObsolete;
import com.wo.module.internalRegulationObsolete.model.InternalRegulationTest;
import com.wo.module.internalRegulationObsolete.vo.InternalRegulationObsoleteVo;
import com.wo.module.user.model.User;

@Repository("internalRegulationObsoleteDao")
public class InternalRegulationObsoleteDaoImpl 
extends GenericDAOHibernate<InternalRegulationObsolete, Long> 
implements InternalRegulationObsoleteDao, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1185965275205744356L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationTest> findAllInternalRegulationTest() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT irt.internal_regulation_id, irt.internal_regulation_title ");
		sb.append("FROM wo_tmp_internal_reg_test irt ");
		sb.append("WHERE 1=1 ");
		sb.append("ORDER BY irt.internal_regulation_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List resultList = query.getResultList();
		
		List<InternalRegulationTest> internalRegulationTestList = 
				new ArrayList<InternalRegulationTest>();
		
		if(resultList != null) {
			for(int i=0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				InternalRegulationTest intRegTest = new InternalRegulationTest();
				
				intRegTest.setInternalRegulationId(MathUtil.returnIdObjectToLong(obj[0]));
				intRegTest.setInternalRegulationTitle(obj[1].toString());
				
				internalRegulationTestList.add(intRegTest);
			}
		}
		
		return internalRegulationTestList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationObsoleteVo> searchData(List<? extends SearchObject> searchCriteria, int first,
			int pageSize, String sortField, SortOrder sortOrder) throws Exception {
		
		List<InternalRegulationObsoleteVo> internalRegulationObsoleteVOList = searchDataCriteria(searchCriteria, first, pageSize);
		
		return internalRegulationObsoleteVOList;
	}
	
	@SuppressWarnings("rawtypes")
	private List<InternalRegulationObsoleteVo> searchDataCriteria(List<? extends SearchObject> searchCriteria, int first,
			int pageSize) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT "
				+ " wtio.IRG_OBSOLETE_ID, "
				+ " wtio.OBSOLETE_TITLE, "
				+ " wmpd.NAME_IN AS OBSOLETE_TYPE_STR, "
				+ " wtio.OBSOLETE_NUMBER, "
				+ " wtio.OBSOLETE_INFO, "
				+ " wmd.DIVISION_NAME, "
				+ " wtio.PUBLISH_DIRECTORATE_NAME, "
				+ " TO_CHAR(wtio.OBSOLETE_DATE,'DD-MON-YYYY'), "
				+ " wmpd2.NAME_IN AS OBSOLETE_STATUS_STR, "
				+ " wmu.NAME AS PIC_IRG_NAME_1 ");
		sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL wmpd ON wtio.REG_OBSOLETE_TYPE = wmpd.PARAMETER_DTL_ID ");
		sb.append(" INNER JOIN WO_MST_PARAMETER_DTL wmpd2 ON wtio.OPEN_CLOSE_REG_OBSOLETE = wmpd2.PARAMETER_DTL_ID ");
		sb.append(" INNER JOIN WO_MST_DIVISION wmd ON wtio.PUBLISH_DIVISION_ID = wmd.DIVISION_ID ");
		sb.append(" LEFT JOIN WO_MST_USER wmu ON wtio.PIC_IRG_USER_ID_1 = wmu.USER_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND wtio.ENABLED_FLAG = 'Y' ");
		
		this.getQueryWhereString(sb, searchCriteria);
		
		sb.append(" ORDER BY wtio.IRG_OBSOLETE_ID DESC ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setFirstResult(first);
		query.setMaxResults(pageSize);
		
		List resultList = query.getResultList();
		List<InternalRegulationObsoleteVo> internalRegulationObsoVoList = new ArrayList<InternalRegulationObsoleteVo>();
		
		if(resultList != null) {
			for(int i=0; i < resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				InternalRegulationObsoleteVo vo = new InternalRegulationObsoleteVo();
				
				vo.setInternalRegulasiObsoleteId(MathUtil.returnIdObjectToLong(obj[0]));
				vo.setJudulRegulasi(obj[1].toString());
				vo.setTipeRegulasiObsolete(obj[2].toString());
				vo.setNomorObsolete(obj[3].toString());
				vo.setInfoObsolete(obj[4].toString());
				vo.setPublishDivision(obj[5].toString());
				vo.setPublishDirectorateName(obj[6] != null ? obj[6].toString() : "");
				vo.setTanggalObsoleteStr(obj[7] != null ? obj[7].toString() : "");
				vo.setStatusOpenCloseObsolete(obj[8].toString());
				vo.setPicIrgName(obj[9] != null ? obj[9].toString() : "");
				
				internalRegulationObsoVoList.add(vo);
			}
		}
		
		return internalRegulationObsoVoList;
	}
	
	@SuppressWarnings("rawtypes")
	private void getQueryWhereString(StringBuilder sb, List<? extends SearchObject> searchCriteria) {
		if (searchCriteria != null) {
			for (SearchObject searchVal : searchCriteria) {
				String col = searchVal.getSearchColumn();
				String val = searchVal.getSearchValue() != null ? searchVal.getSearchValueAsString() : "";
				
				if(!StringUtils.isBlank(val) || !val.equals("")) {
					if(StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_JUDUL_OBSOLETE, col)) {
						sb.append(" AND UPPER(wtio.OBSOLETE_TITLE) LIKE UPPER('%" + val + "%') ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_TIPE_OBSOLETE, col)) {
						sb.append(" AND wtio.REG_OBSOLETE_TYPE = " + val + " ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_START_DATE_OBSOLETE, col)) {
						sb.append(" AND TRUNC(wtio.OBSOLETE_DATE) >= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_END_DATE_OBSOLETE, col)) {
						sb.append(" AND TRUNC(wtio.OBSOLETE_DATE) <= TO_DATE('" + val + "', 'yyyy-MM-dd') ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_NO_OBSOLETE, col)) {
						sb.append(" AND UPPER(wtio.OBSOLETE_NUMBER) LIKE UPPER('%" + val + "%') ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_INFO_OBSOLETE, col)) {
						sb.append(" AND UPPER(wtio.OBSOLETE_INFO LIKE UPPER('%" + val + "%') ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_PUBLISHER_DIVISION,col)) {
						sb.append(" AND wtio.PUBLISH_DIVISION_ID = " + val + " ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_OPEN_CLOSE_OBS_REGULATION, col)) {
						sb.append(" AND wtio.OPEN_CLOSE_REG_OBSOLETE = "+ val +" ");
					}else if (StringUtils.equals(InternalRegulationObsoleteConstants.WHERE_PIC_IRG_NAME, col)) {
						sb.append(" AND wtio.PIC_IRG_USER_ID_1 = "+ val +" ");
					}
				}
			}
		}
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long searchCountData(List<? extends SearchObject> searchCriteria) throws Exception {
		
		Number result = searchCountDataCriteria(searchCriteria);
		if(result == null) {
			result = 0;
		}
		
		return result.longValue();
	}

	@SuppressWarnings("rawtypes")
	private Number searchCountDataCriteria(List<? extends SearchObject> searchCriteria) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND wtio.ENABLED_FLAG = 'Y' ");
		
		this.getQueryWhereString(sb, searchCriteria);

		Query query = getSession().createSQLQuery(sb.toString());

		return (Number) query.getSingleResult();
	}

	//UNUSED - FOR TESTING
	@Override
	public InternalRegulationTest findInternalRegulationTestById(Long internalRegulationId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT irt.internal_regulation_id, irt.internal_regulation_title ");
		sb.append("FROM wo_tmp_internal_reg_test irt ");
		sb.append("WHERE 1=1 ");
		sb.append("AND irt.internal_regulation_id = :internalRegulationId ");
		sb.append("ORDER BY irt.internal_regulation_id ASC");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		query.setParameter("internalRegulationId" , internalRegulationId);
		
		Object[] resultObj = null;
		resultObj = (Object[]) query.getSingleResult();
		
		InternalRegulationTest result = null;
		
		if(resultObj != null) {
			result = new InternalRegulationTest();
			
			result.setInternalRegulationId(MathUtil.returnIdObjectToLong(resultObj[0]));
			result.setInternalRegulationTitle(resultObj[1].toString());
			
			return result;
		}else {
			System.out.println("findInternalRegulationTestById failed to find a result");
			return null;
		}
	}

	@Override
 	public boolean checkIfExistByIrgId(Long irgId) {
		boolean exist = false;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT * ");
			sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
			sb.append(" WHERE 1=1 ");
			sb.append(" AND wtio.IRG_ID = "+ irgId +" ");
			
			Query query = getSession().createSQLQuery(sb.toString());
			Object[] result = (Object[]) query.getSingleResult();
			
			if(result != null) {
				exist = true;
			}
			
			return exist;
		}catch(NoResultException ex) {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<String> getDataObsoleteTitle(String query) {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT wtio.IRG_OBSOLETE_ID,wtio.OBSOLETE_TITLE ");
		sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND wtio.ENABLED_FLAG = 'Y' ");
		sb.append(" AND UPPER(wtio.OBSOLETE_TITLE) LIKE UPPER('%"+query+"%') ");
		
		Query sqlQuery = getSession().createSQLQuery(sb.toString());
		
		List resultList = sqlQuery.getResultList();
		List<String> titleList = new ArrayList<String>();
		
		if(resultList != null) {
			for(int i=0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				titleList.add(obj[1].toString());
			}
		}
		
		return titleList;
	}

	@Override
	public boolean checkIRGObsoleteByObsoleteTitle(String judulObsolete) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT wtio.IRG_OBSOLETE_ID,wtio.OBSOLETE_TITLE ");
			sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
			sb.append(" WHERE 1=1 ");
			sb.append(" AND wtio.ENABLED_FLAG = 'Y' ");
			sb.append(" AND LOWER(wtio.OBSOLETE_TITLE) = LOWER('"+judulObsolete+"') ");
			
			Query sqlQuery = getSession().createSQLQuery(sb.toString());
			
			Object[] result = (Object[]) sqlQuery.getSingleResult();
			
			if(result != null) 
				return true;
			else
				return false;
		}catch(NoResultException ex) {
			return false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<User> findAllUniquePicIrgNames() {
		try {
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT DISTINCT "
					+ " u.USER_ID, "
					+ " u.NAME ");
			sb.append(" FROM WO_TRC_IRG_OBSOLETE io ");
			sb.append(" INNER JOIN WO_MST_USER u ON io.PIC_IRG_USER_ID_1 =  u.USER_ID ");
			sb.append(" ORDER BY u.NAME ASC ");
			
			Query query = getSession().createSQLQuery(sb.toString());
			
			List<User> userList = new ArrayList<User>();
			List resultList = query.getResultList();
			
			for(int i=0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				User user = new User();
				
				user.setUserId(MathUtil.returnIdObjectToLong(obj[0]));
				user.setName(obj[1].toString());
				
				userList.add(user);
			}
				
			return userList;
		}catch(NoResultException e) {
			return new ArrayList<User>();
		}
	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<InternalRegulationObsoleteVo> getAllData() {
		List<InternalRegulationObsoleteVo> irgObsoleteVoList = new ArrayList<InternalRegulationObsoleteVo>();
		try {
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT "
					+ "io.OBSOLETE_TITLE, "
					+ "pd1.NAME_IN AS OBSOLETE_TYPE_STR, "
					+ "io.OBSOLETE_NUMBER, "
					+ "io.OBSOLETE_INFO, "
					+ "d1.DIVISION_NAME, "
					+ "io.PUBLISH_DIRECTORATE_NAME, "
					+ "TO_CHAR(io.OBSOLETE_DATE, 'DD-MON-YYYY') AS OBSOLETE_DATE_STR, "
					+ "pd2.NAME_IN AS OBSOLETE_STATUS_STR, "
					+ "u1.NAME AS PIC_IRG_NAME1, "
					+ "u2.NAME AS PIC_IRG_NAME2, "
					+ "d2.DIVISION_NAME AS PIC_DIVISION_NAME, "
					+ "u3.NAME AS PIC1, "
					+ "u4.NAME AS PIC2, "
					+ "u5.NAME AS PIC3, "
					+ "TO_CHAR(iopk.TARGET_DATE_KONV, 'DD-MON-YYYY') AS TARGET_DATE_KONV_STR, "
					+ "iopk.NOTES ");
			sb.append("FROM WO_TRC_IRG_OBSOLETE io ");
			sb.append("LEFT JOIN WO_TRC_IRG_OBSOLETE_PIC_KONV iopk ON iopk.IRG_OBSOLETE_ID = io.IRG_OBSOLETE_ID ");
			sb.append("INNER JOIN WO_MST_PARAMETER_DTL pd1 ON io.REG_OBSOLETE_TYPE = pd1.PARAMETER_DTL_ID ");
			sb.append("INNER JOIN WO_MST_PARAMETER_DTL pd2 ON io.OPEN_CLOSE_REG_OBSOLETE = pd2.PARAMETER_DTL_ID ");
			sb.append("INNER JOIN WO_MST_DIVISION d1 ON io.PUBLISH_DIVISION_ID = d1.DIVISION_ID ");
			sb.append("LEFT JOIN WO_MST_USER u1 ON io.PIC_IRG_USER_ID_1 = u1.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u2 ON io.PIC_IRG_USER_ID_2 = u2.USER_ID ");
			sb.append("LEFT JOIN WO_MST_DIVISION d2 ON iopk.DIVISION_ID = d2.DIVISION_ID ");
			sb.append("LEFT JOIN WO_MST_USER u3 ON iopk.USER_ID_1  = u3.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u4 ON iopk.USER_ID_2  = u4.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u5 ON iopk.USER_ID_3  = u5.USER_ID ");
			sb.append("ORDER BY io.CREATION_DATE DESC ");
			
			Query query = getSession().createSQLQuery(sb.toString());
			
			List resultList = query.getResultList();
			
			
			
			for(int i = 0; i<resultList.size(); i++) {
				Object[] obj = (Object[]) resultList.get(i);
				InternalRegulationObsoleteVo vo = new InternalRegulationObsoleteVo();
				
				vo.setJudulRegulasi(obj[0] != null ? obj[0].toString() : null);
				vo.setTipeRegulasiObsolete(obj[1] != null ? obj[1].toString() : null);
				vo.setNomorObsolete(obj[2] != null ? obj[2].toString() : null);
				vo.setInfoObsolete(obj[3] != null ? obj[3].toString() : null);
				vo.setPublishDivision(obj[4] != null ? obj[4].toString() : null);
				vo.setPublishDirectorateName(obj[5] != null ? obj[5].toString() : null);
				vo.setTanggalObsoleteStr(obj[6] != null ? obj[6].toString() : null);
				vo.setStatusOpenCloseObsolete(obj[7] != null ? obj[7].toString() : null);
				vo.setPicIrgName(obj[8] != null ? obj[8].toString() : null);
				vo.setPicIrgName2(obj[9] != null ? obj[9].toString() : null);
				vo.setUnitKerjaPicStr(obj[10] != null ? obj[10].toString() : null);
				vo.setPicName1(obj[11] != null ? obj[11].toString() : null);
				vo.setPicName2(obj[12] != null ? obj[12].toString() : null);
				vo.setPicName3(obj[13] != null ? obj[13].toString() : null);
				vo.setTanggalKonversiStr(obj[14] != null ? obj[14].toString() : null);
				vo.setNotes(obj[15] != null ? obj[15].toString() : null);
				
				irgObsoleteVoList.add(vo);
			}
			
			return irgObsoleteVoList;
		}catch (NoResultException ex) {
			return irgObsoleteVoList;
		}
	}

	@Override
	public boolean checkIRGObsoleteByObsoleteNum(String nomorObsolete) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT wtio.IRG_OBSOLETE_ID,wtio.OBSOLETE_NUMBER ");
			sb.append(" FROM WO_TRC_IRG_OBSOLETE wtio ");
			sb.append(" WHERE 1=1 ");
			sb.append(" AND wtio.ENABLED_FLAG = 'Y' ");
			sb.append(" AND LOWER(wtio.OBSOLETE_NUMBER) = LOWER('"+nomorObsolete+"') ");
			
			Query sqlQuery = getSession().createSQLQuery(sb.toString());
			
			Object[] result = (Object[]) sqlQuery.getSingleResult();
			
			if(result != null) 
				return true;
			else
				return false;
		}catch(NoResultException ex) {
			return false;
		}
	}
}
