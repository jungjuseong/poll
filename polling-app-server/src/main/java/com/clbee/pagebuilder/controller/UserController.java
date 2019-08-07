package com.clbee.pagebuilder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.clbee.pagebuilder.exception.ResourceNotFoundException;
import com.clbee.pagebuilder.model.User;
import com.clbee.pagebuilder.payload.*;
import com.clbee.pagebuilder.repository.UserRepository;
import com.clbee.pagebuilder.security.CurrentUser;
import com.clbee.pagebuilder.security.UserPrincipal;
import com.clbee.pagebuilder.service.DocumentService;

import com.clbee.pagebuilder.util.AppConstants;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentService documentService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getFullname(), currentUser.getEmail());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Long documentCount = documentService.getDocumentCountByCreatedBy(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getFullname(), user.getEmail(), user.getCreatedAt(), documentCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/documents")
    public PagedResponse<DocumentResponse> getDocumentsCreatedBy(@PathVariable(value = "username") String username,
                 @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return documentService.getDocumentsCreatedBy(username, page, size);
    }
}
