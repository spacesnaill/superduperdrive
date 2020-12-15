package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {

    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credentials> getCredentialsByUserId(Integer userId) {
        return credentialsMapper.getCredentialsByUserId(userId);
    }

    public int createCredential(Credentials credential) {
        String encodedKey = createEncodedKey();
        credential.setKey(encodedKey);

        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedKey));

        return credentialsMapper.insertCredential(credential);
    }

    private String createEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        return Base64.getEncoder().encodeToString(key);
    }
}
