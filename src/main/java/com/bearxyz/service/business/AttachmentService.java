package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Attachment;
import com.bearxyz.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bearxyz on 2017/8/29.
 */
@Service
@Transactional
public class AttachmentService {

    @Autowired
    private AttachmentRepository repository;

    public Attachment getById(String id){
        return repository.getOne(id);
    }

    public void delete(String id){
        repository.delete(id);
    }

    public Attachment save(MultipartFile file) throws IOException {
        Attachment attachment = null;
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(originalFileName.lastIndexOf('\\') + 1, originalFileName.indexOf('.'));
            String suffix = originalFileName.substring(originalFileName.indexOf('.') + 1, originalFileName.length());
            byte[] bytes = file.getBytes();
            attachment = new Attachment();
            attachment.setFileContent(bytes);
            attachment.setName(fileName);
            attachment.setFileType(suffix);
            attachment.setFileSize(file.getSize());
            repository.save(attachment);
        }
        return attachment;
    }

}
