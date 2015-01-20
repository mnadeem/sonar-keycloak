/*
 * Sonar Keycloak Plugin
 * Copyright (C) 2012 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.keycloak;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.security.UserDetails;
import org.sonar.api.web.ServletFilter;

/**
 * Validate tokens forwarded by the OpenID provider after the request initiated by {@link KeycloakAuthenticationFilter}.
 * If authenfication is successful, then object of type UserDetails is added to request attributes.
 */
public final class KeycloakValidationFilter extends ServletFilter {

  static final String USER_ATTRIBUTE = "openid_user";

  public KeycloakValidationFilter() {

  }

  @Override
  public UrlPattern doGetPattern() {
    return UrlPattern.create("/openid/validate");
  }

  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    StringBuffer receivingURL = httpRequest.getRequestURL();
    String queryString = httpRequest.getQueryString();
    if (StringUtils.isNotEmpty(queryString)) {
      receivingURL.append("?").append(httpRequest.getQueryString());
    }

    UserDetails user = null;
    if (user != null) {
      request.setAttribute(USER_ATTRIBUTE, user);
    }

    filterChain.doFilter(request, response);
  }

  public void destroy() {
  }
}