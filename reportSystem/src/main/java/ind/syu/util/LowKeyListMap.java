package ind.syu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LowKeyListMap extends ArrayList<LowKeyMap<String, Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LowKeyListMap(List<Map<String, Object>> list){
		for (int i = 0; i < list.size(); i++) {
			this.add(new LowKeyMap<String, Object>(list.get(i)));
		}
	}

}
