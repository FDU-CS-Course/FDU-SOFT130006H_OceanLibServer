package com.oriole.ocean.common.po.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Document(collection = "note_favor")
public class FavorEntity implements java.io.Serializable {
    @Id
    private String id;

    private String username;
    private String noteId;
}
