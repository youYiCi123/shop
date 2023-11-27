package com.jxm.business.service;

import com.jxm.business.dto.ActiveSubmitDto;

import java.text.ParseException;

public interface ActiveService {

    int submitContent(ActiveSubmitDto activeSubmitDto) throws ParseException;
}
