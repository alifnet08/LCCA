package com.wo.module.mstProvinceLocation.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.mstProvince.model.MstProvinceLocation;


public interface MstProvinceLocationService extends RetrieverDataPage<MstProvinceLocation> {
	
	public void save(MstProvinceLocation entity);
	
	public void update(MstProvinceLocation entity);
	
	public void delete(MstProvinceLocation entity);
	
	public MstProvinceLocation findById(Long id);

	public MstProvinceLocation getProvinceLocationByProvince(String province);
	
}