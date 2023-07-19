package com.example.adsponsor.service;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.Creative;

public interface CreativeService {
    Creative createCreative(Creative request) throws AdException;
}
