package com.oriole.ocean.dao;

import com.oriole.ocean.model.FileInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Data Access Object for file information operations.
 */
@Mapper
public interface FileInfoDao {
    
    /**
     * Counts the total number of files owned by the current user.
     *
     * @param username Username of the current user
     * @param isFolder Whether to count folders (false for files)
     * @return Total count of files
     */
    @Select("SELECT COUNT(*) FROM file f " +
            "WHERE f.upload_username = #{username} " +
            "AND ((#{isFolder} = true AND f.file_id IN (SELECT folder_id FROM file WHERE folder_id IS NOT NULL)) " +
            "OR (#{isFolder} = false AND f.file_id NOT IN (SELECT folder_id FROM file WHERE folder_id IS NOT NULL)))")
    int countMyFiles(@Param("username") String username, @Param("isFolder") boolean isFolder);
    
    /**
     * Retrieves a paginated list of files owned by the current user.
     *
     * @param username Username of the current user
     * @param isFolder Whether to fetch folders (false for files)
     * @param offset Offset for pagination
     * @param limit Number of items to fetch
     * @return List of file information
     */
    @Select("SELECT f.*, fe.* FROM file f " +
            "LEFT JOIN file_extra fe ON f.file_id = fe.file_id " +
            "WHERE f.upload_username = #{username} " +
            "AND ((#{isFolder} = true AND f.file_id IN (SELECT folder_id FROM file WHERE folder_id IS NOT NULL)) " +
            "OR (#{isFolder} = false AND f.file_id NOT IN (SELECT folder_id FROM file WHERE folder_id IS NOT NULL))) " +
            "ORDER BY f.upload_date DESC " +
            "LIMIT #{offset}, #{limit}")
    List<FileInfo> getMyFiles(@Param("username") String username,
                             @Param("isFolder") boolean isFolder, 
                             @Param("offset") int offset, 
                             @Param("limit") int limit);
} 