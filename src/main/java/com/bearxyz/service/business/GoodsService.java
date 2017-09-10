package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Goods;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.repository.GoodsRepository;
import com.bearxyz.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
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

    @Autowired
    private PackageRepository packageRepository;

    public Goods save(Goods goods) {
        return repository.save(goods);
    }

    public void deleteById(String id) {
        repository.delete(id);
    }

    public void delete(String[] ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    public void deletePackageById(String id) {
        packageRepository.delete(id);
    }

    public Goods getById(String id) {
        return repository.findOne(id);
    }

    public DataTable<Goods> getGoods() {
        List<Goods> goods = repository.findAll();
        for (Goods g : goods) {
            Dict d = dictRepository.findByMask(g.getProject());
            if (d != null)
                g.setProjectName(d.getName());
            d = dictRepository.findByMask(g.getType());
            if (d != null)
                g.setTypeName(d.getName());
            d = dictRepository.findByMask(g.getSubtype());
            if (d != null)
                g.setSubtypeName(d.getName());
        }
        DataTable<Goods> partners = new DataTable<>();
        partners.setRecordsTotal((long) goods.size());
        partners.setRecordsFiltered((long) goods.size());
        partners.setData(goods);
        return partners;
    }

    public List<Goods> getByIds(String[] ids) {
        List<Goods> goods = new ArrayList<>();
        for (String id : ids)
            goods.add(repository.findOne(id));
        return goods;
    }

    public List<Goods> getGoodsByType(String project, String type, String subtype) {
        Specification<Goods> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (project != null && !StringUtils.isEmpty(project))
                predicate.getExpressions().add(cb.equal(root.get("project"), project));
            if (type != null &&  !StringUtils.isEmpty(type))
                predicate.getExpressions().add(cb.equal(root.get("type"), type));
            if (subtype != null &&  !StringUtils.isEmpty(subtype))
                predicate.getExpressions().add(cb.equal(root.get("subtype"), subtype));
            return predicate;
        };
        List<Goods> goods = repository.findAll(specification);
        return goods;
    }

}
