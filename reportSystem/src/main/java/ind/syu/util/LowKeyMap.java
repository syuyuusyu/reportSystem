package ind.syu.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LowKeyMap<K,V> extends HashMap<String, V>{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public LowKeyMap(Map<String, V> map){
		for(Iterator<Entry<String, V>> it=map.entrySet().iterator();it.hasNext();){
			Entry<String, V> en=it.next();
			this.put( en.getKey().toLowerCase(),en.getValue());
		}
	}

}
