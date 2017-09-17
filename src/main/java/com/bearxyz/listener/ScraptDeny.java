package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Scrapt;
import com.bearxyz.domain.po.business.ScraptItem;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.ScraptRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/9/14.
 */
@Transactional
@Component
public class ScraptDeny implements ExecutionListener {

    @Autowired
    private ScraptRepository repository;
    @Autowired
    private GoodsRepository goodsRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Scrapt scrapt = repository.findOne(execution.getProcessBusinessKey());
        scrapt.setApproved(true);
        for(ScraptItem item:scrapt.getItems()){
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            goods.setStock(goods.getStock()+item.getAmmount());
            goodsRepository.save(goods);
            item.setApproved(true);
        }
        repository.save(scrapt);
    }
}
