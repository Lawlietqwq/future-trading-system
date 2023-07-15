package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.vo.RemoteVO;

public interface HttpClientService {
    public int doPostParams(RemoteVO remoteVO, String url);
}
