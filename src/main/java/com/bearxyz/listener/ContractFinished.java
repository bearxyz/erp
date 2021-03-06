package com.bearxyz.listener;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.ForUseRepository;
import com.bearxyz.repository.StockRepository;
import com.bearxyz.utility.OrderUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private ForUseRepository forUseRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Contract contract = contractRepository.findOne(execution.getProcessBusinessKey());
        Company company = companyRepository.findOne(contract.getCompanyId());
        contract.setApproved(true);
        contract.setInvalid(false);
        company.setSigned(true);
        company.setBalance(company.getBalance()+contract.getPrestore());
        if(contract.getItems()!=null&&contract.getItems().size()>0) {
            ForUse forUse=new ForUse();
            forUse.setType("PRESENT");
            forUse.setApproved(true);
            Stock stock = new Stock();
            stock.setType("STOCK-OUT");
            stock.setMask("STOCK_OUT_OTHER");
            stock.setCode(OrderUtils.genSerialnumber("CK"));
            stock.setPurpose("合同赠品");
            stock.setApproved(true);
            stock.setDeliverAddress(contract.getSignProvince()+contract.getSignCity()+contract.getSignDistrict()+contract.getSignAddress());
            for (Present item : contract.getItems()) {
                StockItem it = new StockItem();
                it.setGoodsId(item.getGoodsId());
                it.setUnit(item.getUnit());
                it.setPrice((float) 0.0);
                it.setSpec(item.getSpec());
                it.setAmmount(item.getAmmount());
                it.setApproved(true);
                it.setCount(item.getCount());
                stock.getItems().add(it);
                ForUseItem fit = new ForUseItem();
                fit.setGoodsId(item.getGoodsId());
                fit.setUnit(item.getUnit());
                fit.setSpec(item.getSpec());
                fit.setAmmount(item.getAmmount());
                fit.setCount(item.getCount());
                forUse.getItems().add(fit);
            }
            stockRepository.save(stock);
            forUseRepository.save(forUse);
        }

        contractRepository.save(contract);
        companyRepository.saveAndFlush(company);
    }
}
