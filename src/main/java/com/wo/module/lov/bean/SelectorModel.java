/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wo.module.lov.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.SortOrder;

import com.wo.module.common.paging.DBLazyDataModel;
import com.wo.module.common.paging.DefaultSearchObject;
import com.wo.module.common.paging.RetrieverDataPage;
import com.wo.module.common.paging.SearchObject;
import com.wo.module.lov.model.ColumnModel;
import com.wo.module.lov.model.CountLimit;



/**
 *
 * @author hendra
 */
public class SelectorModel {
    public static final String SEARCH_COL_QUERY_ALL = "ALL";
    public static final String SEARCH_COL_QUERY_ALL_COUNT = "ALL_COUNT";
    
    private CountLimit multipleCountLimit;
    private String jsOnSelected;
    private String jsFunctionOnSelected;
    private String userQueryString;
    private String paramString;
    private SelectorInfo selectorInfo;
    private String clientId;
    List<ColumnModel> columnModels;
    private List<String> selectSearchType;
    
    private String typeString;
    
    @SuppressWarnings("rawtypes")
	private DBLazyDataModel dataModel;

    public SelectorModel(            
            @SuppressWarnings("rawtypes") RetrieverDataPage retriever,
            SelectorInfo selectorInfo, 
            String clientId,
            int pageSize) {
        this(retriever, selectorInfo, clientId, pageSize, new CountLimit());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public SelectorModel(            
            RetrieverDataPage retriever,
            SelectorInfo selectorInfo, 
            String clientId,
            int pageSize,
            CountLimit countLimit) {
        this.multipleCountLimit = countLimit;
        this.selectorInfo = selectorInfo;
        this.clientId = clientId;
        this.dataModel = new DBLazyDataModel(retriever, 
                Arrays.asList(
                new DefaultSearchObject(SEARCH_COL_QUERY_ALL, selectorInfo.getQueryFilledWithUserQuery("","")),
                new DefaultSearchObject(SEARCH_COL_QUERY_ALL_COUNT, selectorInfo.getQueryCountFilledWithUserQuery("",""))                
                ),
                pageSize);
        initColumnModels();
    }
    
    private void initColumnModels() {
        List<ColumnModel> colModels = new ArrayList<ColumnModel>();
        
        List<String> colProps = selectorInfo.getColumnProp();
        List<String> colHeaders = selectorInfo.getColumns();
        for (int i = 0 ; i < colProps.size() ; i++) {
            if (colHeaders.size() >= i) {
                colModels.add(new ColumnModel(
                    colHeaders.get(i), colProps.get(i)));
            } else {
                colModels.add(new ColumnModel(
                    "Column_" + i, colProps.get(i)));
            }
        }
        
        columnModels = colModels;

        if(selectorInfo.getSearchSelectItems() != null && !selectorInfo.getSearchSelectItems().isEmpty())
        	selectSearchType = selectorInfo.getSearchSelectItems();
    }
    
    @SuppressWarnings("unchecked")
	public void search(String userQuery,String param) {
        this.userQueryString = userQuery;
        this.paramString = param;
        dataModel.setSearchCriteria(
                Arrays.asList(
                buildQueryDataSearchObject(userQuery,param),
                buildQueryCountSearchObject(userQuery,param)));
    }
    
    @SuppressWarnings("unchecked")
	public void search(String userQuery,String param, String type) {
        this.userQueryString = userQuery;
        this.paramString = param;
        this.typeString = type;
        dataModel.setSearchCriteria(
                Arrays.asList(
                buildQueryDataSearchObject(userQuery,param,type),
                buildQueryCountSearchObject(userQuery,param,type)));
    }
    
    @SuppressWarnings("rawtypes")
	public SearchObject buildQueryCountSearchObject(String userQuery,String param) {
        return new DefaultSearchObject(
                    SEARCH_COL_QUERY_ALL_COUNT, 
                    selectorInfo.getQueryCountFilledWithUserQuery(userQuery,param)
                );
    }
    
    @SuppressWarnings("rawtypes")
   	public SearchObject buildQueryCountSearchObject(String userQuery,String param, String type) {
           return new DefaultSearchObject(
                       SEARCH_COL_QUERY_ALL_COUNT, 
                       selectorInfo.getQueryCountFilledWithUserQuery(userQuery,param, type)
                   );
       }
    
    @SuppressWarnings("rawtypes")
	public SearchObject buildQueryDataSearchObject(String userQuery,String param) {
        return new DefaultSearchObject(
                    SEARCH_COL_QUERY_ALL, 
                    selectorInfo.getQueryFilledWithUserQuery(userQuery,param)
                );
    }

    @SuppressWarnings("rawtypes")
   	public SearchObject buildQueryDataSearchObject(String userQuery,String param, String type) {
           return new DefaultSearchObject(
                       SEARCH_COL_QUERY_ALL, 
                       selectorInfo.getQueryFilledWithUserQuery(userQuery,param,type)
                   );
       }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List retrieveAllData() throws Exception {
        RetrieverDataPage retriever = dataModel.getRetrieverData();
        return retriever.searchData(Arrays.asList(
                buildQueryDataSearchObject(userQueryString,paramString),
                buildQueryCountSearchObject(userQueryString,paramString)), 
                0, -1, null, SortOrder.UNSORTED);
    }
    
    public List<ColumnModel> getColumnModels() {
        return columnModels;
    }
    
    

    @SuppressWarnings("rawtypes")
	public DBLazyDataModel getDataModel() {
        return dataModel;
    }

    public CountLimit getMultipleCountLimit() {
        return multipleCountLimit;
    }

    
    public String getUserQueryString() {
        return userQueryString;
    }

    public String getJsOnSelected() {
        return jsOnSelected;
    }

    public void setJsOnSelected(String jsOnSelected) {
        this.jsOnSelected = jsOnSelected;
    }

    public String getJsFunctionOnSelected() {
        return jsFunctionOnSelected;
    }

    public void setJsFunctionOnSelected(String jsFunctionOnSelected) {
        this.jsFunctionOnSelected = jsFunctionOnSelected;
    }
    
    public String constructJsFunctionParamAware(Object item) {
        if (StringUtils.isNotBlank(jsFunctionOnSelected)) {
            return selectorInfo.constructJsFunctionParamAware(jsFunctionOnSelected, item);
        } else {
            return "";
        }
    }
    
    public static class SelectorInfo implements Serializable{
    	
        private static final long serialVersionUID = 579014089385130395L;
		private String dbQuery;
        private String dbQueryCount;
        private List <String> columns;
        private List <String> columnProp;
        private Boolean isHql;
        private List<String> searchSelectItems;
        @SuppressWarnings("rawtypes")
		private SelectedItemConverter converter;

        public SelectorInfo(
                String dbQuery, 
                String dbQueryCount,
                List<String> columns, List<String> columnProp) {
            this(dbQuery, dbQueryCount, columns, columnProp, true);
        }
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public SelectorInfo(
                String dbQuery, 
                String dbQueryCount,
                List<String> columns, List<String> columnProp,
                Boolean isHql) {
            this(dbQuery, dbQueryCount, columns, columnProp, isHql, new DefaultSelectedItemConverter(columnProp));
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		public SelectorInfo(
                String dbQuery, 
                String dbQueryCount,
                List<String> columns, List<String> columnProp,
                Boolean isHql,
                List<String> searchSelectItems) {
            this(dbQuery, dbQueryCount, columns, columnProp, isHql, new DefaultSelectedItemConverter(columnProp), searchSelectItems);
        }

        public SelectorInfo(
                String dbQuery, 
                String dbQueryCount,
                List<String> columns, List<String> columnProp,
                Boolean isHql, 
                @SuppressWarnings("rawtypes") SelectedItemConverter converter) {
            this.dbQuery = dbQuery;
            this.dbQueryCount = dbQueryCount;
            this.columns = columns;
            this.columnProp = columnProp;
            this.isHql = isHql;
            this.converter = converter;
        }
        
        public SelectorInfo(
                String dbQuery, 
                String dbQueryCount,
                List<String> columns, 
                List<String> columnProp,
                Boolean isHql, 
                @SuppressWarnings("rawtypes") SelectedItemConverter converter,
                List<String> searchSelectItems
        		) {
            this.dbQuery = dbQuery;
            this.dbQueryCount = dbQueryCount;
            this.columns = columns;
            this.columnProp = columnProp;
            this.isHql = isHql;
            this.converter = converter;
            this.setSearchSelectItems(searchSelectItems);
        }

        public List<String> getColumns() {
            return columns;
        }

        public List<String> getColumnProp() {
            return columnProp;
        }

        public void setDbQuery(String dbQuery) {
            this.dbQuery = dbQuery;
        }

        public void setDbQueryCount(String dbQueryCount) {
            this.dbQueryCount = dbQueryCount;
        }

        
        public String getDbQuery() {
            return dbQuery;
        }

        public String getDbQueryCount() {
            return dbQueryCount;
        }

        public Boolean getIsHql() {
            return isHql;
        }
        
        @SuppressWarnings("unchecked")
		String constructJsFunctionParamAware(String jsFunctionOnSelected, Object item) {
            StringBuilder sb = new StringBuilder();
            sb.append(jsFunctionOnSelected)
                    .append("(")
                    .append(converter.convertToJsParam(item))
                    .append(");");
            return sb.toString();            
        }
        
        public String getQueryFilledWithUserQuery(String userQuery,String param) {
            return dbQuery.replace("{0}", userQuery).replace("{1}", StringUtils.isEmpty(param)?"0":param);
        }
        public String getQueryCountFilledWithUserQuery(String userQuery,String param) {
            return dbQueryCount.replace("{0}", userQuery).replace("{1}", StringUtils.isEmpty(param)?"0":param);
        }
        
        public String getQueryFilledWithUserQuery(String userQuery,String param, String regulationType) {
            return dbQuery.replace("{0}", userQuery).replace("{1}", StringUtils.isEmpty(param)?"0":param).replace("{2}", regulationType);
        }
        
        public String getQueryCountFilledWithUserQuery(String userQuery,String param, String regulationType) {
            return dbQueryCount.replace("{0}", userQuery).replace("{1}", StringUtils.isEmpty(param)?"0":param).replace("{2}", regulationType);
        }

		public List<String> getSearchSelectItems() {
			return searchSelectItems;
		}

		public void setSearchSelectItems(List<String> searchSelectItems) {
			this.searchSelectItems = searchSelectItems;
		}
    }

	public SelectorInfo getSelectorInfo() {
		return selectorInfo;
	}

	public void setSelectorInfo(SelectorInfo selectorInfo) {
		this.selectorInfo = selectorInfo;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setMultipleCountLimit(CountLimit multipleCountLimit) {
		this.multipleCountLimit = multipleCountLimit;
	}

	public void setUserQueryString(String userQueryString) {
		this.userQueryString = userQueryString;
	}

	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}

	public void setDataModel(@SuppressWarnings("rawtypes") DBLazyDataModel dataModel) {
		this.dataModel = dataModel;
	}

	public String getParamString() {
		return paramString;
	}

	public void setParamString(String paramString) {
		this.paramString = paramString;
	}

	public List<String> getSelectSearchType() {
		return selectSearchType;
	}

	public void setSelectSearchType(List<String> selectSearchType) {
		this.selectSearchType = selectSearchType;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}

	
    
    
}
