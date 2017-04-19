/**
 * 
 */
/**
 * @author syu
 *
 */
package test;

import ind.syu.dao.ReportInputDao;
import ind.syu.util.LowKeyListMap;
import ind.syu.util.ParamsToList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


public class Test{
	ApplicationContext ac=new ClassPathXmlApplicationContext("persistent-common.xml");
	@org.junit.Test
	public void test1(){
		NamedParameterJdbcTemplate nj=(NamedParameterJdbcTemplate) ac.getBean("namedParameterJdbcTemplate");
		System.out.println(nj);
		String sql="select * from test_report where 1=1 and area_id=:area_id and sub_area_id in(:sub_area_id)";
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("area_id", 10002);
		map.put("sub_area_id", "10031,10033");
		//nj.queryForList(sql, map);
	}
	//@org.junit.Test
	public void test2(){
		JdbcTemplate dj=(JdbcTemplate) ac.getBean("jdbcTemplate");
		List<Map<String, Object>> list=dj.queryForList("select * from report_tree");
		LowKeyListMap list2=new LowKeyListMap(list);
		for (int i = 0; i < list2.size(); i++) {
			String s=(String) list2.get(0).get("text");
			System.out.println(s);
		}
	}
	
	@org.junit.Test
	public void test3(){
		String sql="and cif.area_name in(:areaName)";
        Matcher m6=Pattern.compile("and\\s*\\w+.?\\w+\\s*in\\s*\\((:\\s*(\\w+))\\s*\\)").matcher(sql);
        while(m6.find()){
        	System.out.println("sdsdsd");
        	System.out.println(m6.group(1));
            
        }
	}
	
	@org.junit.Test
	public void test4(){
		ExpressionParser parser = new SpelExpressionParser();
		
		String[] symbols=new String[]{"+","-","*","/"};
		float[] arr=new float[]{1,2,3,4,5,6,7,8,9,10,11};
		Random rand=new Random();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < 100000; j++) {
				String elStr="10"+symbols[rand.nextInt(4)]+"10"+symbols[rand.nextInt(4)]+"10"+symbols[rand.nextInt(4)]+"10";
				Expression exp = parser.parseExpression( elStr);
				try {
					float a=(float) exp.getValue();
					if(a==arr[i]){
						System.out.println(elStr+"="+a);
						break;
					}
				} catch (Exception e) {
					System.out.println(exp.getValue()+"-----");
				}
				

			}
		
		}
	}
	
	@org.junit.Test
	public void test5(){
		String s="1,2,3,4";
		List<Integer> list=ParamsToList.getList(s, Integer.class);
		for (Integer integer : list) {
			System.out.println(integer);
		}
	}
	

	public void test6(){
		ReportInputDao dao=ac.getBean("reportInputDao", ReportInputDao.class);
		dao.somefind("sdsd");
		System.out.println(dao);
	}
}