package com.wo.module.report.reportGen.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;

public class ReportGenDetailTableModel extends LazyDataModel<ReportGen> {
	
	private static final long serialVersionUID = -8743331468757096934L;
	static Logger logger = Logger.getLogger(ReportGenDetailTableModel.class);
	private RetrieverDataPage<ReportGen> retrieverData;
	@SuppressWarnings("rawtypes")
	private List<? extends SearchObject> searchCriteria;

	public ReportGenDetailTableModel(RetrieverDataPage<ReportGen> retrieverData, int pageSize) {
		this.retrieverData = retrieverData;
		setPageSize(pageSize);
		updateRowCount();
	}

	@SuppressWarnings("rawtypes")
	public void setSearchCriteria(List<? extends SearchObject> searchCriteria) {
		this.searchCriteria = searchCriteria;
		updateRowCount();
	}

	@SuppressWarnings("rawtypes")
	public List<? extends SearchObject> getSearchCriteria() {
		return searchCriteria;
	}

	@Override
	public Object getRowKey(ReportGen reportGen) {
		return reportGen.getReportGenId();
	}
	
	@Override
	public ReportGen getRowData(String rowKey) {
		List<ReportGen> list = (List<ReportGen>) getWrappedData();

		for (ReportGen ejb : list) {
			if (ejb.getReportGenId() == Long.parseLong(rowKey)) {
				return ejb;
			}
		}
		return null;
	}

	public void updateRowCount() {
		if (searchCriteria == null)
			setRowCount(0);
		else {
			try {

				Long totalRowCount = retrieverData.searchCountData(searchCriteria);

				setRowCount(totalRowCount.intValue());
			} catch (Exception ex) {
				logger.debug("Exception while searching row count, use 0 as result", ex);
				setRowCount(0);
			}
		}

	}
	
	@Override
	public List<ReportGen> load(int first, int pageSize, String sortField,
			SortOrder sortOrder, Map<String, Object> filters) {
		List<ReportGen> results = new ArrayList<ReportGen>();
		try {
			if (StringUtils.isNotEmpty(sortField)) {
				if (sortOrder.name().equalsIgnoreCase("ASCENDING"))
					results = retrieverData.searchData(searchCriteria, first,
							pageSize, sortField, SortOrder.ASCENDING);
				else
					results = retrieverData.searchData(searchCriteria, first,
							pageSize, sortField, SortOrder.DESCENDING);
			} else {
				results = retrieverData.searchData(searchCriteria, first,
						pageSize, sortField, SortOrder.ASCENDING);
			}

			if (results != null && results.size() == 0 && first >= 10) {
				return this.load(0, pageSize, sortField, sortOrder, filters);
			}

			// filter - filter has already been on Hibernate level so the page
			// level filter is only
			// for retrieved data filter
			if (filters != null && !filters.isEmpty()) {
				for (ReportGen obj : results) {
					boolean match = true;

					for (Iterator<String> it = filters.keySet().iterator(); it
							.hasNext();) {
						try {
							String filterProperty = it.next();
							String filterValue = (String)filters.get(filterProperty);
							String fieldValue = String.valueOf(obj.getClass()
									.getField(filterProperty).get(obj));

							if (filterValue == null
									|| fieldValue.startsWith(filterValue)) {
								match = true;
							} else {
								match = false;
								break;
							}
						} catch (Exception e) {
							match = false;
						}
					}

					if (!match) {
						// remove from list if not found
						results.remove(obj);
					}
				}
			}

			// sort
			// sort has been done in the query, so this sort is redundant
			if (sortField != null && results != null && sortOrder != null) {
				// Collections.sort(results, new LazySorter<E>(sortField,
				// sortOrder));
			}

			setWrappedData(results);
			updateRowCount();

			if (results != null) {
				// rowCount
				int dataSize = results.size();

				// paginate
				if (dataSize > pageSize) {
					try {
						return results.subList(first, first + pageSize);
					} catch (IndexOutOfBoundsException e) {
						return results.subList(first, first
								+ (dataSize % pageSize));
					}
				} else {
					return results;
				}
			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug(
					"Exception while trying search param detail, returning empty list",
					e);
			return new ArrayList<ReportGen>();
		}
	}

	public RetrieverDataPage<ReportGen> getRetrieverData() {
		return retrieverData;
	}

	public void setRetrieverData(RetrieverDataPage<ReportGen> retrieverData) {
		this.retrieverData = retrieverData;
	}
}
