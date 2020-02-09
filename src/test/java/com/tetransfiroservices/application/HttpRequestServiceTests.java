package com.tetransfiroservices.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tetransfiro.services.TetransfiroServicesApplication;
import com.tetransfiro.services.application.HttpRequestService;
import com.tetransfiroservices.utils.TestUtils;

@SpringBootTest(classes = TetransfiroServicesApplication.class)
public class HttpRequestServiceTests {

	private static final String ACCEPT = "Accept";
	private static final String CONTENT_TYPE = "Content-type";
	private static final String APPLICATION_JSON = "application/json";
	private static final String API_URL = "https://api.razorpay.com/v1/invoices";

	@MockBean
	private CloseableHttpClient closeableHttpClient;

	@MockBean
	private CloseableHttpResponse closeableHttpResponse;

	@MockBean
	private BasicScheme basicScheme;

	@Autowired
	private HttpRequestService httpRequestService;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${payment.link.service.username}")
	private String username;

	@Value("${payment.link.service.password}")
	private String password;

	@Test
	public void shouldCreateTheExpectedPostRequestWhenValidDataIsReceived() throws Exception {
		var expectedHttpPost = createHttpPostForTest();
		var entity = createJsonEntityForTest();
		var argumentCaptor = ArgumentCaptor.forClass(HttpPost.class);
		when(closeableHttpClient.execute(any(HttpRequestBase.class))).thenReturn(closeableHttpResponse);
		when(closeableHttpResponse.getEntity()).thenReturn(mock(HttpEntity.class));

		httpRequestService.sendAutheticatedPostRequestTo(API_URL, entity, username, password, closeableHttpClient);

		verify(closeableHttpClient).execute(argumentCaptor.capture());
		var capturedParameter = argumentCaptor.getValue();

		assertThat(capturedParameter.getURI()).isEqualTo(expectedHttpPost.getURI());
		assertThat(getEntityAsString(capturedParameter)).isEqualTo(getEntityAsString(expectedHttpPost));
		expectedHttpPost.headerIterator()
		                .forEachRemaining(expectedHeader -> {
		                    assertThat(capturedParameter.containsHeader(expectedHeader.toString()));
		                });
	}

	private HttpPost createHttpPostForTest() throws Exception {
		var httpPost = new HttpPost(API_URL);

		httpPost.setEntity(new StringEntity(createJsonEntityForTest()));
		httpPost.addHeader(new BasicScheme().authenticate(new UsernamePasswordCredentials(username, password),
		                                                  httpPost,
		                                                  null));
		httpPost.addHeader(ACCEPT, APPLICATION_JSON);
		httpPost.addHeader(CONTENT_TYPE, APPLICATION_JSON);

		return httpPost;
	}

	private String createJsonEntityForTest() throws JsonProcessingException {
		return objectMapper.writeValueAsString(TestUtils.createPersonTotalForTest());
	}

	private String getEntityAsString(HttpPost httpPost) throws Exception {
		return EntityUtils.toString(httpPost.getEntity());
	}
}
