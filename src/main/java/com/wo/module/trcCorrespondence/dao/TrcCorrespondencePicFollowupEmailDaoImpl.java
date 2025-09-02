package com.wo.module.trcCorrespondence.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;

@Repository("trcCorrespondencePicFollowupEmailDao")
public class TrcCorrespondencePicFollowupEmailDaoImpl extends GenericDAOHibernate<TrcCorrespondencePicFollowupEmail, Long>
		implements TrcCorrespondencePicFollowupEmailDao {

	@SuppressWarnings("rawtypes")
	@Override
	public SendEmailVO getEmailPicByFollowupEmailId(Long id) {
		String query = "SELECT DISTINCT " + 
				"	u1.email pic1, " + 
				"	u2.email pic2, " + 
				"	u3.email pic3, " + 
				"	COALESCE(d.email_to, 'REMINDER_PIC1') email_to, " + 
				"	d.email_cc_1, " + 
				"	d.email_cc_2 " + 
				"from wo_trc_correspondence c " + 
				"inner join wo_trc_crpdc_pic_fp_email e on e.correspondence_id = c.correspondence_id " + 
				"left join wo_mst_counter_type ct on ct.counter_type_id =c.counter_type_id and ct.enabled_flag = 'Y' " + 
				"left join wo_mst_counter_type_dtl d on d.counter_type_id = ct.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla " + 
				"left join wo_mst_user u1 on u1.user_id = c.user_id_1 " + 
				"left join wo_mst_user u2 on u2.user_id = c.user_id_2 " + 
				"left join wo_mst_user u3 on u3.user_id = c.user_id_3 " + 
				"WHERE 1=1 AND e.crpdc_pic_fp_email_id = :id";

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
