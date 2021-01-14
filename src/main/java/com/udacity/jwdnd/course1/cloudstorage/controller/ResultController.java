package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResultController {

    private final NotesService notesService;
    private final CredentialsService credentialsService;
    private static final  String RESULT_STATE = "resultState";
    private static final String RESULT = "result";

    public ResultController(NotesService notesService, CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
        this.notesService = notesService;
    }

    @GetMapping
    @RequestMapping("/result")
    public String resultView() {
        return RESULT;
    }

    @GetMapping
    @RequestMapping("/result/deletenote/{id}")
    public String deleteNoteResultView(@PathVariable("id") int id, Model model) {
        if(notesService.deleteNote(id) > 0) {
            model.addAttribute(RESULT_STATE, true);
        }
        else {
            model.addAttribute(RESULT_STATE, false);
        }
        return RESULT;
    }

    @GetMapping
    @RequestMapping("/result/deletecredential/{id}")
    public String deleteCredentialResultView(@PathVariable("id") int id, Model model) {
        if(credentialsService.deleteCredential(id) > 0){
            model.addAttribute(RESULT_STATE, true);
        }
        else {
            model.addAttribute(RESULT_STATE, false);
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
        model.addAttribute(RESULT_STATE, false);
        return RESULT;
    }
}
