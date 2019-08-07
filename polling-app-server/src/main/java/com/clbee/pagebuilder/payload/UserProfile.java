package com.clbee.pagebuilder.payload;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfile {
    private Long id;

    private String username;
    private String fullname;
    private String email;
    private Instant joinedAt;
    private Long documentCount;
}
