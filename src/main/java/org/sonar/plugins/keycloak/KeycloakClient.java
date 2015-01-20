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

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.adapters.ServerRequest;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.IDToken;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.util.KeycloakUriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;
import org.sonar.api.security.UserDetails;

public class KeycloakClient implements ServerExtension {
	
	public static final String REFERER_ATTRIBUTE = KeycloakClient.class.getName() + ".referer";
	private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakClient.class);
	
	private static final String KEYCLOAK_SERVER_URL = "sonar.keycloak.auth.serverlUrl";
	private static final String KEYCLOAK_REALM = "sonar.keycloak.realm";
	private static final String KEYCLOAK_REALM_PUBLIC_KEY = "sonar.keycloak.realm.publicKey";
	private static final String KEYCLOAK_RESOURCE = "sonar.keycloak.resource";
	private static final String KEYCLOAK_SSL_REQUIRED = "sonar.keycloak.sslRequired";
	private static final String KEYCLOAK_SECRET = "sonar.keycloak.secret";

	public static final String KEYCLOAK_USER_ATTRIBUTE = "keycloak_user";
	
	public static final String SONAR_VALIDATE_URL = "/keycloak/validate";
	public static final String SONAR_UN_AUTHORIZED_URL = "/keycloak/unauthorized";

	private Settings sonarSettings;

	private KeycloakDeployment keycloakDeployment;

	public KeycloakClient(final Settings settings) {
		this.sonarSettings = settings;
		init();
	}

	private void init() {
		this.keycloakDeployment = KeycloakDeploymentBuilder.build(newAdapterConfig());
	}

	private AdapterConfig newAdapterConfig() {
		AdapterConfig adapterConfig =  new AdapterConfig();
		adapterConfig.setRealm(getRealm());
		adapterConfig.setRealmKey(getRealmPublicKey());
		adapterConfig.setResource(getResource());
		adapterConfig.setSslRequired(getSslRequired());
		adapterConfig.setClientKeyPassword(getClientSecret());
		adapterConfig.setAuthServerUrl(getAuthServerUrl());

		return adapterConfig;
	}
	
	private String getRealm() {
		return sonarSettings.getString(KEYCLOAK_REALM);
	}
	
	private String getRealmPublicKey() {
		return sonarSettings.getString(KEYCLOAK_REALM_PUBLIC_KEY);
	}
	
	private String getAuthServerUrl() {
		return sonarSettings.getString(KEYCLOAK_SERVER_URL);
	}
	
	private String getResource() {
		return sonarSettings.getString(KEYCLOAK_RESOURCE);
	}
	
	private String getClientSecret() {
		return sonarSettings.getString(KEYCLOAK_SECRET);
	}
	
	private String getSslRequired() {
		return sonarSettings.getString(KEYCLOAK_SSL_REQUIRED);
	}
	public String getSonarUrl() {
		return sonarSettings.getString(KEYCLOAK_SSL_REQUIRED);
	}

	public String getAuthUrl(HttpServletRequest servletRequest) {
		String redirect = redirectUrl(servletRequest);
		String state = UUID.randomUUID().toString();
		String authUrl = keycloakDeployment.getAuthUrl().clone()
				.queryParam(OAuth2Constants.CLIENT_ID, keycloakDeployment.getResourceName())
				.queryParam(OAuth2Constants.REDIRECT_URI, redirect)
				.queryParam(OAuth2Constants.STATE, state)
				.build().toString();
		return authUrl;
	}

	private String redirectUrl(HttpServletRequest request) {
		KeycloakUriBuilder builder = KeycloakUriBuilder.fromUri(request.getRequestURL().toString())
				.replacePath(request.getContextPath())
				.replaceQuery(null)
				.path(SONAR_VALIDATE_URL);
		String redirect = builder.toTemplate();
		return redirect;
	}

	public UserDetails getUser(HttpServletRequest request) throws Exception {
		String redirect = redirectUrl(request);
		UserDetails userDetails = null;
		
		LOGGER.info("request.getParameter : " +request.getParameter("code"));
		LOGGER.info("redirect : " +redirect);
		AccessTokenResponse tokenResponse = ServerRequest.invokeAccessCodeToToken(keycloakDeployment, request.getParameter("code"), redirect, null);

		ObjectMapper mapper = new ObjectMapper();

		LOGGER.info(mapper.writeValueAsString(tokenResponse));

		String idTokenString = tokenResponse.getIdToken();
		
		LOGGER.info("idTokenString : " +idTokenString);

		if (idTokenString != null) {
			JWSInput input = new JWSInput(idTokenString);
			IDToken idToken = input.readJsonContent(IDToken.class);
			userDetails = new UserDetails();
			userDetails.setEmail(idToken.getEmail());
			userDetails.setName(idToken.getPreferredUsername());
		}
		return userDetails;		
	}

	public KeycloakDeployment getKeycloakDeployment() {
		return keycloakDeployment;
	}
}
