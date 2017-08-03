package com.bearxyz.repository;

import com.bearxyz.domain.po.business.Package;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by bearxyz on 2017/7/26.
 */
public interface PackageRepository extends JpaRepository<Package, String> {
}
