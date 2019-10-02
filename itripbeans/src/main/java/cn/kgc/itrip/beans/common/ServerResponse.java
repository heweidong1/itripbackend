package cn.kgc.itrip.beans.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * 通用对象返回器
 *
 * @author 27498
 *
 * JsonSerialize 保证序列化json的时候，如果是null的对象key会消失
 */
/*@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)*/
public class ServerResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private String success;

    /**
     * 状态码对应的数据
     */
    private String msg;

    /**
     * 泛型类型的数据
     */
    private T data;

    private ServerResponse(String success) {
        this.success = success;
    }

    private ServerResponse(String success, T data) {
        this.success = success;
        this.data = data;
    }

    private ServerResponse(String success, T data, String msg) {
        this.success = success;
        this.data = data;
        this.msg = msg;
    }

    private ServerResponse(String status, String msg) {
        this.success = status;
        this.msg = msg;
    }

    /**
     * 我们不希望 isSuccess 出现在返回的数据中， 通过@JsonIgnore 来实现这一目标
     * @return
     */
    @JsonIgnore
    public boolean isSuccess(){
        return this.success.equals(ResponseCode.SUCCESS.getDesc()) ;
    }

    public String getSuccess(){
        return success;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    /**
     * 作为统一返回的数据需要 各种类型的方法 以下方法主要处理成功时的场景
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getDesc());
    }
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getDesc(),msg);
    }
    public static  <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getDesc(),data);
    }
    public static  <T> ServerResponse<T> createBySuccess(String msg ,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getDesc(),data,msg);
    }

    /**
     * 以下方法主要处理返回错误时的场景
     * @param <T>
     * @return
     */
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getDesc(),ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getDesc(),errorMessage);
    }
    public static <T> ServerResponse<T> createByErrorCodeMessage(String errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }



}
