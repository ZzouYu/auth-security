package com.zy.password.controller;

import com.zy.password.config.MDA;
import com.zy.password.entity.TokenInfo;
import com.zy.password.utils.Result;
import com.zy.password.utils.SystemErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zy
 * @date: 2021/8/17 上午11:36
 * @description:
 */
@Controller @Slf4j public class LoginController {

    @Autowired private RestTemplate restTemplate;

    @RequestMapping("/login") @ResponseBody public Result login(@RequestParam(value = "username") String userName,
        @RequestParam(value = "password") String password, HttpServletRequest request) {
        //通过网关认证，拿到token信息
        ResponseEntity<TokenInfo> response;
        try {
            //通过网关认证，拿到token信息
            response = restTemplate
                .exchange(MDA.AUTH_SERVER_URL, HttpMethod.POST, warpRequest(userName, password), TokenInfo.class);
        } catch (RestClientException e) {
            log.error("userName:{},password:{}去认证服务器登陆异常,异常信息:{}", userName, password, e);
            return Result.fail(SystemErrorType.LOGIN_FAIL);
        }
        settingToke2Session(response, request, userName);
        return Result.success(SystemErrorType.LOGIN_SUCCESS);
    }

    private void settingToke2Session(ResponseEntity<TokenInfo> response, HttpServletRequest request, String userName) {
        //把token存放到session中
        TokenInfo tokenInfo = response.getBody();
        tokenInfo.setLoginUser(userName);
        request.getSession().setAttribute(MDA.TOKEN_INFO_KEY, response.getBody());
    }

    private HttpEntity<MultiValueMap<String, String>> warpRequest(String userName, String password) {
        //封装oauth2 请求头 clientId clientSecret
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(MDA.CLIENT_ID, MDA.CLIENT_SECRET);

        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        reqParams.add("username", userName);
        reqParams.add("password", password);
        reqParams.add("grant_type", "password");
        reqParams.add("scope", "read");
        //封装请求参数
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(reqParams, httpHeaders);
        return entity;
    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest) {
        httpServletRequest.getSession().invalidate();
        return "home";
    }
}
