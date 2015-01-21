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
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.web.ServletFilter;
/**
 * 
 * @author Mohammad Nadeem
 *
 */
public final class KeycloakLogoutFilter extends ServletFilter {

	static final Logger LOG = LoggerFactory.getLogger(KeycloakLogoutFilter.class);

	private final KeycloakClient keycloakClient;

	public KeycloakLogoutFilter(KeycloakClient keycloakClient) {
		this.keycloakClient = keycloakClient;
	}

	@Override
	public UrlPattern doGetPattern() {
		return UrlPattern.create(KeycloakClient.SONAR_LOG_OUT_URL);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		this.keycloakClient.logOut(httpRequest);
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		if (session != null) {
			session.invalidate();
		}
		filterChain.doFilter(request, response);		
	}

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}
}