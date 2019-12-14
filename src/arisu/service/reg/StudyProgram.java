package arisu.service.reg;

import arisu.service.http.QueryKeyValue;

public enum StudyProgram implements QueryKeyValue {
	SEMESTER("S"), TRIMESTER("T"), INTERNATIONAL("I");

	private String value;

	StudyProgram(String value) {
		this.value = value;
	}

	@Override
	public String queryValue() {
		return this.value;
	}

	@Override
	public String queryKey() {
		return "studyProgram";
	}
}
