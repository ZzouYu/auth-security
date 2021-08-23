package com.zy.authserver.config.role.service;

import com.zy.authserver.config.role.entity.SysUser;

/**
 * Created by smlz on 2019/12/20.
 */
public interface ISysUserService {

    SysUser getByUsername(String username);
}
