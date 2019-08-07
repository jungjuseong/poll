package com.clbee.pagebuilder.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DocumentRequest {
    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 3)
    private String deadmark;

    @Size(max = 2048)
    private String preference;

    @Size(max = 10000000)
    private String contents;
}
