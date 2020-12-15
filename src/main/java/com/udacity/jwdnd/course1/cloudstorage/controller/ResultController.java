package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/result")
public class ResultController {

    private NotesService notesService;

    public ResultController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping
    @RequestMapping("/result")
    public String resultView() {
        return "result";
    }

    @GetMapping
    @RequestMapping("/result/delete/{id}")
    public String deleteResultView(@PathVariable("id") int id) {
        notesService.deleteNote(id);
        return "result";
    }
