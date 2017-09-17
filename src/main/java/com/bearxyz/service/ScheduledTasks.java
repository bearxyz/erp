package com.bearxyz.service;

import com.bearxyz.mapper.ContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * Created by bearxyz on 2017/9/15.
 */
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private ContractMapper contractMapper;

    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron(){
        contractMapper.checkContractValide();
    }

}
