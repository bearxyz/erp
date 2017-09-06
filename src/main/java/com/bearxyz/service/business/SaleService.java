package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.common.PaginationCriteria;
import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.business.Package;
import com.bearxyz.domain.po.sys.Dict;
import com.bearxyz.repository.*;
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
    private SaleAttachmentService saleAttachmentService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private SysService sysService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private ConfigRepository configRepository;

    public List<SaleItem> getItemsBySaleId(String id) {
        Sale sale = repository.findOne(id);
        return sale.getItems();
    }

    public void apply(Sale sale, List<MultipartFile> pics, List<MultipartFile> files) throws IOException {
        save(sale, pics, files);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("name", "商品包装审批");
        variables.put("url", "/sale/");
        variables.put("bid", sale.getId());
        variables.put("applyer", sale.getCreatedBy());
        variables.put("applyUserId", sale.getCreatedBy());
        sale.setProcessInstanceId(workflowService.startWorkflow("product-sale", sale.getId(), sale.getCreatedBy(), variables));
    }

    public void save(Sale sale, List<MultipartFile> pics, List<MultipartFile> files) throws IOException {
        if (pics != null && pics.size() > 0) {
            if (sale.getImages() != null) {
                for (Picture pic : sale.getImages()) {
                    pictureService.delete(pic.getId());
                }
            }
            for (MultipartFile file : pics) {
                Picture attachment = pictureService.save(file);
                if (attachment != null)
                    sale.getImages().add(attachment);
            }
        }
        if (files != null && files.size() > 0) {
            if (sale.getResources() != null) {
                for (SaleAttachment attachment : sale.getResources()) {
                    saleAttachmentService.delete(attachment.getId());
                }
            }
            for (MultipartFile file : files) {
                SaleAttachment attachment = saleAttachmentService.save(file);
                if (attachment != null)
                    sale.getResources().add(attachment);
            }
        }
        for (SaleItem item : sale.getItems()) {
            Goods goods = goodsRepository.findOne(item.getGoodsId());
            if (item.getPackageId() != null && !item.getPackageId().isEmpty()) {
                Package pkg = packageRepository.findOne(item.getPackageId());
                item.setSpec(goods.getModel());
                item.setUnit(pkg.getPackageUnit());
                item.setAmmount(pkg.getAmmount() * item.getCount());
            } else {
                item.setSpec(goods.getModel());
                item.setUnit(goods.getUnit());
                item.setAmmount(item.getCount());
            }
        }
        repository.save(sale);
    }

    public DataTable<Sale> getSales(Boolean approved, String category) {
        Config config = configRepository.findAll().get(0);
        DataTable<Sale> result = new DataTable<>();
        Specification<Sale> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (approved != null)
                predicate.getExpressions().add(cb.equal(root.get("approved"), approved));
            if (category != null)
                predicate.getExpressions().add(cb.equal(root.get("category"), category));
            return predicate;
        };
        List<Sale> page = repository.findAll(specification);
        List<Sale> content = page;
        for (Sale sale : content) {
            String stock = "正常";
            Dict dict = sysService.getDictByMask(sale.getProject());
            if (dict != null)
                sale.setProjectName(dict.getName());
            dict = sysService.getDictByMask(sale.getType());
            if (dict != null)
                sale.setTypeName(dict.getName());
            dict = sysService.getDictByMask(sale.getCategory());
            if (dict != null)
                sale.setCategoryName(dict.getName());
            for (SaleItem item : sale.getItems()) {
                Goods goods = goodsRepository.findOne(item.getGoodsId());
                if (goods.getStock() <= config.getStockAlert())
                    stock = "危险";
            }
            sale.setStock(stock);
        }

        result.setRecordsTotal((long) page.size());
        result.setRecordsFiltered((long) page.size());
        result.setData(content);
        return result;
    }

    public Sale getById(String id) {
        return repository.findOne(id);
    }

    public void setStatus(String id) {
        Sale sale = getById(id);
        sale.setOnSale(!sale.getOnSale());
        repository.save(sale);
    }

    public List<Sale> getSaleList(){
        return repository.findAll();
    }

    public List<Sale> getSaleByTypeList(String companyId,String category){
        if(category !=null && (!category.equals(""))){
            return repository.findSalesByCompanyIdAndCategory(companyId,category);
        }else{
            return repository.findAll();
        }
    }

}
