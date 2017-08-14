package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
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

    public DataTable<ForUse> getForUse(String uid, PaginationCriteria req){
        DataTable<ForUse> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<ForUse> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.like(root.get("title"),"%"+StringUtils.trimAllWhitespace("")+"%"));
            if(!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            return predicate;
        };
        Page<ForUse> page = repository.findAll(specification, request);
        List<ForUse> content = page.getContent();
        for(ForUse forUse: content){
            String goods = "";
            for(ForUseItem item : forUse.getItems()){
                Goods g = goodsRepository.findOne(item.getGoodsId());
                goods+= g.getName()+item.getCount()+item.getUnit()+";";
            }
            Task task = workflowService.getTaskByBussinessId(forUse.getId());
            forUse.setTaskId(task.getId());
            forUse.setTaskName(task.getName());
            forUse.setGoods(goods);
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    public void approve(){}

    public void deny(){}

    public void reapply(){}

}
