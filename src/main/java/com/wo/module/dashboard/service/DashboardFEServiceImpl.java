/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.dashboard.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.dashboard.dao.DashboardFEDao;
import com.wo.module.dashboard.vo.MapVO;

@Transactional
@Service("dashboardFEService")
public class DashboardFEServiceImpl implements DashboardFEService {
	@Autowired
	@Qualifier("dashboardFEDao")
	private DashboardFEDao dashboardFEDao;

	public DashboardFEDao getDashboardFEDao() {
		return dashboardFEDao;
	}

	public void setDashboardFEDao(DashboardFEDao dashboardFEDao) {
		this.dashboardFEDao = dashboardFEDao;
	}

	public Long getCountUserLogin() throws Exception{
		return dashboardFEDao.getCountUserLogin();
	}
	
	public List<MapVO> getMapData() throws Exception{
		return dashboardFEDao.getMapData();
	}
}
