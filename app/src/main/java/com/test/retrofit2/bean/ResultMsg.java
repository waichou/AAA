/**
  * Copyright 2018 bejson.com 
  */
package com.test.retrofit2.bean;

/**
 * Auto-generated: 2018-05-08 11:49:1
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ResultMsg {

    private int code;
    private String result;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setResult(String result) {
         this.result = result;
     }
     public String getResult() {
         return result;
     }


    @Override
    public String toString() {
        return "ResultMsg{" +
                "code=" + code +
                ", result='" + result + '\'' +
                '}';
    }
}