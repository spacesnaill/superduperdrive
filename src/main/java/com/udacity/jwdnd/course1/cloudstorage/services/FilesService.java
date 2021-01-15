package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Files;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilesService {

    private final FilesMapper filesMapper;

    public FilesService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public List<Files> getFilesByUserId(Integer userId) {
        return filesMapper.getFilesByUserId(userId);
    }

    public Files getFileByFileId(Integer fileId) {
        List<Files> files = filesMapper.getFilesByFileId(fileId);
        return files.get(0);
    }

    public List<String> getFileNamesByUserId(Integer userId) {
        return filesMapper.getFileNamesByUserId(userId);
    }

    public int createFile(Files file) {
        return  filesMapper.insertFile(file);
    }

    public int deleteFile(Integer fileid) {
        return filesMapper.deleteFile(fileid);
    }

    public boolean doesUserOwnFile(Integer userId, Integer fileId) {
        Files fileWithFileId = getFileByFileId(fileId);

        return fileWithFileId.getUserid().equals(userId);
    }

}
