package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    private final NotesMapper notesMapper;

    public NotesService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

    public List<Notes> getNotesByUserId(Integer userId) {
        return notesMapper.getNotesByUserId(userId);
    }

    public int createNote(Notes note) {
        return notesMapper.insertNote(note);
    }

    public int updateNote(Notes note) {
        return notesMapper.updateNote(note);
    }

}
