package com.bearxyz.controller.rest;

import com.bearxyz.domain.po.business.*;
import com.bearxyz.domain.po.sys.User;
import com.bearxyz.domain.vo.ClientResource;
import com.bearxyz.domain.vo.ClientResourceItem;
import com.bearxyz.repository.PictureRepository;
import com.bearxyz.repository.SaleRepository;
import com.bearxyz.repository.UserRepository;
import com.bearxyz.utility.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearxyz on 2017/9/3.
 */

@RestController
@RequestMapping("api")
public class ClientApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaleRepository saleRepository;

    @PostMapping("auth")
    public String auth(@RequestParam("username") String username, @RequestParam("password") String password) {
        String result = "fail";
        User user = userRepository.findByEmail(username);
        if (user != null || user.getEnabled() || !user.getAccountNonExpired() || !user.getAccountNonLocked()) {
            if(BCrypt.checkpw(password, user.getPassword()))
                result = "true";
        }
        return result;
    }

    @PostMapping("get")
    public List<ClientResource> getBuyed(@RequestParam("username") String username, @RequestParam("password") String password){
        List<Sale> sales = new ArrayList<>();
        User user = userRepository.findByEmail(username);
        if (user != null || user.getEnabled() || !user.getAccountNonExpired() || !user.getAccountNonLocked()) {
            if(BCrypt.checkpw(password, user.getPassword()))
            {
                //sales = saleRepository.findSalesByCompanyIdAndCategory(user.getCompanyId(),"GOODS_VIRTUAL");
                sales = saleRepository.findAllByCategory("GOODS_VIRTUAL");
            }
        }
        List<ClientResource> resources = new ArrayList<>();
        for(Sale s: sales){
            ClientResource cr = new ClientResource();
            cr.setId(s.getId());
            cr.setName(s.getName());
            if(s.getImages().size()>0)
                cr.setImageUrl("http://erp.xjdpx.com/public/showImg/"+s.getImages().get(0).getId());
            for(SaleAttachment att: s.getResources()){
                ClientResourceItem item = new ClientResourceItem();
                item.setId(att.getId());
                item.setFileName(att.getName());
                item.setFileType(att.getFileType());
                cr.getAttachments().add(item);
            }
            resources.add(cr);
        }
        return resources;
    }

}
