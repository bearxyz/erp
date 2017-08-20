package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.StockItemRepository;
import com.bearxyz.repository.StockRepository;
import com.bearxyz.service.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class StockService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StockRepository repository;

    @Autowired
    private StockItemRepository itemRepository;

    @Autowired
    private WorkflowService workflowService;

    public void applyLoad(Stock stock){
        save(stock);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","入库申请");
        variables.put("url","/stock/");
        variables.put("bid",stock.getId());
        variables.put("applyer", stock.getCreatedBy());
        stock.setProcessInstanceId(workflowService.startWorkflow("for-use",stock.getId(),stock.getCreatedBy(),variables));
    }

    public void applyUnload(Stock stock){
        save(stock);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name","出库申请");
        variables.put("url","/stock/");
        variables.put("bid",stock.getId());
        variables.put("applyer", stock.getCreatedBy());
        stock.setProcessInstanceId(workflowService.startWorkflow("for-use",stock.getId(),stock.getCreatedBy(),variables));
    }

    public void save(Stock stock){
        repository.save(stock);
    }

    public void load(Goods goods, Integer ammount){
        goods.setStock(goods.getStock()+ammount);
        goodsRepository.save(goods);
    }

    public void unload(Goods goods, Integer ammount){
        goods.setStock(goods.getStock()-ammount);
        goodsRepository.save(goods);
    }

}
