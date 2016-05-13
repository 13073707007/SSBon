package com.loongme.activity.common;

/**
 * <p>
 * HTTP访问，返回数据的实体类
 * </p>
 * 示例：
 * 
 * <pre>
 *     {"result":"ok",
 *     errormsg":"",
 *     values":{
 *         "userID":"58c82f439af24c869199ea0b9a384aab",
 *         "createTime":1410097935287,
 *         "linkHospitalID":"",
 *         "userTitle":"",
 *         "userRoleID":1003
 *     },
 *     "errcode":200}
 * </pre>
 * 
 * @author zhaojianyu
 */
public class ResultObj {

  /**
   * result 返回的内容是字符串 ok或error，都有常量
   */
  public static final String TAG_RESULT = "result";
  /**
   * 返回结果正常
   */
  public static final String RESULT_OK = "ok";
  /**
   * 返回结果异常
   */
  public static final String RESULT_ERROR = "error";
  /**
   * 返回的数据是一个JSON对象（需要检查null） values
   */
  public static final String TAG_VALUES = "values";
  /**
   * 如果返回结果是error，可以取得错误信息，如果返回结果是ok，则为空字符串 errormsg
   */
  public static final String TAG_ERROR_MSG = "errormsg";
  /**
   * 如果返回结果是error，可以取得特定错误的错误号，如果返回结果是ok，则为空字符串 errcode
   */
  public static final String TAG_ERRCODE = "errcode";

  /**
   * 401,"用户未登录"，可以额外处理
   */
  public static final int ERRCODE_NOT_LOGIN = 401;
  /**
   * 402,"诊疗号已被其他用户领用进行处理" com.ydyl.server.ExceptionType.HavedConsume
   */
  public static final int ERRCODE_BEEN_CONSUMED = 402;
  /**
   * 业务逻辑异常
   */
  public static final int ERRCODE_OTHER = 499;
  /**
   * 正常状态下对应的错误码
   */
  public static final int ERRCODE_STATE_OK = 200;

}
