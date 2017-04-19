package ind.syu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class ParamsToList {
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> getList(String parm,Class<T> requiredType){
		if(StringUtils.isEmpty(parm)) return null;
		List<T> list=new ArrayList<>();
		String[] ss=parm.split(",");
		for (String string : ss) {
			if("java.lang.Integer".equals(requiredType.getName())){
				list.add((T) Integer.valueOf(string));
			}
			if("java.lang.String".equals(requiredType.getName())){
				list.add((T) string);
			}
			if("java.lang.Long".equals(requiredType.getName())){
				list.add((T) Long.valueOf(string));
			}
		}
		return list;
	}
	

}
