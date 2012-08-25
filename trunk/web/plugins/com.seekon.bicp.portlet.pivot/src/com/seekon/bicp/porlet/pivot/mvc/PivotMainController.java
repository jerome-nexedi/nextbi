package com.seekon.bicp.porlet.pivot.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping({"VIEW"})
public final class PivotMainController
{
  private Log log;

  public PivotMainController()
  {
    this.log = LogFactory.getLog(PivotMainController.class);
  }

  @RequestMapping
  public ModelAndView doView(RenderRequest request)
  {
    Map model = new HashMap();
    return new ModelAndView("/pivotMain", model);
  }
}