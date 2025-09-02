package com.wo.module.dashboard.service;

import java.util.List;

import com.wo.module.dashboard.vo.MapVO;

public interface DashboardFEService {
	
	public Long getCountUserLogin() throws Exception;
	
	public List<MapVO> getMapData() throws Exception;
	
}
