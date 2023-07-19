package com.example.adsponsor.service.impl;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.MaterialType;
import com.example.adsponsor.entity.Creative;
import com.example.adsponsor.repository.CreativeRepository;
import com.example.adsponsor.service.CreativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@AllArgsConstructor
@Service
public class CreativeServiceImpl implements CreativeService {

    private CreativeRepository creativeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Creative createCreative(Creative request) throws AdException {
        Creative creativeObj = new Creative(
                request.getName(),
                request.getType(),
                request.getMaterialType(),
                request.getHeight(),
                request.getWidth(),
                request.getSize(),
                request.getUserId(),
                request.getUrl()
        );
        if (request.getDuration() != null) {
            creativeObj.setDuration(request.getDuration());
        } else {
            creativeObj.setDuration(0);
        }
        return creativeRepository.save(creativeObj);
    }
}
