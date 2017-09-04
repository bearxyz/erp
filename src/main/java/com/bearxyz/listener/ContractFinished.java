package com.bearxyz.listener;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.StockRepository;
import com.bearxyz.utility.OrderUtils;
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
    @Autowired
    private StockRepository stockRepository;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        Contract contract = contractRepository.findOne(execution.getProcessBusinessKey());
        Company company = companyRepository.findOne(contract.getCompanyId());
        contract.setApproved(true);
        company.setSigned(true);
        Stock stock = new Stock();
        stock.setType("STOCK-OUT");
        stock.setMask("STOCK_OUT_OTHER");
        stock.setCode(OrderUtils.genSerialnumber("CK"));
        stock.setPurpose("合同赠品");
        stock.setDeliverAddress(contract.getPresentAddress());
        for(Present item: contract.getItems()){
            StockItem it = new StockItem();
            it.setGoodsId(item.getGoodsId());
            it.setUnit(item.getUnit());
            it.setPrice((float)0.0);
            it.setAmmount(item.getAmmount());
            it.setCount(item.getCount());
            stock.getItems().add(it);
        }
        stockRepository.save(stock);
        contractRepository.save(contract);
        companyRepository.saveAndFlush(company);
    }
}
