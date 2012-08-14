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

package org.jasig.portal.security.xslt;

import java.util.Locale;

import org.jasig.portal.i18n.LocaleManager;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Service;

@Service
public class XalanMessageHelperBean implements IXalanMessageHelper,
  MessageSourceAware {
  private MessageSource messageSource;

  @Override
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String getMessage(String code, String language) {
    final Locale locale = LocaleManager.parseLocale(language);
    final String message = messageSource.getMessage(code, null, locale);
    return message;
  }

  @Override
  public String getMessage(String code, String language, String arg1) {
    final Locale locale = LocaleManager.parseLocale(language);
    return messageSource.getMessage(code, new Object[] { arg1 }, locale);
  }

  @Override
  public String getMessage(String code, String language, String arg1, String arg2) {
    final Locale locale = LocaleManager.parseLocale(language);
    return messageSource.getMessage(code, new Object[] { arg1, arg2 }, locale);
  }

  @Override
  public String getMessage(String code, String language, String arg1, String arg2,
    String arg3) {
    final Locale locale = LocaleManager.parseLocale(language);
    return messageSource.getMessage(code, new Object[] { arg1, arg2, arg3 }, locale);
  }
}
