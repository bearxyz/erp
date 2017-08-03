package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.PersonRepository;
import com.bearxyz.repository.TrackRepository;
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
 * Created by bearxyz on 2017/7/27.
 */
@Transactional
@Service
public class ClientService {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TrackRepository trackRepository;


    public Company saveCompany(Company company){
        return  companyRepository.saveAndFlush(company);
    }

    public Company getCompanyById(String id){
        return companyRepository.findOne(id);
    }

    public DataTable<Company> getCompanyByConditions(PaginationCriteria req){
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if(req.getOrder()!=null&&req.getOrder().get(0)!=null&&req.getOrder().get(0).getColumn()>0){
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart()/req.getLength(),req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Company> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.like(root.get("title"),"%"+StringUtils.trimAllWhitespace("")+"%"));
            if(!StringUtils.isEmpty(""))
                predicate.getExpressions().add(cb.equal(root.get("mask"), ""));
            return predicate;
        };
        Page<Company> page = companyRepository.findAll(specification, request);
        List<Company> content = page.getContent();
        DataTable<Company> companies = new DataTable<>();
        companies.setRecordsTotal(page.getTotalElements());
        companies.setRecordsFiltered(page.getTotalElements());
        companies.setData(content);
        return companies;
    }

}
