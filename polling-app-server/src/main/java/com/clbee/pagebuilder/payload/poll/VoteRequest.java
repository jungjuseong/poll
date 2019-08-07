package com.clbee.pagebuilder.payload.poll;
import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Integer choiceId;

    public Integer getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Integer choiceId) {
        this.choiceId = choiceId;
    }
}

