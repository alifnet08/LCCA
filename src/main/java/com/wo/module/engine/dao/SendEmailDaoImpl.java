package com.wo.module.engine.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.StoredProcedureQuery;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;
import com.wo.module.complianceTestingMockup.model.ComplianceTestingPICFollowupEmail;
import com.wo.module.cpsa.vo.CompliancePlanSelfAssessmentPicEmailVo;
import com.wo.module.engine.vo.SendEmailDetailVO;
import com.wo.module.engine.vo.SendEmailIRGObsoleteVO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanAttachmentVo;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpgVo;
import com.wo.module.internalRegulationPenerbitan.vo.InternalRegulationPenerbitanPicTpkVo;
import com.wo.module.log.model.LogHeader;
import com.wo.module.lov.bean.FacesUtil;
import com.wo.module.parameter.model.ParameterDetail;
import com.wo.module.qa.model.QA;
import com.wo.module.regulationMonitoring.model.RegMonitoringPICFollowUpEmailTrc;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;
import com.wo.module.trcAudit.model.TrcAuditPicFollowupEmail;
import com.wo.module.trcCorrespondence.model.TrcCorrespondencePicFollowupEmail;
import com.wo.module.trcFineApproval.model.TrcFinePicFollowupEmail;
import com.wo.module.trcRmd.model.TrcRmdPicFollowup;
import com.wo.module.trcRmd.model.TrcRmdPicFollowupEmail;
//import com.wo.module.regulationSocialization.vo.RegulationSocializationVO;

@Repository("sendEmailDao")
public class SendEmailDaoImpl extends GenericDAOHibernate<LogHeader, Long> implements SendEmailDao {

	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataSocialization() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "select distinct u1.email pic1 ,u2.email pic2,u3.email pic3,email_date,'H' || (case when e.sla != 0 then to_char(e.sla_type) else '' end) || (case when e.sla != 0 then to_char(e.sla) else '' end) counter_type, COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2, "
					+ "rn.name_in regulationTitleIn,rn.name_en regulationTitleEn,f.target_date, rn.document_no,rn.published_date,rn.effective_date,f.socialization_pic_followup_id, e.sclztion_pic_fp_email_id,e.sla_type,e.sla "
					+ ",(SELECT LISTAGG(comp.email, ',') WITHIN GROUP (ORDER BY pc.socialization_pic_cmplc_id) FROM wo_trc_socialization_pic_cmplc pc, wo_mst_user comp WHERE comp.user_id = pc.user_id and pc.socialization_id = f.socialization_id) emailCcCompliance "
					+ "from wo_trc_socialization_pic_fp f "
					+ "inner join wo_trc_socialization s on f.socialization_id = s.socialization_id "
					+ "inner join wo_trc_scilization_pc_fp_email e on f.socialization_pic_followup_id = e.socialization_pic_followup_id "
					+ "inner join wo_trc_socialization_rgltn r on r.socialization_id = s.socialization_id "
					+ "inner join wo_mst_regulation rn on rn.regulation_id = r.regulation_id "
					+ "left join wo_mst_counter_type c on c.counter_type_id = s.counter_type_id and c.enabled_flag = 'Y' "
					+ "left join wo_mst_counter_type_dtl d on d.counter_type_id = c.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla "
					+ "left join wo_mst_user u1 on u1.user_id = f.user_id_1 "
					+ "left join wo_mst_user u2 on u2.user_id = f.user_id_2 "
					+ "left join wo_mst_user u3 on u3.user_id = f.user_id_3 "
					+ "where (f.followup_status is null or f.followup_status <> 'PIC_DONE')  "
					+ "and s.reminder_status = 'REMINDER_ACTIVE' and s.follow_up = 'Y' "
					+ "and r.primary_flag = 'Y' and s.enabled_flag = 'Y' and e.email_date is not null and TO_CHAR(e.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy')";

			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setRegulationTitleIn(obj[8] != null ? (String) obj[8] : null);
					data.setRegulationTitleEn(obj[9] != null ? (String) obj[9] : null);
					data.setTargetDate(obj[10] != null ? (Date) obj[10] : null);
					data.setDocumentNo(obj[11] != null ? (String) obj[11] : null);
					data.setPublishedDate(obj[12] != null ? (Date) obj[12] : null);
					data.setEffctiveDate(obj[13] != null ? (Date) obj[13] : null);
					data.setId(obj[14] != null ? (MathUtil.returnIdObjectToLong(obj[14])) : null);
					data.setEmailId(obj[15] != null ? (MathUtil.returnIdObjectToLong(obj[15])) : null);
					data.setSlaType(obj[16] != null ? (String) obj[16] : null);
					data.setSla(obj[17] != null ? ((Number) obj[17]).toString() : null);
					data.setEmailCcCompliance(obj[18] != null ? (String) obj[18] : null);
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataCorrespondence() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "select distinct u1.email pic1 ,u2.email pic2,u3.email pic3,e.email_date,'H' || (case when e.sla != 0 then to_char(e.sla_type) else '' end) || (case when e.sla != 0 then to_char(e.sla) else '' end) counter_type, "
					+ "            COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2, "
					+ "			   dbms_lob.substr(c.perihal_in, 4000, 1 ) perihal_in, dbms_lob.substr(c.perihal_en, 4000, 1 ) perihal_en, "
					+ "            pc.target_date, c.letter_received_date,c.letter_no,c.letter_date,e.correspondence_id, e.crpdc_pic_fp_email_id,e.sla_type,e.sla, "
					+ "			   (case when pc.division_id is not null "
					+ "                  then (case when pc.division_id = u1.division_id then u1.division_name "
					+ "                  when pc.division_id = u2.division_id then u2.division_name "
					+ "                  when pc.division_id = u3.division_id then u3.division_name end) else null end) division, "
					+ "            u1.name pic_1_name, u2.name pic_2_name, u3.name pic_3_name, "
					+ "            sender.name_in, sender.name_en, dbms_lob.substr(c.letter_summary, 4000, 1 ) letter_summary, sender.parameter_code sender_code "
					+ "            ,pc.CRPDC_PIC_CONFIRM_ID "
					+ "       from wo_trc_correspondence c "
					+ "     	   inner join wo_trc_crpdc_pic_fp_email e on e.correspondence_id = c.correspondence_id "
					+ "		       inner join WO_TRC_CRPDC_PIC_CONFIRM pc ON pc.correspondence_id = c.correspondence_id "
					+ "	 	       left join wo_mst_counter_type ct on ct.counter_type_id =c.counter_type_id and ct.enabled_flag = 'Y' "
					+ "		       left join wo_mst_counter_type_dtl d on d.counter_type_id = ct.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla "
					+ "		       left join wo_mst_user u1 on u1.user_id = pc.user_id_1 "
					+ "		       left join wo_mst_user u2 on u2.user_id = pc.user_id_2 "
					+ "		       left join wo_mst_user u3 on u3.user_id = pc.user_id_3 "
					+ "		       left join wo_mst_parameter_dtl sender on sender.parameter_code like 'SENDER%' and sender.parameter_dtl_code = c.sender_code "
					+ "		       left join wo_mst_parameter_dtl pd on pd.parameter_dtl_id = pc.STATUS_PIC_ID "
					//+ "where (c.followup_status is null or c.followup_status NOT IN ('PIC_DONE', 'I_ATTEND', 'NOT_ATTEND')) "
					+ "      where (pd.parameter_dtl_code IS NULL OR pd.parameter_dtl_code NOT IN ('PIC_DONE', 'I_ATTEND', 'NOT_ATTEND')) "
					+ "		       and c.reminder_status = 'REMINDER_ACTIVE' "
					+ "		       and c.follow_up = 'Y'  "
					+ "		       and e.email_date is not null "
					+ "		       and c.enabled_flag = 'Y' "
					+ "		       and TO_CHAR(email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy') ";

			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setRegulationTitleIn(obj[8] != null ? (String) obj[8] : null);
					data.setRegulationTitleEn(obj[9] != null ? (String) obj[9] : null);
					data.setTargetDate(obj[10] != null ? (Date) obj[10] : null);
					data.setLetterReceiveDate(obj[11] != null ? (Date) obj[11] : null);
					data.setLetterNo(obj[12] != null ? (String) obj[12] : null);
					data.setLetterDate(obj[13] != null ? (Date) obj[13] : null);
					data.setId(obj[14] != null ? (MathUtil.returnIdObjectToLong(obj[14])) : null);
					data.setEmailId(obj[15] != null ? (MathUtil.returnIdObjectToLong(obj[15])) : null);
					data.setSlaType(obj[16] != null ? (String) obj[16] : null);
					data.setSla(obj[17] != null ? ((Number) obj[17]).toString() : null);
					data.setDivisionName(obj[18] != null ? (String) obj[18] : null);
					data.setPic1Name(obj[19] != null ? (String) obj[19] : null);
					data.setPic2Name(obj[20] != null ? (String) obj[20] : null);
					data.setPic3Name(obj[21] != null ? (String) obj[21] : null);
					data.setSenderIn(obj[22] != null ? (String) obj[22] : null);
					data.setSenderEn(obj[23] != null ? (String) obj[23] : null);
					data.setLetterSummary(obj[24] != null ? (String) obj[24] : null);
					data.setSenderCode(obj[25] != null ? (String) obj[25] : null);
					data.setCrpdcPicConfirmId(obj[26] != null ? (MathUtil.returnIdObjectToLong(obj[26])) : null);
					
					String detailQuery = "SELECT "
							+ "(case when c.division_id is not null then (case when c.division_id = u1.division_id then u1.division_name when c.division_id = u2.division_id then u2.division_name when c.division_id = u3.division_id then u3.division_name end) else null end) division, "
							+ "u1.name pic_1_name, u2.name pic_2_name, u3.name pic_3_name, "
							+ "u1.email pic_1_email, u2.email pic_2_email, u3.email pic_3_email "
							+ "FROM wo_trc_correspdc_sup_unit c "
							+ "left join wo_mst_user u1 on u1.user_id = c.email_cc_id_1 "
							+ "left join wo_mst_user u2 on u2.user_id = c.email_cc_id_2 "
							+ "left join wo_mst_user u3 on u3.user_id = c.email_cc_id_3 "
							+ "WHERE c.correspondence_id = " + String.valueOf(data.getId().longValue());
					Query detailResult = getSession().createSQLQuery(detailQuery);

