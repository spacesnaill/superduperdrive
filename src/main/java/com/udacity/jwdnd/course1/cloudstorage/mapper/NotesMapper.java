package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("SELECT * FROM NOTES WHERE USERID=#{userid}")
    List<Notes> getNotesByUserId(Integer userid);

    @Select("SELECT * FROM NOTES WHERE NOTEID=#{noteId}")
    List<Notes> getNotesByNoteId(Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Notes note);

    @Update("UPDATE NOTES SET notetitle=#{notetitle}, notedescription=#{notedescription} WHERE noteid=#{noteid}")
    int updateNote(Notes note);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteid}")
    int deleteNote(Integer noteid);
}
