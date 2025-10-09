/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.service;

import java.util.List;

import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wo.module.litigation.dao.LitigationNewDao;
import com.wo.module.litigation.dao.LitigationPihakKuratorTurutTergugatDao;
import com.wo.module.litigation.dao.LitigationPihakPenggugatPemohonDao;
import com.wo.module.litigation.dao.LitigationPihakTergugatTermohonDao;
import com.wo.module.litigation.dao.LitigationProgressPerkaraDao;
import com.wo.module.litigation.dao.LitigationPutusanPengadilanDao;
import com.wo.module.litigation.model.LitigationNew;
import com.wo.module.litigation.model.LitigationPihakKuratorTurutTergugat;
import com.wo.module.litigation.model.LitigationPihakPenggugatPemohon;
import com.wo.module.litigation.model.LitigationPihakTergugatTermohon;
import com.wo.module.litigation.model.LitigationProgressPerkara;
import com.wo.module.litigation.model.LitigationPutusanPengadilan;
import com.wo.module.common.paging.SearchObject;

@Transactional
@Service("litigationNewService")
public class LitigationNewServiceImpl implements LitigationNewService {
	
    @Autowired
    @Qualifier("litigationNewDao")
    private LitigationNewDao litigationNewDao;
    
    @Autowired
    @Qualifier("litigationPihakPenggugatPemohonDao")
    private LitigationPihakPenggugatPemohonDao litigationPihakPenggugatPemohonDao;
    
    @Autowired
    @Qualifier("litigationPihakTergugatTermohonDao")
    private LitigationPihakTergugatTermohonDao litigationPihakTergugatTermohonDao;
    
    @Autowired
    @Qualifier("litigationProgressPerkaraDao")
    private LitigationProgressPerkaraDao litigationProgressPerkaraDao;
    
    @Autowired
    @Qualifier("litigationPutusanPengadilanDao")
    private LitigationPutusanPengadilanDao litigationPutusanPengadilanDao;
    
    @Autowired
    @Qualifier("litigationPihakKuratorTurutTergugatDao")
    private LitigationPihakKuratorTurutTergugatDao litigationPihakKuratorTurutTergugatDao;

