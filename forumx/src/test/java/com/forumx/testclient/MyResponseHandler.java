package com.forumx.testclient;

import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;

class MyResponseHandler implements HttpClientResponseHandler<String> {

	public String handleResponse(final ClassicHttpResponse response) throws IOException {

		final int status = response.getCode();
		String protocol = response.getVersion().format();

		if (status >= HttpStatus.SC_SUCCESS && status < HttpStatus.SC_REDIRECTION) {
			final HttpEntity entity = response.getEntity();
			try {
				StringBuilder sb = new StringBuilder();
				sb.append(protocol + " " + status + "\n");
				Header[] headers = response.getHeaders();
				for (Header header : headers) {
					sb.append(header.getName() + ": " + header.getValue()).append("\n");
				}
				String string = entity != null ? "\n" + EntityUtils.toString(entity) : null;
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				sb.append(string);
				System.out.println(sb);
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

				return string;
			} catch (final ParseException ex) {
				throw new ClientProtocolException(ex);
			}
		} else {
			throw new ClientProtocolException("Unexpected response status: " + status);
		}
	}
}
