package arisu.service.reg;

import arisu.service.http.QueryKeyValue;

public enum Semester implements QueryKeyValue {
	FIRST(1), SECOND(2), THIRD(3);

	final int value;

	Semester(int value) {
		this.value = value;
	}

	@Override
	public String queryValue() {
		return String.valueOf(value);
	}

	@Override
	public String queryKey() {
		return "semester";
	}
}
