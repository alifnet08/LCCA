package com.wo.module.dashboard.dao;

import java.util.List;

import com.wo.module.dashboard.vo.MapVO;

public interface DashboardFEDao {

	public Long getCountUserLogin() throws Exception;
	
	public List<MapVO> getMapData() throws Exception;
}
