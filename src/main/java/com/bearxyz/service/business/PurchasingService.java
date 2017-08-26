package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import com.bearxyz.repository.PurchasingDetailRepository;
import com.bearxyz.repository.PurchasingRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
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
    private SysService sysService;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private WorkflowService workflowService;

    public Purchasing getOneById(String id) {
        return repository.findOne(id);
    }

    public void apply(Purchasing purchasing) {
        save(purchasing);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "采购申请");
        variables.put("url", "/purchasing/");
        variables.put("bid", purchasing.getId());
        variables.put("applyer", purchasing.getCreatedBy());
        variables.put("applyUserId", purchasing.getCreatedBy());
        purchasing.setProcessInstanceId(workflowService.startWorkflow("purchasing-audit", purchasing.getId(), purchasing.getCreatedBy(), variables));
    }

    public void save(Purchasing purchasing) {
        for (PurchasingDetail item : purchasing.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (item.getPackageId() != null && !item.getPackageId().isEmpty()) {
                Package pkg = packageRepository.findOne(item.getPackageId());
                item.setSpec(pkg.getPackageSpec());
                item.setUnit(pkg.getPackageUnit());
                item.setAmmount(pkg.getAmmount() * item.getCount());
            } else {
                item.setSpec(goods.getSpec());
                item.setUnit(goods.getUnit());
                item.setAmmount(item.getCount());
            }
        }
        repository.save(purchasing);
    }

    public DataTable<Purchasing> getPurchasing(String uid, boolean approved, PaginationCriteria req) {
        DataTable<Purchasing> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Purchasing> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.like(root.get("title"), "%" + StringUtils.trimAllWhitespace("") + "%"));
            if (!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        Page<Purchasing> page = repository.findAll(specification, request);
        List<Purchasing> content = page.getContent();
        for (Purchasing purchasing : content) {
            String goods = "";
            for (PurchasingDetail item : purchasing.getItems()) {
                Goods g = goodsRepository.findOne(item.getGoodsId());
                goods += g.getName() + item.getCount() + item.getUnit() + ";";
            }
            Task task = workflowService.getTaskByBussinessId(purchasing.getId());
            if (task != null) {
                purchasing.setTaskId(task.getId());
                purchasing.setTaskName(task.getName());
                purchasing.setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(purchasing.getId());
                if(historicTaskInstance!=null) {
                    Object cancel = workflowService.getHistoryVarByProcessId(historicTaskInstance.getId(), "cancel");
                    if (cancel!=null&&(boolean)cancel)
                        purchasing.setTaskName("已取消");
                    else
                        purchasing.setTaskName("已完成");
                purchasing.setTaskId(historicTaskInstance.getId());
                purchasing.setFinishedDate(historicTaskInstance.getEndTime());
                }
            }
            purchasing.setGoods(goods);
            User user = sysService.getUserById(purchasing.getCreatedBy());
            purchasing.setApplyer(user.getFirstName() + user.getLastName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    public void deleteItemById(String id) {
        detailRepository.delete(id);
    }

}
