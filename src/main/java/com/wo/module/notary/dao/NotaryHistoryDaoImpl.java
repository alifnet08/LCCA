package com.wo.module.notary.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;
import com.wo.module.notary.model.Notary;
import com.wo.module.notary.model.NotaryHistory;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.parameter.model.ParameterHeader;

@Repository("notaryHistoryDao")
public class NotaryHistoryDaoImpl extends GenericDAOHibernate<NotaryHistory, Long>
		implements NotaryHistoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<NotaryHistory> getNotaryHistoryByNotaryId(Long notaryId) throws Exception {
		String hql = " from NotaryHistory where notary.notaryId = :notaryId "
				+ " and (enabledFlag is null or enabledFlag = 'Y') order by notaryHistoryId desc ";
		Query result = getSession().createQuery(hql);
		result.setParameter("notaryId", notaryId);
		return (List<NotaryHistory>) result.getResultList();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<NotaryHistory> searchHistory(String notaryName) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT h.NOTARY_HISTORY_ID, h.STATUS, h.CATATAN_REVISI, h.CREATION_DATE, ");
		sql.append("        n.NOTARY_ID, n.NOTARY_NAME, n.ADDRESS, n.AREA, dtl.NAME_IN, h.JENIS_PENGAJUAN, ");
		sql.append("        NVL(h.NO_PENGAJUAN, n.NO_PENGAJUAN) ");
		sql.append("   FROM WO_MST_NOTARY_HISTORY h ");
		sql.append("   LEFT JOIN WO_MST_NOTARY n ON n.NOTARY_ID = h.NOTARY_ID ");
		sql.append("   LEFT JOIN WO_MST_PARAMETER_DTL dtl ON n.NOTARY_CATEGORY = dtl.PARAMETER_DTL_CODE ");
		sql.append("        AND dtl.PARAMETER_CODE = :notaryCategoryHeader ");
		sql.append("  WHERE (h.ENABLED_FLAG IS NULL OR UPPER(TRIM(h.ENABLED_FLAG)) <> 'N') ");
		sql.append("    AND (h.DEL_ID IS NULL OR h.DEL_ID = 0) ");
		boolean filterName = notaryName != null && notaryName.trim().length() > 0;
		if (filterName) {
			sql.append(" AND UPPER(n.NOTARY_NAME) LIKE UPPER(:notaryName) ");
		}
		sql.append(" ORDER BY n.NOTARY_ID ASC, h.NOTARY_HISTORY_ID ASC ");

		Query query = getSession().createSQLQuery(sql.toString());
		query.setParameter("notaryCategoryHeader", ParameterHeader.PARAM_HEAD_NOTARY_CATEGORY);
		if (filterName) {
			query.setParameter("notaryName", "%" + notaryName.trim() + "%");
		}

		List resultList = query.getResultList();
		List<NotaryHistory> histories = new ArrayList<NotaryHistory>();
		if (resultList == null) {
			return histories;
		}
		for (int i = 0; i < resultList.size(); i++) {
			Object[] obj = (Object[]) resultList.get(i);
			NotaryHistory data = new NotaryHistory();
			data.setNotaryHistoryId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
			data.setStatus(obj[1] != null ? obj[1].toString() : null);
			data.setCatatanRevisi(obj[2] != null ? obj[2].toString() : null);
			data.setCreationDate(toTimestamp(obj[3]));
			data.setEnabledFlag("Y");

			Notary notary = new Notary();
			notary.setNotaryId(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]) : null);
			notary.setNotaryName(obj[5] != null ? obj[5].toString() : null);
			notary.setAddress(obj[6] != null ? obj[6].toString() : null);
			notary.setArea(obj[7] != null ? obj[7].toString() : null);
			ParameterDetail category = new ParameterDetail();
			category.setNameIn(obj[8] != null ? obj[8].toString() : null);
			notary.setNotaryCategory(category);
			data.setNotary(notary);
			if (obj.length > 9) {
				data.setJenisPengajuan(obj[9] != null ? obj[9].toString() : null);
			}
			if (obj.length > 10 && obj[10] != null) {
				data.setNotaryNo(obj[10].toString());
				notary.setNotaryNo(obj[10].toString());
			}
			histories.add(data);
		}
		return histories;
	}

	private Timestamp toTimestamp(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Timestamp) {
			return (Timestamp) value;
		}
		if (value instanceof Date) {
			return new Timestamp(((Date) value).getTime());
		}
		try {
			Object converted = value.getClass().getMethod("timestampValue").invoke(value);
			if (converted instanceof Timestamp) {
				return (Timestamp) converted;
			}
			if (converted instanceof Date) {
				return new Timestamp(((Date) converted).getTime());
			}
		} catch (Exception ignored) {
			return null;
		}
		return null;
	}

}
