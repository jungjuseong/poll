package com.clbee.pagebuilder.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class DocumentResponse {
    private Long id;
    private String name;
    // private UserSummary createdBy;
    private Instant creationDateTime;
    private String deadmark;
    private String preference;
    private String contents;
}
