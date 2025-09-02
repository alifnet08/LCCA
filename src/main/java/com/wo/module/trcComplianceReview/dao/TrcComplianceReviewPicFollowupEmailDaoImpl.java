package com.wo.module.trcComplianceReview.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcComplianceReview.model.TrcComplianceReviewPicFollowupEmail;

@Repository("trcComplianceReviewPicFollowupEmailDao")
public class TrcComplianceReviewPicFollowupEmailDaoImpl extends GenericDAOHibernate<TrcComplianceReviewPicFollowupEmail, Long>
		implements TrcComplianceReviewPicFollowupEmailDao {

	@SuppressWarnings("rawtypes")
	@Override
	public SendEmailVO getEmailPicByFollowupEmailId(Long id) throws Exception{
		
			String query = "SELECT DISTINCT " + 
					"	u1.email pic1, " + 
					"	u2.email pic2, " + 
					"	u3.email pic3, " + 
					"	COALESCE(d.email_to, 'REMINDER_PIC1') email_to, " + 
					"	d.email_cc_1," + 
					"	d.email_cc_2 " + 
					",(SELECT GROUP_CONCAT(comp.email SEPARATOR ',') FROM wo_trc_cmplc_review_pic_cmplc pc, wo_mst_user comp WHERE comp.user_id = pc.user_id and pc.compliance_review_id = f.compliance_review_id) emailCcCompliance " + 
					"FROM wo_trc_cmplc_review_pic_fp f  " + 
					"INNER JOIN wo_trc_compliance_review cr ON f.compliance_review_id = cr.compliance_review_id  " + 
					"INNER JOIN wo_trc_cmplc_review_pic_fp_email e ON f.compliance_review_pic_followup_id = e.compliance_review_pic_followup_id  " + 
					"LEFT JOIN wo_mst_counter_type c ON c.counter_type_id = cr.counter_type_id AND c.enabled_flag = 'Y'  " + 
					"LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = c.counter_type_id AND e.sla_type = d.sla_type AND e.sla = d.sla  " + 
					"LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  " + 
					"LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 " + 
					"LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 " + 
					"WHERE 1=1 AND e.compliance_review_pic_followup_email_id = :id";

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
				data.setEmailCcCompliance(obj[6] != null ? (String) obj[6] : null);
			}
			
			return data;
		
	}
}
