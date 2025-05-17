package com.oriole.ocean.service;

import java.util.Map;

/**
 * Service interface for document information operations.
 */
public interface DocInfoService {
    /**
     * Retrieves a paginated list of files owned by the current user.
     *
     * @param username Username of the current user
     * @param isFolder Whether to fetch folders (false for files)
     * @param pageNum Page number for pagination
     * @param pageSize Number of items per page
     * @return Map containing the list of files and pagination info
     */
    Map<String, Object> getMyFileList(String username, boolean isFolder, int pageNum, int pageSize);
} 