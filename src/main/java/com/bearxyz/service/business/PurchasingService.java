package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.repository.PurchasingDetailRepository;
import com.bearxyz.repository.PurchasingRepository;
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
 * Created by bearxyz on 2017/8/16.
 */
@Transactional
@Service
public class PurchasingService {

    @Autowired
    private PurchasingRepository repository;

    @Autowired
    private PurchasingDetailRepository detailRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private WorkflowService workflowService;

    public Purchasing getOneById(String id){
        return repository.findOne(id);
    }

    public void apply(Purchasing purchasing){
        save(purchasing);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","采购申请");
        variables.put("url","/purchasing/");
        variables.put("bid",purchasing.getId());
        variables.put("applyer", purchasing.getCreatedBy());
        purchasing.setProcessInstanceId(workflowService.startWorkflow("purchasing-audit",purchasing.getId(),purchasing.getCreatedBy(),variables));
    }

    public void save(Purchasing purchasing){
        for(PurchasingDetail item: purchasing.getItems()){
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
        repository.save(purchasing);
    }

    public DataTable<Purchasing> getForUse(String uid, PaginationCriteria req){
        DataTable<Purchasing> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Purchasing> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.like(root.get("title"),"%"+StringUtils.trimAllWhitespace("")+"%"));
            if(!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            return predicate;
        };
        Page<Purchasing> page = repository.findAll(specification, request);
        List<Purchasing> content = page.getContent();
        for(Purchasing purchasing: content){
            String goods = "";
            for(PurchasingDetail item : purchasing.getItems()){
                Goods g = goodsRepository.findOne(item.getGoodsId());
                goods+= g.getName()+item.getCount()+item.getUnit()+";";
            }
            Task task = workflowService.getTaskByBussinessId(purchasing.getId());
            if(task!=null) {
                purchasing.setTaskId(task.getId());
                purchasing.setTaskName(task.getName());
            }
            else {
                purchasing.setTaskName("已结束");
            }
            purchasing.setGoods(goods);
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

}
