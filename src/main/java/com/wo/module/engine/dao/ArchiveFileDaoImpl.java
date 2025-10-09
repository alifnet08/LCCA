package com.wo.module.engine.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.constant.CommonConstants;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.cpsa.constant.CompliancePlanSelfAssessmentConstant;
import com.wo.module.log.model.LogHeader;

@Repository("archiveFileDao")
public class ArchiveFileDaoImpl extends GenericDAOHibernate<LogHeader, Long> implements ArchiveFileDao {

	@SuppressWarnings("rawtypes")
	@Override
	public String getSystemProperty(String propertyCode) {
		String result = null;
//		getSession().beginTransaction();
		try {
			String query = "SELECT NAME_IN FROM wo_mst_parameter_dtl " + "WHERE PARAMETER_DTL_CODE = :propertyCode";
			
			Query queryResult = getSession().createSQLQuery(query);
			queryResult.setParameter("propertyCode", propertyCode);
			result = (String) queryResult.uniqueResult();

		} catch (Exception se) {
			throw se;
		} finally {
//			getSession().close();
		}

		return result;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<String> getFileExpiredFromDate(Integer expiredDays) throws Exception {
		List<String> results = null;
//		getSession().beginTransaction();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(" 	SELECT	mau.FILE_ID, '' AS TEMP	FROM	WO_MST_ABOUT_US	mau	WHERE	mau.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mau.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mad.FILE_ID, '' AS TEMP FROM	WO_MST_ARTICLE_DOCUMENT	mad	WHERE	mad.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mad.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mcrd.FILE_ID, '' AS TEMP FROM WO_MST_CMPLC_RVW_DOC_ATTACH	mcrd	WHERE	mcrd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mcrd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	'"+CommonConstants.FILE_SEPARATOR+CompliancePlanSelfAssessmentConstant.FOLDER_CPSA+CommonConstants.FILE_SEPARATOR+"' || cpsa.FILE_ID AS FILE_ID, '' AS TEMP FROM WO_MST_CPSA	cpsa	WHERE	cpsa.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	cpsa.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	'"+CommonConstants.FILE_SEPARATOR+CompliancePlanSelfAssessmentConstant.FOLDER_CPSA+CommonConstants.FILE_SEPARATOR+"' || cpsa.FILE_QUEST_ID AS FILE_ID, '' AS TEMP FROM	WO_MST_CPSA	cpsa	WHERE	cpsa.FILE_QUEST_ID	IS NOT NULL	AND	extract(day from sysdate - 	cpsa.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	'"+CommonConstants.FILE_SEPARATOR+CompliancePlanSelfAssessmentConstant.FOLDER_CPSA+CommonConstants.FILE_SEPARATOR+"' || cpsa.FILE_PATH_DOWNLOAD	AS FILE_ID,'' AS TEMP FROM WO_MST_CPSA	cpsa	WHERE	cpsa.FILE_PATH_DOWNLOAD	IS NOT NULL	AND	extract(day from sysdate - 	cpsa.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mdcd.FILE_ID, '' AS TEMP FROM	WO_MST_DB_COMPLIANCE_DOCUMENT	mdcd	WHERE	mdcd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mdcd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mfd.FILE_ID, '' AS TEMP	FROM WO_MST_FAQ_DOCUMENT	mfd	WHERE	mfd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mfd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mla.FILE_ID, '' AS TEMP	FROM WO_MST_LITIGATION_ATTACHMENT	mla	WHERE	mla.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mla.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mola.FILE_ID, '' AS TEMP FROM WO_MST_OUTGOING_LETTER_ATTACH	mola	WHERE	mola.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mola.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mra.FILE_ID, '' AS TEMP	FROM WO_MST_REGULATION_ATTACHMENT	mra	WHERE	mra.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mra.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	mtd.FILE_ID, '' AS TEMP	FROM WO_MST_TEMPLATE_DOCUMENT	mtd	WHERE	mtd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	mtd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tad.FILE_ID, '' AS TEMP	FROM WO_TMP_ARTICLE_DOCUMENT	tad	WHERE	tad.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tad.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	ta.FILE_ID, '' AS TEMP	FROM WO_TMP_ATTACHMENTS	ta	WHERE	ta.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	ta.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tad.FILE_ID, '' AS TEMP	FROM WO_TRC_AUDIT_DOCUMENT	tad	WHERE	tad.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tad.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tapf.FILE_ID, '' AS TEMP FROM WO_TRC_AUDIT_PIC_FP_ATCH	tapf	WHERE	tapf.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tapf.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tcrp.FILE_ID, '' AS TEMP FROM WO_TRC_CMPLC_RVW_PC_FP_PTS_ACH	tcrp	WHERE	tcrp.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tcrp.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tcrd.FILE_ID, '' AS TEMP FROM WO_TRC_COMPLIANCE_REVIEW_DOC	tcrd	WHERE	tcrd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tcrd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tctd.FILE_ID, '' AS TEMP FROM WO_TRC_COMP_TESTING_DOC	tctd	WHERE	tctd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tctd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tctp.FILE_ID, '' AS TEMP FROM WO_TRC_COMP_TEST_PIC_FP_ATTACH	tctp	WHERE	tctp.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tctp.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tctp.FILE_ID, '' AS TEMP FROM WO_TRC_COMP_TEST_PIC_FP_EXT	tctp	WHERE	tctp.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tctp.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tcd.FILE_ID, '' AS TEMP FROM WO_TRC_CORRESPONDENCE_DOCUMENT	tcd	WHERE	tcd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tcd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tcpf.FILE_ID, '' AS TEMP FROM WO_TRC_CRSPDC_PIC_FP_ATCH	tcpf	WHERE	tcpf.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tcpf.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tfa.FILE_ID, '' AS TEMP	FROM WO_TRC_FINE_ATTACHMENT	tfa	WHERE	tfa.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tfa.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tfd.FILE_ID, '' AS TEMP	FROM WO_TRC_FINE_DOCUMENT	tfd	WHERE	tfd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tfd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tfpf.FILE_ID, '' AS TEMP FROM WO_TRC_FINE_PIC_FOLLOWUP_ATTAC	tfpf	WHERE	tfpf.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tfpf.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tipt.FILE_ID, '' AS TEMP FROM WO_TRC_IRG_PIC_TPK	tipt	WHERE	tipt.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tipt.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	trmd.FILE_ID, '' AS TEMP FROM WO_TRC_REG_MONITORING_DOCUMENT	trmd	WHERE	trmd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	trmd.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	trmp.FILE_ID, '' AS TEMP FROM WO_TRC_REG_MONITOR_PIC_FP_ATCH	trmp	WHERE	trmp.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	trmp.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	gen.REPORT_FILE_ID AS FILE_ID, '' AS TEMP FROM WO_TRC_REPORT_GEN	gen	WHERE	gen.REPORT_FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	gen.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	trpf.FILE_ID, '' AS TEMP FROM WO_TRC_RMD_PIC_FOLLOWUP_ATCH	trpf	WHERE	trpf.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	trpf.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tspf.FILE_ID, '' AS TEMP FROM WO_TRC_SCLIZATION_PIC_FP_ATCH	tspf	WHERE	tspf.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tspf.CREATION_DATE	) = :expiredDays	UNION ALL	");
			sb.append(" 	SELECT	tsd.FILE_ID, '' AS TEMP	FROM WO_TRC_SOCIALIZATION_DOCUMENT	tsd	WHERE	tsd.FILE_ID	IS NOT NULL	AND	extract(day from sysdate - 	tsd.CREATION_DATE	) = :expiredDays		");

			
			Query queryResult = getSession().createSQLQuery(sb.toString());
			queryResult.setParameter("expiredDays", expiredDays);
			List<Object[]> rows = queryResult.getResultList();
			results = new ArrayList<>();
			for(int x=0; x < rows.size();x++) {
				Object[] row = rows.get(x);
				
				results.add((String) row[0]);
			}

		} catch (Exception se) {
			throw se;
		} finally {
//			getSession().close();
		}

		return results;
	}

	
	
}
