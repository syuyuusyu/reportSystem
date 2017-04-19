package ind.syu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ind.syu.service.CfgService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cfg")
public class CfgController {

	
	@Autowired
	CfgService cfgService;
	
	@Autowired(required=true)
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value ="/cfg.op")
	public String toCfg(){
		return "reportConfig";
	}
	
	@RequestMapping(value ="/showInput.op")
	@ResponseBody
	public Map<String, Object> showInput(){
		int total=jdbcTemplate.queryForObject("select count(1) from report_input",Integer.class);
		List<Map<String, Object>> data=jdbcTemplate.queryForList("select * from report_input");
		Map<String, Object> result=new HashMap<String, Object>();
		result.put("data", data);
		result.put("total",total);
		return result;
	}
	
	
	@RequestMapping(value ="/deleteInput.op")
	@ResponseBody
	public Map<String, String> deleteInput(String id){
		Map<String, String> map=new HashMap<String, String>();
		map.put("success", "true");
		jdbcTemplate.execute("delete from report_input where id="+id);
		return map;
	}
	
	@RequestMapping(value ="/addTree.op")
	@ResponseBody
	public Map<String, String> addTree(String parentId,String title){
		Map<String, String> map=new HashMap<String, String>();
		map.put("success", "true");
		cfgService.addTree(parentId, title);
		return map;
	}

	@RequestMapping(value ="/updateInput.op")
	@ResponseBody
	public Map<String, String> updateInput(String ID,String DESCRIPTION,
			String NAME,String TYPE,String LABLE,String MULTISELECT,
			String VALIDATEEXP,String ALLOWBLANK,String DEPENDCOLUMN,
			String DEPENDINPUT,String DISABLEMSG,String DATASQL,String isUpdate){
		Map<String, String> map=new HashMap<String, String>();
		System.out.println(MULTISELECT+"-MULTISELECT");
		System.out.println(ALLOWBLANK+"-ALLOWBLANK");
		map.put("success", "false");
		if("update".equals(isUpdate)){
			String sql="update report_input set DESCRIPTION=?," +
					"NAME=?,TYPE=?,LABLE=?,MULTISELECT=?," +
					"VALIDATEEXP=?,ALLOWBLANK=?,DEPENDCOLUMN=?," +
					"DEPENDINPUT=?,DISABLEMSG=?,DATASQL=? " +
					"where id=?";
			jdbcTemplate.update(sql, DESCRIPTION,NAME,TYPE,LABLE,MULTISELECT,VALIDATEEXP,ALLOWBLANK,DEPENDCOLUMN,DEPENDINPUT,DISABLEMSG,DATASQL,ID);					
			map.put("success", "true");
		}else if("create".equals(isUpdate)){
			String sql="insert into report_input (ID,DESCRIPTION,NAME,TYPE,LABLE,MULTISELECT,VALIDATEEXP,ALLOWBLANK,DEPENDCOLUMN,DEPENDINPUT,DISABLEMSG,DATASQL)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			jdbcTemplate.update(sql, ID,DESCRIPTION,NAME,TYPE,LABLE,MULTISELECT,VALIDATEEXP,ALLOWBLANK,DEPENDCOLUMN,DEPENDINPUT,DISABLEMSG,DATASQL);
			map.put("success", "true");		
		}
		return map;
	}
}
