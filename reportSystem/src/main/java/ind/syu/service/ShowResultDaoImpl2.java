package ind.syu.service;

import ind.syu.dao.ReportConfigsqlDao;
import ind.syu.dao.ShowResultDao;
import ind.syu.entity.ReportConfigsql;
import ind.syu.entity.ReportConfigsqlId;
import ind.syu.entity.ShowResult;
import ind.syu.util.ClobUtil;
import ind.syu.util.LowKeyListMap;
import ind.syu.util.LowKeyMap;
import ind.syu.util.ParamsToList;

import java.math.BigDecimal;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Service;


@Service
public class ShowResultDaoImpl2 {
	private Logger logger = Logger.getLogger(ShowResultDaoImpl.class);
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	ReportConfigsqlDao sqlDao;
	
	@Autowired
	ShowResultDao showdao;
	
	public ResultDto getResultDto(String reportName,String sqlName,String columnIndex){
		
		ReportConfigsql configSql=sqlDao.findOne(new ReportConfigsqlId(reportName, sqlName));
		
		List<ShowResult> showResults=null;
		
		
		boolean isIndex=StringUtils.isEmpty(columnIndex);
		if(!isIndex){
			showResults=showdao.findById(configSql.getShowid());
		}else{
			showResults=showdao.findById(configSql.getShowid(), ParamsToList.getList(columnIndex, Integer.class));
		}
	 
		List<String> seriesHeader=new ArrayList<String>();
		List<String> seriesDataIndex=new ArrayList<String>();
		List<String> dataField =new ArrayList<String>();
		List<String> dataFieldType=new ArrayList<String>();
		List<Column> columns=new ArrayList<Column>();
		ResultDto r=new ResultDto();		
		
		r.setCaption( showResults.get(0).getCaption());
		if(isIndex){
			r.setCateDataIndex(showResults.get(0).getDataindex());
		}
		for (ShowResult showResult : showResults) {
			if("1".equals(showResult.getSeriesheader())){
				seriesHeader.add(showResult.getHeader());
			}
			if("1".equals(showResult.getSeriesdataindex())){
				seriesDataIndex.add(showResult.getDataindex().toLowerCase());
			}
			dataField.add(showResult.getDataindex().toLowerCase());
			dataFieldType.add(showResult.getDatafieldtype());
			if("1".equals(showResult.getGroupdataindex())){
				r.setGroupDataIndex(showResult.getDataindex());
			}
			if(!isIndex){
				if(columnIndex.equals((showResult.getCatedataindex()))){
					r.setCateDataIndex(showResult.getDataindex().toLowerCase());
				}				
			}

//			if(((String)map.get("columnIndex")).equals((String)map.get("cateDataIndex"))){
//				r.setCateDataIndex((String) map.get("dataIndex"));
//			}
			if("1".equals(showResult.getGroupheader())){
				r.setGroupHeader(showResult.getHeader());
			}
			Column c=new Column();
			c.setDataIndex(showResult.getDataindex().toLowerCase());
			c.setHeader(showResult.getHeader());
			c.setHidden(("1".equals(showResult.getHidden())?true:false));
			c.setWidth(showResult.getWidth());
			if("1".equals(showResult.getIsrender())){
				c.setRenderer(ClobUtil.clobToString(showResult.getRenderer()));
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

		ReportConfigsql configSql=sqlDao.findOne(new ReportConfigsqlId(reportName, sqlName));
		return ClobUtil.clobToString(configSql.getSql());
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
