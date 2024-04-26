package com.Lu.service.impl;
import com.Lu.mapper.CategoryMapper;
import com.Lu.mapper.CommonMapper;
import com.Lu.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    CommonMapper commonMapper;
    @Override
    public void upload(String path) {
        log.info("commonservice成功调用");
        commonMapper.upload(path);

        //进行数据库操作
    }
}
