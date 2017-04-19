package ind.syu.service;

import java.util.List;
import java.util.Map;

public class ResultDto {
    private String caption;//报表标题
    private List<String> seriesHeader; //线形图等展示的数据的标题  例如："年生产值" 、" 月收入"等
    private List<String> seriesDataIndex; //对应得线形图等展示的数据的列  例如："cz" 、"Monthly Income"等.并且dataFieldType必须为"int"或者"float"
    private String cateDataIndex;//分类标志  例如对比所有员工的月收入 这个就是  员工姓名  "name"
    
    private List<String> dataField;//传入的数据的DataIndex 
    private List<String> dataFieldType;//传入的数据的DataIndex的类型  例如："int" , "float",其余的都是"" 。必须和DataIndex唯一对应
    private List<Column> columns; 

    private Map<String, Object> data; //查询到的实际数据。并且顺序必须dataField的顺序一致

    private String groupDataIndex; //对数据进行分类图形展示的dataIndex。(非必要) 例如：对不同的信号进行分类展示。这里就应该是“signalId”
    private String groupHeader;//对数据进行分类图形展示的Header
    private String reportId;
    private String reportName;
    private String sqlName;
    
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getSqlName() {
		return sqlName;
	}

	public void setSqlName(String sqlName) {
		this.sqlName = sqlName;
	}

	public ResultDto() {
		super();
	}

	public ResultDto(String caption, String cateDataIndex, List<Column> columns,
			Map<String, Object> data, List<String> dataField, List<String> dataFieldType,
			List<String> seriesDataIndex, List<String> seriesHeader) {
		super();
		this.caption = caption;
		this.cateDataIndex = cateDataIndex;
		this.columns = columns;
		this.data = data;
		this.dataField = dataField;
		this.dataFieldType = dataFieldType;
		this.seriesDataIndex = seriesDataIndex;
		this.seriesHeader = seriesHeader;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<String> getSeriesHeader() {
		return seriesHeader;
	}

	public void setSeriesHeader(List<String> seriesHeader) {
		this.seriesHeader = seriesHeader;
	}

	public List<String> getSeriesDataIndex() {
		return seriesDataIndex;
	}

	public void setSeriesDataIndex(List<String> seriesDataIndex) {
		this.seriesDataIndex = seriesDataIndex;
	}

	public String getCateDataIndex() {
		return cateDataIndex;
	}

	public void setCateDataIndex(String cateDataIndex) {
		this.cateDataIndex = cateDataIndex;
	}

	public List<String> getDataField() {
		return dataField;
	}

	public void setDataField(List<String> dataField) {
		this.dataField = dataField;
	}

	public List<String> getDataFieldType() {
		return dataFieldType;
	}

	public void setDataFieldType(List<String> dataFieldType) {
		this.dataFieldType = dataFieldType;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

 

	public String getGroupDataIndex() {
		return groupDataIndex;
	}

	public void setGroupDataIndex(String groupDataIndex) {
		this.groupDataIndex = groupDataIndex;
	}

	public String getGroupHeader() {
		return groupHeader;
	}

	public void setGroupHeader(String groupHeader) {
		this.groupHeader = groupHeader;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	
}
