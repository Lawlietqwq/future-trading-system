package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.service.HttpClientService;
import com.csubigdata.futurestradingsystem.util.HttpClientUtil;
import com.csubigdata.futurestradingsystem.vo.RemoteVO;
import org.springframework.stereotype.Service;

@Service
public class HttpClientServiceImpl implements HttpClientService {
    @Override
    public int doPostParams(RemoteVO remoteVO, String url) {
//        HttpClientUtil.doPostParams(tradingVO, url);
        return  HttpClientUtil.doPostParams(remoteVO, url);
    }


}
