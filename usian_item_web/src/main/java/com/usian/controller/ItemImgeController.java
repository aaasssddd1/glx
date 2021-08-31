package com.usian.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("file")
public class ItemImgeController {
    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping("upload")
    public Result upload(MultipartFile file){
        try {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
            StorePath storePath = this.storageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), suffix, null);
            String path="http://image.usian.com/"+storePath.getFullPath();
            System.out.println("上次路径："+path);
            return Result.ok(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok();
    }
}
