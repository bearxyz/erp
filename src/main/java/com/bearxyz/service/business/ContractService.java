package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.Attachment;
import com.bearxyz.domain.po.business.Company;
import com.bearxyz.domain.po.business.Contract;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.repository.CompanyRepository;
import com.bearxyz.repository.ContractRepository;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.service.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/7/27.
 */

@Transactional
@Service
public class ContractService {

    @Autowired
    private ContractRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private SysService sysService;

    public void apply(Contract contract, List<MultipartFile> files) throws IOException {
        save(contract, files);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "合同审核");
        variables.put("url", "/contract/");
        variables.put("bid", contract.getId());
        variables.put("applyer", contract.getCreatedBy());
        variables.put("applyUserId", contract.getCreatedBy());
        contract.setProcessInstanceId(workflowService.startWorkflow("contract-audit", contract.getId(), contract.getCreatedBy(), variables));
    }

    public void save(Contract contract, List<MultipartFile> files) throws IOException {
        if(contract.getAttachments()!=null) {
            for (Attachment attachment : contract.getAttachments()) {
                attachmentService.delete(attachment.getId());
            }
        }
        for (MultipartFile file : files) {
            Attachment attachment = attachmentService.save(file);
            contract.getAttachments().add(attachment);
        }
        repository.save(contract);
    }

    public DataTable<Contract> getContacts(String companyId, PaginationCriteria req) {
        DataTable<Contract> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Contract> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (!StringUtils.isEmpty(companyId))
                predicate.getExpressions().add(cb.equal(root.get("companyId"), companyId));
            return predicate;
        };
        Page<Contract> page = repository.findAll(specification, request);
        List<Contract> content = page.getContent();
        for (Contract record : content) {
            Company company = companyRepository.findOne(record.getCompanyId());
            record.setCompanyName(company.getName());
            User user = userRepository.findOne(record.getCreatedBy());
            record.setOperator(user.getFirstName() + user.getLastName());
            Dict dict = sysService.getDictByMask(record.getProject());
            if (dict != null)
                record.setProjectName(dict.getName());
            dict = sysService.getDictByMask(record.getAgentLevel());
            if (dict != null)
                record.setAgentLevelName(dict.getName());
            dict = sysService.getDictByMask(record.getAgentType());
            if (dict != null)
                record.setAgentTypeName(dict.getName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    public Contract getById(String id) {
        return repository.findOne(id);
    }

}
