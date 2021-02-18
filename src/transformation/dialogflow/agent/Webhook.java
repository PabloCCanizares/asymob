package transformation.dialogflow.agent;

import java.util.HashMap;
import java.util.Map;

import generator.GeneratorFactory;
import generator.HTTPRequest;
import generator.Method;

public class Webhook {

	private String url;
	private String username;
	private Map<String, String> headers;

	public Webhook() {
	}

	public String getUrl() {
		if (url == null) {
			url="https://<example>";
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, String> getHeaders() {
		if (headers == null) {
			headers = new HashMap<>();
		}
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "Webhook [url=" + url + ", username=" + username + ", headers=" + headers + "]";
	}

	public HTTPRequest getRequestAction() {
		HTTPRequest ret = GeneratorFactory.eINSTANCE.createHTTPRequest();
		ret.setName("HttpRequest");
		ret.setURL(getUrl());
		if (getUsername()!= null)
			ret.setBasicAuth(getUsername(), "");
		for (String key : getHeaders().keySet()) {
			String value = getHeaders().get(key);
			ret.setHeader(key, value);
		}
		ret.setMethod(Method.POST);
		return ret;
	}

	public static HTTPRequest getDefaultRequest() {
		HTTPRequest ret = GeneratorFactory.eINSTANCE.createHTTPRequest();
		ret.setName("HttpRequest");
		ret.setURL("");
		ret.setMethod(Method.POST);
		return ret;
	}

}
