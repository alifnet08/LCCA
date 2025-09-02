package com.wo.module.common.utility;

public interface SCMApi {
	public String upload() throws Exception;
	public void download() throws Exception;
	public void delete() throws Exception;
	public String sendEmail() throws Exception;
}
