package com.wo.module.runningNumber.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.runningNumber.model.RunningNumber;

public interface RunningNumberDao extends GenericDAO<RunningNumber, Long>, RetrieverDataPage<RunningNumber> {

	public RunningNumber getRunningNumber(String runningNumberNo, String runningNumberReset, String runningNumberType)
			throws Exception;

	public Integer getRunningNumberSeq(String runNumberNo, String runNumberReset) throws Exception;

}
