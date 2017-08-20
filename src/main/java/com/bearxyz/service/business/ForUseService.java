package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.ForUseItemRepository;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.history.HistoricTaskInstance;
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
    private ForUseItemRepository forUseItemRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private SysService sysService;

    @Autowired
    private WorkflowService workflowService;

    public void deleteItemById(String id)
    {
        forUseItemRepository.delete(id);
    }

    public ForUse getOneById(String id){
        ForUse forUse = repository.findOne(id);
        return forUse;
    }

    public void apply(ForUse forUse){
        save(forUse);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","物品领用");
        variables.put("url","/foruse/");
        variables.put("bid",forUse.getId());
        variables.put("applyer", forUse.getCreatedBy());
        forUse.setProcessInstanceId(workflowService.startWorkflow("for-use",forUse.getId(),forUse.getCreatedBy(),variables));
    }

    public void save(ForUse forUse){
        for(ForUseItem item: forUse.getItems()){
            Goods goods = goodsService.getById(item.getGoodsId());
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
        repository.save(forUse);
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
                Goods g = goodsService.getById(item.getGoodsId());
                goods+= g.getName()+item.getCount()+item.getUnit()+";";
            }
            Task task = workflowService.getTaskByBussinessId(forUse.getId());
            if(task!=null) {
                forUse.setTaskId(task.getId());
                forUse.setTaskName(task.getName());
            }
            else {
                forUse.setTaskName("已结束");
            }
            forUse.setGoods(goods);
            User user = sysService.getUserById(forUse.getCreatedBy());
            forUse.setApplyer(user.getFirstName()+user.getLastName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

}
