package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/6/3.
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Page<User> findUsersByType(String type, Pageable request);

}
