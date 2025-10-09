package com.wo.module.trcAudit.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;

@Repository("trcAuditPICFollowupEmailDao")
public class TrcAuditPICFollowupEmailDaoImpl extends GenericDAOHibernate<TrcAuditPicFollowupEmail, Long>
		implements TrcAuditPICFollowupEmailDao {

	@SuppressWarnings("rawtypes")
	@Override
	public SendEmailVO getEmailPicByFollowupEmailId(Long id) throws Exception {
		String query = "SELECT DISTINCT  " + 
				"	u1.email pic1, " + 
				"	u2.email pic2, " + 
				"	u3.email pic3,  " + 
				"	COALESCE(d.email_to, 'REMINDER_PIC1') email_to, " + 
				"	d.email_cc_1, " + 
				"	d.email_cc_2 " + 
				"FROM wo_trc_audit r " + 
				"INNER JOIN wo_trc_audit_pic_followup f ON f.audit_id = r.audit_id " + 
				"inner join wo_trc_audit_pic_followup_email e ON e.audit_pic_followup_id = f.audit_pic_followup_id " + 
				"LEFT JOIN wo_mst_counter_type ct ON ct.counter_type_id =r.counter_type_id AND ct.enabled_flag = 'Y' " + 
				"LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = ct.counter_type_id AND e.sla_type = d.sla_type AND e.sla = d.sla " + 
				"LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1 " + 
				"LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 " + 
				"LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 " + 
				"WHERE 1=1 AND e.adt_pic_fp_email_id = :id";

		Query queryResult = getSession().createSQLQuery(query);
		queryResult.setParameter("id", id);
		Object[] obj = (Object[]) queryResult.getSingleResult();
		SendEmailVO data = null;
		if(obj != null) {
			data = new SendEmailVO();
			data.setPic1(obj[0] != null ? (String) obj[0] : null);
			data.setPic2(obj[1] != null ? (String) obj[1] : null);
			data.setPic3(obj[2] != null ? (String) obj[2] : null);
			data.setEmailTo(obj[3] != null ? (String) obj[3] : null);
			data.setEmailCc1(obj[4] != null ? (String) obj[4] : null);
			data.setEmailCc2(obj[5] != null ? (String) obj[5] : null);
		}
		
		return data;
	}

}
