package com.wo.module.externalRegulation.model;

import java.io.Serializable;

import com.wo.module.common.model.BaseEntity;
import com.wo.module.user.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegulationProposerUnit extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 9099707931289644347L;

	private Long regulationProposerUnitId;
	
	private Regulation regulation;
	private User pic;
	private User puk;
	
	private String directorate;
	private String publisherUnit;
	private String picNameTemp;
	private String pukNameTemp;
	
	private Integer sequence;
	
}
