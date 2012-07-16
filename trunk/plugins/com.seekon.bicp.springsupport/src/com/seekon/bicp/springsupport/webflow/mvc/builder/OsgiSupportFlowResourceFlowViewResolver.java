package com.seekon.bicp.springsupport.webflow.mvc.builder;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.mvc.builder.FlowResourceFlowViewResolver;

public class OsgiSupportFlowResourceFlowViewResolver extends FlowResourceFlowViewResolver{
	
	private static final boolean JSTL_PRESENT = ClassUtils.isPresent("javax.servlet.jsp.jstl.fmt.LocalizationContext");

	@Override
	public View resolveView(String viewId, RequestContext context) {
		if (viewId.startsWith("/")) {
			return getViewInternal(viewId, context, context.getActiveFlow().getApplicationContext());
		} else {
			ApplicationContext flowContext = context.getActiveFlow().getApplicationContext();
			if (flowContext == null) {
				throw new IllegalStateException("A Flow ApplicationContext is required to resolve Flow View Resources");
			}
			Resource viewResource = flowContext.getResource(viewId);
			if(viewResource instanceof UrlResource){
				String viewPath = null;
				try {
					viewPath = ((UrlResource)viewResource).getURL().getFile();
				} catch (IOException e) {
					throw new IllegalStateException(e);
				}
				return this.getViewInternal(viewPath, context, flowContext);
			}
			
			if (!(viewResource instanceof ContextResource)) {
				throw new IllegalStateException(
						"A ContextResource is required to get relative view paths within this context");
			}
			return getViewInternal(((ContextResource) viewResource).getPathWithinContext(), context, flowContext);
		}
	}
	
	private View getViewInternal(String viewPath, RequestContext context, ApplicationContext flowContext) {
		if (viewPath.endsWith(".jsp") || viewPath.endsWith(".jspx")) {
			if (JSTL_PRESENT) {
				JstlView view = new JstlView(viewPath);
				view.setApplicationContext(flowContext);
				return view;
			} else {
				InternalResourceView view = new InternalResourceView(viewPath);
				view.setApplicationContext(flowContext);
				return view;
			}
		} else {
			throw new IllegalArgumentException("Unsupported view type " + viewPath
					+ " only types supported by this FlowViewResolver implementation are [.jsp] and [.jspx]");
		}
	}
}
