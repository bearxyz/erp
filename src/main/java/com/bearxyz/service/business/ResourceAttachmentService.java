package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Attachment;
import com.bearxyz.domain.po.business.ResourceAttachment;
import com.bearxyz.repository.AttachmentRepository;
import com.bearxyz.repository.ResourceAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bearxyz on 2017/8/30.
 */

@Service
@Transactional
public class ResourceAttachmentService {

    @Autowired
    private ResourceAttachmentRepository repository;

    public ResourceAttachment getById(String id){
        return repository.getOne(id);
    }

    public void delete(String id){
        repository.delete(id);
    }

    public ResourceAttachment save(MultipartFile file) throws IOException {
        ResourceAttachment attachment = null;
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            String fileName = originalFileName.substring(originalFileName.lastIndexOf('\\') + 1, originalFileName.indexOf('.'));
            String suffix = originalFileName.substring(originalFileName.indexOf('.') + 1, originalFileName.length());
            byte[] bytes = file.getBytes();
            attachment = new ResourceAttachment();
            attachment.setFileContent(bytes);
            attachment.setName(fileName);
            attachment.setFileType(suffix);
            attachment.setFileSize(file.getSize());
            repository.save(attachment);
        }
        return attachment;
    }

}
