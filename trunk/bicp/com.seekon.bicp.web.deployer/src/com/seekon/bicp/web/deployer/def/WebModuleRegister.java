package com.seekon.bicp.web.deployer.def;

import java.util.Dictionary;

import javax.servlet.Filter;
import javax.servlet.ServletException;

import org.dom4j.Document;
import org.osgi.service.http.NamespaceException;

import com.seekon.bicp.register.Register;
import com.seekon.bicp.register.RegisterException;

public interface WebModuleRegister extends Register {

  public void registerDefault() throws RegisterException;

  public void registerFilters(Document doc) throws RegisterException;

  public void registerServlets(Document doc) throws RegisterException;

  public void registerFilter(String filterPath, String servletName, Filter servletFilter,
    Dictionary dictionary) throws ServletException, NamespaceException;
}
