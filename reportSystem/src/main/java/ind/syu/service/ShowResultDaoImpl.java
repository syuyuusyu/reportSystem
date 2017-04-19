package ind.syu.service;

import ind.syu.util.LowKeyListMap;
import ind.syu.util.LowKeyMap;
import ind.syu.util.TableName;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobHandler;


public class ShowResultDaoImpl {



	private Logger logger = Logger.getLogger(ShowResultDaoImpl.class);
	//private JdbcTemplate jdbc;
	private LobHandler lobHandler;
	
	private JdbcTemplate jdbcTemplate;
	
	public ResultDto getResultDto(String reportName,String sqlName,String columnIndex){
		boolean isIndex=StringUtils.isEmpty(columnIndex);
		String indexSql="";
		if(!isIndex){
			indexSql=" and columnIndex in("+columnIndex+")";
		}
		Long id=jdbcTemplate.queryForObject("select showId from "+TableName.REPORT_CONFIGSQL+" where " +
				"reportName='"+reportName+"' and sqlName='"+sqlName+"'", Long.class);
		String sql="select id,caption,header,dataIndex,width,isRender,hidden,dataFieldType,seriesHeader,\n" +
				"seriesDataIndex,cateDataIndex,groupDataIndex,groupHeader,columnIndex\n" +
				" from "+TableName.SHOW_RESULT+" where id="+id+indexSql+" order by columnIndex";   
		List<Map<String, Object>> result=jdbcTemplate.queryForList(sql); 
		List<String> seriesHeader=new ArrayList<String>();
		List<String> seriesDataIndex=new ArrayList<String>();
		List<String> dataField =new ArrayList<String>();
		List<String> dataFieldType=new ArrayList<String>();
		List<Column> columns=new ArrayList<Column>();
		ResultDto r=new ResultDto();
		r.setCaption((String) result.get(0).get("caption"));
		if(isIndex){
			r.setCateDataIndex((String) result.get(0).get("dataIndex"));
		}
		for (Map<String, Object> map : result) {
			if("1".equals(map.get("seriesHeader"))){
				seriesHeader.add((String) map.get("header"));
			}
			if("1".equals(map.get("seriesDataIndex"))){
				seriesDataIndex.add((String) map.get("dataIndex").toString().toLowerCase());
			}
			dataField.add((String) map.get("dataIndex").toString().toLowerCase());
			dataFieldType.add((String) map.get("dataFieldType"));
			if("1".equals(map.get("groupDataIndex"))){
				r.setGroupDataIndex((String) map.get("dataIndex"));
			}
			if(!isIndex){
				if(columnIndex.equals((String)map.get("cateDataIndex"))){
					r.setCateDataIndex((String) map.get("dataIndex").toString().toLowerCase());
				}				
			}

//			if(((String)map.get("columnIndex")).equals((String)map.get("cateDataIndex"))){
//				r.setCateDataIndex((String) map.get("dataIndex"));
//			}
			if("1".equals(map.get("groupHeader"))){
				r.setGroupHeader((String) map.get("header"));
			}
			Column c=new Column();
			c.setDataIndex((String) map.get("dataIndex").toString().toLowerCase());
			c.setHeader((String) map.get("header"));
			c.setHidden(("1".equals(map.get("hidden"))?true:false));
			c.setWidth(bigDecimalToint((BigDecimal) map.get("width")));
			System.out.println(map.get("isRender")+"---");
			if("1".equals(map.get("isRender"))){
				String sql2="select renderer from "+TableName.SHOW_RESULT+" where id="+id+
						" and columnIndex="+bigDecimalToint((BigDecimal) map.get("columnIndex"));
				c.setRenderer(findClobPropertyByTableProNameAndResId(sql2));
			}
			columns.add(c);
		}
		r.setDataField(dataField);
		r.setDataFieldType(dataFieldType);
		r.setSeriesDataIndex(seriesDataIndex);
		r.setSeriesHeader(seriesHeader);
		r.setColumns(columns);
		return r; 
	}
	
	
	@SuppressWarnings("unchecked")
	private String findClobPropertyByTableProNameAndResId(String sql) {

		final StringBuilder clobSB = new StringBuilder();
		try{
			jdbcTemplate.query(sql ,new AbstractLobStreamingResultSetExtractor() {
				@Override
				protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
					String clob = lobHandler.getClobAsString(rs, "renderer");
					clobSB.append(clob);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return clobSB.toString();
	}
	
	public Map<String, Object> getData(Map<String, String> param){
		String reportName= param.get("reportName");
		String sqlName=param.get("sqlName");
		int start=Integer.valueOf( param.get("start"));
		int limit=Integer.valueOf( param.get("limit"));
		String sql=getSql(reportName, sqlName);

		sql=formatSql(sql, param);
		String countSql="select count(1) from("+sql+")";
        sql="select * from\n" +
        				"(select t1.*,rownum rn from ("+sql+") t1 where rownum<="+(start+limit)+")\n" + 
        				"where rn >"+start;
		logger.info(sql);
		
		//NamedParameterJdbcTemplate njdbc=new NamedParameterJdbcTemplate(jdbcTemplate);	
		int total=jdbcTemplate.queryForObject(countSql, Integer.class);
		List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
		List<LowKeyMap<String, Object>> dataLow=new LowKeyListMap(data);
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("data", dataLow);
		result.put("total",total);
		return result;
	}
	
	private String getSql(String reportName,String sqlName ){

		String sql="select sql from "+TableName.REPORT_CONFIGSQL+" where reportName='"+reportName+"' and sqlName='"+sqlName+"'";
		System.out.println(sql);
		return findClobPropertyByTableProNameAndResId(sql,"sql");
	}
	
	private String formatSql(String sql,Map<String, String> param){  
        Matcher m=Pattern.compile("and\\s*\\w+.?\\w+\\s*(?:[=<>]|>=|<=)\\s*(:\\s*(\\w+))\\s*").matcher(sql);
        while(m.find()){
        	if(  null==param.get(m.group(2)) || ( param.get(m.group(2))).matches("\\s*|null|-1")   ){         	
                sql=sql.replace(m.group(), "");
            }else{
            	sql=sql.replace(m.group(1), "'"+param.get(m.group(2))+"'");
            }
        }
		Matcher m2=Pattern.compile("and\\s*\\w+.?\\w+\\s*(?:[=<>]|>=|<=)\\s*to_date\\('(:\\s*(\\w+))'\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*").matcher(sql);
        while(m2.find()){
        	if(null==param.get(m2.group(2)) || ( param.get(m2.group(2))).matches("\\s*|null|-1")  ){         	
                sql=sql.replace(m2.group(), "");
            }else{
            	sql=sql.replace(m2.group(1), param.get(m2.group(2)));
            }
        }
		Matcher m3=Pattern.compile("and\\s*\\w+.?\\w+\\s*between\\s*to_date\\('(:\\s*(\\w+))'\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*and\\s*to_date\\('(:\\s*(\\w+))'\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*").matcher(sql);
        while(m3.find()){
           	if(null==param.get(m3.group(2)) || ( param.get(m3.group(2))).matches("\\s*|null|-1") 
           			|| null==param.get(m3.group(4)) || ( param.get(m3.group(4))).matches("\\s*|null|-1")){         	
                sql=sql.replace(m3.group(), "");
            }else{
            	sql=sql.replace(m3.group(1), param.get(m3.group(2)));
            	sql=sql.replace(m3.group(3), param.get(m3.group(4)));
            }       	
        }
        
        Matcher m4=Pattern.compile("and\\s*\\w+.?\\w+\\s*(?:[=<>]|>=|<=)\\s*to_date\\((:\\s*(\\w+))\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*").matcher(sql);
        while(m4.find()){
        	if(  null==param.get(m4.group(2)) || ( param.get(m4.group(2))).matches("\\s*|null|-1")   ){         	
                sql=sql.replace(m4.group(), "");
            }else{
            	sql=sql.replace(m4.group(1), "'"+param.get(m4.group(2))+"'");
            }
        }
        
		Matcher m5=Pattern.compile("and\\s*\\w+.?\\w+\\s*between\\s*to_date\\((:\\s*(\\w+))\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*and\\s*to_date\\((:\\s*(\\w+))\\s*,\\s*'yyyy-mm-dd (?:HH|hh)24:(?:MI|mi):(?:SS|ss)'\\s*\\)\\s*").matcher(sql);
        while(m5.find()){
           	if(null==param.get(m5.group(2)) || ( param.get(m5.group(2))).matches("\\s*|null|-1") 
           			|| null==param.get(m5.group(4)) || ( param.get(m5.group(4))).matches("\\s*|null|-1")){         	
                sql=sql.replace(m5.group(), "");
            }else{
            	sql=sql.replace(m5.group(1), "'"+param.get(m5.group(2))+"'");
            	sql=sql.replace(m5.group(3), "'"+param.get(m5.group(4))+"'");
            }       	
        }   
        
        Matcher m6=Pattern.compile("and\\s*\\w+.?\\w+\\s*in\\s*\\((:\\s*(\\w+))\\s*\\)").matcher(sql);
        while(m6.find()){
        	if(  null==param.get(m6.group(2)) || ( param.get(m6.group(2))).matches("\\s*|null|-1")   ){         	
                sql=sql.replace(m6.group(), "");
            }else{
            	String[] s=param.get(m6.group(2)).split(",");
            	String str="";
            	for (int i = 0; i < s.length; i++) {
					str+="'"+s[i]+"',";
				}
            	str=str.substring(0, str.length()-1);
            	sql=sql.replace(m6.group(1), str);
            }
        }
        return sql;
	}
	
	@SuppressWarnings("unchecked")
	private String findClobPropertyByTableProNameAndResId(String sql,final String columnName) {

		final StringBuilder clobSB = new StringBuilder();
		try{
			jdbcTemplate.query(sql ,new AbstractLobStreamingResultSetExtractor() {
				@Override
				protected void streamData(ResultSet rs) throws SQLException, IOException, DataAccessException {
					String clob = lobHandler.getClobAsString(rs, columnName);
					clobSB.append(clob);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		return clobSB.toString();
	}
	
	private int bigDecimalToint(BigDecimal i){
		return i.intValue();
	}




	public LobHandler getLobHandler() {
		return lobHandler;
	}


	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}


	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public HSSFWorkbook outputExcel(Map<String, String> param){
		String reportName= param.get("reportName");
		String sqlName=param.get("sqlName");
		String handName=param.get("handName");
		String[] hander=handName.split(",");		
		String sql=getSql(reportName, sqlName);
		sql=formatSql(sql, param);
		List<Map<String, Object>> data=jdbcTemplate.queryForList(sql);
		HSSFWorkbook wb = new HSSFWorkbook();  
		HSSFSheet sheet=null;
		int sheetIndex=0;
		for (int i = 0; i < data.size(); i++) {
			if(i%65535==0){
				sheet=wb.createSheet("sheet-"+sheetIndex);
				HSSFRow rowH=sheet.createRow(0);
				for (int j = 0; j < hander.length; j++) {
					HSSFCell cell=rowH.createCell(j);
					cell.setCellValue(hander[j]);
				}
				sheetIndex++;
			}
			HSSFRow row=sheet.createRow(i%65535+1);
			int cellIndex=0;
			for (Iterator<Entry<String, Object>> it=data.get(i).entrySet().iterator();it.hasNext();) {
				HSSFCell cell=row.createCell(cellIndex);
				Object obj=it.next().getValue();
				if(obj instanceof String)
					cell.setCellValue((String)obj);
				if(obj instanceof BigDecimal)
					cell.setCellValue(((BigDecimal) obj).longValue());
				if(obj instanceof Date){					
					cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj));
				}
				cellIndex++;
			}
		}		
		return wb;
	}
	
}
