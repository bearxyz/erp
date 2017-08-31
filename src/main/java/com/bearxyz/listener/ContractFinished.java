package com.bearxyz.listener;

import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by bearxyz on 2017/8/29.
 */
@Transactional
@Component
public class ContractFinished implements ExecutionListener {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Contract contract = contractRepository.findOne(execution.getProcessBusinessKey());
        Company company = companyRepository.findOne(contract.getCompanyId());
        contract.setApproved(true);
        company.setSigned(true);
        contractRepository.save(contract);
        companyRepository.saveAndFlush(company);
    }
}
