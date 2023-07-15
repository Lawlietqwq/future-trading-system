package com.csubigdata.futurestradingsystem.common;

/**
 * 返回的消息
 */
public enum ResultTypeEnum {

    /**
     * 操作成功
     */
    SUCCESS(Constants.SUCCESS, "OK"),
    QUERY_OK(Constants.SUCCESS, "查询成功"),

    /**
     * 操作失败
     */
    ERROR(Constants.ERROR, "操作失败"),
    QUERY_FAIL(Constants.ERROR, "没有数据"),
    REPEAT_ERROR(Constants.ERROR, "请勿重复提交"),

    /**
     * 用户操作
     */
    LOGIN_FAIL(Constants.ERROR, "用户名或密码错误"),
    NO_AUTH(Constants.NO_AUTH, "无权限访问，请联系管理员"),
    RENEW_TOKEN(Constants.SUCCESS, "token生成成功"),
    QUERY_INFO_FAIL(Constants.ERROR,"用户信息查询失败"),
    QUERY_INFO_OK(Constants.SUCCESS,"用户信息查询成功"),
    LOGOUT_OK(Constants.SUCCESS,"用户退出成功"),
    REGISTER_OK(Constants.SUCCESS, "注册成功"),
    REGISTER_FAIL(Constants.ERROR, "注册失败"),
    USER_EXIST(Constants.ERROR, "注册失败,用户名已存在"),


    /**
     * 模型操作
     */
    CREATE_MODEL_ERROR(Constants.ERROR, "新增模型失败"),
    UPDATE_MODEL_ERROR(Constants.ERROR, "修改模型失败"),
    START_ERROR(Constants.ERROR, "启动失败"),
    NO_START_ERROR(Constants.ERROR, "模型未启动"),
    PAUSE_ERROR(Constants.ERROR, "暂停失败"),
    OPEN_ERROR(Constants.ERROR, "开仓失败"),
    FORCE_CLOSE_ERROR(Constants.ERROR, "平仓失败"),
    NO_HOLDING_ERROR(Constants.ERROR,"没有持仓"),
    ALREADY_CLOSE_ERROR(Constants.ERROR, "已经系统平仓"),
    CLOSE_ERROR(Constants.ERROR, "关闭失败"),
    DELETE_MODEL_ERROR(Constants.ERROR,"删除模型失败"),
    DELETE_CONFLICT(Constants.ERROR,"模型无法删除,请先暂停模型"),
    FIND_STRATEGY_ERROR(Constants.ERROR,"策略不存在"),
    CREATE_PARAM_ERROR(Constants.ERROR,"新增参数失败"),
    CLASS_NOT_FOUND(Constants.ERROR,"未找到该策略"),
    LOT_OUT(Constants.ERROR,"超出手数"),
    IS_OPENNING(Constants.ERROR,"正在开仓"),
    IS_CLOSING(Constants.ERROR,"正在平仓"),

    /**
     * 记录操作
     */
    INSERT_RECORD_ERROR(Constants.ERROR,"插入记录失败"),
    DELETE_RECORD_ERROR(Constants.ERROR,"删除记录失败");

    private int code;
    private String message;

    ResultTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (ResultTypeEnum result : ResultTypeEnum.values()) {
            if (result.getCode() == code) {
                return result.message;
            }
        }
        return null;
    }

    /**
     * 获取结果类型代号
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取结果类型描述
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.code + ": " + this.message;
    }
}

