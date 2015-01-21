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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.sonar.api.security.UserDetails;
/**
 * 
 * @author Mohammad Nadeem
 *
 */
public class KeycloakAuthentication  implements Serializable {

	private static final long serialVersionUID = 1L;

	private final String userName;
	private final String email;
	private Set<String> roles = new HashSet<String>();
	private String refreashToken;

	public KeycloakAuthentication(IDToken idToken, AccessToken accessToken, String refreashToken) {
		buildRoles(accessToken);
		this.userName = idToken.getName();
		this.refreashToken = refreashToken;
		this.email = idToken.getEmail();
	}

	private void buildRoles(AccessToken accessToken) {

		if (accessToken != null && accessToken.getRealmAccess() != null) {
			for (String role : accessToken.getRealmAccess().getRoles()) {
				roles.add(role);
			}
		}		
	}

	public UserDetails toUserDetails () {
		UserDetails userDetails = new UserDetails();
		userDetails.setName(this.userName);
		userDetails.setEmail(this.email);
		return userDetails;
	}

	public String getRefreashToken() {
		return refreashToken;
	}

	@Override
	public String toString() {
		return "KeycloakAuthentication [userName=" + userName + ", email="
				+ email + ", roles=" + roles + "]";
	}	
}
