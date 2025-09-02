package com.wo.module.mstProvince.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstProvince.model.MstProvince;

public interface MstProvinceService extends RetrieverDataPage<MstProvince> {
	
	public void save(MstProvince entity);
	
	public void update(MstProvince entity);
	
	public void delete(MstProvince entity);
	
	public MstProvince findById(Long id);
	
	public Integer countBranchCodeDuplicate(String branchCode);
	
	public MstProvince getProvinceByProvinceName(String name);
	
}