package com.wo.module.report.reportLogAccess.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportLogAccessVo implements Serializable {

	private static final long serialVersionUID = -3865971440345527631L;
	
	private Long logAccessId;
	private Long accessId;
	
	private String userName;
	private String sourceIp;
	private String accessAction;
	private String divisionName;
	
	private Timestamp accessTime;
	
}
