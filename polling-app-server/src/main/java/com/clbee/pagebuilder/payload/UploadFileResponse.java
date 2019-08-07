package com.clbee.pagebuilder.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String name;
    private String downloadUri;
    private String type;
    private Long size;
}