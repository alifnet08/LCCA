/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menuSession.service;

import java.util.List;

import com.wo.module.menuSession.model.MenuSession;

/**
 *
 * @author hendra
 */
public interface MenuSessionService {
   
	public List <MenuSession> retrieveAllMenuAllowed(String userLogin);

	List<MenuSession> getListMenuAllowed(String userLogin);

   
}
