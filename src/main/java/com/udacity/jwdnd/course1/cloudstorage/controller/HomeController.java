package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value={"/home", "/"})
public class HomeController {
    private final NotesService notesService;
    private final UserService userService;
    private final CredentialsService credentialsService;
    private final FilesService filesService;

    public HomeController(NotesService notesService, UserService userService, CredentialsService credentialsService, FilesService filesService){
        this.notesService = notesService;
        this.userService = userService;
        this.credentialsService = credentialsService;
        this.filesService = filesService;
    }

    @GetMapping
    public String homeView(Notes notes, CredentialsForm credentialsForm, Files files, Authentication authentication, Model model) {
        Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();

        model.addAttribute("noteList", notesService.getNotesByUserId(userIdOfCurrentUser));
        model.addAttribute("credentialsList", credentialsService.getCredentialsByUserId(userIdOfCurrentUser));
        model.addAttribute("filesList", filesService.getFilesByUserId(userIdOfCurrentUser));

        model.addAttribute(credentialsForm);
        model.addAttribute(notes);
        model.addAttribute(files);

        return "home";
    }

    @PostMapping
    @RequestMapping("/logout")
    public String logout(Authentication authentication) {
        return "login";
    }

    @PostMapping
    @RequestMapping("/home/notes")
    public String postNotes(Authentication authentication, Notes note, Model model ){
        if(note.getNoteid() == null){
            Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();
            note.setUserid(userIdOfCurrentUser);
            if(notesService.createNote(note) > 0) {
                return "redirect:/result/createnote/success";
            }
            else {
                return "redirect:/result/createnote/failure";
            }
        }
        else {
            if(notesService.updateNote(note) > 0) {
                return "redirect:/result/updatenote/" + note.getNoteid() + "/success";
            }
            else {
                return "redirect:/result/updatenote" + note.getNoteid() + "/failure";
            }
        }
    }

    @PostMapping
    @RequestMapping("/home/credentials")
    public String postCredentials(Authentication authentication, CredentialsForm credentialsForm, Model model) {
        if(credentialsForm.getCredentialid() == null){
            Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();
            credentialsForm.setUserid(userIdOfCurrentUser);
            if(credentialsService.createCredential(credentialsForm) > 0) {
                return "redirect:/result/createcredential/success";
            }
            else {
                return "redirect:/result/createcredential/failure";
            }
        }
        else {
            if(credentialsService.updateCredential(credentialsForm) > 0) {
                return "redirect:/result/updatecredential/" + credentialsForm.getCredentialid() + "/success";
            }
            else {
                return "redirect:/result/updatecredential/" + credentialsForm.getCredentialid() + "/failure";
            }
        }
    }

    @PostMapping
    @RequestMapping("/home/file-upload")
    public String postFiles(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) throws IOException {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        String fileSize = Long.toString(fileUpload.getSize());

        Files newFile = new Files(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(), fileSize, userId, fileUpload.getBytes());

        if(filesService.createFile(newFile) > 0) {
            return "redirect:/result/createfile/success";
        }
        else {
            return "redirect:/result/createfile/failure";
        }
    }

}
