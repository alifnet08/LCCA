package com.wo.module.email.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.email.vo.EmailVO;

public interface EmailService extends RetrieverDataPage<EmailVO> {

	void updateEmailFollowup(EmailVO email) throws Exception;

	void updateEmailFollowupResend(Long emailFollowId, String emailType, String user) throws Exception;

}
