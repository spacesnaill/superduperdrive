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

    public List<Notes> getNotesByNoteId(Integer noteId) { return notesMapper.getNotesByNoteId(noteId); }

    public int createNote(Notes note) {
        return notesMapper.insertNote(note);
    }

    public int updateNote(Notes note) {
        return notesMapper.updateNote(note);
    }

    public int deleteNote(Integer noteId) {
        return notesMapper.deleteNote(noteId);
    }

    public boolean doesUserOwnNote(Integer userId, Integer noteId) {
        Notes noteWithNoteId = getNotesByNoteId(noteId).get(0);

        return noteWithNoteId.getUserid().equals(userId);
    }

}
