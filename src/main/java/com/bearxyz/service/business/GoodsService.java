package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.repository.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/25.
 */
@Transactional
@Service
public class GoodsService {

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private DictRepository dictRepository;

    public Goods save(Goods goods) {
        return repository.saveAndFlush(goods);
    }

    public void deleteById(String id) {
        repository.delete(id);
    }

    public void delete(String[] ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    public Goods getById(String id){
        return repository.findOne(id);
    }

    public DataTable<Goods> getByNature(String nature){
        List<Goods> goods = repository.findAllByNature(nature);
        for(Goods good:goods){
            Dict dict = dictRepository.findByMask(good.getUnit());
            if(dict!=null)
                good.setUnitName(dict.getName());
        }
        DataTable<Goods> partners = new DataTable<>();
        partners.setRecordsTotal((long)goods.size());
        partners.setRecordsFiltered((long)goods.size());
        partners.setData(goods);
        return partners;
    }

}
