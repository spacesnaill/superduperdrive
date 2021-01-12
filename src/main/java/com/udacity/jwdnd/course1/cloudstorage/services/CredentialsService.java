package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialsForm;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
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

    public List<CredentialsForm> getCredentialsByUserId(Integer userId) {
        List<CredentialsForm> credentialsForms = new ArrayList<CredentialsForm>();

        List<Credentials> credentialsList = credentialsMapper.getCredentialsByUserId(userId);
        for(Credentials credential : credentialsList) {
            credentialsForms.add(new CredentialsForm(credential.getCredentialid(), credential.getUrl(), credential.getUsername(), credential.getKey(), credential.getPassword(), credential.getUserid() ,encryptionService.decryptValue(credential.getPassword(), credential.getKey())));
        }

        return credentialsForms;
    }

    public int createCredential(CredentialsForm credential) {
        String encodedKey = createEncodedKey();
        credential.setKey(encodedKey);

        credential.setPassword(encryptionService.encryptValue(credential.getDecryptedPassword(), encodedKey));

        return credentialsMapper.insertCredential(credential);
    }

    private String createEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        return Base64.getEncoder().encodeToString(key);
    }
}