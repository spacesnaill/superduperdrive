package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FilesMapper {

    @Select("SELECT * FROM FILES WHERE USERID=#{userid}")
    List<Files> getFilesByUserId(Integer userid);

    @Select("SELECT * FROM FILES WHERE FILEID=#{fileid}")
    List<Files> getFilesByFileId(Integer fileid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(Files file);

    @Delete("DELETE FROM FILES WHERE FILEID=#{fileid}")
    int deleteFile(Integer fileid);
}
