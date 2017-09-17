package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.business.SupportApply;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.repository.SupportApplyRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.service.workflow.WorkflowService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/9/4.
 */
@Service
@Transactional
public class SupportApplyService {

    @Autowired
    private SupportApplyRepository repository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public SupportApply getById(String id) {
        return repository.getOne(id);
    }

    public void apply(SupportApply supportApply) {
        save(supportApply);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "扶持申请");
        variables.put("url", "/supportapply/");
        variables.put("bid", supportApply.getId());
        variables.put("applyer", supportApply.getCreatedBy());
        variables.put("applyUserId", supportApply.getCreatedBy());
        supportApply.setProcessInstanceId(workflowService.startWorkflow("support-apply", supportApply.getId(), supportApply.getCreatedBy(), variables));
    }

    public void save(SupportApply supportApply) {
        repository.save(supportApply);
    }

    public DataTable<SupportApply> getApplyByConditions(String uid, String companyId) {
        Sort sort = new Sort(Sort.Direction.fromString("desc"), "lastUpdated");
        Specification<SupportApply> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (!StringUtils.isEmpty(uid))
                predicate.getExpressions().add(cb.equal(root.get("createdBy"), uid));
            if (companyId != null)
                predicate.getExpressions().add(cb.equal(root.get("companyId"), companyId));
            return predicate;
        };
        List<SupportApply> content = repository.findAll(specification, sort);
        for (SupportApply sa : content) {
            Sale sale = saleRepository.findOne(sa.getSaleId());
            sa.setSale(sale);

            Task task = workflowService.getTaskByBussinessId(sa.getId());
            if (task != null) {
                sa.setTaskId(task.getId());
                sa.setTaskName(task.getName());
                sa.setFinishedDate(task.getDueDate());
            } else {
                HistoricProcessInstance historicTaskInstance = workflowService.getHistoryProcessByBussinessId(sa.getId());
                if (historicTaskInstance != null) {
                    if (workflowService.getHistoryVarByProcessId(historicTaskInstance.getId(), "deptLeaderPass") != null && (boolean) workflowService.getHistoryVarByProcessId(historicTaskInstance.getId(), "deptLeaderPass"))
                        sa.setTaskName("已完成");
                    else
                        sa.setTaskName("已取消");
                    sa.setTaskId(historicTaskInstance.getId());
                    sa.setFinishedDate(historicTaskInstance.getEndTime());
                }

            }
            Company company = companyRepository.findOne(sa.getCompanyId());
            sa.setApplyer(company.getName());
        }
        DataTable<SupportApply> companies = new DataTable<>();
        companies.setRecordsTotal((long) content.size());
        companies.setRecordsFiltered((long) content.size());
        companies.setData(content);
        return companies;
    }

}
