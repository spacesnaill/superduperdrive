package com.udacity.jwdnd.course1.cloudstorage.model;

public enum ErrorMessage {
    PERMISSION_ERROR("You do not have ownership of this item."),
    CREATION_FAILURE("Sorry, there was an error creating the item."),
    UPDATE_FAILURE("Sorry, there was an error updating the item."),
    DELETION_FAILURE("Sorry, there was an error in deleting this item."),
    FILE_TOO_LARGE("Please upload files of size 10MB or less.");

    public final String message;

    ErrorMessage(String message){
        this.message = message;
    }

}
