package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/3.
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

    Page<User> findUsersByType(String type, Pageable request);

    @Query(value = "select u.* from SYS_USER u inner join SYS_ROLE_USER ru on u.id = ru.USER_ID inner join SYS_ROLE r on ru.ROLE_ID=r.id where r.name like '%经理%' and u.id=?1;", nativeQuery = true)
    User findDepLeader(String uid);

    @Query(value = "select * from SYS_USER u inner join SYS_ROLE_USER ru on u.id = ru.USER_ID inner join SYS_ROLE r on ru.ROLE_ID=r.id where r.name=(select r.name from SYS_USER u inner join SYS_ROLE_USER ru on u.id = ru.USER_ID inner join SYS_ROLE r on ru.ROLE_ID=r.id where r.type='ROLE_TYPE_DEPARTMENT' and u.id=?1)", nativeQuery = true)
    List<User> findAllDepPersonByUid(String uid);

}
