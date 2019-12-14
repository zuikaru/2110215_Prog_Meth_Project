package arisu.service.http;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class QueryString implements Map<String, Object> {

	private Map<String, Object> parameters;

	public QueryString() {
		this.parameters = new LinkedHashMap<>();
	}

	public void add(QueryKeyValue query) {
		if (query != null)
			this.put(query.queryKey(), query.queryValue());
	}

	@Override
	public String toString() {
		String result = "";
		int index = 0;
		for (Map.Entry<String, Object> pair : this.entrySet()) {
			result += pair.getKey() + "=" + pair.getValue().toString();
			if (index < this.size() - 1)
				result += "&";
			index++;
		}
		return result;
	}

	public String appendTo(String another) {
		return another + "?" + this.toString();
	}

	@Override
	public int size() {
		return parameters.size();
	}

	@Override
	public boolean isEmpty() {
		return parameters.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return parameters.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return parameters.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return parameters.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return parameters.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return parameters.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		parameters.putAll(m);

	}

	@Override
	public void clear() {
		parameters.clear();
	}

	@Override
	public Set<String> keySet() {
		return parameters.keySet();
	}

	@Override
	public Collection<Object> values() {
		return parameters.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return parameters.entrySet();
	}

}
