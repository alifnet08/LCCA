package com.wo.module.trcCorrespondence.service;

import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirm;

public interface TrcCrpdcPicConfirmService {
	
	public TrcCrpdcPicConfirm findById(Long id);
	
	public void update(TrcCrpdcPicConfirm entity);
	
}