package com.wo.module.regulatoryReportingFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.regulatoryReportingFE.vo.RegulatoryReportingFEVo;
import com.wo.module.trcRmd.model.TrcRmd;

public interface RegulatoryReportingFEDao extends GenericDAO<TrcRmd, Long>, RetrieverDataPage<RegulatoryReportingFEVo>{

}
