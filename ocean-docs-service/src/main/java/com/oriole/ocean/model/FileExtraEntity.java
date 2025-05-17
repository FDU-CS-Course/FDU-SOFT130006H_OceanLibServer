package com.oriole.ocean.model;

import lombok.Data;

/**
 * Represents additional statistics and metadata for a file.
 */
@Data
public class FileExtraEntity {
    private Integer readNum;
    private Double score;
    private Integer ratersNum;
    private Boolean isVipIncome;
    private Integer downloadNum;
    private Integer likeNum;
    private Integer collectionNum;
    private Integer commentNum;
    private Integer isProCert;
} 