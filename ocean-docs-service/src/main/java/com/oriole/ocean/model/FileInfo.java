package com.oriole.ocean.model;

import lombok.Data;
import java.util.List;

/**
 * Represents a file's information in the system.
 */
@Data
public class FileInfo {
    private Integer fileId;
    private String title;
    private String abstractContent;
    private String fileType;
    private String previewPictureObjectName;
    private String uploadDate;
    private Integer paymentMethod;
    private Integer paymentAmount;
    private List<String> tagNames;
    private FileExtraEntity fileExtraEntity;
    
    // Additional fields from file table
    private Integer size;
    private Integer folderId;
    private String previewPdfObjectName;
    private String uploadUsername;
    private String realObjectName;
    private Double hideScore;
    private Boolean isApproved;
    private Boolean isAllowAnon;
    private Boolean isAllowVipfree;
    private Boolean isAllowComment;
    private String indexString;
    private Integer typeId;
} 