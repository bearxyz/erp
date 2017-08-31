package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.CommunicationRecord;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CommunicationRecordRepository;
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

@Transactional
@Service
public class CommunicationRecordService {

    @Autowired
    private CommunicationRecordRepository repository;
    @Autowired
    private UserRepository userRepository;

    public void save(CommunicationRecord record){
        repository.save(record);
    }

    public DataTable<CommunicationRecord> getCommunicationRecords(String companyId, String uid, PaginationCriteria req){
        DataTable<CommunicationRecord> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<CommunicationRecord> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            if(!StringUtils.isEmpty(companyId))
                predicate.getExpressions().add(cb.equal(root.get("companyId"), companyId));
            return predicate;
        };
        Page<CommunicationRecord> page = repository.findAll(specification, request);
        List<CommunicationRecord> content = page.getContent();
        for(CommunicationRecord record: content){
            User user = userRepository.findOne(record.getCreatedBy());
            record.setOperator(user.getFirstName()+user.getLastName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

}
