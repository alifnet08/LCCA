package com.wo.module.common.utility;

import java.util.List;

public interface SCMApiDelete {
	public void delete(String fileId) throws Exception;
	public void bulkDelete(List<String> fileIds) throws Exception;
}
