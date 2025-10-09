/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.tmpAuditApproval.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.tmpAudit.model.TmpAudit;
import com.wo.module.tmpAuditApproval.vo.TmpAuditApprovalVO;

public interface TmpAuditApprovalDao extends  GenericDAO<TmpAudit, Long>, RetrieverDataPage<TmpAuditApprovalVO>{
	
	
}
