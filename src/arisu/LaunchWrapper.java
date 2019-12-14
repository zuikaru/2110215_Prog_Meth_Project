package arisu;

import java.lang.reflect.Method;

public class LaunchWrapper {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		Class appClass = Class.forName(args.length > 0 ? args[0] : "arisu.Arisu");
		Method mainMethod = appClass.getDeclaredMethod("main", String[].class);
		mainMethod.invoke(null, new Object[] {args});
	}
}
