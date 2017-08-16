package com.bearxyz.listener;

import com.bearxyz.domain.po.business.ForUse;
import com.bearxyz.domain.po.business.ForUseItem;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.service.business.StockService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ForUseUnLoad implements ExecutionListener {

    @Autowired
    private ForUseRepository repository;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private StockService service;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        ForUse forUse = repository.findOne(execution.getProcessBusinessKey());
        for(ForUseItem item: forUse.getItems()){
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            service.unload(goods, item.getAmmount());
        }
        repository.save(forUse);
    }

}
