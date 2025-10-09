/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;

public interface ComplianceTestingService extends RetrieverDataPage<ComplianceTestingVO> {

	public void save(ComplianceTesting entity);

	public void update(ComplianceTesting entity);

	public void delete(ComplianceTesting entity);

	public ComplianceTesting findById(Long id);

	public List<ComplianceTestingVO> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception;
	
}
