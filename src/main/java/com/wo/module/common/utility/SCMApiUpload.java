package com.wo.module.common.utility;

import com.wo.module.common.model.UploadedFileWO;

public interface SCMApiUpload {
	public String upload() throws Exception;

	public UploadedFileWO getAsUploadedFileWO() throws Exception;
}
