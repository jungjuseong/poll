package com.clbee.pagebuilder.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
    private Long id;
}
