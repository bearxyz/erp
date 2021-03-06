package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by bearxyz on 2017/8/30.
 */
@Entity
@Table(name = "T_SALE")
public class Sale extends BaseDomain {

    private static final long serialVersionUID = -7147062502551641984L;

    @Column(length = 50)
    private String category = "";
    @Column(length = 50)
    private String code = "";
    @Column(length = 50)
    private String name = "";
    @Column(length = 50)
    private String project = "";
    @Column
    private Float price = (float) 0.0;
    @Column
    private String unit = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 50)
    private String subtype = "";
    @Column(length = 50)
    private String processInstanceId = "";
    @Column
    private Boolean isPublic = true;
    @Column
    private Boolean canUseCoupon = false;
    @Column(length = 36)
    private String goodsId = "";
    @Column
    private Integer count = 0;
    @Column
    private Integer ammount = 0;
    @Column(length = 36)
    private String packageId = "";
    @Column
    private Integer days = 0;

    @Column
    private java.sql.Date startDate;
    @Column
    private java.sql.Date endDate;
    @Column
    private Integer buyer = 0;
    @Column
    private Boolean approved = false;
    @Column
    private Boolean onSale = false;
    @Column
    private Boolean agentOnSale = false;
    @Column(length = 1000)
    private String memo = "";
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "CLOB")
    private String content;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_ID")
    private List<Picture> images = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BUSINESS_ID")
    private Set<SaleAttachment> resources = new HashSet<>();

    @Transient
    private List<GroupBuy> groupBuys;
    @Transient
    private String companyId;
    @Transient
    private Float agentPrice;
    @Transient
    private String taskId;
    @Transient
    private String taskName;
    @Transient
    private String applyer;
    @Transient
    private Date finishedDate;
    @Transient
    private String projectName = "";
    @Transient
    private String typeName = "";
    @Transient
    private String subtypeName="";
    @Transient
    private String categoryName = "";
    @Transient
    private String stock = "";
    @Transient
    private String statusName="";

    @Transient
    private Goods goods;
    @Transient
    private Package aPackage;
    @Transient
    private String saleId;
    @Transient
    private int saleStatus=0;
    @Transient
    private float salePrice=(float) 0.0;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Picture> getImages() {
        return images;
    }

    public void setImages(List<Picture> images) {
        this.images = images;
    }


    public Integer getBuyer() {
        return buyer;
    }

    public void setBuyer(Integer buyer) {
        this.buyer = buyer;
    }

    public Set<SaleAttachment> getResources() {
        return resources;
    }

    public void setResources(Set<SaleAttachment> resources) {
        this.resources = resources;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Float getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(Float agentPrice) {
        this.agentPrice = agentPrice;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public java.sql.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    public java.sql.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.sql.Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getAgentOnSale() {
        return agentOnSale;
    }

    public void setAgentOnSale(Boolean agentOnSale) {
        this.agentOnSale = agentOnSale;
    }

    public Boolean getCanUseCoupon() {
        return canUseCoupon;
    }

    public void setCanUseCoupon(Boolean canUseCoupon) {
        this.canUseCoupon = canUseCoupon;
    }

    public List<GroupBuy> getGroupBuys() {
        return groupBuys;
    }

    public void setGroupBuys(List<GroupBuy> groupBuys) {
        this.groupBuys = groupBuys;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getAmmount() {
        return ammount;
    }

    public void setAmmount(Integer ammount) {
        this.ammount = ammount;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }

    public float getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleId() {
        return saleId;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public int getSaleStatus() {
        return saleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        this.saleStatus = saleStatus;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public String getSubtypeName() {
        return subtypeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }
}
