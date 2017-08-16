package com.bearxyz.repository;

import com.bearxyz.domain.po.sys.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by bearxyz on 2017/6/3.
 */
public interface PermissionRepository extends JpaRepository<Permission, String> {

    Permission findByMaskOrderBySeqAsc(String mask);

    List<Permission> findAllByTypeOrderBySeqAsc(String type);

    Page<Permission> findPermissionsByParentId(String pid, Pageable request);

    List<Permission> findAllByParentIdOrderBySeqAsc(String pid);

    List<Permission> findAllByTypeNotNullOrderBySeqAsc();

    @Query(value = "select DISTINCT(p.id), p.* from SYS_PERMISSION p inner join SYS_PERMISSION_ROLE pr on pr.PERMISSION_ID = p.ID inner join SYS_ROLE_USER ru on pr.ROLE_ID = ru.ROLE_ID " +
            "inner join SYS_USER su on ru.USER_ID = su.ID where p.TYPE='PERMISSION_TYPE_MENU' AND su.ID=?1 order by seq ASC", nativeQuery = true)
    List<Permission> findAllByUserId(String id);

    @Query(value = "select max(seq) FROM SYS_PERMISSION WHERE TYPE ='PERMISSION_TYPE_MENU'", nativeQuery = true)
    Integer findMaxSeq();

    List<Permission> findAllBySeqLessThanEqual(Integer seq);

    @Query(value = "SELECT * FROM SYS_PERMISSION WHERE SEQ > ?1 AND SEQ <=?2", nativeQuery = true)
    List<Permission> findAllBySeqSmallToBig(@Param("s")Integer s, @Param("b")Integer b);

    @Query(value = "SELECT * FROM SYS_PERMISSION WHERE SEQ >= ?1 AND SEQ <?2", nativeQuery = true)
    List<Permission> findAllBySeqBigToSmall(@Param("s")Integer s, @Param("b")Integer b);

}
