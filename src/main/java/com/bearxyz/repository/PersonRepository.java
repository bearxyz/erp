package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/7/27.
 */
public interface PersonRepository extends JpaRepository<Person, String> {
}
