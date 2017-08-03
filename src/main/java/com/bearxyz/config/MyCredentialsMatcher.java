package com.bearxyz.config;

import com.bearxyz.utility.BCrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * Created by bearxyz on 2017/6/12.
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String accountCredentials = (String) info.getCredentials();
        String password = String.valueOf(token.getPassword());
        return BCrypt.checkpw(password, accountCredentials);
    }
}
