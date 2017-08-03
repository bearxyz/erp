package com.bearxyz.config;

import com.bearxyz.domain.po.sys.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

/**
 * Created by bearxyz on 2017/6/23.
 */
@Configuration
public class UserAuditorBean implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(user==null)
            return null;
        return user.getId();
    }
}
