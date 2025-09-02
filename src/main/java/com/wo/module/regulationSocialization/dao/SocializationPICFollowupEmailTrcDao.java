package com.wo.module.regulationSocialization.dao;

import com.wo.module.common.dao.GenericDAO;
import com.wo.module.engine.vo.SendEmailVO;
import com.wo.module.regulationSocialization.model.SocializationPICFollowupEmailTrc;

public interface SocializationPICFollowupEmailTrcDao extends  GenericDAO<SocializationPICFollowupEmailTrc, Long>{

	SendEmailVO getEmailPicByFollowupEmailId(Long emailFollowId);

	
}
