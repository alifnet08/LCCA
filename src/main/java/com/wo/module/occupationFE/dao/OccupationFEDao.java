package com.wo.module.occupationFE.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.occupation.model.Occupation;
import com.wo.module.occupationFE.vo.OccupationFEVO;

public interface OccupationFEDao extends GenericDAO<Occupation, Long>, RetrieverDataPage<OccupationFEVO>{

}
