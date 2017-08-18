package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Notice;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.NoticeVO;
import com.bearxyz.repository.DictRepository;
import com.bearxyz.repository.NoticeRepository;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.utility.RelativeDateFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/6/23.
 */

@Transactional
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository repository;

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private UserRepository userRepository;

    public Notice save(Notice notice){
        return repository.save(notice);
    }

    public void deleteById(String id){
        repository.deleteById(id);
    }

    public void delete(String[] ids){
        for(String id: ids){
            deleteById(id);
        }
    }

    public Notice getById(String id){
        Notice notice = repository.findById(id);
        notice.setDatetime(RelativeDateFormat.format(notice.getLastUpdated()));
        return notice;
    }

    public List<Notice> getTopAndType(int top, String type){
        List<Notice> notices = repository.findAllByTopAndType(top, type);
        for(Notice notice:notices){
            notice.setDatetime(RelativeDateFormat.format(notice.getLastUpdated()));
        }
        return notices;
    }

    public DataTable<NoticeVO> getByConditions(PaginationCriteria req){
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Notice> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.like(root.get("title"),"%"+StringUtils.trimAllWhitespace("")+"%"));
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.equal(root.get("mask"), ""));
            return predicate;
        };
        Page<Notice> page = repository.findAll(specification, request);
        List<Notice> content = page.getContent();
        List<NoticeVO> vo = new ArrayList<>();
        for(Notice notice: content){
            NoticeVO v = new NoticeVO();
            BeanUtils.copyProperties(notice, v);
            Dict dict = dictRepository.findByMask(notice.getType());
            User createBy = userRepository.getOne(notice.getCreatedBy());
            User lastModifiedBy = userRepository.getOne(notice.getLastModifiedBy());
            v.setType(dict.getName());
            v.setCreatedBy(createBy.getFirstName()+createBy.getLastName());
            v.setLastModifiedBy(lastModifiedBy.getFirstName()+createBy.getLastName());
            vo.add(v);
        }
        DataTable<NoticeVO> notices = new DataTable<>();
        notices.setRecordsTotal(page.getTotalElements());
        notices.setRecordsFiltered(page.getTotalElements());
        notices.setData(vo);
        return notices;
    }

}
