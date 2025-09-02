package com.wo.module.common.util;

import java.sql.Timestamp;
import java.util.Date;

import com.wo.module.common.constant.Constants;
import com.wo.module.common.model.BaseEntity;

public class EntityUtil {
	
	public static<E extends BaseEntity> void setCreationInfo(E e, String createdBy){
		e.setCreatedBy(createdBy);
		e.setCreationDate(new Timestamp(new Date().getTime()));
		e.setDelId(new Long(0));
		e.setEnabledFlag(Constants.CONSTANT_YES);
	}
	
	public static<E extends BaseEntity> void setUpdateInfo(E e, String updateBy){
		e.setLastUpdateBy(updateBy);
		e.setLastUpdateDate(new Timestamp(new Date().getTime()));
	}
	
	public static<E extends BaseEntity> void setUpdateToDeleteFlag(E e, String updateBy){
		e.setLastUpdateBy(updateBy);
		e.setLastUpdateDate(new Timestamp(new Date().getTime()));
		e.setEnabledFlag(Constants.CONSTANT_NO);
	}
}
