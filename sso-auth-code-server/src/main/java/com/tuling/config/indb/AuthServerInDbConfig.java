package com.tuling.config.indb;

import com.tuling.config.TulingUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * Created by smlz on 2019/12/25.
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerInDbConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TulingUserDetailService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    JsonSerializationStrategy jsonSerializationStrategy;
    /**
     * ??????????????????: ????????????????????????token???????????????  ??????|db|redis|jwt
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:17
     */
    @Bean
    public TokenStore tokenStore(){
       // return new JdbcTokenStore(dataSource);
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
         //redisTokenStore.setSerializationStrategy(jsonSerializationStrategy);
         return redisTokenStore;
    }



    /**
     * ??????????????????:?????????????????????????????? ???????????????token  ????????????????????????????????? ?????????
     * ???????????? ???????????????????????????db??????
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:18
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * ??????????????????:????????????????????????????????????????????? ?????????????????? ???????????? oauth_client_details
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:19
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * ??????????????????:?????????????????????????????????
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:21
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(tokenStore()) //????????????????????????token ???????????????
                .userDetailsService(userDetailsService) //???????????????token??????????????? ??????????????????
                .authenticationManager(authenticationManager);
        //????????????accessToken????????????????????????
        //endpoints.tokenServices(defaultTokenServices());

    }

    /**
     * ???????????????????????????????????????(??????????????????????????????????????????????????? ?????????????????????)
     * https://stackoverflow.com/questions/35056169/how-to-get-custom-user-info-from-oauth2-authorization-server-user-endpoint/35092561
     * @return
     */
/*    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        tokenServices.setAccessTokenValiditySeconds(60*60*12); // token?????????????????????????????????12??????
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//??????30??????????????????
        return tokenServices;
    }*/

    /**
     * ??????????????????:???????????????????????????
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:23
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //????????????????????????token???????????? clientId ???clientSecret?????????
        security .checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();
    }

}
