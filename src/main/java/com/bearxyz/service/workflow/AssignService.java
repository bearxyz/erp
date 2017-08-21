package com.bearxyz.service.workflow;

import com.bearxyz.domain.po.sys.User;
import com.bearxyz.service.sys.SysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by bearxyz on 2017/8/21.
 */
@Service
public class AssignService {

    @Autowired
    private SysService sysService;

    public String getManagerForEmp(String empId){
        String managerId = null;
        User user = sysService.getManagerByUid(empId);
        return user.getId();
    }

}
