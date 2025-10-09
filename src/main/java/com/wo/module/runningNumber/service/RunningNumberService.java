package com.wo.module.runningNumber.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.runningNumber.model.RunningNumber;

public interface RunningNumberService extends RetrieverDataPage<RunningNumber> {

	public RunningNumber getRunningNumber(String runningNumberNo, String runningNumberReset, String runningNumberType)
			throws Exception;

	public Integer getRunningNumberSeq(String runningNumberNo, String runningNumberReset, String runningNumberType,
			String userLogin) throws Exception;
}