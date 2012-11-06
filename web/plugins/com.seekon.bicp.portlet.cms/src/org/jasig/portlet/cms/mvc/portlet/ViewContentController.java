/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portlet.cms.mvc.portlet;

import java.util.Locale;

import javax.portlet.PortletRequest;

import org.jasig.portlet.cms.service.dao.IContentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ViewContentController provides the main view of the portlet.
 * 
 * @author Jen Bourey, jbourey@unicon.net
 * @version $Revision: 24329 $
 */
@Controller
@RequestMapping("VIEW")
public class ViewContentController {
    
    private IContentDao contentDao;
    
    @Autowired
    public void setContentDao(IContentDao contentDao) {
        this.contentDao = contentDao;
    }
    
    /**
     * Display the main user-facing view of the portlet.
     * 
     * @param request
     * @return
     */
    @RequestMapping
    public String viewContent() {
        return "viewContent";
    }
    
    /**
     * Get the configured user-facing content for this portlet configuration.
     * 
     * @param request
     * @return
     */
    @ModelAttribute("content")
    public String getContent(PortletRequest request){
    	Locale locale = request.getLocale();
        return this.contentDao.getContent(request, locale.toString());
    }
    
}
