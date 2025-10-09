/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.aboutUs.service;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.aboutUs.model.AboutUs;

public interface AboutUsService extends RetrieverDataPage<AboutUs> {

	public void save(AboutUs entity);

	public void update(AboutUs entity);

	public void delete(AboutUs entity);

	public AboutUs findById(Long id);
	
}
