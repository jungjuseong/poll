package com.clbee.pagebuilder.util;

import com.clbee.pagebuilder.model.Document;
import com.clbee.pagebuilder.model.User;
import com.clbee.pagebuilder.payload.DocumentResponse;
import com.clbee.pagebuilder.payload.UserSummary;

public class ModelMapper {

    public static DocumentResponse mapDocumentToDocumentResponse(Document doc,  User creator) {
        DocumentResponse response = new DocumentResponse();

        response.setId(doc.getId());
        response.setName(doc.getName());
        response.setCreationDateTime(doc.getCreatedAt());
        response.setDeadmark(doc.getDeadmark());
        response.setContents(doc.getContents());
        response.setPreference(doc.getPreference());

         //UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getFullname(), creator.getEmail());
        // response.setCreatedBy(creatorSummary);

        return response;
    }
}
