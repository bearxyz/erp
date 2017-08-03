package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/7.
 */
public interface RoleRepository extends JpaRepository<Role, String> {

    Role findByMask(String mask);

    List<Role> findAllByType(String type);

}
