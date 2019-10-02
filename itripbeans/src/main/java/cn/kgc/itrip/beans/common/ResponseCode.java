package cn.kgc.itrip.beans.common;

/**
 * 响应状态码
 * @author 27498
 */
public enum  ResponseCode {
    //成功
    SUCCESS(0,"true"),

    //失败
    ERROR(1,"false"),

    //需要登入
    NEED_LOGIN(10,"NEED_LOGIN"),

    //参数错误
    ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
    /**
     * 状态码
     */
    private final int code;

    /**
     * 与状态码相对应的描述
     */
    private final String desc;

    ResponseCode(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }
}
