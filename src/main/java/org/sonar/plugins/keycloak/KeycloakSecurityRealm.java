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

import org.sonar.api.security.Authenticator;
import org.sonar.api.security.ExternalGroupsProvider;
import org.sonar.api.security.ExternalUsersProvider;
import org.sonar.api.security.SecurityRealm;
/**
 * 
 * @author Mohammad Nadeem
 *
 */
public final class KeycloakSecurityRealm extends SecurityRealm {

  public static final String KEY = "keycloak";

  @Override
  public Authenticator doGetAuthenticator() {
    return new KeycloakAuthenticator();
  }

  @Override
  public ExternalUsersProvider getUsersProvider() {
    return new KeycloakUserProvider();
  }
  
  @Override
	public ExternalGroupsProvider getGroupsProvider() {		
		return super.getGroupsProvider();
	}

  @Override
  public String getName() {
    return KEY;
  }
}
