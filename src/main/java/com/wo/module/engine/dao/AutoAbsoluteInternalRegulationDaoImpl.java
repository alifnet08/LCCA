package com.wo.module.engine.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.engine.vo.AutoAbsoluteInternalRegulationVO;
import com.wo.module.log.model.LogHeader;

@Repository("autoAbsoluteInternalRegulationDao")
public class AutoAbsoluteInternalRegulationDaoImpl extends GenericDAOHibernate<LogHeader, Long> implements AutoAbsoluteInternalRegulationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AutoAbsoluteInternalRegulationVO> getDataAutoAbsoluteInternalRegulation() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT wmr.REGULATION_ID ");
		sb.append(" 	,wmu.USER_ID ");
		sb.append(" FROM WO_MST_REGULATION wmr ");
		sb.append(" 	INNER JOIN WO_MST_DOCUMENT_TYPE wmdt ON wmdt.DOCUMENT_TYPE_ID = wmr.DOCUMENT_TYPE_ID ");
		sb.append(" 	LEFT JOIN WO_MST_USER wmu ON wmu.NIK = wmr.CREATED_BY ");
		sb.append(" 	LEFT JOIN WO_MST_PARAMETER_DTL wmpd ON wmpd.PARAMETER_DTL_CODE = 'INTERNAL_REGULATION_TYPE_REVIEW_DATE_SIX_MONTH' ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmr.ENABLED_FLAG = 'Y' ");
		sb.append(" 	AND wmr.STATUS = 'DATA_ACTIVE' ");
		sb.append(" 	AND upper(wmdt.DOCUMENT_TYPE_IN) = upper('Peraturan Sementara') ");
		sb.append(" 	AND CAST(ADD_MONTHS(wmr.PUBLISHED_DATE, wmpd.NAME_IN) + INTERVAL  '1' DAY AS DATE) = CAST(to_char(systimestamp, 'DD-MON-YYYY') AS DATE) ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List<Object[]> result = query.getResultList();
		List<AutoAbsoluteInternalRegulationVO> vo = new ArrayList<>();
		
		if (!result.isEmpty()) {
			for (Object[] obj : result) {
				AutoAbsoluteInternalRegulationVO data = new AutoAbsoluteInternalRegulationVO();
				
				data.setRegulationId(obj[0] != null ? ((Number) obj[0]).longValue() : null);
				data.setUserId(obj[1] != null ? ((Number) obj[1]).longValue() : null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
