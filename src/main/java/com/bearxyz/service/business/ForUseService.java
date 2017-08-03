package com.bearxyz.service.business;

import com.bearxyz.repository.ForUseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/2.
 */
@Transactional
@Service
public class ForUseService {

    @Autowired
    private ForUseRepository repository;

    public void apply(){}

    public void approve(){}

    public void deny(){}

    public void reapply(){}

}
