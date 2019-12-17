package arisu.service.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import arisu.exception.HttpConnectionException;

public abstract class HttpAPI {
	static final String COOKIES_HEADER = "Set-Cookie";
	protected CookieManager cookieManager;
	protected String baseURL;
	protected String encoding;

	public HttpAPI(String baseURL) {
		this.baseURL = baseURL;
		this.cookieManager = new CookieManager();
	}

	public void init() throws HttpConnectionException {
		this.initSession();
	}

	public void refresh() throws HttpConnectionException {
		this.refreshSession();
	}

	public abstract void initSession() throws HttpConnectionException;

	public void refreshSession() throws HttpConnectionException {
		this.initSession();
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getEncoding() {
		return this.encoding;
	}

	protected void injectCookie(HttpURLConnection connection) {
		if (cookieManager.getCookieStore().getCookies().size() > 0) {
			connection.setRequestProperty("Cookie", String.join(";", cookieManager.getCookieStore().getCookies()
					.stream().map(HttpCookie::toString).collect(Collectors.toList())));
		}
	}

	public String sendGet(String endpoint) throws HttpConnectionException {
		HttpsURLConnection con = null;
		String url = endpoint;
		try {
			con = (HttpsURLConnection) this.createConnection(url);
			con.setRequestMethod("GET");
			con.connect();
		} catch (IOException e) {
			throw new HttpConnectionException("Can't connect to" + url + "!", con, e);
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream(), encoding));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			return content.toString();
		} catch (UnsupportedEncodingException e) {
			throw new HttpConnectionException(encoding + " encoding is not supported!", con, e);
		} catch (IOException e) {
			throw new HttpConnectionException("Problem while reading response!", con, e);
		}
	}

	public String sendGet(String endpoint, String queryString) throws HttpConnectionException {
		return this.sendGet(endpoint + "?" + queryString);
	}

	public String sendGet(String endpoint, AbstractQuery query) throws HttpConnectionException {
		return this.sendGet(endpoint, query.toString());
	}

	protected String getFullEndpointURL(String endpoint) {
		String str = baseURL;
		if (str.endsWith("/"))
			str = str.substring(0, str.length() - 1);
		if (endpoint.startsWith("/"))
			endpoint = endpoint.substring(1, endpoint.length());
		return str + "/" + endpoint;
	}

	protected HttpURLConnection createConnection(String endpoint) throws MalformedURLException, IOException {
		URL url = new URL(this.getFullEndpointURL(endpoint));
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		this.injectCookie(con);
		return con;
	}

	protected void initCookieFromConnection(HttpURLConnection connection) {
		Map<String, List<String>> headerFields = connection.getHeaderFields();
		List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

		if (cookiesHeader != null) {
			for (String cookie : cookiesHeader) {
				this.cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
			}
		}
	}
}
