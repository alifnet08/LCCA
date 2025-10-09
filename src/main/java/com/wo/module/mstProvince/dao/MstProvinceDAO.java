package com.wo.module.mstProvince.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstProvince.model.MstProvince;

public interface MstProvinceDAO extends GenericDAO<MstProvince, Long>, RetrieverDataPage<MstProvince> {
	
	public Integer countBranchCodeDuplicate(String branchCode);
	public MstProvince getProvinceByProvinceName(String name);

}