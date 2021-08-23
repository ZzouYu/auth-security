package com.zy.authserver.config.indb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthServerInDbConfig extends AuthorizationServerConfigurerAdapter  {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 把token存储到redis中
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        //生产上 需要把token存储到redis中或者使用jwt
        //return  new RedisTokenStore(redisConnectionFactory);
        return new JdbcTokenStore(dataSource);
    }
    /**
     * 方法实现说明:认证服务器基于配置可以给哪些第三方客户端 有二种存储方式
     * ①:基于内存的
     * ②:基于db的,那么需要创建数据库的表 oauth_client_details
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:09
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 方法实现说明:第三方客户端读取组件 专门用于读取oauth_client_details
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:11
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 方法实现说明:认证服务器核心配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:13
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(tokenStore())//token存储的方式
            .authenticationManager(authenticationManager);


    }

    /**
     * 方法实现说明:配置校验token需要带入clientId 和clientSeret配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:14
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security .checkTokenAccess("isAuthenticated()");
    }
}
