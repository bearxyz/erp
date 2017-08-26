package com.bearxyz.service.business;

import com.bearxyz.common.DataTable;
import com.bearxyz.domain.po.business.OfficialPartner;
import com.bearxyz.repository.OfficialPartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bearxyz on 2017/7/24.
 */
@Transactional
@Service
public class OfficialPartnerService {

    @Autowired
    private OfficialPartnerRepository repository;

    public OfficialPartner save(OfficialPartner officialPartner) {
        return repository.save(officialPartner);
    }

    public void deleteById(String id) {
        repository.delete(id);
    }

    public void delete(String[] ids) {
        for (String id : ids) {
            deleteById(id);
        }
    }

    public OfficialPartner getById(String id){
        return repository.findOne(id);
    }

    public List<OfficialPartner> getListByType(String type){
        return repository.findAllByType(type);
    }

    public DataTable<OfficialPartner> getByType(String type){
        List<OfficialPartner> page = repository.findAllByType(type);
        DataTable<OfficialPartner> partners = new DataTable<>();
        partners.setRecordsTotal((long)page.size());
        partners.setRecordsFiltered((long)page.size());
        partners.setData(page);
        return partners;
    }

}
