/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.menuSession.dao;


import java.io.Serializable;
import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.common.util.MathUtil;
import com.wo.module.menuSession.model.MenuSession;

/**
 *
 * @author hendra
 */
@SuppressWarnings("deprecation")
@Repository("menuSessionDao")
public class MenuSessionDaoImpl extends GenericDAOHibernate<Object, Serializable> 
    implements MenuSessionDao {
    @SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MenuSessionDaoImpl.class);
    
@SuppressWarnings("rawtypes")
public List<MenuSession> retrieveAllMenuAllowed(String userLogin) {
    	
    	String hqlStr = "select m.menu_id, m.name_in,m.name_en, m.action, m.parent_id, m.description,m.fontawesome,m.menu_level,m.menu_order " + 
    			"from wo_mst_menu m " + 
    			"inner join wo_mst_responsibility_dtl rd on m.menu_id = rd.menu_id " + 
    			"inner join wo_mst_responsibility r on r.responsibility_id = rd.responsibility_id "+
    			"inner join wo_mst_user u on u.responsibility_id = r.responsibility_id " + 
    			"where u.nik = :nik " + 
    			"order by menu_level,menu_order asc ";
    			//"order by menu_level,menu_order ";
        
    	SQLQuery query = getSession().createSQLQuery(hqlStr);
        query.setParameter("nik", userLogin);
        
        //System.out.println("query===" + query);
        List list = query.list();
        
        List<MenuSession> result = new ArrayList<MenuSession>();
        for(int i=0;i<list.size();i++) {
        	Object[] obj = (Object[])list.get(i);
        	MenuSession ms = new MenuSession();
        	//ms.setMenuId(obj[0]!=null?((BigInteger)obj[0]).longValue():null);
        	ms.setMenuId(obj[0]!=null?(MathUtil.returnIdObjectToLong(obj[0])):null);
        	ms.setMenuNameIn(obj[1]!=null?(String)obj[1]:null);
        	ms.setMenuNameEn(obj[2]!=null?(String)obj[2]:null);
        	ms.setMenuAction(obj[3]!=null?(String)obj[3]:null);
        	//ms.setParentId(obj[4]!=null?((BigInteger)obj[4]).longValue():null);
        	ms.setParentId(obj[4]!=null?(MathUtil.returnIdObjectToLong(obj[4])):null);
        	ms.setDescription(obj[5]!=null?(String)obj[5]:null);
        	ms.setFontawesome(obj[6]!=null?(String)obj[6]:null);
        	ms.setMenuLevel(obj[7]!=null?((BigDecimal)obj[7]).intValue():null);
        	ms.setMenuOrder(obj[8]!=null?((BigDecimal)obj[8]).intValue():null);
        	result.add(ms);
        }
        
        return result;
    }
    
}
