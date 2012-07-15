package org.apache.felix.http.base.internal.handler;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RequestDispatcherAdaptor implements RequestDispatcher {

	private RequestDispatcher requestDispatcher;

	public RequestDispatcherAdaptor(RequestDispatcher requestDispatcher) {
		this.requestDispatcher = requestDispatcher;
	}

	public void forward(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		if (req instanceof ServletHandlerRequest)
			req = ((ServletHandlerRequest) req).getRequest();

		requestDispatcher.forward(req, resp);
	}

	public void include(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
		if (req instanceof ServletHandlerRequest)
			req = ((ServletHandlerRequest) req).getRequest();

		requestDispatcher.include(req, resp);
	}
}
