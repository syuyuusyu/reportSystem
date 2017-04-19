package ind.syu.service;

import org.springframework.jdbc.core.JdbcTemplate;

public class CfgService {
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void addTree(String parentId,String title){
		int maxId=jdbcTemplate.queryForObject("select count(1) from report_tree",Integer.class);
		maxId++;
		jdbcTemplate.execute("insert into report_tree (id,parentId,text) values("+maxId+","+parentId+",'"+title+"')");
	}
}
