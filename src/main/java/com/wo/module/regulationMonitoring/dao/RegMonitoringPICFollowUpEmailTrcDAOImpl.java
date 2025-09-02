package com.wo.module.regulationMonitoring.dao;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;

@Repository("regMonitoringPICFollowUpEmailTrcDAO")
public class RegMonitoringPICFollowUpEmailTrcDAOImpl extends GenericDAOHibernate<RegMonitoringPICFollowUpEmailTrc, Long>
		implements RegMonitoringPICFollowUpEmailTrcDAO {

	@SuppressWarnings("rawtypes")
	@Override
	public SendEmailVO getEmailPicByFollowUpEmailId(Long emailFollowId) {
		String query = "SELECT DISTINCT " + 
				"	u1.email pic1, " + 
				"	u2.email pic2, " + 
				"	u3.email pic3, " + 
				"	COALESCE(d.email_to, 'REMINDER_PIC1') email_to, " + 
				"	d.email_cc_1," + 
				"	d.email_cc_2 " + 
				",(SELECT GROUP_CONCAT(comp.email SEPARATOR ',') FROM WO_TRC_REG_MONITOR_PIC_CMPLC pc, wo_mst_user comp WHERE comp.user_id = pc.user_id and pc.REG_MONITORING_ID = s.REG_MONITORING_ID) emailCcCompliance " + 
				"from WO_TRC_REG_MONITORING_PIC_FP f " + 
				"inner JOIN WO_TRC_REG_MONITORING s on f.REG_MONITORING_ID = s.REG_MONITORING_ID " + 
				"inner join WO_TRC_REG_MONITOR_PC_FP_EMAIL e on f.REG_MONITORING_PIC_FOLLOWUP_ID = e.REG_MONITORING_PIC_FOLLOWUP_ID " + 
				"inner join WO_TRC_REG_MONITOR_REGULATION r on r.REG_MONITORING_ID = s.REG_MONITORING_ID " + 
				"inner join wo_mst_regulation rn on rn.regulation_id = r.regulation_id " + 
				"left join wo_mst_counter_type c on c.counter_type_id = s.counter_type_id and c.enabled_flag = 'Y' " + 
				"left join wo_mst_counter_type_dtl d on d.counter_type_id = c.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla " + 
				"left join wo_mst_user u1 on u1.user_id = f.user_id_1 " + 
				"left join wo_mst_user u2 on u2.user_id = f.user_id_2 " + 
				"left join wo_mst_user u3 on u3.user_id = f.user_id_3 " + 
				"WHERE 1=1 AND e.REG_MONITORING_PIC_FP_EMAIL_ID = :id";

		Query queryResult = getSession().createSQLQuery(query);
		queryResult.setParameter("id", emailFollowId);
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
