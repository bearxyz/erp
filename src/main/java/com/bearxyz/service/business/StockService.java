package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
import com.bearxyz.service.workflow.WorkflowService;
import com.bearxyz.utility.OrderUtils;
import org.activiti.engine.history.HistoricProcessInstance;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StockService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private PurchasingOrderRepository purchasingOrderRepository;

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private PurchasingOrderItemRepository purchasingOrderItemRepository;

    @Autowired
    private OfficialPartnerRepository officialPartnerRepository;

    @Autowired
    private StockRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockItemRepository itemRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private WorkflowService workflowService;

    public Stock getStockById(String id) {
        return stockRepository.findOne(id);
    }

    public List<StockItem> getItemsByStockId(String id) {
        Stock stock = stockRepository.findOne(id);
        for (StockItem item : stock.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            item.setGoods(goods);
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                item.setSupplierName(partner.getName());
            }

        }
        return stock.getItems();
    }

    public List<PurchasingOrderItem> getPurchasingOrderItemsByIds(String ids) {
        List<PurchasingOrderItem> items = new ArrayList<>();
        String[] str = ids.split(",");
        for (String id : str) {
            PurchasingOrderItem it = purchasingOrderItemRepository.findOne(id);
            Goods goods = goodsRepository.findOne(it.getGoodsId());
            it.setGoods(goods);
            items.add(it);
        }
        return items;
    }

    public DataTable<StockItem> getStockItems(boolean approved) {
        DataTable<StockItem> result = new DataTable<>();
        Specification<StockItem> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        List<StockItem> page = stockItemRepository.findAll(specification);
        List<StockItem> content = page;
        for (StockItem item : content) {
            Dict dict = dictRepository.findByMask(item.getStock().getMask());
            item.getStock().setTypeName(dict.getName());
            User user = userRepository.findOne(item.getStock().getLastModifiedBy());
            item.getStock().setOperator(user.getFirstName() + user.getLastName());
        }
        result.setRecordsTotal((long) page.size());
        result.setRecordsFiltered((long) page.size());
        result.setData(content);
        return result;
    }

    public DataTable<Stock> getStocks(String type, boolean approved, PaginationCriteria req) {
        Sort sort = new Sort(Sort.Direction.fromString("desc"), "lastUpdated");
        DataTable<Stock> result = new DataTable<>();
        Specification<Stock> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (type != null && !StringUtils.isEmpty(type))
                predicate.getExpressions().add(cb.equal(root.get("type"), type));
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        List<Stock> page = stockRepository.findAll(specification,sort);
        List<Stock> content = page;
        for (Stock stock : content) {
            Dict dict = dictRepository.findByMask(stock.getMask());
            stock.setTypeName(dict.getName());
            User user = userRepository.findOne(stock.getCreatedBy());
            if (stock.getType().equals("STOCK-OUT"))
                user = userRepository.findOne(stock.getLastModifiedBy());
            stock.setOperator(user.getFirstName() + user.getLastName());
            Task task = workflowService.getTaskByBussinessId(stock.getId());
            if (task != null) {
                stock.setTaskId(task.getId());
                stock.setTaskName(task.getName());
                stock.setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(stock.getId());
                if (historicTaskInstance != null) {
                    stock.setTaskName("已完成");
                    stock.setTaskId(historicTaskInstance.getId());
                    stock.setFinishedDate(historicTaskInstance.getEndTime());
                } else {
                    stock.setTaskName("已完成");
                    stock.setFinishedDate(stock.getLastUpdated());
                }
            }
        }
        result.setRecordsTotal((long) page.size());
        result.setRecordsFiltered((long) page.size());
        result.setData(content);
        return result;
    }

    public DataTable<PurchasingOrderItem> getPurchasingOrderItems(boolean approved, PaginationCriteria req) {
        DataTable<PurchasingOrderItem> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<PurchasingOrderItem> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        Page<PurchasingOrderItem> page = purchasingOrderItemRepository.findAll(specification, request);
        List<PurchasingOrderItem> content = page.getContent();
        for (PurchasingOrderItem item : content) {
            if (item.getSupplier() != null) {
                OfficialPartner partner = officialPartnerRepository.findOne(item.getSupplier());
                item.setSupplierName(partner.getName());
            }
            if (item.getOrder().getOperator() != null) {
                User user = userRepository.findOne(item.getOrder().getOperator());
                item.getOrder().setApplyer(user.getFirstName() + user.getLastName());
            }
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            item.setGoods(goods);
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    public void applyLoad(Stock stock) {
        stock.setType("STOCK-IN");
        stock.setCode(OrderUtils.genSerialnumber("RK"));
        save(stock);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "入库申请");
        variables.put("url", "/stock/");
        variables.put("bid", stock.getId());
        variables.put("applyer", stock.getCreatedBy());
        stock.setProcessInstanceId(workflowService.startWorkflow("purchasing-in-stock", stock.getId(), stock.getCreatedBy(), variables));
    }

    public void applyUnload(Stock stock) {
        stock.setType("STOCK-OUT");
        save(stock);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "出库申请");
        variables.put("url", "/stock/");
        variables.put("bid", stock.getId());
        variables.put("applyer", stock.getCreatedBy());
        stock.setProcessInstanceId(workflowService.startWorkflow("stock-audit", stock.getId(), stock.getCreatedBy(), variables));
    }

    public void save(Stock stock) {
        for (StockItem item : stock.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (item.getPackageId() != null && !item.getPackageId().isEmpty()) {
                Package pkg = packageRepository.findOne(item.getPackageId());
                item.setSpec(goods.getModel());
                item.setUnit(pkg.getPackageUnit());
                item.setAmmount(pkg.getAmmount() * item.getCount());
            } else {
                item.setSpec(goods.getModel());
                item.setUnit(goods.getUnit());
                item.setAmmount(item.getCount());
            }
        }
        repository.save(stock);
    }

    public void load(Goods goods, Integer ammount) {
        goods.setStock(goods.getStock() + ammount);
        goodsRepository.save(goods);
    }

    public void unload(Goods goods, Integer ammount) {
        goods.setStock(goods.getStock() - ammount);
        goodsRepository.save(goods);
    }

}
