package com.zy.authserver.config.role.service.impl;


import com.zy.authserver.config.role.entity.SysUser;
import com.zy.authserver.config.role.mapper.SysUserMapper;
import com.zy.authserver.config.role.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by smlz on 2019/12/20.
 */
@Component
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserMapper.findByUserName(username);
    }
}
