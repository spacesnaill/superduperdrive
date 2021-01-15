package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import com.udacity.jwdnd.course1.cloudstorage.services.FilesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController {
    private final FilesService filesService;
    private final UserService userService;

    public FileController(FilesService filesService, UserService userService) {
        this.filesService = filesService;
        this.userService = userService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @RequestMapping(value={"/file/{fileId}/{fileName}"})
    public ResponseEntity<Resource> getFile(@PathVariable("fileId") int fileId, Authentication authentication, Model model){
        Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();

        if(filesService.doesUserOwnFile(userIdOfCurrentUser, fileId)){
            Files retrievedFile = filesService.getFileByFileId(fileId);
            HttpHeaders headers = new HttpHeaders();

            ByteArrayResource resource = new ByteArrayResource(retrievedFile.getFiledata());

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
