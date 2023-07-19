package com.example.adsponsor.service.impl;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.AdUser;
import com.example.adsponsor.repository.AdUserRepository;
import com.example.adsponsor.service.AdUserService;
import com.example.adsponsor.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Slf4j
@AllArgsConstructor
@Service
public class AdUserServiceImpl implements AdUserService {
    private final AdUserRepository userRepository;

    /**
     * 创建新用户。
     *
     * @param adUser 要创建的用户对象
     * @return 创建后的用户对象
     * @throws AdException 如果用户已存在，则抛出AdException异常
     */
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

        return userRepository.save(newUserObj);
    }


    /**
     * 用户登录。
     *
     * @param username 用户名
     * @param token    用户令牌
     * @return 登录成功后的用户对象
     * @throws AdException 如果登录凭据无效或用户状态无效，则抛出AdException异常
     */
    @Override
    public AdUser loginUser(String username, String token) throws AdException {
        AdUser dbUser = userRepository.findByUsername(username);
        if (dbUser == null || !dbUser.getToken().equals(token) || dbUser.getUserStatus() != CommonStatus.VALID.getStatus()) {
            throw new AdException(Constants.ErrorMsg.INVALID_CREDENTIALS);
        }
        return dbUser;
    }


    /**
     * 更新用户信息。
     *
     * @param username    要更新的用户的用户名
     * @param newUsername 新的用户名
     * @param newToken    新的用户令牌
     * @return 更新后的用户对象
     * @throws AdException 如果找不到用户，则抛出AdException异常
     */
    @Override
    @Transactional
    public AdUser updateUser(String username, String newUsername, String newToken) throws AdException {
        AdUser dbUser = userRepository.findByUsername(username);
        if (dbUser == null) {
            throw new AdException(Constants.ErrorMsg.USER_NOT_FOUND);
        }

        if (newUsername != null && !newUsername.isEmpty()) {
            dbUser.setUsername(newUsername);
        }
        if (newToken != null && !newToken.isEmpty()) {
            dbUser.setToken(newToken);
        }
        dbUser.setUpdateTime(LocalDateTime.now());

        return userRepository.save(dbUser);
    }


}
