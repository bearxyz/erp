package com.bearxyz.domain.po.business;

import com.bearxyz.domain.po.BaseDomain;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by bearxyz on 2017/7/27.
 */
@Entity
@Table(name = "T_PERSON")
public class Person extends BaseDomain {

    private static final long serialVersionUID = 6005979111305892883L;

    @Column(length = 50)
    private String name = "";
    @Column(length = 50)
    private String type = "";
    @Column(length = 50)
    private String project = "";
    @Column(length = 10)
    private String gender = "";
    @Column(length = 50)
    private String nation = "";
    @Column
    private Date birthday;
    @Column(length = 100)
    private String birthPlace = "";
    @Column(length = 50)
    private String nativePlace = "";
    @Column(length = 50)
    private String post = "";
    @Column(length = 50)
    private String position = "";
    @Column
    private Date hireDate;
    @Column(length = 50)
    private String character = "";
    @Column(length = 50)
    private String temperament = "";
    @Column(length = 50)
    private String maritalStatus = "";
    @Column(length = 100)
    private String interest = "";
    @Column(length = 100)
    private String honouraryOffice = "";
    @Column(length = 50)
    private String qualifications = "";
    @Column(length = 50)
    private String major = "";
    @Column(length = 1000)
    private String experience = "";
    @Column(length = 1000)
    private String background = "";
    @Column(length = 50)
    private String performance = "";
    @Column(length = 50)
    private String modeOfThinking = "";
    @Column(length = 100)
    private String hobby = "";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getHonouraryOffice() {
        return honouraryOffice;
    }

    public void setHonouraryOffice(String honouraryOffice) {
        this.honouraryOffice = honouraryOffice;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getModeOfThinking() {
        return modeOfThinking;
    }

    public void setModeOfThinking(String modeOfThinking) {
        this.modeOfThinking = modeOfThinking;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