	@SuppressWarnings("rawtypes")
	@Override
    @Transactional(readOnly=true)
	public List<LitigationNew> searchData(List<? extends SearchObject> searchCriteria, int first, int pageSize, String sortField, SortOrder sortOrder)
			throws Exception {
		return litigationNewDao.searchData(searchCriteria, first, pageSize, sortField, sortOrder);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    @Transactional(readOnly=true)
	public Long searchCountData(List searchCriteria) throws Exception {
		return litigationNewDao.searchCountData(searchCriteria);
	}
	
	public void save(LitigationNew entity) {
		litigationNewDao.save(entity);
	}
	
	public void update(LitigationNew entity) {
		litigationNewDao.update(entity);
	}
	
	public void delete(LitigationNew entity) {
		litigationNewDao.delete(entity);
	}
  
    public LitigationNew findById(Long id) {
    	return litigationNewDao.getById(id);
    }

	public LitigationNewDao getLitigationNewDao() {
		return litigationNewDao;
	}

	public void setLitigationNewDao(LitigationNewDao litigationNewDao) {
		this.litigationNewDao = litigationNewDao;
	}

	@Override
	public void deleteRemovedDetails(List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon,
			List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon,
			List<LitigationProgressPerkara> deletedListProgressPerkara,
			List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat,
			List<LitigationPutusanPengadilan> deletedListPutusanPengadilan) {
		
		if(deletedListPihakPenggugatPemohon.size() > 0) {
			for(LitigationPihakPenggugatPemohon dtlPihakP : deletedListPihakPenggugatPemohon) {
				if(dtlPihakP != null && dtlPihakP.getLitiPihakPenggugatPemohonId() != null && dtlPihakP.getLitiPihakPenggugatPemohonId() > 0) {
					litigationPihakPenggugatPemohonDao.delete(dtlPihakP);
				}
			}
		}
		
		if(deletedListPihakTergugatTermohon.size() > 0) {
			for(LitigationPihakTergugatTermohon dtlPihakT : deletedListPihakTergugatTermohon) {
				if(dtlPihakT != null && dtlPihakT.getLitiPihakTergugatTermohonId() != null && dtlPihakT.getLitiPihakTergugatTermohonId() > 0) {
					litigationPihakTergugatTermohonDao.delete(dtlPihakT);
				}
			}
		}
		
		if(deletedListProgressPerkara.size() > 0) {
			for(LitigationProgressPerkara dtlProgress : deletedListProgressPerkara) {
				if(dtlProgress != null && dtlProgress.getLitiProgressPerkaraId() != null && dtlProgress.getLitiProgressPerkaraId() > 0) {
					litigationProgressPerkaraDao.delete(dtlProgress);
				}
			}
		}
		
		if(deletedListPihakKuratorTurutTergugat.size() > 0) {
			for(LitigationPihakKuratorTurutTergugat dtlKurator : deletedListPihakKuratorTurutTergugat) {
				if(dtlKurator != null && dtlKurator.getLitiPihakKuratorTurutTergugatId() != null && dtlKurator.getLitiPihakKuratorTurutTergugatId() > 0) {
					litigationPihakKuratorTurutTergugatDao.delete(dtlKurator);
				}
			}
		}
		
		if(deletedListPutusanPengadilan.size() > 0) {
			for(LitigationPutusanPengadilan dtlPutusan : deletedListPutusanPengadilan) {
				if(dtlPutusan != null && dtlPutusan.getLitiPutusanPengadilanId() != null && dtlPutusan.getLitiPutusanPengadilanId() > 0) {
					litigationPutusanPengadilanDao.delete(dtlPutusan);
				}
			}
		}
		
		
	}

	@Override
	public void udpateLitigation(LitigationNew entity,
			List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon,
			List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon,
			List<LitigationProgressPerkara> deletedListProgressPerkara,
			List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat,
			List<LitigationPutusanPengadilan> deletedListPutusanPengadilan) {
		
		litigationNewDao.update(entity);
		
		if(deletedListPihakPenggugatPemohon.size() > 0) {
			for(LitigationPihakPenggugatPemohon dtlPihakP : deletedListPihakPenggugatPemohon) {
				if(dtlPihakP != null && dtlPihakP.getLitiPihakPenggugatPemohonId() != null && dtlPihakP.getLitiPihakPenggugatPemohonId() > 0) {
					litigationPihakPenggugatPemohonDao.delete(dtlPihakP);
				}
			}
		}
		
		if(deletedListPihakTergugatTermohon.size() > 0) {
			for(LitigationPihakTergugatTermohon dtlPihakT : deletedListPihakTergugatTermohon) {
				if(dtlPihakT != null && dtlPihakT.getLitiPihakTergugatTermohonId() != null && dtlPihakT.getLitiPihakTergugatTermohonId() > 0) {
					litigationPihakTergugatTermohonDao.delete(dtlPihakT);
				}
			}
		}
		
		if(deletedListProgressPerkara.size() > 0) {
			for(LitigationProgressPerkara dtlProgress : deletedListProgressPerkara) {
				if(dtlProgress != null && dtlProgress.getLitiProgressPerkaraId() != null && dtlProgress.getLitiProgressPerkaraId() > 0) {
					litigationProgressPerkaraDao.delete(dtlProgress);
				}
			}
		}
		
		if(deletedListPihakKuratorTurutTergugat.size() > 0) {
			for(LitigationPihakKuratorTurutTergugat dtlKurator : deletedListPihakKuratorTurutTergugat) {
				if(dtlKurator != null && dtlKurator.getLitiPihakKuratorTurutTergugatId() != null && dtlKurator.getLitiPihakKuratorTurutTergugatId() > 0) {
					litigationPihakKuratorTurutTergugatDao.delete(dtlKurator);
				}
			}
		}
		
		if(deletedListPutusanPengadilan.size() > 0) {
			for(LitigationPutusanPengadilan dtlPutusan : deletedListPutusanPengadilan) {
				if(dtlPutusan != null && dtlPutusan.getLitiPutusanPengadilanId() != null && dtlPutusan.getLitiPutusanPengadilanId() > 0) {
					litigationPutusanPengadilanDao.delete(dtlPutusan);
				}
			}
		}		
		
	}   
}
