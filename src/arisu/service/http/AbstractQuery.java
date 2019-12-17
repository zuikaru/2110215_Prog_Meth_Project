package arisu.service.http;

import java.lang.reflect.Field;
import java.util.logging.Level;

import arisu.Arisu;

public abstract class AbstractQuery {
	@Override
	public String toString() {
		QueryString qs = new QueryString();
		addParameters(qs);
		addParametersUsingReflection(qs);
		return qs.toString();
	}

	public void addParameters(QueryString qs) {

	}

	protected void addParametersUsingReflection(QueryString qs) {
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			if (QueryKeyValue.class.isAssignableFrom(f.getType())) {
				QueryKeyValue q;
				try {
					q = (QueryKeyValue) f.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Arisu.logger().log(Level.SEVERE, "Failed to get value of field " + f.getName() + " from "
							+ this.getClass().getSimpleName(), e);
					continue;
				}
				qs.add(q);
			}
		}
	}
}
