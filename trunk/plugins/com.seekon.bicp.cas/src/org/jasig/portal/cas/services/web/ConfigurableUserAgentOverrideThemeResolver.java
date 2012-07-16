package org.jasig.portal.cas.services.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.theme.AbstractThemeResolver;

public class ConfigurableUserAgentOverrideThemeResolver extends AbstractThemeResolver
{
  private ServicesManager servicesManager;
  private List<ArgumentExtractor> argumentExtractors;
  private Map<String, Map<Pattern, String>> overrides;

  public String resolveThemeName(HttpServletRequest request)
  {
    String themeName = resolveServiceThemeName(request);
    String userAgent;
    if (this.overrides.containsKey(themeName))
    {
      userAgent = request.getHeader("User-Agent");

      for (Map.Entry entry : this.overrides.get(themeName).entrySet()) {
        if (((Pattern)entry.getKey()).matcher(userAgent).matches()) {
          return (String)entry.getValue();
        }

      }

    }

    return themeName;
  }

  public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName)
  {
  }

  protected String resolveServiceThemeName(HttpServletRequest request)
  {
    if (this.servicesManager == null) {
      return getDefaultThemeName();
    }

    Service service = WebUtils.getService(this.argumentExtractors, request);

    RegisteredService rService = this.servicesManager.findServiceBy(service);

    return ((service != null) && (rService != null) && (StringUtils.hasText(rService.getTheme()))) ? rService.getTheme() : getDefaultThemeName();
  }

  public void setServicesManager(ServicesManager servicesManager)
  {
    this.servicesManager = servicesManager;
  }

  public void setArgumentExtractors(List<ArgumentExtractor> argumentExtractors)
  {
    this.argumentExtractors = argumentExtractors;
  }

  public void setOverrides(Map<String, Map<Pattern, String>> overrides)
  {
    this.overrides = new HashMap<String, Map<Pattern, String>>();

    for (Map.Entry<String, Map<Pattern, String>> themeMapping : overrides.entrySet()) {
      Map mappings = new LinkedHashMap();
      for (Map.Entry browserMapping : themeMapping.getValue().entrySet()) {
        Pattern p = Pattern.compile((String)browserMapping.getKey());
        mappings.put(p, browserMapping.getValue());
      }
      this.overrides.put(themeMapping.getKey(), mappings);
    }
  }
}