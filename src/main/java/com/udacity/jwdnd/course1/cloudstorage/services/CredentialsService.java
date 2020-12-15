package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialsService {

    private final CredentialsMapper credentialsMapper;

    public CredentialsService(CredentialsMapper credentialsMapper) {
        this.credentialsMapper = credentialsMapper;
    }

    public List<Credentials> getCredentialsByUserId(Integer userId) {
        return credentialsMapper.getCredentialsByUserId(userId);
    }

    public int createCredential(Credentials credential) {
        return credentialsMapper.insertCredential(credential);
    }
}
