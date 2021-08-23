package com.zy.authserver.config.role.mapper;

import com.zy.authserver.config.role.entity.SysUser;

/**
 * Created by smlz on 2019/12/20.
 */
public interface SysUserMapper {

    SysUser findByUserName(String userName);
}
