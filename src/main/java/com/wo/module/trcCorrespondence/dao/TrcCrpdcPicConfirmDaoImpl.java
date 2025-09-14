/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.trcCorrespondence.dao;

import org.springframework.stereotype.Repository;

import com.wo.module.common.dao.GenericDAOHibernate;
import com.wo.module.trcCorrespondence.model.TrcCrpdcPicConfirm;

@Repository("trcCrpdcPicConfirmDao")
public class TrcCrpdcPicConfirmDaoImpl extends GenericDAOHibernate<TrcCrpdcPicConfirm, Long> implements TrcCrpdcPicConfirmDao {

}