/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.complianceTestingMockup.dao;

import java.util.List;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.complianceTestingMockup.model.ComplianceTesting;
import com.wo.module.complianceTestingMockup.vo.ComplianceTestingVO;

/**
 *
 * @author hendra
 */
public interface ComplianceTestingDao extends GenericDAO<ComplianceTesting, Long>, RetrieverDataPage<ComplianceTestingVO> {

	List<ComplianceTestingVO> searchDataXls(List<? extends SearchObject> searchCriteria) throws Exception;
}
