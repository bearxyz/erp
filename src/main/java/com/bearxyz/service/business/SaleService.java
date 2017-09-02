package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.SaleItemRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.service.sys.SysService;
import com.bearxyz.service.workflow.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Service
@Transactional
public class SaleService {

    @Autowired
    private SaleRepository repository;
    @Autowired
    private SaleItemRepository itemRepository;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private SysService sysService;
    @Autowired
    private WorkflowService workflowService;

    public List<SaleItem> getItemsBySaleId(String id){
        Sale sale = repository.findOne(id);
        return sale.getItems();
    }

    public void apply(Sale sale, List<MultipartFile> files) throws IOException {
        save(sale, files);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "商品包装审批");
        variables.put("url", "/sale/");
        variables.put("bid", sale.getId());
        variables.put("applyer", sale.getCreatedBy());
        variables.put("applyUserId", sale.getCreatedBy());
        sale.setProcessInstanceId(workflowService.startWorkflow("product-sale", sale.getId(), sale.getCreatedBy(), variables));
    }

    public void save(Sale sale, List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            Picture attachment = pictureService.save(file);
            sale.getImages().add(attachment);
        }
        repository.save(sale);
    }

    public DataTable<Sale> getSales(Boolean approved, PaginationCriteria req){
        DataTable<Sale> result = new DataTable<>();
        String order = "lastUpdated";
        String direction = "desc";
        req.getOrder().get(0).getDir();
        if (req.getOrder() != null && req.getOrder().get(0) != null && req.getOrder().get(0).getColumn() > 0) {
            direction = req.getOrder().get(0).getDir();
            order = req.getColumns().get(req.getOrder().get(0).getColumn()).getData();
        }
        PageRequest request = new PageRequest(req.getStart() / req.getLength(), req.getLength(), new Sort(Sort.Direction.fromString(direction), order));
        Specification<Sale> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved!=null)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            return predicate;
        };
        Page<Sale> page = repository.findAll(specification, request);
        List<Sale> content = page.getContent();
        for(Sale sale: content){
            Dict dict=sysService.getDictByMask(sale.getProject());
            if(dict!=null)
                sale.setProjectName(dict.getName());
            dict = sysService.getDictByMask(sale.getType());
            if(dict!=null)
                sale.setTypeName(dict.getName());
        }
        result.setRecordsTotal(page.getTotalElements());
        result.setRecordsFiltered(page.getTotalElements());
        result.setData(content);
        return result;
    }

    public Sale getById(String id){
        return repository.findOne(id);
    }

    public void setStatus(String id){
        Sale sale = getById(id);
        sale.setOnSale(!sale.getOnSale());
        repository.save(sale);
    }

}
