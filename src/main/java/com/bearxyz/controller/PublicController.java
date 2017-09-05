package com.bearxyz.controller;

import com.bearxyz.domain.po.business.Picture;
import com.bearxyz.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by bearxyz on 2017/9/5.
 */
@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PictureRepository pictureRepository;

    @RequestMapping("/showImg/{id}")
    public void showImg(@PathVariable("id")String id, final HttpServletResponse response) throws IOException {
        Picture picture = pictureRepository.findOne(id);
        response.setContentType("image/jpeg");
        response.setCharacterEncoding("UTF-8");
        OutputStream outputSream = response.getOutputStream();
        InputStream in = new ByteArrayInputStream(picture.getFileContent());
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf, 0, 1024)) != -1) {
            outputSream.write(buf, 0, len);
        }
        outputSream.close();
    }

}
