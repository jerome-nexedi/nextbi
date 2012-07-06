package com.seekon.bicp.springsupport.web.portlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.osgi.context.support.OsgiBundleXmlApplicationContext;
import org.springframework.web.portlet.DispatcherPortlet;
import org.springframework.web.portlet.context.ConfigurablePortletApplicationContext;

import com.seekon.bicp.springsupport.web.portlet.context.OsgiSupportXmlPortletApplicationContext;

public class OsgiSupportDispatcherPortlet extends DispatcherPortlet {

	private String contextAttribute;

	public String getContextAttribute() {
		return contextAttribute;
	}

	public void setContextAttribute(String contextAttribute) {
		this.contextAttribute = contextAttribute;
	}

	@Override
	protected ApplicationContext createPortletApplicationContext(
			ApplicationContext parent) {
		if (parent == null && contextAttribute != null) {
			parent = (ApplicationContext) getPortletContext().getAttribute(
					contextAttribute);
		}
		this.setContextClass(OsgiSupportXmlPortletApplicationContext.class);

		OsgiSupportXmlPortletApplicationContext pac = new OsgiSupportXmlPortletApplicationContext();

		String osgiConextPath = getPortletContext().getInitParameter("Osgi-Context-Path");
		if(osgiConextPath != null){
			setViewRendererUrl(osgiConextPath + DEFAULT_VIEW_RENDERER_URL);
		}
		// Assign the best possible id value.
		String portletContextName = getPortletContext().getPortletContextName();
		if (portletContextName != null) {
			pac
					.setId(ConfigurablePortletApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
							+ portletContextName + "." + getPortletName());
		} else {
			pac
					.setId(ConfigurablePortletApplicationContext.APPLICATION_CONTEXT_ID_PREFIX
							+ getPortletName());
		}

		if(parent != null && parent instanceof OsgiBundleXmlApplicationContext){
			pac.setBundleContext(((OsgiBundleXmlApplicationContext)parent).getBundleContext());
		}
		
		pac.setParent(parent);
		pac.setPortletContext(getPortletContext());
		pac.setPortletConfig(getPortletConfig());
		pac.setNamespace(getNamespace());
		pac.setConfigLocation(getContextConfigLocation());
		pac.addApplicationListener(new SourceFilteringListener(pac, this));

		postProcessPortletApplicationContext(pac);
		pac.refresh();

		return pac;
	}

}
