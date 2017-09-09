package com.bearxyz.controller;

import com.bearxyz.domain.po.business.Picture;
import com.bearxyz.domain.po.business.SaleAttachment;
import com.bearxyz.repository.PictureRepository;
import com.bearxyz.repository.SaleAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by bearxyz on 2017/9/5.
 */
@Controller
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private SaleAttachmentRepository saleAttachmentRepository;

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

    @RequestMapping(value = "/download/{id}")
    public void downloadResource(@PathVariable("id")String id, final HttpServletResponse response) throws IOException {
        SaleAttachment attachment = saleAttachmentRepository.findOne(id);
        String fileName = attachment.getName()+"."+attachment.getFileType();
        fileName = URLEncoder.encode(fileName, "UTF-8");
        byte[] data = attachment.getFileContent();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = "/downloadImg/{id}")
    public void downloadImg(@PathVariable("id")String id, final HttpServletResponse response) throws IOException {
        Picture attachment = pictureRepository.findOne(id);
        String fileName = attachment.getName()+"."+attachment.getFileType();
        fileName = URLEncoder.encode(fileName, "UTF-8");
        byte[] data = attachment.getFileContent();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

}
