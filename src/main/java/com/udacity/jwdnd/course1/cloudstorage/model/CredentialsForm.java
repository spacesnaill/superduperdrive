package com.udacity.jwdnd.course1.cloudstorage.model;

public class CredentialsForm extends Credentials{

    private String decryptedPassword;

    public CredentialsForm(Integer credentialid, String url, String username, String key, String password, Integer userid, String decryptedPassword) {
        super(credentialid, url, username, key, password, userid);
        this.decryptedPassword = decryptedPassword;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

}
