package com.oriole.ocean.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oriole.ocean.common.po.mysql.FileEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface FileDao extends BaseMapper<FileEntity> {
    Page<FileEntity> getFileListByTypeIDAndTagIDAndIndexString(Page<FileEntity> page, Integer typeID, String[] tagIDs, String indexString);
    Page<FileEntity> getFileListByUsername(Page<FileEntity> page, String username,Boolean isApproved, Boolean isFolder);
    List<FileEntity> getFileListByFolderID(String folderID);

    FileEntity getFileDetailInfoById(@Param("fileID") Integer fileID);
    List<FileEntity> getFileDetailInfosByIds(@Param("fileIDs") List<Integer> fileIDs);
}