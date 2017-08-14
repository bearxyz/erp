package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.business.ForUseItem;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.service.workflow.ForUseWorkflowService;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bearxyz on 2017/8/2.
 */
@Transactional
@Service
public class ForUseService {

    @Autowired
    private ForUseRepository repository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private WorkflowService workflowService;

    public void apply(ForUse forUse){
        for(ForUseItem item: forUse.getItems()){
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if(item.getPackageId()!=null&&!item.getPackageId().isEmpty()) {
                Package pkg = packageRepository.findOne(item.getPackageId());
                item.setSpec(pkg.getPackageSpec());
                item.setUnit(pkg.getPackageUnit());
                item.setAmmount(pkg.getAmmount() * item.getCount());
            }else{
                item.setSpec(goods.getSpec());
                item.setUnit(goods.getUnit());
                item.setAmmount(item.getCount());
            }
        }
        Map<String, Object> variables = new HashMap<String, Object>();
        repository.saveAndFlush(forUse);
        forUse.setProcessInstanceId(workflowService.startWorkflow("for-use",forUse.getId(),forUse.getCreatedBy(),variables));
    }

    public void approve(){}

    public void deny(){}

    public void reapply(){}

}
