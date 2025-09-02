package com.wo.module.tmpAudit.dao;

import java.sql.Clob;
import java.sql.SQLException;

import javax.persistence.Query;
import javax.sql.rowset.serial.SerialException;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.tmpAudit.model.TmpAuditCheckPoint;

@Repository("tmpAuditCheckPointDao")
public class TmpAuditCheckPointDaoImpl extends GenericDAOHibernate<TmpAuditCheckPoint, Long> implements TmpAuditCheckPointDao{

	@Override
	public Boolean hasDuplicateBankCommitment(Long auditId,String auditFinding,String bankResponse,String bankCommitment){
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT COUNT(1) ");
		sb.append(" FROM WO_TMP_AUDIT_CHECK_POINT a "
				+ " INNER JOIN wo_tmp_adt_pc_fp_bank_commit b on a.AUDIT_CHECK_POINT_ID = b.AUDIT_CHECK_POINT_ID "
				+ " INNER JOIN wo_tmp_audit c on c.audit_id = a.audit_id ");
		sb.append(" where 1=1 and c.enabled_flag = 'Y' ");
		sb.append(" and dbms_lob.substr(audit_findings, 4000, 1 ) = :auditFinding ");
		sb.append(" and dbms_lob.substr(bank_response, 4000, 1 ) = :bankResponse ");
		sb.append(" and dbms_lob.substr(bank_commitment, 4000, 1 ) = :bankCommitment ");
		sb.append(" and a.audit_id <> :auditId ");
		Query query = getSession().createSQLQuery(sb.toString());

			query.setParameter("auditFinding", auditFinding);
			query.setParameter("bankResponse", bankResponse);
			query.setParameter("bankCommitment", bankCommitment);
		
		
		if(auditId == null) {
			query.setParameter("auditId", 0);
		}else {
			query.setParameter("auditId", auditId);
		}
		
		Number count = (Number) query.getSingleResult();
		if(count == null) {
			count = 0;
		}
		
		
		if( count.longValue() > 0 ) {
			return true;
		} else {
			return false;
		}
	}
}
