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

package org.jasig.portlet.cms.service;

/**
 * IStringCleaningService is responsible for cleaning HTML content according
 * to any configured validation rules.  Implementations may remove
 * invalid, unsafe, or undesired tags and attributes.
 * 
 * @author Jen Bourey, jbourey@unicon.net
 * @version $Revision: 24329 $
 */
public interface IStringCleaningService {
    
    /**
     * Return a safe HTML string version of the provided content.
     * 
     * @param content
     * @return
     */
    public String getSafeContent(String content);
    
    public String getTextContent(String content);

}
