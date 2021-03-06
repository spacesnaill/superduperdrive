package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.udacity.jwdnd.course1.cloudstorage.model.ErrorMessage;

@Controller
public class ResultController {

    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private final FilesService filesService;
    private final UserService userService;
    private static final String RESULT_STATE = "resultState";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String RESULT = "result";

    public ResultController(NotesService notesService, CredentialsService credentialsService, FilesService filesService, UserService userService) {
        this.credentialsService = credentialsService;
        this.notesService = notesService;
        this.filesService = filesService;
        this.userService = userService;
    }

    @GetMapping
    @RequestMapping("/result")
    public String resultView() {
        return RESULT;
    }

    @GetMapping
    @RequestMapping("/result/deletenote/{noteId}")
    public String deleteNoteResultView(Authentication authentication, @PathVariable("noteId") int noteId, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();

        if(notesService.doesUserOwnNote(userId, noteId)) {
            if(notesService.deleteNote(noteId) > 0){
                model.addAttribute(RESULT_STATE, true);
            }
            else {
                showError(ErrorMessage.DELETION_FAILURE, model);
            }
        }
        else {
            showError(ErrorMessage.PERMISSION_ERROR, model);
        }
        return RESULT;
    }

    @GetMapping
    @RequestMapping("/result/deletecredential/{credentialId}")
    public String deleteCredentialResultView(Authentication authentication, @PathVariable("credentialId") int credentialId, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();

        if(credentialsService.doesUserOwnCredential(userId, credentialId)){
            if(credentialsService.deleteCredential(credentialId) > 0){
                model.addAttribute(RESULT_STATE, true);
            }
            else {
                showError(ErrorMessage.DELETION_FAILURE, model);
            }
        }
        else {
            showError(ErrorMessage.PERMISSION_ERROR, model);
        }
        return RESULT;
    }

    @GetMapping
    @RequestMapping("/result/deletefile/{fileId}")
    public String deleteFileResultView(Authentication authentication, @PathVariable("fileId") int fileId, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();

        if(filesService.doesUserOwnFile(userId, fileId)) {
            if(filesService.deleteFile(fileId) > 0){
                model.addAttribute(RESULT_STATE, true);
            }
            else {
                showError(ErrorMessage.DELETION_FAILURE, model);
            }
        }
        else {
            showError(ErrorMessage.PERMISSION_ERROR, model);
        }
        return RESULT;
    }

    @GetMapping
    @RequestMapping(value={"/result/createnote/success", "/result/createcredential/success", "/result/createfile/success"})
    public String createSuccessView(Model model) {
        model.addAttribute(RESULT_STATE, true);
        return RESULT;
    }

    @GetMapping
    @RequestMapping(value={"/result/createnote/failure", "/result/createcredential/failure", "/result/createfile/failure/*"})
    public String createFailureView(Model model) {
        model.addAttribute(RESULT_STATE, false);
        showError(ErrorMessage.CREATION_FAILURE, model);
        return RESULT;
    }

    @GetMapping
    @RequestMapping(value={"/result/updatenote/{id}/success", "/result/updatecredential/{id}/success"})
    public String updateSuccessView(@PathVariable("id") int id, Model model) {
        model.addAttribute(RESULT_STATE, true);
        return RESULT;
    }

    @GetMapping
    @RequestMapping(value={"/result/updatenote/{id}/failure", "/result/updatecredential/{id}/failure"})
    public String updateFailureView(@PathVariable("id") int id, Model model) {
        showError(ErrorMessage.UPDATE_FAILURE, model);
        return RESULT;
    }

    @GetMapping
    @RequestMapping(value = "/result/createfile/failure/file-too-large")
    public String fileTooLargeView(Model model){
        showError(ErrorMessage.FILE_TOO_LARGE, model);
        return RESULT;
    }

    private void showError(ErrorMessage error, Model model) {
        model.addAttribute(RESULT_STATE, false);
        model.addAttribute(ERROR_MESSAGE, error.message);
    }
}
