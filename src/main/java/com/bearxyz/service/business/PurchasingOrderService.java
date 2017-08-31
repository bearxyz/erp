package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.OfficialPartnerRepository;
import com.bearxyz.repository.PurchasingOrderRepository;
import com.bearxyz.repository.UserRepository;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/8/23.
 */
@Service
@Transactional
public class PurchasingOrderService {

    @Autowired
    private PurchasingOrderRepository repository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficialPartnerRepository officialPartnerRepository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private PurchasingOrderAttachmentService attachmentService;

    public void apply(PurchasingOrder order) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "采购单审核");
        variables.put("url", "/purchasingorder/");
        variables.put("bid", order.getId());
        variables.put("applyer", order.getOperator());
        variables.put("applyUserId", order.getOperator());
        order.setProcessInstanceId(workflowService.startWorkflow("purchasing-check", order.getId(), order.getOperator(), variables));
        save(order);
    }

    public void save(PurchasingOrder order) {
        repository.save(order);
    }

    public void save(PurchasingOrder order, List<MultipartFile> files) throws IOException {
        if(files!=null) {
            if (order.getAttachments() != null) {
                for (PurchasingOrderAttachment attachment : order.getAttachments()) {
                    attachmentService.delete(attachment.getId());
                }
            }
            for (MultipartFile file : files) {
                PurchasingOrderAttachment attachment = attachmentService.save(file);
                order.getAttachments().add(attachment);
            }
        }
        repository.save(order);
    }

    public PurchasingOrder getOneById(String id) {
        PurchasingOrder order = repository.findOne(id);
        giveValueToItems(order);
        return repository.findOne(id);
    }

    public List<PurchasingOrderItem> getItemsByPurchasingId(String id) {
        PurchasingOrder order = repository.findOne(id);
        giveValueToItems(order);
        return order.getItems();
    }

    public DataTable<PurchasingOrder> getPurchasingOrders(boolean approved, PaginationCriteria req) {
        DataTable<PurchasingOrder> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<PurchasingOrder> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        Page<PurchasingOrder> page = repository.findAll(specification, request);
        List<PurchasingOrder> content = page.getContent();
        for (PurchasingOrder purchasingOrder : content) {
            giveValueToItems(purchasingOrder);
            if (purchasingOrder.getOperator() != null) {
                User user = userRepository.findOne(purchasingOrder.getOperator());
                purchasingOrder.setApplyer(user.getFirstName() + user.getLastName());
            }
            Task task = workflowService.getTaskByBussinessId(purchasingOrder.getId());
            if (task != null) {
                purchasingOrder.setTaskId(task.getId());
                purchasingOrder.setTaskName(task.getName());
                purchasingOrder.setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(purchasingOrder.getId());
                if (historicTaskInstance != null) {
                    if(purchasingOrder.getApproved())
                        purchasingOrder.setTaskName("已完成");
                    else
                        purchasingOrder.setTaskName("入库中");
                    purchasingOrder.setTaskId(historicTaskInstance.getId());
                    purchasingOrder.setFinishedDate(historicTaskInstance.getEndTime());
                }
            }
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    private void giveValueToItems(PurchasingOrder order) {
        for (PurchasingOrderItem item : order.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                item.setSupplierName(partner.getName());
            }
        }
    }

}
