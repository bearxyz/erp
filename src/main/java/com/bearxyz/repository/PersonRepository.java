package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */
public interface PersonRepository extends JpaRepository<Person, String> {

    List<Person> findAllByCompanyIdAndType(String cid, String type);

}
