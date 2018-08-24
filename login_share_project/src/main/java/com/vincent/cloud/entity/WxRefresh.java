/**
  * Copyright 2018 bejson.com 
  */
package com.vincent.cloud.entity;

/**
 * Auto-generated: 2018-07-21 15:6:34
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class WxRefresh {

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    public void setAccess_token(String access_token) {
         this.access_token = access_token;
     }
     public String getAccess_token() {
         return access_token;
     }

    public void setExpires_in(int expires_in) {
         this.expires_in = expires_in;
     }
     public int getExpires_in() {
         return expires_in;
     }

    public void setRefresh_token(String refresh_token) {
         this.refresh_token = refresh_token;
     }
     public String getRefresh_token() {
         return refresh_token;
     }

    public void setOpenid(String openid) {
         this.openid = openid;
     }
     public String getOpenid() {
         return openid;
     }

    public void setScope(String scope) {
         this.scope = scope;
     }
     public String getScope() {
         return scope;
     }

}