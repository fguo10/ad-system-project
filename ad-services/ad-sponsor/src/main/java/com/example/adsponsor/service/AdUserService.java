package com.example.adsponsor.service;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdUser;

public interface AdUserService {
    AdUser createUser(AdUser adUser) throws AdException;
    AdUser loginUser(String username, String token) throws AdException;
    AdUser updateUser(String username, String newUsername, String newToken) throws AdException;
}
