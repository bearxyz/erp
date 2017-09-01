package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/7/27.
 */
@Entity
@Table(name = "T_COMPANY")
public class Company extends BaseDomain {

    private static final long serialVersionUID = -1264316047321649030L;

    @Column
    private Boolean isAssigned = false;
    @Column
    private Boolean autoAssigned = false;
    @Column(length = 50)
    private String comeFrom = "";
    @Column(length = 100)
    private String name = "";
    @Column(length = 50)
    private String province = "";
    @Column(length = 50)
    private String city = "";
    @Column(length = 50)
    private String district = "";
    @Column(length = 100)
    private String address = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 50)
    private String contactor = "";
    @Column(length = 50)
    private String contactPhone = "";
    @Column(length = 50)
    private String managementModel = "";
    @Column
    private Integer numberOfTeacher = 0;
    @Column
    private Integer numberOfFulltime = 0;
    @Column
    private Integer numberOfParttime = 0;
    @Column
    private Integer numberOfOtherTeacher = 0;
    @Column
    private Integer numberOfOther = 0;
    @Column(length = 50)
    private String scale = "";
    @Column(length = 50)
    private String position = "";
    @Column(length = 50)
    private String planning = "";
    @Column(length = 50)
    private String license = "";

    @Column(length = 50)
    private String payment = "";
    @Column(length = 50)
    private String bank = "";
    @Column(length = 50)
    private String credit = "";
    @Column(length = 50)
    private String fund = "";
    @Column(length = 50)
    private String attitudeOfPayment = "";

    @Column
    private boolean relationship;
    @Column(length = 1000)
    private String relationReason = "";
    @Column(length = 1000)
    private String understandingLevel = "";
    @Column(length = 1000)
    private String attitude = "";
    @Column(length = 1000)
    private String longGoal = "";
    @Column(length = 1000)
    private String shortGoal = "";
    @Column(length = 1000)
    private String mostConcern = "";
    @Column(length = 1000)
    private String thinking = "";
    @Column(length = 1000)
    private String opinion = "";
    @Column(length = 1000)
    private String fond = "";
    @Column(length = 1000)
    private String keyPoint = "";
    @Column(length = 1000)
    private String competition = "";

    @Column(length = 50)
    private String consultProvince = "";
    @Column(length = 50)
    private String consultCity = "";
    @Column(length = 50)
    private String consultDistrict = "";
    @Column(length = 50)
    private String consultProject = "";

    @Column(length = 50)
    private String status = "";
    @Column(length = 36)
    private String salerId = "";
    @Column(length = 36)
    private String serviceId = "";

    @Column
    private boolean signed = false;

    @Transient
    private boolean hasAccount = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "company")
    private List<Person> persons = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getManagementModel() {
        return managementModel;
    }

    public void setManagementModel(String managementModel) {
        this.managementModel = managementModel;
    }

    public Integer getNumberOfTeacher() {
        return numberOfTeacher;
    }

    public void setNumberOfTeacher(Integer numberOfTeacher) {
        this.numberOfTeacher = numberOfTeacher;
    }

    public Integer getNumberOfFulltime() {
        return numberOfFulltime;
    }

    public void setNumberOfFulltime(Integer numberOfFulltime) {
        this.numberOfFulltime = numberOfFulltime;
    }

    public Integer getNumberOfParttime() {
        return numberOfParttime;
    }

    public void setNumberOfParttime(Integer numberOfParttime) {
        this.numberOfParttime = numberOfParttime;
    }

    public Integer getNumberOfOtherTeacher() {
        return numberOfOtherTeacher;
    }

    public void setNumberOfOtherTeacher(Integer numberOfOtherTeacher) {
        this.numberOfOtherTeacher = numberOfOtherTeacher;
    }

    public Integer getNumberOfOther() {
        return numberOfOther;
    }

    public void setNumberOfOther(Integer numberOfOther) {
        this.numberOfOther = numberOfOther;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPlanning() {
        return planning;
    }

    public void setPlanning(String planning) {
        this.planning = planning;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getAttitudeOfPayment() {
        return attitudeOfPayment;
    }

    public void setAttitudeOfPayment(String attitudeOfPayment) {
        this.attitudeOfPayment = attitudeOfPayment;
    }

    public boolean isRelationship() {
        return relationship;
    }

    public void setRelationship(boolean relationship) {
        this.relationship = relationship;
    }

    public String getRelationReason() {
        return relationReason;
    }

    public void setRelationReason(String relationReason) {
        this.relationReason = relationReason;
    }

    public String getUnderstandingLevel() {
        return understandingLevel;
    }

    public void setUnderstandingLevel(String understandingLevel) {
        this.understandingLevel = understandingLevel;
    }

    public String getAttitude() {
        return attitude;
    }

    public void setAttitude(String attitude) {
        this.attitude = attitude;
    }

    public String getLongGoal() {
        return longGoal;
    }

    public void setLongGoal(String longGoal) {
        this.longGoal = longGoal;
    }

    public String getShortGoal() {
        return shortGoal;
    }

    public void setShortGoal(String shortGoal) {
        this.shortGoal = shortGoal;
    }

    public String getMostConcern() {
        return mostConcern;
    }

    public void setMostConcern(String mostConcern) {
        this.mostConcern = mostConcern;
    }

    public String getThinking() {
        return thinking;
    }

    public void setThinking(String thinking) {
        this.thinking = thinking;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getFond() {
        return fond;
    }

    public void setFond(String fond) {
        this.fond = fond;
    }

    public String getKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(String keyPoint) {
        this.keyPoint = keyPoint;
    }

    public String getCompetition() {
        return competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getSalerId() {
        return salerId;
    }

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getConsultProvince() {
        return consultProvince;
    }

    public void setConsultProvince(String consultProvince) {
        this.consultProvince = consultProvince;
    }

    public String getConsultCity() {
        return consultCity;
    }

    public void setConsultCity(String consultCity) {
        this.consultCity = consultCity;
    }

    public String getConsultDistrict() {
        return consultDistrict;
    }

    public void setConsultDistrict(String consultDistrict) {
        this.consultDistrict = consultDistrict;
    }

    public String getConsultProject() {
        return consultProject;
    }

    public void setConsultProject(String consultProject) {
        this.consultProject = consultProject;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Boolean getAutoAssigned() {
        return autoAssigned;
    }

    public void setAutoAssigned(Boolean autoAssigned) {
        this.autoAssigned = autoAssigned;
    }

    public Boolean getAssigned() {
        return isAssigned;
    }

    public void setAssigned(Boolean assigned) {
        isAssigned = assigned;
    }

    public boolean isHasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }
}
