package com.csubigdata.futurestradingsystem.common;

public class Constants {

    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

    public static final int SUCCESS = 200;

    //部分平仓
    public static final int HOLDING = 300;

    public static final int ERROR = 500;

    public static final int NO_LOGIN = 600;

    public static final int NO_AUTH = 700;

    public static final String CLOSE_DEFAULT = "CloseDefault";

    public static final String MODEL_PREFIX = "com.csubigdata.futurestradingsystem.strategy.impl.";

//    public static final String FLASK_BASE_URL = "http://127.0.0.1:8013";
    public static final String FLASK_BASE_URL = "http://127.0.0.1:8013";

    public static final String OPEN_URL = "/open";

    public static final String CLOSE_URL = "/close";

}
