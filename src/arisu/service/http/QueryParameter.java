package arisu.service.http;

import java.lang.reflect.Constructor;
import java.util.function.Function;

public class QueryParameter<T> implements QueryKeyValue {

	private final String key;
	private T value;
	private Function<T, String> mapper;

	public QueryParameter(String key) {
		this(key, null, null);
	}

	public QueryParameter(String key, T value) {
		this(key, value, null);
	}

	public QueryParameter(String key, T value, Function<T, String> mapper) {
		this.key = key;
		this.value = value;
		this.mapper = mapper;
	}

	public QueryParameter(QueryParameter<T> qp) {
		this.key = qp.key;
		this.value = (T) copyUsingCopyConstructor(qp.value);
		this.mapper = qp.mapper;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean containsValue() {
		return this.value != null;
	}

	public void clear() {
		this.value = null;
	}

	@Override
	public String queryValue() {
		if (mapper != null) {
			return mapper.apply(value);
		} else {
			if (value instanceof QueryKeyValue) {
				return ((QueryKeyValue) value).queryValue();
			} else {
				return value != null ? value.toString() : "";
			}
		}
	}

	@Override
	public String queryKey() {
		return key;
	}

	private static Object copyUsingCopyConstructor(Object o) {
		if (o == null)
			return o;
		try {
			Constructor<? extends Object> c = o.getClass().getConstructor(o.getClass());
			return c.newInstance(o);
		} catch (ReflectiveOperationException ignored) {
		}
		return o;
	}

}
