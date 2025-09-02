package com.wo.module.trcComplianceReview.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.trcComplianceReview.model.TrcComplianceReview;
import com.wo.module.trcComplianceReview.vo.TrcComplianceReviewVO;

public interface TrcComplianceReviewDao extends  GenericDAO<TrcComplianceReview, Long>, RetrieverDataPage<TrcComplianceReviewVO> {

}
