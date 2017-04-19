package ind.syu.service;

import ind.syu.util.LowKeyListMap;
import ind.syu.util.LowKeyMap;
import ind.syu.util.TableName;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ReportService {
	
	private JdbcTemplate jdbcTemplate;
	

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	


	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public void setNamedParameterJdbcTemplate(
			NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public List<Map<String, Object>> getInput(String sql){
		return jdbcTemplate.queryForList(sql);
	}
	
	
	public Map<String, Object> loadTree(String parentId){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("success", true);
		//List<Map<String, Object>> list=jdbcTemplate.queryForList("select * from report_tree where parentId="+parentId);
		LowKeyListMap list=new LowKeyListMap(jdbcTemplate.queryForList("select * from "+TableName.REPORT_TREE+" where parentId="+parentId+" order by id"));
		for (LowKeyMap<String, Object> lkmap : list) {
			if("true".equals(lkmap.get("leaf")) && !StringUtils.isEmpty((String) lkmap.get("inputs"))){
				//LowKeyListMap inputs=new LowKeyListMap(jdbcTemplate.queryForList("select * from "+TableName.REPORT_INPUT+" where id in("+(String) lkmap.get("inputs")+")"));
				String inputsStr=(String) lkmap.get("inputs");
				String[] strArr=inputsStr.split(",");
				String sql="";
				for (int i = 0; i < strArr.length; i++) {
					if(i==strArr.length-1){
						sql+="select * from "+TableName.REPORT_INPUT+" where id="+strArr[i];
					}else{
						sql+="select * from "+TableName.REPORT_INPUT+" where id="+strArr[i]+" union all ";
					}
				}
				LowKeyListMap inputs=new LowKeyListMap(jdbcTemplate.queryForList(sql));
				for (LowKeyMap<String, Object> input : inputs) {
					input.put("extid", (String)input.get("name")+lkmap.get("id"));
				}
				lkmap.put("inputInfo", inputs);
			}
		}
		map.put("children", list);
		return map;
	}
	


}
