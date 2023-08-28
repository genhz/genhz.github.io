package com.sky.web.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common/upload")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    //文件上传的话:参数类型是固定的
    @PostMapping
    public Result upload(MultipartFile file) throws IOException {
        String filePath = aliOssUtil.upload(file.getOriginalFilename(), file.getInputStream());
        log.info("文件上传之后的访问路径是:"+filePath);
        return Result.success(filePath);
    }
}
