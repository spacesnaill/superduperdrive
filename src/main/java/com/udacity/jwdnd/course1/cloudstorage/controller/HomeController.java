package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/home", "/"})
public class HomeController {
    private final NotesService notesService;
    private final UserService userService;

    public HomeController(NotesService notesService, UserService userService){
        this.notesService = notesService;
        this.userService = userService;
    }

    @GetMapping
    public String homeView(Notes notes, Authentication authentication, Model model) {
        Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();
        model.addAttribute("noteList", notesService.getNotesByUserId(userIdOfCurrentUser));

        return "home";
    }

    @PostMapping
    public String postNotes(Authentication authentication, Notes note, Model model ){
        Integer userIdOfCurrentUser = userService.getUser(authentication.getName()).getUserid();
        note.setUserid(userIdOfCurrentUser);
        notesService.createNote(note);

        return "redirect:/result";
    }

}
