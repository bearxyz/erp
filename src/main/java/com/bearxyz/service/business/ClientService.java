package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.*;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DictRepository dictRepository;
    @Autowired
    private ContractRepository contractRepository;


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

    public Float getClientBenefit(String userId){
        Float discount = (float)1;

        User user = userRepository.findOne(userId);
        Company company = companyRepository.findOne(user.getCompanyId());
        Contract contract = contractRepository.findNewestContractByCompanyId(company.getId());
        if(contract!=null) {
            Dict dict = dictRepository.findByMask(contract.getAgentLevel());
            if(dict!=null){
                discount=dict.getDiscount();
            }
        }
        return discount;
    }

}
