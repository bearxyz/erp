package com.bearxyz.config;

import com.bearxyz.domain.po.sys.Permission;
import com.bearxyz.domain.po.sys.Role;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by bearxyz on 2017/6/12.
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for(Role role: user.getRoles()){
            authorizationInfo.addRole(role.getMask());
            for(Permission permission: role.getPermissions()){
                authorizationInfo.addStringPermission(permission.getUrl());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        User user = userRepository.findByUsername(username);
        if(user==null||!user.getEnabled()||user.getAccountNonExpired()||user.getAccountNonLocked()) throw new AuthenticationException("登录失败");
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }
}
