/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.litigation.service;

import java.util.List;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.litigation.model.LitigationNew;
import com.wo.module.litigation.model.LitigationPihakKuratorTurutTergugat;
import com.wo.module.litigation.model.LitigationPihakPenggugatPemohon;
import com.wo.module.litigation.model.LitigationPihakTergugatTermohon;
import com.wo.module.litigation.model.LitigationProgressPerkara;
import com.wo.module.litigation.model.LitigationPutusanPengadilan;

public interface LitigationNewService extends RetrieverDataPage<LitigationNew> {

	public void save(LitigationNew entity);

	public void update(LitigationNew entity);

	public void delete(LitigationNew entity);

	public LitigationNew findById(Long id);

	public void deleteRemovedDetails(List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon,
			List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon,
			List<LitigationProgressPerkara> deletedListProgressPerkara,
			List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat,
			List<LitigationPutusanPengadilan> deletedListPutusanPengadilan);
	
	public void udpateLitigation(LitigationNew entity, List<LitigationPihakPenggugatPemohon> deletedListPihakPenggugatPemohon,
			List<LitigationPihakTergugatTermohon> deletedListPihakTergugatTermohon,
			List<LitigationProgressPerkara> deletedListProgressPerkara,
			List<LitigationPihakKuratorTurutTergugat> deletedListPihakKuratorTurutTergugat,
			List<LitigationPutusanPengadilan> deletedListPutusanPengadilan);
	
}
