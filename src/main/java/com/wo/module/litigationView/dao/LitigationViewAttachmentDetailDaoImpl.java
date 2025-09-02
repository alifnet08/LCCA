package com.wo.module.litigationView.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;
import com.wo.module.litigationView.vo.LitigationViewAttachmentVo;

@Repository("litigationViewAttachmentDetailDao")
public class LitigationViewAttachmentDetailDaoImpl extends GenericDAOHibernate<Object, Long>
	implements LitigationViewAttachmentDetailDao, Serializable{

	private static final long serialVersionUID = -9199961140379362012L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<LitigationViewAttachmentVo> getLitigationAttachmentList(Long litigationId, Long attachId,
			String attachCode) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" SELECT la.LITIGATION_ATTACHMENT_ID ATTACH_ID ");
		sb.append("       ,la.ATTACHMENT_CODE ATTACH_CODE ");
		sb.append("       ,la.ATTACHMENT_FILE ATTACH_FILE ");
		sb.append("       ,la.FILE_ID FILE_ID ");
		sb.append("       ,la.FILE_SIZE FILE_SIZE ");
		sb.append(" FROM WO_MST_LITIGATION_ATTACHMENT la ");
		sb.append(" WHERE 1 = 1 ");
		sb.append("     AND la.ENABLED_FLAG <> 'N' ");
		
		if (attachId != null && attachId > 0) {
			sb.append("     AND la.LITIGATION_ATTACHMENT_ID = :attachId ");			
		}
		if (litigationId != null && litigationId > 0) {
			sb.append("     AND la.LITIGATION_ID = :litigationId ");			
		}
		if (attachCode != null && !attachCode.equals("")) {
			sb.append("     AND la.ATTACHMENT_CODE = :attachCode ");
		}
		
		Query query = getSession().createSQLQuery(sb.toString());
		if (attachId != null && attachId > 0) {
			query.setParameter("attachId", attachId);
		}
		if (litigationId != null && litigationId > 0) {
			query.setParameter("litigationId", litigationId);
		}
		if (attachCode != null && !attachCode.equals("")) {
			query.setParameter("attachCode", attachCode);
		}
		
		List result = query.getResultList();
		List<LitigationViewAttachmentVo> vo = new ArrayList<LitigationViewAttachmentVo>();
		
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				Object[] obj = (Object[]) result.get(i);
				LitigationViewAttachmentVo data = new LitigationViewAttachmentVo();
				
				data.setAttachmentId(obj[0] != null ? MathUtil.returnIdObjectToLong(obj[0]) : null);
				data.setAttachmentCode(obj[1] != null ? (String) obj[1] : null);
				data.setAttachmentFile(obj[2] != null ? (String) obj[2] : null);
				data.setFileId(obj[3] != null ? (String) obj[3] : null);
				data.setFileSize(obj[4] != null ? MathUtil.returnIdObjectToLong(obj[4]): null);
				
				vo.add(data);
			}
		}
		
		return vo;
	}

}
