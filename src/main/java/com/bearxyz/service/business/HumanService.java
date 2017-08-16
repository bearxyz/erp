package com.bearxyz.service.business;

import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/16.
 */
@Service
public class HumanService {

    @Autowired
    private UserRepository repository;

    public String getDirectLeader(String uid) {
        List<User> users = repository.findAllDepPersonByUid(uid);
        User u = new User();
        for (User user : users) {
            User temp = repository.findDepLeader(user.getId());

        }
        return u.getId();
    }

}
