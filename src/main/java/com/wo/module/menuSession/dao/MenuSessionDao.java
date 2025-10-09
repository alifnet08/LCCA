/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menuSession.dao;


import java.util.List;

import com.wo.module.menuSession.model.MenuSession;

/**
 *
 * @author hendra
 */
public interface MenuSessionDao {
    
	public List<MenuSession> retrieveAllMenuAllowed(String userLogin);
}
