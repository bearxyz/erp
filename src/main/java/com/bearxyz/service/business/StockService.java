package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Stock;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.StockItemRepository;
import com.bearxyz.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StockService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private StockRepository repository;

    @Autowired
    private StockItemRepository itemRepository;

    public void applyLoad(){}

    public void applyUnload(){}

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
