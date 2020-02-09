package com.tetransfiro.services.application;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestService {

	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-type";
	private static final String APPLICATION_JSON = "application/json";

	private Logger logger = LoggerFactory.getLogger(HttpRequestService.class);

	public String sendAutheticatedPostRequestTo(String url,
	                                            String entity,
	                                            String user,
	                                            String pwd,
	                                            CloseableHttpClient client)
	        throws IOException, AuthenticationException {

		var httpPost = new HttpPost(url);

		httpPost.setEntity(new StringEntity(entity));
		httpPost.addHeader(createAuthHeader(user, pwd, httpPost));
		httpPost.addHeader(ACCEPT, APPLICATION_JSON);
		httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON);

		logger.debug("Executing request {}", httpPost.getRequestLine());
		var ent = execute(httpPost, client);
		return EntityUtils.toString(ent);
	}

	private Header createAuthHeader(String user, String pwd, HttpPost httpPost) throws AuthenticationException {
		return new BasicScheme().authenticate(new UsernamePasswordCredentials(user, pwd), httpPost, null);
	}

	private HttpEntity execute(HttpRequestBase httpObject, CloseableHttpClient client) throws IOException {
		var resp = client.execute(httpObject);
		return resp.getEntity();
	}
}
