package com.bearxyz.mapper;

import com.bearxyz.domain.po.sys.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Mapper
public interface UserMapper {

    @Insert("INSERT INTO T_USER_PROVINCE(USER_ID, PROVINCE) VALUES(#{uid}, #{province})")
    void saveUserProvince(@Param("uid") String uid, @Param("province") String province);

    @Insert("INSERT INTO T_USER_CLIENT(USER_ID, COMPANY_ID) VALUES(#{uid},#{companyId})")
    void saveUserClient(@Param("uid") String uid, @Param("companyId") String companyId);

    @Select("SELECT PROVINCE FROM T_USER_PROVINCE WHERE USER_ID = #{uid}")
    List<String> findProvinceByUser(String uid);

    @Select("SELECT COMPANY_ID FROM T_USER_CLIENT WHERE USER_ID = #{uid}")
    List<String> findClientIdByUser(String uid);

    @Delete("DELETE from T_USER_PROVINCE WHERE USER_ID = #{uid}")
    void deleteProvinceByUser(String uid);

    @Delete("DELETE from T_USER_CLIENT WHERE USER_ID = #{uid}")
    void deleteClientByUser(String uid);

    @Select("SELECT u.* FROM SYS_USER u INNER JOIN T_USER_PROVINCE up ON u.ID=up.USER_ID INNER JOIN SYS_ROLE_USER ru ON ru.USER_ID=u.ID INNER JOIN SYS_ROLE r ON r.ID=ru.ROLE_ID WHERE up.PROVINCE=#{province} AND r.MASK='ROLE_DEPARTMENT_INVITEINVESTMENTS'")
    User findUserByProvince(String province);

    @Select("SELECT u.* FROM SYS_USER u INNER JOIN T_USER_CLIENT uc ON u.ID=uc.USER_ID WHERE uc.COMPANY_ID=#{companyId}")
    User findUserByClientId(String companyId);
}
