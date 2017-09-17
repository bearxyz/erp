package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/3.
 */
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    @Query(value = "SELECT * FROM SYS_USER WHERE EMAIL = ?1 OR MOBILE = ?1", nativeQuery = true)
    User findByPersonEmailOrMobile(String login);

    List<User> findUsersByType(String type);

    List<User> findUsersByTypeAndCompanyId(String type, String companyId);

    @Query(value = "select ou.* from SYS_USER ou inner join SYS_ROLE_USER oru on ou.id = oru.USER_ID inner join SYS_ROLE osr on osr.id = oru.ROLE_ID where ou.ID in " +
            "(select USER_ID from SYS_ROLE_USER where ROLE_ID in " +
            "(select ru.ROLE_ID from SYS_ROLE_USER ru inner join SYS_ROLE r on ru.ROLE_ID=r.ID where ru.USER_ID = ?1 and r.TYPE='ROLE_TYPE_DEPARTMENT')) and osr.mask like '%MANAGER%'", nativeQuery = true)
    User findManagerByUid(String uid);

    @Query(value = "select u.* from SYS_USER u inner join SYS_ROLE_USER ru on ru.USER_ID=u.ID inner join SYS_ROLE r on r.ID=ru.ROLE_ID where r.TYPE='ROLE_TYPE_POST' and r.MASK like '%MANAGER%'", nativeQuery = true)
    List<User> findDepartmentManager();

    @Query(value = "SELECT u.* FROM SYS_USER u INNER JOIN SYS_ROLE_USER ru on ru.USER_ID=u.ID INNER JOIN SYS_ROLE r on r.ID=ru.ROLE_ID where r.MASK=?1", nativeQuery = true)
    List<User> findUsersByRolesMask(String mask);

    Integer countUsersByCompanyId(String id);

    @Query(value = "SELECT u.* FROM SYS_USER u INNER JOIN SYS_ROLE_USER ru on ru.USER_ID=u.ID INNER JOIN SYS_ROLE r on r.ID=ru.ROLE_ID WHERE u.COMPANY_ID=?1 AND r.MASK=?2", nativeQuery = true)
    User findUserByCompanyIdAndRole(String cid, String role);

    List<User> findAllByCompanyIdOrParentCompanyId(String cid, String pcid);

}
