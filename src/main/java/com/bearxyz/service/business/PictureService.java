package com.bearxyz.service.business;

import com.bearxyz.domain.po.business.Picture;
import com.bearxyz.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by bearxyz on 2017/8/30.
 */

@Service
@Transactional
public class PictureService {

    @Autowired
    private PictureRepository repository;

    public Picture getById(String id){
        return repository.getOne(id);
    }

    public void delete(String id){
        repository.delete(id);
    }

    public Picture save(MultipartFile file) throws IOException {
        Picture picture = null;
        if (file != null) {
            String originalFileName = file.getOriginalFilename();
            if(originalFileName!=null&& !StringUtils.isEmpty(originalFileName)) {
                String fileName = originalFileName.substring(originalFileName.lastIndexOf('\\') + 1, originalFileName.indexOf('.'));
                String suffix = originalFileName.substring(originalFileName.indexOf('.') + 1, originalFileName.length());
                byte[] bytes = file.getBytes();
                picture = new Picture();
                picture.setFileContent(bytes);
                picture.setName(fileName);
                picture.setFileType(suffix);
                picture.setFileSize(file.getSize());
                repository.save(picture);
            }
        }
        return picture;
    }

}
