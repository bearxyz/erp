package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.PersonRepository;
import com.bearxyz.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return  companyRepository.save(company);
    }

    public Company getCompanyById(String id){
        return companyRepository.findOne(id);
    }

    public DataTable<Company> getCompanyByConditions(String uid, Boolean signed){
        Specification<Company> specification = (root, query, cb)->{
            Predicate predicate = cb.conjunction();
            if(!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            if(signed!=null)
                predicate.getExpressions().add(cb.equal(root.get("signed"),signed));
            return predicate;
        };
        List<Company> content = companyRepository.findAll(specification);
        DataTable<Company> companies = new DataTable<>();
        companies.setRecordsTotal((long)content.size());
        companies.setRecordsFiltered((long)content.size());
        companies.setData(content);
        return companies;
    }

}
