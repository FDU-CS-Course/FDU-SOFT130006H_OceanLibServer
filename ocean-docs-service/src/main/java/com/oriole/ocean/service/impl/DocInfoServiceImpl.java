package com.oriole.ocean.service.impl;

import com.oriole.ocean.dao.FileInfoDao;
import com.oriole.ocean.model.FileInfo;
import com.oriole.ocean.service.DocInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the document information service.
 */
@Service
public class DocInfoServiceImpl implements DocInfoService {

    @Autowired
    private FileInfoDao fileInfoDao;

    @Override
    public Map<String, Object> getMyFileList(String username, boolean isFolder, int pageNum, int pageSize) {
        // Calculate offset for pagination
        int offset = (pageNum - 1) * pageSize;
        
        // Get total count and list of files
        int totalCount = fileInfoDao.countMyFiles(username, isFolder);
        List<FileInfo> files = fileInfoDao.getMyFiles(username, isFolder, offset, pageSize);
        
        // Prepare response
        Map<String, Object> result = new HashMap<>();
        result.put("list", files);
        result.put("isLastPage", (offset + pageSize) >= totalCount);
        
        return result;
    }
} 