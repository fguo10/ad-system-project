package com.example.adsponsor.service.impl;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.AdUser;
import com.example.adsponsor.repository.AdUserRepository;
import com.example.adsponsor.service.AdUserService;
import com.example.adsponsor.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@AllArgsConstructor
@Service
public class AdUserServiceImpl implements AdUserService {
    private final AdUserRepository userRepository;


    @Override
    @Transactional
    public AdUser createUser(AdUser adUser) throws AdException {
        AdUser dbUser = userRepository.findByUsername(adUser.getUsername());
        if (dbUser != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_ERROR);
        }

        AdUser newUserObj = new AdUser();
        newUserObj.setUsername(adUser.getUsername());
        newUserObj.setToken(CommonUtils.md5(adUser.getUsername()));

        AdUser newUser = userRepository.save(newUserObj);

        return newUser;
    }

}
