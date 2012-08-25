package com.seekon.bicp.porlet.spreadsheet.mvc;

import java.util.HashMap;
import java.util.Map;
import javax.portlet.RenderRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping({"VIEW"})
public final class ProxyCasController
{
  private Log log;
  //private TicketValidator validator;
  //private String serviceUrl;
  //private String proxyTicketKey;

  public ProxyCasController()
  {
    this.log = LogFactory.getLog(ProxyCasController.class);

    //this.serviceUrl = "http://localhost:8090/spreadsheet";

    //this.proxyTicketKey = "casProxyTicket";
  }

//  @Autowired(required=true)
//  public void setTicketValidator(TicketValidator validator)
//  {
//    this.validator = validator;
//  }
//
//  public void setServiceUrl(String serviceUrl)
//  {
//    this.serviceUrl = serviceUrl;
//  }

  @RequestMapping
  public ModelAndView validateProxyCas(RenderRequest request)
  {
    Map model = new HashMap();

//    Map userInfo = (Map)request.getAttribute("javax.portlet.userinfo");
//    String proxyTicket = (String)userInfo.get(this.proxyTicketKey);
//    if (proxyTicket == null) {
//      model.put("success", Boolean.valueOf(false));
//      model.put("message", "No proxy ticket in UserInfo map");
//      return new ModelAndView("/spreadsheetList", model);
//    }
//
//    try
//    {
//      Assertion assertion = this.validator.validate(proxyTicket, this.serviceUrl);
//      model.put("success", Boolean.valueOf(true));
//    } catch (TicketValidationException e) {
//      this.log.error("Exception attempting to validate proxy ticket", e);
//      model.put("success", Boolean.valueOf(false));
//      model.put("message", "Unable to validate proxy ticket");
//    }

    return new ModelAndView("/spreadsheetList", model);
  }
}