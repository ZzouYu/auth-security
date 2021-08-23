package com.zy.authserver.config;

import com.alibaba.fastjson.JSON;
import com.zy.authserver.config.role.domin.TulingUser;
import com.zy.authserver.config.role.entity.SysPermission;
import com.zy.authserver.config.role.entity.SysUser;
import com.zy.authserver.config.role.mapper.SysUserMapper;
import com.zy.authserver.config.role.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 改变为db,需要去数据库中查询
 */
@Component("userDetailsService")
@Slf4j
public class TulingUserDetailService implements UserDetailsService {

    //密码加密组件
    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private SysUserMapper sysUserMapper;

    @Autowired private ISysPermissionService sysPermissionService;

    @Override public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        SysUser sysUser = sysUserMapper.findByUserName(userName);

        if (null == sysUser) {
            log.warn("根据用户名:{}查询用户信息为空", userName);
            throw new UsernameNotFoundException(userName);
        }

        List<SysPermission> sysPermissionList = sysPermissionService.findByUserId(sysUser.getId());

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sysPermissionList)) {
            for (SysPermission sysPermission : sysPermissionList) {
                authorityList.add(new SimpleGrantedAuthority(sysPermission.getUri()));
            }
        }

        TulingUser tulingUser =
            new TulingUser(sysUser.getUsername(), passwordEncoder.encode(sysUser.getPassword()), authorityList);
        log.info("用户登陆成功:{}", JSON.toJSONString(tulingUser));
        return tulingUser;
    }
}
