package com.oriole.ocean.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oriole.ocean.common.po.mysql.FileUploadTempEntity;
import com.oriole.ocean.dao.FileUploadTempDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FileUploadTempService extends ServiceImpl<FileUploadTempDao, FileUploadTempEntity> {
    //新增或变更临时文档信息
    public void saveOrUpdateUploadTempFileInfo(FileUploadTempEntity fileUploadTempEntity) {
        saveOrUpdate(fileUploadTempEntity);
    }

    //文档上传临时文件信息查询
    public FileUploadTempEntity getUploadTempFileInfo(Integer uploadID, String username) {
        QueryWrapper<FileUploadTempEntity> queryWrapper = new QueryWrapper<>();
        if (username != null) {
            queryWrapper.eq("upload_username", username);
        }
        queryWrapper.eq("upload_id", uploadID);
        return getOne(queryWrapper);
    }

    //文档上传临时文件信息批量查询
    public Page<FileUploadTempEntity> getAllUploadTempFileInfoByUsername(String username, Page<FileUploadTempEntity> page) {
        QueryWrapper<FileUploadTempEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upload_username", username);
        return page(page, queryWrapper);
    }
}
