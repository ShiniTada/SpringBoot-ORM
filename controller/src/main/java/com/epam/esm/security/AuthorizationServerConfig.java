package com.epam.esm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
@Import(SecurityConfig.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  private static final String CLIENT_ID = "client";
  private static final String CLIENT_SECRET = "secret";
  private static final String GRANT_TYPE_PASSWORD = "password";

  private static final String AUTHORIZATION_CODE = "authorization_code";
  private static final String REFRESH_TOKEN = "refresh_token";
  private static final String IMPLICIT = "implicit";
  private static final String SCOPE_READ = "read";
  private static final String SCOPE_WRITE = "write";
  private static final String TRUST = "trust";
  private static final int ONE_DAY = 60 * 60 * 24;
  private static final int THIRTY_DAYS = 60 * 60 * 24 * 30;

  @Autowired private TokenStore tokenStore;

  @Autowired private JwtAccessTokenConverter jwtTokenEnhancer;

  @Autowired private UserApprovalHandler userApprovalHandler;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  @Qualifier("usersService")
  private UserDetailsService userDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient(CLIENT_ID)
        .secret(CLIENT_SECRET)
        .authorizedGrantTypes(GRANT_TYPE_PASSWORD, AUTHORIZATION_CODE, REFRESH_TOKEN, IMPLICIT)
        .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
        .accessTokenValiditySeconds(ONE_DAY)
        .refreshTokenValiditySeconds(THIRTY_DAYS);
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints
        .tokenStore(tokenStore)
        .tokenEnhancer(jwtTokenEnhancer)
        .userApprovalHandler(userApprovalHandler)
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);
  }
}
