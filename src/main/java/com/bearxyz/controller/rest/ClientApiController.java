package com.bearxyz.controller.rest;

import com.bearxyz.domain.po.business.Sale;
import com.bearxyz.domain.po.sys.User;
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

    @GetMapping("get")
    public List<Sale> getBuyed(@RequestParam("username") String username, @RequestParam("password") String password){
        List<Sale> sales = new ArrayList<>();
        User user = userRepository.findByEmail(username);
        if (user != null || user.getEnabled() || !user.getAccountNonExpired() || !user.getAccountNonLocked()) {
            if(BCrypt.checkpw(password, user.getPassword()))
            {
                sales = saleRepository.findSalesByCompanyIdAndCategory(user.getCompanyId(),"GOODS_VIRTUAL");
            }
        }
        return sales;
    }

}
