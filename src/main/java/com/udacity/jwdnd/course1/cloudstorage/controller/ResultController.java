package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
    public String deleteResultView(@PathVariable("id") int id, Model model) {
        if(notesService.deleteNote(id) > 0) {
            model.addAttribute("resultState", true);
        }
        else {
            model.addAttribute("resultState", false);
        }
        return "result";
    }

    @GetMapping
    @RequestMapping(value={"/result/createnote/success", "/result/createcredential/success"})
    public String createSuccessView(Model model) {
        model.addAttribute("resultState", true);
        return "result";
    }

    @GetMapping
    @RequestMapping(value={"/result/createnote/failure", "/result/createcredential/failure"})
    public String createFailureView(Model model) {
        model.addAttribute("resultState", false);
        return "result";
    }

    @GetMapping
    @RequestMapping("/result/updatenote/{id}/success")
    public String updateNoteSuccessView(@PathVariable("id") int id, Model model) {
        model.addAttribute("resultState", true);
        return "result";
    }
}
