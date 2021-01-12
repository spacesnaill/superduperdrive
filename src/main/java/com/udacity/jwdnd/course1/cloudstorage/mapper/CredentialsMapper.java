package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE USERID=#{userid}")
    List<Credentials> getCredentialsByUserId(Integer userid);

    @Select("SELECT * FROM CREDENTIALS WHERE CREDENTIALID=#{credentialid}")
    Credentials getCredentialsByCredentialId(Integer credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credentials credential);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, key=#{key}, password=#{password} WHERE credentialid=#{credentialid}")
    int updateCredential(Credentials credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid=#{credentialid}")
    int deleteCredential(Integer credentialid);
}
