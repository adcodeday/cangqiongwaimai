package com.Lu.controller.admin;

import com.Lu.properties.FileOperationProperties;
import com.Lu.result.Result;
import com.Lu.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用相关接口")
@Slf4j
public class CommonController {
    @Autowired
    private FileOperationProperties fileOperationProperties;
    @Autowired
    private CommonService commonService;
    @PostMapping("/upload")
    @ApiOperation("文件上传接口")
    @Transactional
    public Result<String> upload(MultipartFile file) throws IOException {

        log.info("文件开始上传：{}",file);
        String[] split = file.getOriginalFilename().split("\\.");
        String extension=split[1];
        String fullName= UUID.randomUUID().toString()+"."+extension;
        fullName=fullName.replace("-","");
        String path=fileOperationProperties.defaultPath+fullName;
        //写入文件
        file.transferTo(new File(path));
        commonService.upload(path);
        //写入数据库
        return Result.success("img/图片存储/"+fullName);
    }
}
