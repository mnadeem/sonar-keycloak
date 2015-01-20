package org.sonar.plugins.keycloak;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.OAuth2Constants;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.keycloak.util.KeycloakUriBuilder;
import org.sonar.api.ServerExtension;
import org.sonar.api.config.Settings;

public class KeycloakClient implements ServerExtension {
	
	public static final String KEYCLOAK_SERVER_URL = "sonar.keycloak.serverlUrl";
	
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
		// TODO Auto-generated method stub
		return null;
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
				.path("/keycloak/validate");
		String redirect = builder.toTemplate();
		return redirect;
	}

	public KeycloakDeployment getKeycloakDeployment() {
		return keycloakDeployment;
	}
}
