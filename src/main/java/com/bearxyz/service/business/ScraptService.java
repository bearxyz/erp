package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.business.Scrapt;
import com.bearxyz.domain.po.business.ScraptItem;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/8/27.
 */
@Service
@Transactional
public class ScraptService {

    @Autowired
    private ScraptRepository repository;

    @Autowired
    private ScraptItemRepository scraptItemRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkflowService workflowService;

    public void apply(Scrapt scrapt){
        save(scrapt);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","报损申请");
        variables.put("url","/scrapt/");
        variables.put("bid",scrapt.getId());
        variables.put("applyer", scrapt.getCreatedBy());
        variables.put("applyUserId", scrapt.getCreatedBy());
        scrapt.setProcessInstanceId(workflowService.startWorkflow("scrap-audit",scrapt.getId(),scrapt.getCreatedBy(),variables));
    }

    public void save(Scrapt scrapt){
        for(ScraptItem item: scrapt.getItems()){
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if(item.getPackageId()!=null&&!item.getPackageId().isEmpty()) {
                Package pkg = packageRepository.findOne(item.getPackageId());
                item.setSpec(pkg.getPackageSpec());
                item.setUnit(pkg.getPackageUnit());
                item.setAmmount(pkg.getAmmount() * item.getCount());
            }else{
                item.setSpec(goods.getModel());
                item.setUnit(goods.getUnit());
                item.setAmmount(item.getCount());
            }
        }
        repository.save(scrapt);
    }

    public DataTable<ScraptItem> getScraptItems(boolean approved, PaginationCriteria req) {
        DataTable<ScraptItem> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<ScraptItem> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        Page<ScraptItem> page = scraptItemRepository.findAll(specification, request);
        List<ScraptItem> content = page.getContent();
        for (ScraptItem item : content) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            item.setGoods(goods);
            User user = userRepository.findOne(item.getCreatedBy());
            item.getScrapt().setApplyer(user.getFirstName() + user.getLastName());
            Task task = workflowService.getTaskByBussinessId(item.getId());
            if (task != null) {
                item.getScrapt().setTaskId(task.getId());
                item.getScrapt().setTaskName(task.getName());
                item.getScrapt().setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(item.getScrapt().getId());
                if (historicTaskInstance != null) {
                    item.getScrapt().setTaskName("已完成");
                    item.getScrapt().setTaskId(historicTaskInstance.getId());
                    item.getScrapt().setFinishedDate(historicTaskInstance.getEndTime());
                }
            }
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

}
