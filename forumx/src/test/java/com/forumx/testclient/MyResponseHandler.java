package com.forumx.testclient;

import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

class MyResponseHandler implements HttpClientResponseHandler<String> {

	public String handleResponse(final ClassicHttpResponse response) throws IOException {

		final int status = response.getCode();

		if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
			final HttpEntity entity = response.getEntity();
			try {
				String string = entity != null ? "\n" + EntityUtils.toString(entity) : null;

				return string;
			} catch (final ParseException ex) {
				throw new ClientProtocolException(ex);
			}
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}
}