					List resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setListDetail(new ArrayList<SendEmailDetailVO>());
						for (int j = 0; j < resultDtl.size(); j++) {
							Object[] det = (Object[]) resultDtl.get(j);
							SendEmailDetailVO dtl = new SendEmailDetailVO();
							dtl.setDivisionName(det[0] != null ? (String) det[0] : null);
							dtl.setPic1Name(det[1] != null ? (String) det[1] : null);
							dtl.setPic2Name(det[2] != null ? (String) det[2] : null);
							dtl.setPic3Name(det[3] != null ? (String) det[3] : null);
							dtl.setPic1Email(det[4] != null ? (String) det[4] : null);
							dtl.setPic2Email(det[5] != null ? (String) det[5] : null);
							dtl.setPic3Email(det[6] != null ? (String) det[6] : null);
							data.getListDetail().add(dtl);
						}
					}

					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataRMD() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "select distinct u1.email pic1 ,u2.email pic2,u3.email pic3,e.email_date,'H'||(case when e.sla != 0 then to_char(e.sla_type) else '' end)||(case when e.sla != 0 then to_char(e.sla) else '' end) counter_type, COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2, "
					+ "(select name_in from wo_mst_regulation rn inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1 ) regulationTitleIn, "
					+ "(select name_en from wo_mst_regulation rn inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1 ) regulationTitleEn, "
					+ "e.target_date,r.report_name_in, r.report_name_en, "
					+ "(select document_no from wo_mst_regulation rn inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1) documentNo,f.rmd_pic_followup_id, "
					+ "r.dedicated_to,r.sanctions, " + "f.target_date dueDate, "
					+ "(select u.name from wo_trc_rmd_supporting_unit su inner join wo_mst_user u on u.user_id = su.email_cc_id_1  where su.rmd_id = r.rmd_id and rownum = 1 ) supportingUnit, "
					+ "e.rmd_pic_followup_email_id,e.sla_type,e.sla, "
					+ "(select publisher_unit from wo_mst_regulation rn inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1 ) publisherUnit, "
					+ "(case when r.division_id is not null then (case when r.division_id = u1.division_id then u1.division_name when r.division_id = u2.division_id then u2.division_name when r.division_id = u3.division_id then u3.division_name end) else null end) division, "
					+ "u1.name pic_1_name, u2.name pic_2_name, u3.name pic_3_name, "
					+ "ct.counter_type_in, ct.counter_type_en, " + "rt.report_type_in, rt.report_type_en ,"
					+ "(select document_category_in from wo_mst_document_category dc inner join wo_mst_regulation rn on dc.document_category_id = rn.document_category_id inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1) document_category_in , "
					+ "(select document_category_en from wo_mst_document_category dc inner join wo_mst_regulation rn on dc.document_category_id = rn.document_category_id inner join wo_trc_rmd_regulation rg on rn.regulation_id = rg.regulation_id where rg.rmd_id = r.rmd_id and rownum = 1) document_category_en , "
					+ "(select letter_no from wo_trc_correspondence tc inner JOIN wo_trc_rmd_correspondence rc on tc.correspondence_id = rc.correspondence_id where rc.rmd_id = r.rmd_id and rownum = 1 ) letterNo,r.rmd_id	"
					+ "from wo_trc_rmd r " + "inner join wo_trc_rmd_pic_followup f on f.rmd_id = r.rmd_id "
					+ "inner join wo_trc_rmd_pic_followup_email e on e.rmd_id = r.rmd_id and e.target_date = f.target_date "
					+ "left join wo_mst_counter_type ct on ct.counter_type_id =r.counter_type_id and ct.enabled_flag = 'Y' "
					+ "left join wo_mst_counter_type_dtl d on d.counter_type_id = ct.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla "
					+ "left join wo_mst_user u1 on u1.user_id = r.user_id_1 "
					+ "left join wo_mst_user u2 on u2.user_id = r.user_id_2 "
					+ "left join wo_mst_user u3 on u3.user_id = r.user_id_3 "
					+ "left join wo_mst_report_type rt on rt.report_type_id = r.report_type_id "
					+ "where (f.followup_status is null or f.followup_status <> 'PIC_DONE') and r.reminder_status = 'REMINDER_ACTIVE'  "
					+ "and e.email_date is not null "
					+ "and r.enabled_flag = 'Y' and TO_CHAR(email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy')";

			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setRegulationTitleIn(obj[8] != null ? (String) obj[8] : null);
					data.setRegulationTitleEn(obj[9] != null ? (String) obj[9] : null);
					data.setTargetDate(obj[10] != null ? (Date) obj[10] : null);
					data.setReportNameIn(obj[11] != null ? (String) obj[11] : null);
					data.setReportNameEn(obj[12] != null ? (String) obj[12] : null);
					data.setDocumentNo(obj[13] != null ? (String) obj[13] : null);
					data.setId(obj[14] != null ? (MathUtil.returnIdObjectToLong(obj[14])) : null);
					data.setDedicatedTo(obj[15] != null ? (String) obj[15] : null);
					data.setSanction(obj[16] != null ? (String) obj[16] : null);
					data.setDueDate(obj[17] != null ? (Date) obj[17] : null);
					data.setSupportingUnit(obj[18] != null ? (String) obj[18] : null);
					data.setEmailId(obj[19] != null ? (MathUtil.returnIdObjectToLong(obj[19])) : null);
					data.setSlaType(obj[20] != null ? (String) obj[20] : null);
					data.setSla(obj[21] != null ? ((Number) obj[21]).toString() : null);
					data.setPublisherUnit(obj[22] != null ? (String) obj[22] : null);
					data.setDivisionName(obj[23] != null ? (String) obj[23] : null);
					data.setPic1Name(obj[24] != null ? (String) obj[24] : null);
					data.setPic2Name(obj[25] != null ? (String) obj[25] : null);
					data.setPic3Name(obj[26] != null ? (String) obj[26] : null);
					data.setCounterTypeIn(obj[27] != null ? (String) obj[27] : null);
					data.setCounterTypeEn(obj[28] != null ? (String) obj[28] : null);
					data.setReportTypeIn(obj[29] != null ? (String) obj[29] : null);
					data.setReportTypeEn(obj[30] != null ? (String) obj[30] : null);
					data.setDocumentCategoryIn(obj[31] != null ? (String) obj[31] : null);
					data.setDocumentCategoryEn(obj[32] != null ? (String) obj[32] : null);
					data.setLetterNo(obj[33] != null ? (String) obj[33] : null);
					data.setParentId(obj[34] != null ? (MathUtil.returnIdObjectToLong(obj[34])) : null);

					String detailQuery = "SELECT "
							+ "(case when c.division_id is not null then (case when c.division_id = u1.division_id then u1.division_name when c.division_id = u2.division_id then u2.division_name when c.division_id = u3.division_id then u3.division_name end) else null end) division, "
							+ "u1.name pic_1_name, u2.name pic_2_name, u3.name pic_3_name, "
							+ "u1.email pic_1_email, u2.email pic_2_email, u3.email pic_3_email "
							+ "FROM wo_trc_rmd_supporting_unit c "
							+ "left join wo_mst_user u1 on u1.user_id = c.email_cc_id_1 "
							+ "left join wo_mst_user u2 on u2.user_id = c.email_cc_id_2 "
							+ "left join wo_mst_user u3 on u3.user_id = c.email_cc_id_3 " + "WHERE c.rmd_id = "
							+ String.valueOf(data.getParentId().longValue());
					Query detailResult = getSession().createSQLQuery(detailQuery);

					List resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setListDetail(new ArrayList<SendEmailDetailVO>());
						for (int j = 0; j < resultDtl.size(); j++) {
							Object[] det = (Object[]) resultDtl.get(j);
							SendEmailDetailVO dtl = new SendEmailDetailVO();
							dtl.setDivisionName(det[0] != null ? (String) det[0] : null);
							dtl.setPic1Name(det[1] != null ? (String) det[1] : null);
							dtl.setPic2Name(det[2] != null ? (String) det[2] : null);
							dtl.setPic3Name(det[3] != null ? (String) det[3] : null);
							dtl.setPic1Email(det[4] != null ? (String) det[4] : null);
							dtl.setPic2Email(det[5] != null ? (String) det[5] : null);
							dtl.setPic3Email(det[6] != null ? (String) det[6] : null);
							data.getListDetail().add(dtl);
						}
					}
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}

	@SuppressWarnings("rawtypes")
	public String getEmailSubject(String templateCode) {
		String result = null;

		try {
			String query = "SELECT EMAIL_SUBJECT FROM wo_mst_email_template "
					+ "WHERE email_template_code = :templateCode and enabled_flag = 'Y'";

			Query queryResult = getSession().createSQLQuery(query);
			queryResult.setParameter("templateCode", templateCode);
			if(queryResult.uniqueResult() instanceof java.sql.Clob){
				result = FacesUtil.convertClobToString((java.sql.Clob)queryResult.uniqueResult());
			}else{
				result = (String) queryResult.uniqueResult();
			}

		} catch (Exception se) {
			throw se;
		} finally {
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	public String getEmailContent(String templateCode) {
		String result = null;

		try {
			String query = "SELECT EMAIL_CONTENT FROM wo_mst_email_template "
					+ "WHERE email_template_code = :templateCode and enabled_flag = 'Y'";

			Query queryResult = getSession().createSQLQuery(query);
			queryResult.setParameter("templateCode", templateCode);
			if(queryResult.uniqueResult() instanceof java.sql.Clob){
				result = FacesUtil.convertClobToString((java.sql.Clob)queryResult.uniqueResult());
			}else{
				result = (String) queryResult.uniqueResult();
			}

		} catch (Exception se) {
			throw se;
		} finally {
		}

		return result;
	}

	public SocializationPICFollowupEmailTrc findEmailSocializationById(Long id) {
		return (SocializationPICFollowupEmailTrc) getSession().load(SocializationPICFollowupEmailTrc.class, id);
	}

	public void updateEmailSocialization(SocializationPICFollowupEmailTrc entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public TrcCorrespondencePicFollowupEmail findEmailCorrespondenceById(Long id) {
		return (TrcCorrespondencePicFollowupEmail) getSession().load(TrcCorrespondencePicFollowupEmail.class, id);
	}

	public void updateEmailCorrespondence(TrcCorrespondencePicFollowupEmail entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public TrcRmdPicFollowupEmail findEmailRmdById(Long id) {
		return (TrcRmdPicFollowupEmail) getSession().load(TrcRmdPicFollowupEmail.class, id);
	}

	public void updateEmailRmd(TrcRmdPicFollowupEmail entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	public void updateFollowupRmd(TrcRmdPicFollowup entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}

	public RegMonitoringPICFollowUpEmailTrc findEmailRegMonitoringById(Long id) {
		return (RegMonitoringPICFollowUpEmailTrc) getSession().load(RegMonitoringPICFollowUpEmailTrc.class, id);
	}
	
	public void updateEmailRegMonitoring(RegMonitoringPICFollowUpEmailTrc entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	public TrcFinePicFollowupEmail findEmailDendaById(Long id) {
		return (TrcFinePicFollowupEmail) getSession().load(TrcFinePicFollowupEmail.class, id);
	}
	
	public void updateEmailDenda(TrcFinePicFollowupEmail entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataComplianceReview() throws Exception {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = " SELECT DISTINCT  u1.email pic1 ,u2.email pic2,u3.email pic3,email_date, "
					+ " 'H'||(CASE WHEN fe.sla != 0 THEN TO_CHAR(fe.sla_type) ELSE '' END)|| "
					+ " (CASE WHEN fe.sla != 0 THEN TO_CHAR(fe.sla) ELSE '' END)  "
					+ " counter_type, COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2, "
					//+ " pd.name_in reviewCategoryIn, pd.name_en reviewCategoryEn, ct.reviewed_branch, ct.document_no,  "
					+ " ct.INSPECTION_TITLE, ct.INSPECTION_NO, SUBJECT, OBSERVATION_RESULTS, RECOMMENDATION,ct.start_date,ct.end_date, f.target_date, "
					+ " f.COMPLIANCE_TEST_PIC_FP_ID,fe.COMP_TEST_PIC_FP_EMAIL_ID,fe.sla_type,fe.sla  "
					//+ ",(SELECT LISTAGG(comp.email, ',') WITHIN GROUP (ORDER BY pc.cmplc_review_pic_cmplc_id) FROM wo_trc_cmplc_review_pic_cmplc pc, wo_mst_user comp WHERE comp.user_id = pc.user_id and pc.compliance_review_id = f.compliance_review_id) emailCcCompliance "
					/*+ " FROM wo_trc_cmplc_review_pic_fp f  "
					+ " INNER JOIN wo_trc_compliance_review cr ON f.compliance_review_id = cr.compliance_review_id  "
					+ " INNER JOIN wo_trc_cmplc_rvw_pic_fp_email e ON f.cmplc_review_pic_followup_id = e.cmplc_review_pic_followup_id  "
					+ " LEFT JOIN wo_mst_counter_type c ON c.counter_type_id = cr.counter_type_id AND c.enabled_flag = 'Y'  "
					+ " LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = c.counter_type_id AND e.sla_type = d.sla_type AND e.sla = d.sla  "
					+ " LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  "
					+ " LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2 "
					+ " LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3 "
					+ " INNER JOIN wo_mst_parameter_dtl pd ON pd.parameter_dtl_code = cr.review_category "
					+ " WHERE (f.followup_status IS NULL OR f.followup_status <> 'PIC_DONE')   "
					+ " AND cr.reminder_status = 'REMINDER_ACTIVE' AND cr.follow_up = 'Y' "
					+ " AND cr.enabled_flag = 'Y' AND e.email_date IS NOT NULL AND TO_CHAR(e.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy') ";*/

					+ " FROM wo_trc_compliance_testing ct"
					+ " INNER JOIN WO_TRC_COMPLIANCE_TESTING_DTL ctd ON ctd.compliance_testing_id = ct.compliance_testing_id "
					+ " INNER JOIN WO_TRC_COMP_TEST_PIC_FP f ON f.COMPLIANCE_TESTING_DTL_ID = ctd.COMPLIANCE_TESTING_DTL_ID "
					+ " INNER JOIN WO_TRC_COMP_TEST_PIC_FP_EMAIL fe ON fe.COMPLIANCE_TEST_PIC_FP_ID = f.COMPLIANCE_TEST_PIC_FP_ID "
					+ " LEFT JOIN wo_mst_parameter_dtl pd2 ON pd2.parameter_dtl_code = f.followup_status  "
					+ " LEFT JOIN wo_mst_parameter_dtl pd3 ON pd3.parameter_dtl_code = ct.status  "
					+ " LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1  "
					+ " LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2  "
					+ " LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3  "
					+ " LEFT JOIN wo_mst_counter_type c ON c.counter_type_id = ct.counter_type_id AND c.enabled_flag = 'Y'  "
					+ " LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = c.counter_type_id AND fe.sla_type = d.sla_type AND fe.sla = d.sla  "
					
					+ " WHERE 1=1 "
					+ " AND ct.enabled_flag = 'Y' "
					+ " AND ct.status = 'DATA_ACTIVE' "
					+ " AND ctd.followup = 'Y' and (f.followup_status is null OR f.followup_status not in ('PIC_DONE','PIC_EXTENSION')) "
					+ " AND fe.email_date IS NOT NULL AND TO_CHAR(fe.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy') ";
			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setInspectionTitle(obj[8] != null ? (String) obj[8] : null);
					data.setInspectionNo(obj[9] != null ? (String) obj[9] : null);
					data.setSubject(obj[10] != null ? (String) obj[10] : null);
					data.setObservationResult(obj[11] != null ? (String) obj[11] : null);
					data.setRecommendation(obj[12] != null ? (String) obj[12] : null);
					data.setStartDate(obj[13] != null ? (Date) obj[13] : null);
					data.setEndDate(obj[14] != null ? (Date) obj[14] : null);
					data.setTargetDate(obj[15] != null ? (Date) obj[15] : null);
					data.setId(obj[16] != null ? (MathUtil.returnIdObjectToLong(obj[16])) : null);
					data.setEmailId(obj[17] != null ? (MathUtil.returnIdObjectToLong(obj[17])) : null);
					data.setSlaType(obj[18] != null ? (String) obj[18] : null);
					data.setSla(obj[19] != null ? ((Number) obj[19]).toString() : null);
					/*data.setEmailCcCompliance(obj[20] != null ? (String) obj[20] : null);
					String detailQuery = "SELECT area_review FROM wo_trc_cmplc_rvw_pc_fp_review WHERE cmplc_review_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					Query detailResult = getSession().createSQLQuery(detailQuery);
					List resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail1(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail1().add((resultDtl.get(j) != null ? (String) resultDtl.get(j) : ""));
					}
					detailQuery = "SELECT findings FROM wo_trc_cmplc_review_pc_fp_fdgs WHERE cmplc_review_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					detailResult = getSession().createSQLQuery(detailQuery);
					resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail2(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail2().add((resultDtl.get(j) != null ? (String) resultDtl.get(j) : ""));
					}
					detailQuery = "SELECT followup_points FROM wo_trc_cmplc_rvw_pc_fp_points WHERE cmplc_review_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					detailResult = getSession().createSQLQuery(detailQuery);
					resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail3(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail3().add((resultDtl.get(j) != null ? (String) resultDtl.get(j) : ""));
					}*/
					
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}

	@Override
	public ComplianceTestingPICFollowupEmail findEmailComplianceReviewById(Long id) {
		return (ComplianceTestingPICFollowupEmail) getSession().load(ComplianceTestingPICFollowupEmail.class, id);
	}


	@Override
	public void updateEmailComplianceReview(ComplianceTestingPICFollowupEmail entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataAudit() throws Exception {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = " SELECT DISTINCT u1.email pic1,u2.email pic2,u3.email pic3,email_date, " + 
					"					 'H'||(CASE WHEN e.sla != 0 THEN to_char(e.sla_type) ELSE '' END)|| " + 
					"					 (CASE WHEN e.sla != 0 THEN to_char(e.sla) ELSE '' END) counter_type, " +
					"					 COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2, " +
					"					 cr.audit_object, cr.audit_topic_in, cr.audit_topic_en, dbms_lob.substr(cr.scope, 4000, 1 ) scope," + 
					"					 TO_CHAR(cr.audit_date_from, 'dd-Mon-yyyy') audit_date_from," + 
					"					 TO_CHAR(cr.audit_date_to, 'dd-Mon-yyyy') audit_date_to, f.target_date, " + 
					"					 f.audit_pic_followup_id,e.audit_pic_followup_email_id,e.sla_type,e.sla, " + 
					"					 ac.name_in audit_category_in,ac.name_en audit_category_en,ao.name_in audit_object_in,ao.name_en audit_object_en, " +
					"					 aud.name_in auditor_in,aud.name_en auditor_en " +
					" FROM wo_trc_audit_pic_followup f" + 
					" INNER JOIN WO_TRC_AUDIT_PC_FP_BANK_CMITMT c ON c.AUD_PC_FP_BANK_CMITMT_ID = f.AUD_PC_FP_BANK_CMITMT_ID " +
					" INNER JOIN WO_TRC_AUDIT_CHECK_POINT p ON p.AUDIT_CHECK_POINT_ID = c.AUDIT_CHECK_POINT_ID " +
					" INNER JOIN wo_trc_audit cr ON p.audit_id = cr.audit_id" + 
					" INNER JOIN wo_trc_audit_pic_fp_email e ON f.audit_pic_followup_id = e.audit_pic_followup_id" + 
					" LEFT JOIN wo_mst_counter_type ct ON ct.counter_type_id = cr.counter_type_id AND ct.enabled_flag = 'Y'" + 
					" LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = ct.counter_type_id AND e.sla_type = d.sla_type AND e.sla = d.sla" + 
					" LEFT JOIN wo_mst_user u1 ON u1.user_id = f.user_id_1" + 
					" LEFT JOIN wo_mst_user u2 ON u2.user_id = f.user_id_2" + 
					" LEFT JOIN wo_mst_user u3 ON u3.user_id = f.user_id_3" +
					" LEFT JOIN wo_mst_parameter_dtl ac ON ac.parameter_code = 'AUDIT_CATEGORY' AND ac.parameter_dtl_code = cr.audit_category" +
					" LEFT JOIN wo_mst_parameter_dtl ao ON ao.parameter_code = 'AUDIT_OBJECT' AND ao.parameter_dtl_code = cr.audit_object" +
					" LEFT JOIN wo_mst_parameter_dtl aud ON aud.parameter_code = 'AUDITOR' AND aud.parameter_dtl_code = cr.auditor" +
					" WHERE (f.followup_status is null OR f.followup_status not in ('PIC_DONE','PIC_EXTENSION')) " + 
					"	AND cr.reminder_status = 'REMINDER_ACTIVE' " + 
					"	AND cr.follow_up = 'Y' " + 
					"	AND cr.enabled_flag = 'Y' " + 
					"	AND e.email_date IS NOT NULL " + 
					"	AND TO_CHAR(e.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy') ";

			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setAuditObject(obj[8] != null ? (String) obj[8] : null);
					data.setAuditTopicIn(obj[9] != null ? (String) obj[9] : null);
					data.setAuditTopicEn(obj[10] != null ? (String) obj[10] : null);
					data.setScope(obj[11] != null ? (String) obj[11] : null);
					data.setAuditDateFrom(obj[12] != null ? (String) obj[12] : null);
					data.setAuditDateTo(obj[13] != null ? (String) obj[13] : null);
					data.setTargetDate(obj[14] != null ? (Date) obj[14] : null);
					data.setId(obj[15] != null ? (MathUtil.returnIdObjectToLong(obj[15])) : null);
					data.setEmailId(obj[16] != null ? (MathUtil.returnIdObjectToLong(obj[16])) : null);
					data.setSlaType(obj[17] != null ? (String) obj[17] : null);
					data.setSla(obj[18] != null ? ((Number) obj[18]).toString() : null);
					data.setAuditCategoryIn(obj[19] != null ? (String) obj[19] : null);
					data.setAuditCategoryEn(obj[20] != null ? (String) obj[20] : null);
					data.setAuditObjectIn(obj[21] != null ? (String) obj[21] : null);
					data.setAuditObjectEn(obj[22] != null ? (String) obj[22] : null);
					data.setAuditTypeIn(obj[23] != null ? (String) obj[23] : null);
					data.setAuditTypeEn(obj[24] != null ? (String) obj[24] : null);
					/*String detailQuery = "SELECT audit_findings FROM wo_trc_audit_pic_fp_aud_fdgs WHERE audit_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					Query detailResult = getSession().createSQLQuery(detailQuery);
					List resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail1(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail1().add((resultDtl.get(j) != null ? FacesUtil.convertClobToString((Clob)resultDtl.get(j)) : ""));
					}
					detailQuery = "SELECT bank_response FROM wo_trc_audit_pic_fp_bank_rsps WHERE audit_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					detailResult = getSession().createSQLQuery(detailQuery);
					resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail2(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail2().add((resultDtl.get(j) != null ? FacesUtil.convertClobToString((Clob)resultDtl.get(j)) : ""));
					}
					detailQuery = "SELECT bank_commitment FROM wo_trc_audit_pc_fp_bank_cmitmt WHERE audit_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					detailResult = getSession().createSQLQuery(detailQuery);
					resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setDetail3(new ArrayList<String>());
						for (int j = 0; j < resultDtl.size(); j++)
							data.getDetail3().add((resultDtl.get(j) != null ? FacesUtil.convertClobToString((Clob)resultDtl.get(j))  : ""));
					}
					detailQuery = "SELECT "
							+ "(case when c.division_id is not null then (case when c.division_id = u1.division_id then u1.division_name when c.division_id = u2.division_id then u2.division_name when c.division_id = u3.division_id then u3.division_name end) else null end) division, "
							+ "u1.name pic_1_name, u2.name pic_2_name, u3.name pic_3_name, "
							+ "u1.email pic_1_email, u2.email pic_2_email, u3.email pic_3_email "
							+ "FROM wo_trc_audit_pic_fp_sup_unit c "
							+ "left join wo_mst_user u1 on u1.user_id = c.email_cc_id_1 "
							+ "left join wo_mst_user u2 on u2.user_id = c.email_cc_id_2 "
							+ "left join wo_mst_user u3 on u3.user_id = c.email_cc_id_3 " + "WHERE c.audit_pic_followup_id = "
							+ String.valueOf(data.getId().longValue());
					detailResult = getSession().createSQLQuery(detailQuery);

					resultDtl = detailResult.getResultList();
					if (resultDtl != null) {
						data.setListDetail(new ArrayList<SendEmailDetailVO>());
						for (int j = 0; j < resultDtl.size(); j++) {
							Object[] det = (Object[]) resultDtl.get(j);
							SendEmailDetailVO dtl = new SendEmailDetailVO();
							dtl.setDivisionName(det[0] != null ? (String) det[0] : null);
							dtl.setPic1Name(det[1] != null ? (String) det[1] : null);
							dtl.setPic2Name(det[2] != null ? (String) det[2] : null);
							dtl.setPic3Name(det[3] != null ? (String) det[3] : null);
							dtl.setPic1Email(det[4] != null ? (String) det[4] : null);
							dtl.setPic2Email(det[5] != null ? (String) det[5] : null);
							dtl.setPic3Email(det[6] != null ? (String) det[6] : null);
							data.getListDetail().add(dtl);
						}
					}*/
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}

	@Override
	public TrcAuditPicFollowupEmail findEmailAuditById(Long id) {
		return (TrcAuditPicFollowupEmail) getSession().load(TrcAuditPicFollowupEmail.class, id);
	}

	@Override
	public void updateEmailAudit(TrcAuditPicFollowupEmail entity) {
		try {
			getSession().update(entity);
			getSession().flush();
		} catch (Exception e) {
			getSession().getTransaction().rollback();
			e.printStackTrace();
		}
	}
	
	public String procedureUpdateEmailDate() throws Exception {
    	StoredProcedureQuery spq = this.getSession().createStoredProcedureQuery("wo_sp_update_email_date");
    	// Stored procedure call
    	spq.execute();
    	return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List<QA> getListDataQAPIC() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
		sb.append(" select qna_id, q_user_id, qUser.name qUserName, q_date, pd2.name_in divisionName, ");
		sb.append(" q_title, ticket_no, question, ");
		sb.append(" SUBSTR(TO_CHAR(SYSDATE - case when qa.last_update_date is null then qa.creation_date else qa.last_update_date end),0,16)  days,qa.category_type ");
		sb.append(" FROM wo_mst_qna qa inner join wo_mst_user qUser on qa.q_user_id = qUser.user_id ");
		sb.append(" left join wo_mst_user adminUser on qa.admin_user_id = adminUser.user_id ");
		sb.append(" left join wo_mst_user aUser on qa.a_user_id = aUser.user_id ");
		sb.append(" left join wo_mst_user pukUser on aUser.puk_nik = pukUser.user_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = category_type ");
		sb.append(" WHERE 1=1 and qa.enabled_flag = 'Y'  and qa.answer is null and (qa.q_status is null or qa.q_status <> 'QNA_STATUS_CLOSE') ");
		sb.append(" and (qa.last_update_date is null or sysdate > (qa.last_update_date + (select name_in from wo_mst_parameter_dtl where parameter_dtl_code= 'QNA_REMINDER_TIME_FOR_ANSWER_2')/24) ) ");
		sb.append(" and (qa.last_update_date is not null or sysdate > (qa.creation_date + (select name_in from wo_mst_parameter_dtl where parameter_dtl_code= 'QNA_REMINDER_TIME_FOR_ANSWER_1')/24) ) ");
//        sb.append(" ORDER BY qna_id DESC ");
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<QA> vo = new ArrayList<QA>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                QA data = new QA();
 
                data.setQnaId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setqUserName(obj[2]!=null?(String)obj[2]:null);
                data.setqDate(obj[3]!=null?(Date)obj[3]:null);
                if(data.getqDate()!=null) {
                	data.setqDateStr(sdf.format(data.getqDate()));
                }
                data.setDivisionName(obj[4]!=null?(String)obj[4]:null);
               
                data.setTitle(obj[5]!=null?(String)obj[5]:null);
                data.setTicketNo(obj[6]!=null?(String)obj[6]:null);
                
                data.setQuestion(obj[7]!=null?(String)obj[7]:null);
                
                if(obj[8]!=null){
                	String dataH = (String)obj[8];
                	String dataSplit[] = dataH.split(" ");
                	data.setH(dataSplit[0].substring(0,1)+Integer.parseInt(dataSplit[0].substring(1))+" "+dataSplit[1]);
                	
                }
                
                data.setCategoryType(obj[9]!=null?(String)obj[9]:null);
                
                
                vo.add(data);
            }
        }
        return vo;
    }
	
	public List<QA> getListDataQAClose() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append(" select qna_id, q_user_id, qUser.name qUserName, q_date, pd2.name_in divisionName, ");
		sb.append(" q_title, ticket_no, question, ");
		sb.append(" SUBSTR(TO_CHAR(SYSDATE - case when qa.last_update_date is null then qa.creation_date else qa.last_update_date end),0,16)  days,qa.category_type ");
		sb.append(" FROM wo_mst_qna qa inner join wo_mst_user qUser on qa.q_user_id = qUser.user_id ");
		sb.append(" left join wo_mst_user adminUser on qa.admin_user_id = adminUser.user_id ");
		sb.append(" left join wo_mst_user aUser on qa.a_user_id = aUser.user_id ");
		sb.append(" left join wo_mst_user pukUser on aUser.puk_nik = pukUser.user_id ");
		sb.append(" left join wo_mst_parameter_dtl pd2 on pd2.parameter_dtl_code = category_type ");
		sb.append(" WHERE 1=1 and qa.enabled_flag = 'Y' and qa.Q_STATUS = 'QNA_STATUS_ANSWERED' and qa.from_qna_id is null and read_flag = 'Y'  and  sysdate >= (qa.last_update_date +(select name_in from wo_mst_parameter_dtl where parameter_dtl_code= 'QNA_REMINDER_TIME_FOR_CLOSE')/24) ");
		
        
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<QA> vo = new ArrayList<QA>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                QA data = new QA();
 
                data.setQnaId(MathUtil.returnIdObjectToLong(obj[0]));
                data.setqUserName(obj[2]!=null?(String)obj[2]:null);
                data.setqDate(obj[3]!=null?(Date)obj[3]:null);
                if(data.getqDate()!=null) {
                	data.setqDateStr(sdf.format(data.getqDate()));
                }
                data.setDivisionName(obj[4]!=null?(String)obj[4]:null);
                
                data.setTitle(obj[5]!=null?(String)obj[5]:null);
                data.setTicketNo(obj[6]!=null?(String)obj[6]:null);
                
                data.setQuestion(obj[7]!=null?(String)obj[7]:null);
                
                if(obj[8]!=null){
                	String dataH = (String)obj[8];
                	String dataSplit[] = dataH.split(" ");
                	data.setH(dataSplit[0].substring(0,1)+Integer.parseInt(dataSplit[0].substring(1))+" "+dataSplit[1]);
                	
                }
                
                data.setCategoryType(obj[9]!=null?(String)obj[9]:null);
                
                
                vo.add(data);
            }
        }
        return vo;
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataRegulationMonitoring() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "	select distinct u1.email pic1 ,u2.email pic2,u3.email pic3,email_date,''counterType, COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2,	"+
					"	rn.name_in regulationTitleIn,rn.name_en regulationTitleEn,f.target_date, rn.document_no,rn.published_date,rn.effective_date,f.reg_monitoring_pic_followup_id, e.REG_MONITORING_PIC_FP_EMAIL_ID,e.sla_type,e.sla 	"+
					"	from WO_TRC_REG_MONITORING_PIC_FP f 	"+
					"	inner join WO_TRC_REG_MONITORING s on f.reg_monitoring_id = s.reg_monitoring_id 	"+
					"	inner join WO_TRC_REG_MONITOR_PC_FP_EMAIL e on f.reg_monitoring_pic_followup_id = e.reg_monitoring_pic_followup_id	"+
					"	inner join WO_TRC_REG_MONITOR_REGULATION r on r.reg_monitoring_id = s.reg_monitoring_id 	"+
					"	inner join wo_mst_regulation rn on rn.regulation_id = r.regulation_id 	"+
					"	left join wo_mst_counter_type c on c.counter_type_id = s.counter_type_id and c.enabled_flag = 'Y' 	"+
					"	left join wo_mst_counter_type_dtl d on d.counter_type_id = c.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla 	"+
					"	left join wo_mst_user u1 on u1.user_id = f.user_id_1 	"+
					"	left join wo_mst_user u2 on u2.user_id = f.user_id_2 	"+
					"	left join wo_mst_user u3 on u3.user_id = f.user_id_3 	"+
					"	where (f.followup_status is null or f.followup_status <> 'PIC_DONE')  	"+
					"	and s.reminder_status = 'REMINDER_ACTIVE' and s.follow_up = 'Y' 	"+
					"	and r.primary_flag = 'Y' and s.enabled_flag = 'Y' 	"+
					"	and e.email_date is not null and TO_CHAR(e.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy')	";


			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setRegulationTitleIn(obj[8] != null ? (String) obj[8] : null);
					data.setRegulationTitleEn(obj[9] != null ? (String) obj[9] : null);
					data.setTargetDate(obj[10] != null ? (Date) obj[10] : null);
					data.setDocumentNo(obj[11] != null ? (String) obj[11] : null);
					data.setPublishedDate(obj[12] != null ? (Date) obj[12] : null);
					data.setEffctiveDate(obj[13] != null ? (Date) obj[13] : null);
					data.setId(obj[14] != null ? (MathUtil.returnIdObjectToLong(obj[14])) : null);
					data.setEmailId(obj[15] != null ? (MathUtil.returnIdObjectToLong(obj[15])) : null);
					data.setSlaType(obj[16] != null ? (String) obj[16] : null);
					data.setSla(obj[17] != null ? ((Number) obj[17]).toString() : null);
					//data.setEmailCcCompliance(obj[18] != null ? (String) obj[18] : null);
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataDenda() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "	select distinct u1.email pic1 ,u2.email pic2,u3.email pic3,email_date,'' counterType, COALESCE(d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1,d.email_cc_2,	"+
					"	f.fine_pic_followup_id, e.FINE_PIC_FOLLOWUP_EMAIL_ID,e.sla_type,e.sla , PD1.NAME_IN senderIn, S.LETTER_NO, S.LETTER_DATE, RC.REGION_CODE, F.FINE_DEBITTED,F.FINE_AMOUNT, dbms_lob.substr(F.BREACHES, 4000, 1 ),	"+
					"	dbms_lob.substr(F.ROOT_CAUSE, 4000, 1 ), PD2.NAME_IN category, U1.DIVISION_NAME,U1.NAME PIC1Name,U2.NAME PIC2Name,U3.NAME PIC3Name,dbms_lob.substr(s.perihal_in, 4000, 1 ) perihal, pd3.NAME_IN reportName	"+
					"	from WO_TRC_FINE_PIC_FOLLOWUP f 	"+
					"	inner join WO_TRC_FINE s on f.fine_id = s.fine_id 	"+
					"	inner join WO_TRC_FINE_PIC_FOLLOWUP_EMAIL e on f.fine_pic_followup_id = e.fine_pic_followup_id	"+
					"	left join wo_mst_counter_type c on c.counter_type_id = s.counter_type_id and c.enabled_flag = 'Y' 	"+
					"	left join wo_mst_counter_type_dtl d on d.counter_type_id = c.counter_type_id and e.sla_type = d.sla_type and e.sla = d.sla 	"+
					"	left join wo_mst_user u1 on u1.user_id = f.user_id_1 	"+
					"	left join wo_mst_user u2 on u2.user_id = f.user_id_2 	"+
					"	left join wo_mst_user u3 on u3.user_id = f.user_id_3 	"+
					"	left join wo_mst_parameter_dtl pd1 on PD1.PARAMETER_DTL_CODE = S.SENDER_CODE	"+
					"	left join wo_mst_rc rc on RC.RC_ID = F.RC_ID	"+
					"	left join wo_mst_parameter_dtl pd2 on PD2.PARAMETER_DTL_CODE = F.CATEGORY	"+
					"	left join wo_mst_parameter_dtl pd3 on PD3.PARAMETER_DTL_CODE = S.REPORT_NAME	"+
					"	where (f.followup_status is null or f.followup_status <> 'PIC_DONE')  	"+
					"	and s.reminder_status = 'REMINDER_ACTIVE' and s.follow_up = 'Y' 	"+
					"	and s.enabled_flag = 'Y' 	"+
					" 	and e.enabled_flag = 'Y'    "+
					"	and e.email_date is not null and TO_CHAR(e.email_date,'dd-Mon-yyyy') = TO_CHAR(sysdate,'dd-Mon-yyyy')	";


			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setPic1(obj[0] != null ? (String) obj[0] : null);
					data.setPic2(obj[1] != null ? (String) obj[1] : null);
					data.setPic3(obj[2] != null ? (String) obj[2] : null);
					data.setEmailDate(obj[3] != null ? (Date) obj[3] : null);
					data.setCounterType(obj[4] != null ? (String) obj[4] : null);
					data.setEmailTo(obj[5] != null ? (String) obj[5] : null);
					data.setEmailCc1(obj[6] != null ? (String) obj[6] : null);
					data.setEmailCc2(obj[7] != null ? (String) obj[7] : null);
					data.setId(obj[8] != null ? (MathUtil.returnIdObjectToLong(obj[8])) : null);
					data.setEmailId(obj[9] != null ? (MathUtil.returnIdObjectToLong(obj[9])) : null);
					data.setSlaType(obj[10] != null ? (String) obj[10] : null);
					data.setSla(obj[11] != null ? ((Number) obj[11]).toString() : null);
					data.setSenderIn(obj[12] != null ? (String) obj[12] : null);
					data.setLetterNo(obj[13] != null ? (String) obj[13] : null);
					data.setLetterDate(obj[14] != null ? (Date) obj[14] : null);
					data.setRegionCode(obj[15] != null ? (String) obj[15] : null);
					data.setDebitted(obj[16] != null ? (Date) obj[16] : null);
					data.setAmount(obj[17] != null ? MathUtil.returnIdObjectToLong(obj[17]) : null);
					data.setBreaches(obj[18] != null ? (String) obj[18] : null);
					data.setRootCause(obj[19] != null ? (String) obj[19] : null);
					data.setCategory(obj[20] != null ? (String) obj[20] : null);
					data.setDivisionName(obj[21] != null ? (String) obj[21] : null);
					data.setPic1Name(obj[22] != null ? (String) obj[22] : null);
					data.setPic2Name(obj[23] != null ? (String) obj[23] : null);
					data.setPic3Name(obj[24] != null ? (String) obj[24] : null);
					data.setPerihalIn(obj[25] != null ? (String) obj[25] : null);
					data.setReportNameIn(obj[26] != null ? (String) obj[26] : null);
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailVO> getListDataExpiredPeraturanInternal() {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "select c.email email_to, u.email email_cc_1, NVL(m.NAME_IN, '6') mm, r.expired_date, r.document_no, r.name_in, r.name_en, r.regulation_id " + 
					"from WO_MST_REGULATION r " + 
					"left join WO_MST_DOCUMENT_TYPE d ON d.document_type_id = r.document_type_id and lower(d.document_type_in) not like '%pemberitahuan%' " + 
					"left join WO_MST_PARAMETER_DTL m on  m.PARAMETER_CODE = 'SYSTEM' AND m.PARAMETER_DTL_CODE = 'INTERNAL_REGULATION_EMAIL_EXPIRED_MONTH' " + 
					"left join WO_MST_USER c on c.nik = r.created_by and c.enabled_flag = 'Y' " + 
					"left join WO_MST_USER u on u.nik = r.last_update_by and u.enabled_flag = 'Y' " + 
					"where r.JENIS_KETENTUAN = 'KETENTUAN_INTERNAL' and r.status = 'DATA_ACTIVE' and r.enabled_flag = 'Y' and r.expired_date BETWEEN " + 
					"ADD_MONTHS(TRUNC(SYSDATE), NVL(TO_NUMBER(m.NAME_IN), 6)) - " + 
					"NVL((SELECT TO_NUMBER(NAME_IN) FROM WO_MST_PARAMETER_DTL WHERE PARAMETER_CODE = 'SYSTEM' AND PARAMETER_DTL_CODE = 'INTERNAL_REGULATION_EMAIL_REPEAT'), 0) " + 
					"AND ADD_MONTHS(TRUNC(SYSDATE), NVL(TO_NUMBER(m.NAME_IN), 6)) and (c.email is not null or u.email is not null)";


			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setEmailTo(obj[0] != null ? (String) obj[0] : null);
					data.setEmailCc1(obj[1] != null ? (String) obj[1] : null);
					data.setSla(obj[2] != null ? (String) obj[2] : null);
					data.setTargetDate(obj[3] != null ? (Date) obj[3] : null);
					data.setDocumentNo(obj[4] != null ? (String) obj[4] : null);
					data.setRegulationTitleIn(obj[5] != null ? (String) obj[5] : null);
					data.setRegulationTitleEn(obj[6] != null ? (String) obj[6] : null);
					data.setId(obj[7] != null ? (MathUtil.returnIdObjectToLong(obj[7])) : null);
					data.setEmailId(obj[7] != null ? (MathUtil.returnIdObjectToLong(obj[7])) : null);
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}
	
	public List<SendEmailVO> getListEmailAdminByQnaCategory(String qaCategory) {
		List<SendEmailVO> listResult = new ArrayList<SendEmailVO>();
		try {
			String query = "select m.user_id, email from WO_MST_QNA_CATEGORY_MAP m INNER JOIN WO_MST_USER u on u.user_id = m.user_id where QNA_CATEGORY_CODE = '"+qaCategory+"' and m.enabled_flag = 'Y' ";
					
			Query queryResult = getSession().createSQLQuery(query);

			List result = queryResult.getResultList();

			if (result != null) {
				for (int i = 0; i < result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailVO data = new SendEmailVO();
					data.setEmailTo(obj[1] != null ? (String) obj[1] : null);
					data.setId(obj[0] != null ? (MathUtil.returnIdObjectToLong(obj[0])) : null);
					listResult.add(data);
				}
			}

		} catch (Exception se) {
			throw se;
		} finally {

		}

		return listResult;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<InternalRegulationPenerbitanPicTpkVo> getListDataIrgPenerbitanPIC() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
//        sb.append(" SELECT DISTINCT ");
//        sb.append(" 	   u1.email pic1, u2.email pic2, u3.email pic3, u1.DIVISION_NAME, ");
//        sb.append("        u1.NAME PIC1Name, u2.NAME PIC2Name, u3.NAME PIC3Name, e.email_date, ");
//        sb.append("        COALESCE (d.email_to, 'REMINDER_PIC1') email_to, d.email_cc_1, d.email_cc_2, ");
//        sb.append("        i.IRG_ID, t.IRG_PIC_ID, e.IRG_PIC_EMAIL_ID, e.sla_type, e.sla, i.IRG_TITLE, ");
//        sb.append("        pd.NAME_IN REGULATION_TYPE_NAME, t.TARGET_DATE, ur1.email irg_pic1, ");
//        sb.append("        ur2.email irg_pic2, ur1.NAME irg_pic_name1, ur2.NAME irg_pic_name2 ");
//        sb.append("   FROM WO_TRC_IRG i ");
//        sb.append("        INNER JOIN WO_TRC_IRG_PIC_IRG r ON i.IRG_ID = r.IRG_ID ");
//        sb.append("        INNER JOIN WO_TRC_IRG_PIC_TPK t ON i.IRG_ID = t.IRG_ID ");
//        sb.append("        INNER JOIN WO_TRC_IRG_PIC_TPK_EMAIL e ON t.IRG_PIC_ID = e.IRG_PIC_ID ");
//        sb.append("        INNER JOIN WO_MST_PARAMETER_DTL pd ON i.REGULATION_TYPE = pd.PARAMETER_DTL_ID ");
//        sb.append("        LEFT JOIN wo_mst_counter_type c ON c.counter_type_id = i.counter_type_id ");
//        sb.append("                                       AND c.enabled_flag = 'Y' ");
//        sb.append("        LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = c.counter_type_id ");
//        sb.append("                                           AND e.sla_type = d.sla_type ");
//        sb.append("                                           AND e.sla = d.sla ");
//        sb.append("		   LEFT JOIN wo_mst_user ur1 ON ur1.user_id = r.user_id_1 ");
//        sb.append("        LEFT JOIN wo_mst_user ur2 ON ur2.user_id = r.user_id_2 ");
//        sb.append("        LEFT JOIN wo_mst_user u1 ON u1.user_id = t.user_id_1 ");
//        sb.append("        LEFT JOIN wo_mst_user u2 ON u2.user_id = t.user_id_2 ");
//        sb.append("        LEFT JOIN wo_mst_user u3 ON u3.user_id = t.user_id_3 ");
//        sb.append("  WHERE 1=1 ");
//        sb.append("        AND t.enabled_flag = 'Y' ");
//        sb.append("        AND i.enabled_flag = 'Y' ");
//        sb.append("        AND e.email_date IS NOT NULL ");
//        sb.append("        AND TO_CHAR (e.email_date, 'dd-Mon-yyyy') = ");
//        sb.append("               TO_CHAR (SYSDATE, 'dd-Mon-yyyy') ");
        sb.append(" SELECT DISTINCT u1.email pic1 ");
        sb.append(" 	,u2.email pic2 ");
        sb.append(" 	,u3.email pic3 ");
        sb.append(" 	,u1.DIVISION_NAME ");
        sb.append(" 	,u1.NAME PIC1Name ");
        sb.append(" 	,u2.NAME PIC2Name ");
        sb.append(" 	,u3.NAME PIC3Name ");
        sb.append(" 	,e.email_date ");
        sb.append(" 	,COALESCE (d.email_to,'REMINDER_PIC1') email_to ");
        sb.append(" 	,d.email_cc_1 ");
        sb.append(" 	,d.email_cc_2 ");
        sb.append(" 	,i.IRG_ID ");
        sb.append(" 	,t.IRG_PIC_ID ");
        sb.append(" 	,e.IRG_PIC_EMAIL_ID ");
        sb.append(" 	,e.sla_type ");
        sb.append(" 	,e.sla ");
        sb.append(" 	,i.IRG_TITLE ");
        sb.append(" 	,pd.NAME_IN REGULATION_TYPE_NAME ");
        sb.append(" 	,t.TARGET_DATE ");
        sb.append(" 	,ur1.email irg_pic1 ");
        sb.append(" 	,ur2.email irg_pic2 ");
        sb.append(" 	,ur1.NAME irg_pic_name1 ");
        sb.append(" 	,ur2.NAME irg_pic_name2 ");
        sb.append("     ,(SELECT LISTAGG(wtieg.EMAIL_GROUP, ', ') ");
    	sb.append("              WITHIN GROUP (ORDER BY wtieg.EMAIL_GROUP) AS EMAIL_GROUPS ");
    	sb.append("         FROM WO_TRC_IRG_EMAIL_GROUP wtieg ");
    	sb.append("        WHERE wtieg.IRG_ID = i.IRG_ID ");
    	sb.append("              AND (wtieg.REVIEW_APPROVAL_FLAG IS NULL OR wtieg.REVIEW_APPROVAL_FLAG = 'N')) EMAIL_GROUP ");        
        sb.append(" 	,ur3.email irg_pic3 ");
        sb.append(" 	,ur3.NAME irg_pic_name3 ");
        sb.append(" 	,i.counter_type_id");
        sb.append(" FROM WO_TRC_IRG i ");
        sb.append(" 	INNER JOIN WO_TRC_IRG_PIC_IRG r ON i.IRG_ID = r.IRG_ID ");
        sb.append(" 	INNER JOIN WO_TRC_IRG_PIC_TPK t ON i.IRG_ID = t.IRG_ID ");
        sb.append(" 	INNER JOIN WO_TRC_IRG_PIC_TPK_EMAIL e ON t.IRG_PIC_ID = e.IRG_PIC_ID ");
        sb.append(" 	INNER JOIN WO_MST_PARAMETER_DTL pd ON i.REGULATION_TYPE = pd.PARAMETER_DTL_ID ");
        sb.append(" 	LEFT JOIN wo_mst_counter_type c ON c.counter_type_id = i.counter_type_id ");
        sb.append(" 		AND c.enabled_flag = 'Y' ");
        sb.append(" 	LEFT JOIN wo_mst_counter_type_dtl d ON d.counter_type_id = c.counter_type_id ");
        sb.append(" 		AND e.sla_type = d.sla_type ");
        sb.append(" 		AND e.sla = d.sla ");
        sb.append(" 	LEFT JOIN wo_mst_user ur1 ON ur1.user_id = r.user_id_1 ");
        sb.append(" 	LEFT JOIN wo_mst_user ur2 ON ur2.user_id = r.user_id_2 ");
        sb.append(" 	LEFT JOIN wo_mst_user ur3 ON ur3.user_id = r.user_id_3 ");
        sb.append(" 	LEFT JOIN wo_mst_user u1 ON u1.user_id = t.user_id_1 ");
        sb.append(" 	LEFT JOIN wo_mst_user u2 ON u2.user_id = t.user_id_2 ");
        sb.append(" 	LEFT JOIN wo_mst_user u3 ON u3.user_id = t.user_id_3 ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" 	AND t.enabled_flag = 'Y' ");
        sb.append(" 	AND i.enabled_flag = 'Y' ");
        sb.append(" 	AND (t.REVIEW_APPROVAL_FLAG is null or t.REVIEW_APPROVAL_FLAG = 'N') ");
        sb.append(" 	AND e.email_date IS NOT NULL ");
        sb.append(" 	AND TO_CHAR (e.email_date, 'dd-Mon-yyyy') = TO_CHAR (SYSDATE, 'dd-Mon-yyyy') ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<InternalRegulationPenerbitanPicTpkVo> vo = new ArrayList<InternalRegulationPenerbitanPicTpkVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
                InternalRegulationPenerbitanPicTpkVo data = new InternalRegulationPenerbitanPicTpkVo();
 
                data.setEmailPic1(obj[0]!=null?(String)obj[0]:null);
                data.setEmailPic2(obj[1]!=null?(String)obj[1]:null);
                data.setEmailPic3(obj[2]!=null?(String)obj[2]:null);
                data.setDivisionName(obj[3]!=null?(String)obj[3]:null);
                data.setUser1Name(obj[4]!=null?(String)obj[4]:null);
                data.setUser2Name(obj[5]!=null?(String)obj[5]:null);
                data.setUser3Name(obj[6]!=null?(String)obj[6]:null);
                data.setEmailDate(obj[7]!=null?(Date)obj[7]:null);
                data.setEmailTo(obj[8]!=null?(String)obj[8]:null);
                data.setEmailCc1(obj[9]!=null?(String)obj[9]:null);
                data.setEmailCc2(obj[10]!=null?(String)obj[10]:null);                
                data.setIrgId(obj[11] != null ? (MathUtil.returnIdObjectToLong(obj[11])) : null);
                data.setIrgPicId(obj[12] != null ? (MathUtil.returnIdObjectToLong(obj[12])) : null);
                data.setIrgPicEmailId(obj[13] != null ? (MathUtil.returnIdObjectToLong(obj[13])) : null);
                data.setSlaType(obj[14]!=null?(String)obj[14]:null);        
                data.setSla(obj[15] != null ? (MathUtil.returnIdObjectToLong(obj[15])) : null);                
                data.setIrgTitle(obj[16]!=null?(String)obj[16]:null);  
                data.setRegulationTypeName(obj[17]!=null?(String)obj[17]:null);  
                data.setTargetDate(obj[18]!=null?(Date)obj[18]:null);
                data.setTargetDateStr(obj[18]!=null?(String)sdf.format((Date)obj[18]):null);
                data.setEmailIrgPic1(obj[19]!=null?(String)obj[19]:null);  
                data.setEmailIrgPic2(obj[20]!=null?(String)obj[20]:null);  
                data.setIrgPicName1(obj[21]!=null?(String)obj[21]:null);  
                data.setIrgPicName2(obj[22]!=null?(String)obj[22]:null);  
                data.setEmailGroupTpk(obj[23] != null ? (String) obj[23] : null);                
                data.setEmailIrgPic3(obj[24]!=null?(String)obj[24]:null);  
                data.setIrgPicName3(obj[25]!=null?(String)obj[25]:null); 
                data.setCounterTypeId(obj[26]!=null?(MathUtil.returnIdObjectToLong(obj[26])):null); 
                
                if (data.getIrgId() != null) {
                	// Query tpg
					StringBuilder sb2 = new StringBuilder();
					
					sb2.append(" SELECT u1.email pic1 ");
					sb2.append(" 	,u2.email pic2 ");
					sb2.append(" 	,u3.email pic3 ");
					sb2.append(" 	,u1.DIVISION_NAME ");
					sb2.append(" 	,u1.NAME PIC1Name ");
					sb2.append(" 	,u2.NAME PIC2Name ");
					sb2.append(" 	,u3.NAME PIC3Name ");
					sb2.append(" FROM WO_TRC_IRG_PIC_TPG wtipt ");
					sb2.append(" 	LEFT JOIN wo_mst_user u1 ON u1.user_id = wtipt.user_id_1 ");
					sb2.append(" 	LEFT JOIN wo_mst_user u2 ON u2.user_id = wtipt.user_id_2 ");
					sb2.append(" 	LEFT JOIN wo_mst_user u3 ON u3.user_id = wtipt.user_id_3 ");
					sb2.append(" WHERE 1 = 1 ");
					sb2.append(" 	AND wtipt.ENABLED_FLAG = 'Y' ");
					sb2.append(" 	AND wtipt.irg_id = :irgId ");
					
					Query query2 = getSession().createSQLQuery(sb2.toString());
					query2.setParameter("irgId", data.getIrgId());
					
					List<Object[]> result2 = query2.getResultList();
					
					if (result2 != null && !result2.isEmpty()) {
						List<InternalRegulationPenerbitanPicTpgVo> irgPicTpgList = new ArrayList<>();
						
						for (Object[] obj2 : result2) {
							InternalRegulationPenerbitanPicTpgVo dataPicTpg = new InternalRegulationPenerbitanPicTpgVo();
							
							dataPicTpg.setUser1Email(obj2[0] != null ? (String) obj2[0] : null);
							dataPicTpg.setUser2Email(obj2[1] != null ? (String) obj2[1] : null);
							dataPicTpg.setUser3Email(obj2[2] != null ? (String) obj2[2] : null);
							dataPicTpg.setDivisionName(obj2[3] != null ? (String) obj2[3] : null);
							dataPicTpg.setUser1Name(obj2[4] != null ? (String) obj2[4] : null);
							dataPicTpg.setUser2Name(obj2[5] != null ? (String) obj2[5] : null);
							dataPicTpg.setUser3Name(obj2[6] != null ? (String) obj2[6] : null);
							
							irgPicTpgList.add(dataPicTpg);
						}
						
						data.setIrgPicTpgVoList(irgPicTpgList);
					}
					// Query tpg
					
					// Query attachment
					StringBuilder sb3 = new StringBuilder();
					
					sb3.append(" SELECT (SELECT wmpd.NAME_IN  FROM WO_MST_PARAMETER_DTL wmpd WHERE PARAMETER_DTL_CODE = 'ATTACHMENT_FILE_PATH') || wtia.ATTACHMENT_FILE ");
					sb3.append(" 	,(SELECT wmpd.NAME_IN  FROM WO_MST_PARAMETER_DTL wmpd WHERE PARAMETER_DTL_CODE = 'ATTACHMENT_FILE_PATH') || wtia.FILE_ID ");
					sb3.append(" FROM WO_TRC_IRG_ATTACHMENT wtia ");
					sb3.append(" WHERE 1 = 1 ");
					sb3.append(" 	AND wtia.ENABLED_FLAG = 'Y' ");
					sb3.append(" 	AND wtia.IRG_ID = :irgId ");
					
					Query query3 = getSession().createSQLQuery(sb3.toString());
					query3.setParameter("irgId", data.getIrgId());
					
					List<Object[]> result3 = query3.getResultList();
					
					if (result3 != null && !result3.isEmpty()) {
						List<InternalRegulationPenerbitanAttachmentVo> irgAttachmentList = new ArrayList<>();
						
						for (Object[] obj3 : result3) {
							InternalRegulationPenerbitanAttachmentVo irgAttachment = new InternalRegulationPenerbitanAttachmentVo();
							
							irgAttachment.setAttachmentFile(obj3[0] != null ? (String) obj3[0] : null);
							irgAttachment.setFileId(obj3[1] != null ? (String) obj3[1] : null);
							
							irgAttachmentList.add(irgAttachment);
						}
						
						data.setIrgAttachmentVoList(irgAttachmentList);
					}
					
				}
                
                vo.add(data);
            }
        }
        return vo;
    }
	
	@SuppressWarnings("rawtypes")
	public List<CompliancePlanSelfAssessmentPicEmailVo> getListDataCpsa() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT DISTINCT ");
        sb.append("        u1.email pic1, u2.email pic2, u3.email pic3, u1.DIVISION_NAME, ");
        sb.append("        u1.NAME PIC1Name, u2.NAME PIC2Name, u3.NAME PIC3Name, c.email_date, ");
        sb.append("        COALESCE (e.email_to, 'REMINDER_PIC1') email_to, e.email_cc_1, e.email_cc_2, ");
        sb.append("        a.CPSA_ID, b.CPSA_PIC_ID, c.CPSA_PIC_EMAIL_ID, a.LETTER_ABOUT, e.sla_type, e.sla, ");
        sb.append("        b.target_date, adm.email cpsaAdmin, adm.name cpsaAdminName ");
        sb.append("   FROM WO_MST_CPSA a ");
        sb.append("        INNER JOIN WO_MST_CPSA_PIC b ON a.CPSA_ID = b.CPSA_ID ");
        sb.append("        INNER JOIN WO_MST_CPSA_PIC_EMAIL c ON b.CPSA_PIC_ID = c.CPSA_PIC_ID ");
        sb.append("        LEFT JOIN wo_mst_counter_type d ON d.counter_type_id = a.COUNTER_TYPE_ID ");
        sb.append("                                       AND d.enabled_flag = 'Y' ");
        sb.append("        LEFT JOIN wo_mst_counter_type_dtl e ON d.counter_type_id = e.counter_type_id ");
        sb.append("                                           AND c.sla_type = e.sla_type ");
        sb.append("                                           AND c.sla = e.sla ");
        sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL pd ON b.STATUS_PIC_ID = pd.PARAMETER_DTL_ID ");
        sb.append("        LEFT JOIN WO_MST_PARAMETER_DTL pd2 ON b.CPSA_STATUS_ID = pd2.PARAMETER_DTL_ID ");
        sb.append("        LEFT JOIN wo_mst_user u1 ON u1.user_id = b.user_id_1 ");
        sb.append("        LEFT JOIN wo_mst_user u2 ON u2.user_id = b.user_id_2 ");
        sb.append("        LEFT JOIN wo_mst_user u3 ON u3.user_id = b.user_id_3 ");
        sb.append("		   LEFT JOIN wo_mst_user adm ON adm.nik = b.created_by ");
        sb.append("  WHERE 1=1 ");
        sb.append("        AND b.enabled_flag = 'Y' ");
        sb.append("        AND a.enabled_flag = 'Y' ");
        sb.append("        AND c.email_date IS NOT NULL ");
        sb.append("        AND (pd.PARAMETER_DTL_CODE is null OR ");
        sb.append("                  pd.PARAMETER_DTL_CODE IN('CPSA_INPROGRESS', 'CPSA_REJECTED')) ");
        sb.append("        AND (pd2.PARAMETER_DTL_CODE is null OR ");
        sb.append("                  pd2.PARAMETER_DTL_CODE IN('COMPLIANCE_OPEN')) ");
        sb.append("        AND TO_CHAR (c.email_date, 'dd-Mon-yyyy') = ");
        sb.append("               TO_CHAR (SYSDATE, 'dd-Mon-yyyy') ");
        
        Query result = getSession().createSQLQuery(sb.toString());
        List resultList = result.getResultList();
        
        List<CompliancePlanSelfAssessmentPicEmailVo> vo = new ArrayList<CompliancePlanSelfAssessmentPicEmailVo>();
        
        if(resultList!=null) {
            for(int i=0; i<resultList.size(); i++) {
                Object[] obj = (Object[]) resultList.get(i);
				CompliancePlanSelfAssessmentPicEmailVo data = new CompliancePlanSelfAssessmentPicEmailVo();
				data.setEmailPic1(obj[0] != null ? (String) obj[0] : null);
				data.setEmailPic2(obj[1] != null ? (String) obj[1] : null);
				data.setEmailPic3(obj[2] != null ? (String) obj[2] : null);
				data.setDivisionName(obj[3] != null ? (String) obj[3] : null);
				data.setUser1Name(obj[4] != null ? (String) obj[4] : null);
				data.setUser2Name(obj[5] != null ? (String) obj[5] : null);
				data.setUser3Name(obj[6] != null ? (String) obj[6] : null);
				data.setEmailDate(obj[7] != null ? (Date) obj[7] : null);
				data.setEmailTo(obj[8] != null ? (String) obj[8] : null);
				data.setEmailCc1(obj[9] != null ? (String) obj[9] : null);
				data.setEmailCc2(obj[10] != null ? (String) obj[10] : null);
				data.setCpsaId(obj[11] != null ? (MathUtil.returnIdObjectToLong(obj[11])) : null);
				data.setCpsaPicId(obj[12] != null ? (MathUtil.returnIdObjectToLong(obj[12])) : null);
				data.setCpsaPicEmailId(obj[13] != null ? (MathUtil.returnIdObjectToLong(obj[13])) : null);
				data.setLetterAbout(obj[14] != null ? (String) obj[14] : null);
				data.setSlaType(obj[15] != null ? (String) obj[15] : null);
				data.setSla(obj[16] != null ? (MathUtil.returnIdObjectToLong(obj[16])) : null);
				data.setTargetDate(obj[17] != null ? (Date) obj[17] : null);
				data.setTargetDateStr(obj[17] != null ? sdf.format(((Date)obj[17])):"N/A");
				data.setCpsaAdminEmail(obj[18] != null ? (String) obj[18] : null);
				data.setCpsaAdminName(obj[19] != null ? (String) obj[19] : null);
				
				vo.add(data);
            }
        }
        
        return vo;
    }
	
	@SuppressWarnings("rawtypes")
	public ParameterDetail getDataParameter(String parameterDtlCode) {
		ParameterDetail paramDtl = new ParameterDetail();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" SELECT PARAMETER_CODE, PARAMETER_DTL_CODE, NAME_IN, NAME_EN ");
			sb.append("   FROM WO_MST_PARAMETER_DTL ");
			sb.append("  WHERE PARAMETER_DTL_CODE = :parameterDtlCode ");
			sb.append("        AND enabled_flag = 'Y' ");

			Query queryResult = getSession().createSQLQuery(sb.toString());
			queryResult.setParameter("parameterDtlCode", parameterDtlCode);
			
			List dataList = queryResult.getResultList();
			for(int i=0;i<dataList.size(); i++) {
				Object[] obj = (Object[])dataList.get(i);
				paramDtl.setParameterCode((String)obj[0]);
				paramDtl.setParameterDtlCode((String)obj[1]);
				paramDtl.setNameIn((String)obj[2]);
				paramDtl.setNameEn((String)obj[3]);				
			}

		} catch (Exception se) {
		   se.printStackTrace();
		}

		return paramDtl;
	}


	@SuppressWarnings("rawtypes")
	@Override
	public List<SendEmailIRGObsoleteVO> getListDataIRGObsolete() {
		List<SendEmailIRGObsoleteVO> resultList = new ArrayList<SendEmailIRGObsoleteVO>();
		try {
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT DISTINCT "
					+ "u1.EMAIL AS PIC_EMAIL1, "
					+ "u1.NAME AS PIC_NAME1, "
					+ "u2.EMAIL AS PIC_EMAIL2, "
					+ "u2.NAME AS PIC_NAME2, "
					+ "u3.EMAIL AS PIC_EMAIL3, "
					+ "u3.NAME AS PIC_NAME3, "
					+ "u4.EMAIL AS PIC_IRG_EMAIL1, "
					+ "u4.NAME AS PIC_IRG_NAME1, "
					+ "u5.EMAIL AS PIC_IRG_EMAIL2, "
					+ "u5.NAME AS PIC_IRG_NAME2, "
					+ "io.OBSOLETE_TITLE, "
					+ "pd.NAME_IN, "
					+ "iope.SLA_TYPE, "
					+ "iope.SLA, "
					+ "io.OBSOLETE_INFO, "
					+ "TO_CHAR(iopk.TARGET_DATE_KONV,'fmDD Month fmYYYY'), "
					+ "iopk.TARGET_DATE_KONV ");
			sb.append("FROM WO_TRC_IRG_OBS_PIC_EMAIL iope ");
			sb.append("INNER JOIN WO_TRC_IRG_OBSOLETE_PIC_KONV iopk ON iopk.KONV_PIC_ID = iope.KONV_PIC_ID ");
			sb.append("LEFT JOIN WO_TRC_IRG_OBSOLETE io ON iopk.IRG_OBSOLETE_ID = io.IRG_OBSOLETE_ID ");
			sb.append("LEFT JOIN WO_MST_PARAMETER_DTL pd ON io.REG_OBSOLETE_TYPE = pd.PARAMETER_DTL_ID ");
			sb.append("LEFT JOIN WO_MST_USER u1 ON iopk.USER_ID_1 = u1.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u2 ON iopk.USER_ID_2 = u2.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u3 ON iopk.USER_ID_3 = u3.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u4 ON io.PIC_IRG_USER_ID_1 = u4.USER_ID ");
			sb.append("LEFT JOIN WO_MST_USER u5 ON io.PIC_IRG_USER_ID_2 = u5.USER_ID ");
			sb.append("WHERE 1=1 ");
			sb.append("AND iope.ENABLED_FLAG = 'Y' ");
			sb.append("AND iope.EMAIL_DATE IS NOT NULL ");
			sb.append("AND (io.open_close_reg_obsolete = 611 OR io.open_close_reg_obsolete = 971)"); // reminder hanya status yang open 
			sb.append("AND TO_CHAR(iope.EMAIL_DATE, 'DD-MM-YYYY') = TO_CHAR(SYSDATE,'DD-MM-YYYY') ");
			
			Query query = getSession().createSQLQuery(sb.toString());
			
			List result = query.getResultList();
			
			if(result != null) {
				for(int i = 0; i<result.size(); i++) {
					Object[] obj = (Object[]) result.get(i);
					SendEmailIRGObsoleteVO vo = new SendEmailIRGObsoleteVO();
					
					vo.setUserEmail1(obj[0] != null ? (String)obj[0] : "");
					vo.setUserName1(obj[1] != null ? (String)obj[1] : "");
					
					vo.setUserEmail2(obj[2] != null ? (String)obj[2] : "");
					vo.setUserName2(obj[3] != null ? (String)obj[3] : "");
					
					vo.setUserEmail3(obj[4] != null ? (String)obj[4] : "");
					vo.setUserName3(obj[5] != null ? (String)obj[5] : "");
					
					vo.setPicIrgEmail1(obj[6] != null ? (String)obj[6] : "");
					vo.setPicIrgName1(obj[7] != null ? (String)obj[7] : "");
					
					vo.setPicIrgEmail2(obj[8] != null ? (String)obj[8] : "");
					vo.setPicIrgName2(obj[9] != null ? (String)obj[9] : "");
					
					vo.setObsoleteTitle(obj[10] != null ? (String)obj[10] : "");
					vo.setRegObsoleteTypeStr(obj[11] != null ? (String)obj[11] : "");
					
					vo.setSlaType(obj[12] != null ? (String)obj[12] : "");
					vo.setSla(obj[13] != null ? (Long)obj[13] : null);
					
					vo.setObsoleteInfo(obj[14] != null ? (String)obj[14] : "");
					
					vo.setEmailDateStr(obj[15] != null ? (String)obj[15] : ""); //no longer used
					
					vo.setEmailDate(obj[16] != null ? (Timestamp)obj[16] : null);
					
					resultList.add(vo);
				}
			}
			
			
		}catch(Exception ex) {
			throw ex;
		}
		
		return resultList;
	}
	
	@Override
	public Boolean isAvailableDate(Date date) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT count(1) ");
		sb.append(" FROM WO_MST_HOLIDAY wmh ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" 	AND wmh.enabled_flag = 'Y' ");
		sb.append(" 	AND :targetDate BETWEEN wmh.HOLIDAY_DATE_FROM AND wmh.HOLIDAY_DATE_TO ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("targetDate", date);

		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		if (result.longValue() == 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Integer getCountDataCounterType(Long id) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT count(1) ");
		sb.append(" FROM wo_mst_counter_type_dtl ");
		sb.append(" WHERE 1 = 1 ");
		sb.append(" AND Counter_Type_ID =:id ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("id", id);

		Number result = (Number) query.getSingleResult();
		
		if (result == null) {
			result = 0;
		}
		
		return result.intValue();
	}

}