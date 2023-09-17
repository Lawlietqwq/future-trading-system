package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.vo.RemoteVO;

public interface HttpClientService {
    /**
     *
     * @param remoteVO
     * @param url
     * @return
     */
    public int doPostParams(RemoteVO remoteVO, String url);
}
