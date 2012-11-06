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

package org.jasig.portlet.cms.mvc.exception;

/**
 * @author Jen Bourey, jbourey@unicon.net
 * @version $Revision: 22695 $
 */
public class StringCleaningException extends RuntimeException {

    private static final long serialVersionUID = 773741965998995908L;

    public StringCleaningException() {
        super();
    }

    public StringCleaningException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringCleaningException(String message) {
        super(message);
    }

    public StringCleaningException(Throwable cause) {
        super(cause);
    }

}
