##Sonar Keycloak Plugin

 ![Keycloak Sonar](https://raw.githubusercontent.com/mnadeem/sonar-keycloak/master/sonar-keycloak.png)

##### this property must be set to true
sonar.authenticator.createUsers=true

##### enable Keycloak plugin
sonar.security.realm=keycloak

#####PLUGIN PROPERTIES

Copy the keycloak.json and paste as value for sonar.keycloak.json, removing the new lines, as can be seen here

sonar.keycloak.json={  "realm": "demo",  "realm-public-key": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjZ1tsS98VEvcoMXm5HpTlWwHCETjjf1jy3ghZw0UBiAkk60myeRb1LHO64H45b7Cfup8zdZVf67vcgYC4f60pYa0/no2RnsZyp785q4lmoxScmyaGlmSsX+6tg7gUqYx9VOnWsIuCLNoAto3JCM+9VxFMN2yG3q3240hzkPiskQIDAQAB",  "auth-server-url": "http://localhost:8080/auth",  "ssl-required": "external",  "resource": "sonar",  "credentials": {    "secret": "70b6fff9-ec46-4ce3-b949-08a0528a3bf4"  }}

Refer [this](https://reachmnadeem.wordpress.com/2015/01/22/integrating-keycloak-with-sonarqube/) blog for more detail

[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/mnadeem/sonar-keycloak/trend.png)](https://bitdeli.com/free "Bitdeli Badge")
