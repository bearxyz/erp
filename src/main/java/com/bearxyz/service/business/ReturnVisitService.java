package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.ReturnVisit;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.ReturnVisitRepository;
import com.bearxyz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created by bearxyz on 2017/8/28.
 */

@Service
@Transactional
public class ReturnVisitService {

    @Autowired
    private ReturnVisitRepository repository;

    @Autowired
    private UserRepository userRepository;

    public void save(ReturnVisit visit){
        repository.save(visit);
    }

    public DataTable<ReturnVisit> getReturnVisits(String companyId, String uid, PaginationCriteria req){
        DataTable<ReturnVisit> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<ReturnVisit> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            if(!StringUtils.isEmpty(companyId))
                predicate.getExpressions().add(cb.equal(root.get("companyId"), companyId));
            return predicate;
        };
        Page<ReturnVisit> page = repository.findAll(specification, request);
        List<ReturnVisit> content = page.getContent();
        for(ReturnVisit record: content){
            User user = userRepository.findOne(record.getCreatedBy());
            record.setOperator(user.getFirstName()+user.getLastName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

}
