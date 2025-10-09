package com.wo.module.aboutUsFE.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.aboutUs.model.AboutUs;
import com.wo.module.aboutUsFE.vo.AboutUsFEVo;
import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;

@Repository("aboutUsFEDao")
public class AboutUsFEDaoImpl extends GenericDAOHibernate<AboutUs, Long>
	implements AboutUsFEDao, Serializable{

	private static final long serialVersionUID = -4477727054526818223L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<AboutUsFEVo> getAllAboutUsFEVoData() {
		StringBuilder sb = new StringBuilder();
		
//		sb.append(" SELECT DISTINCT au.ABOUT_US_ID ");
//		sb.append("       ,au.OUR_NAME ");
//		sb.append("       ,au.OUR_JOB ");
//		sb.append("       ,au.EXT ");
//		sb.append("       ,au.PHOTO_FILE ");
//		sb.append("       ,au.PUK_ID ");
//		sb.append("       ,au.EMAIL ");
//		sb.append("       ,au.FILE_ID ");
//		sb.append("       ,au.FILE_SIZE ");
//		sb.append(" FROM WO_MST_ABOUT_US au where au.enabled_flag = 'Y' ");
//		sb.append(" START WITH au.PUK_ID IS NULL ");
//		sb.append(" CONNECT BY PRIOR au.ABOUT_US_ID = au.PUK_ID ORDER BY (case when puk_id is null then 0 else puk_id end) ASC,au.ABOUT_US_ID asc  ");
		
		sb.append(" SELECT au.ABOUT_US_ID ");
		sb.append("       ,au.OUR_NAME ");
		sb.append("       ,au.OUR_JOB ");
		sb.append("       ,au.EXT ");
		sb.append("       ,au.PHOTO_FILE ");
		sb.append("       ,au.PUK_ID ");
		sb.append("       ,au.EMAIL ");
		sb.append("       ,au.FILE_ID ");
		sb.append("       ,au.FILE_SIZE ");
		sb.append(" FROM WO_MST_ABOUT_US au ");
		sb.append(" where au.enabled_flag = 'Y' ");
		sb.append(" 	AND PUK_ID is null ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		
		List result = query.getResultList();
		List<AboutUsFEVo> vo = new ArrayList<AboutUsFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				AboutUsFEVo data = new AboutUsFEVo();
				
				data.setAboutUsId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setOurName(obj[1] != null ? (String) obj[1] : null);
				data.setOurJob(obj[2] != null ? (String) obj[2] : null);
				data.setExt(obj[3] != null ? (String) obj[3] : null);
				data.setPhotoFile(obj[4] != null ? (String) obj[4] : null);
				data.setPukId(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				data.setEmail(obj[6] != null ? (String) obj[6] : null);
				data.setFileId(obj[7] != null ? (String) obj[7] : null);
				data.setFileSize(obj[8] != null ? MathUtil.returnIdObjectToLong(obj[8]) : null);

				data.setChild(getAboutChild(data.getAboutUsId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}
	
	@SuppressWarnings("rawtypes")
	private List<AboutUsFEVo> getAboutChild(Long pukId) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT au.ABOUT_US_ID ");
		sb.append("       ,au.OUR_NAME ");
		sb.append("       ,au.OUR_JOB ");
		sb.append("       ,au.EXT ");
		sb.append("       ,au.PHOTO_FILE ");
		sb.append("       ,au.PUK_ID ");
		sb.append("       ,au.EMAIL ");
		sb.append("       ,au.FILE_ID ");
		sb.append("       ,au.FILE_SIZE ");
		sb.append(" FROM WO_MST_ABOUT_US au ");
		sb.append(" where au.enabled_flag = 'Y' ");
		sb.append(" 	AND PUK_ID = :pukId ");
		
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("pukId", pukId);
		
		List result = query.getResultList();
		List<AboutUsFEVo> vo = new ArrayList<AboutUsFEVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				AboutUsFEVo data = new AboutUsFEVo();
				
				data.setAboutUsId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setOurName(obj[1] != null ? (String) obj[1] : null);
				data.setOurJob(obj[2] != null ? (String) obj[2] : null);
				data.setExt(obj[3] != null ? (String) obj[3] : null);
				data.setPhotoFile(obj[4] != null ? (String) obj[4] : null);
				data.setPukId(obj[5] != null ? MathUtil.returnIdObjectToLong(obj[5]) : null);
				data.setEmail(obj[6] != null ? (String) obj[6] : null);
				data.setFileId(obj[7] != null ? (String) obj[7] : null);
				data.setFileSize(obj[8] != null ? MathUtil.returnIdObjectToLong(obj[8]) : null);

				data.setChild(getAboutChild(data.getAboutUsId()));
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
