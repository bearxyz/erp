package com.bearxyz.listener;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.business.ForUseItem;
import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.domain.po.business.StockItem;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.StockRepository;
import com.bearxyz.utility.OrderUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class ForUseFinished implements ExecutionListener {

    private static final long serialVersionUID = -4460908118448396698L;
    @Autowired
    private ForUseRepository repository;
    @Autowired
    private StockRepository stockRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        ForUse forUse = repository.findOne(execution.getProcessBusinessKey());
        forUse.setApproved(true);
        Stock stock = new Stock();
        stock.setType("STOCK-OUT");
        stock.setMask("STOCK_OUT_OTHER");
        stock.setCode(OrderUtils.genSerialnumber("CK"));
        stock.setPurpose(forUse.getPurpose());
        stock.setDeliverAddress(forUse.getDeliverAddress());
        for(ForUseItem item: forUse.getItems()){
            StockItem it = new StockItem();
            it.setGoodsId(item.getGoodsId());
            it.setUnit(item.getUnit());
            it.setPrice((float)0.0);
            it.setAmmount(item.getAmmount());
            it.setCount(item.getCount());
            stock.getItems().add(it);
        }
        stockRepository.save(stock);
        repository.save(forUse);
    }
}
