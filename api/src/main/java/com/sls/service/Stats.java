package com.sls.service;

import com.sls.exception.InternalServerErrorException;

import java.util.Map;

public interface Stats {

    Map<String, Integer> getLogCounts(int duration) throws InternalServerErrorException;
}
