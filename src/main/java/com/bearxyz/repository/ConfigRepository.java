package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Config;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/9/3.
 */
public interface ConfigRepository extends JpaRepository<Config, String> {
}
