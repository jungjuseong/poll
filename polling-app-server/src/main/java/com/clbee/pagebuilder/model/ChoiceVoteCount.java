package com.clbee.pagebuilder.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChoiceVoteCount {
    private Long choiceId;
    private Long voteCount;
}

