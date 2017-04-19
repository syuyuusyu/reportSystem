package ind.syu.interceptor;


import ind.syu.util.TableName;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthorityInterceptor implements HandlerInterceptor {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println("查询拦截器");
		List<Map<String, Object>> authoritis=jdbcTemplate.queryForList("select * from "+TableName.TRPORT_AUTHORITY_CONFIG+" where type='0'");
		if(authoritis.size()==0){
			return true;
		}
		String authorityAttrubute="";
		for (Map<String, Object> map : authoritis) {
			String keyWord=(String) map.get("key_word");
			String keyWordValue=(String) request.getSession().getAttribute(keyWord);
			if(StringUtils.isEmpty(keyWordValue)){
				//session超时
				return false;
			}
			List<Map<String, Object>> authoritisCfg=jdbcTemplate.queryForList("select * from "+TableName.TRPORT_AUTHORITY_CONFIG+" " +
					"where type='1' and key_word='"+keyWord+"'");
			for (Map<String, Object> map2 : authoritisCfg) {
				String param=(String) map2.get("param_word");
				authorityAttrubute+=param+",";
				String value=jdbcTemplate.queryForObject((String)map2.get("sql"), String.class,new Object[]{keyWordValue});
				request.setAttribute(param, value);
			}
		}
		authorityAttrubute=authorityAttrubute.substring(0,authorityAttrubute.length()-1);
		request.setAttribute("authorityParam", authorityAttrubute);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
