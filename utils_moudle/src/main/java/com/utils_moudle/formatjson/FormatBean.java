package com.utils_moudle.formatjson;

import java.util.List;

/**
 * Created by zhouwei on 2018/7/25.
 *
 * 参看：关于gsonFormat插件解析json并生成实体类
 *
 * https://blog.csdn.net/qq_30379689/article/details/53081358
 */

public class FormatBean {

    /**
     * statusCode : 200
     * message : successful
     * items : {"verifyReply":"同意","createTime":"2018-07-24 15:09:23","buildingNames":["南郡水云天能用的1号楼","南郡水云天1","龍首壹号院一号楼","龍首壹号院二号楼","龍首壹号院三号楼","龍首壹号院四号楼","隆德华府一号楼","隆德华府二号楼","隆德华府三号楼","隆德华府四号楼","长春豪园一号楼","长春豪园二号楼","长春豪园三号楼","长春豪园四号楼","明珠花园一号楼","明珠花园二号楼","明珠花园三号楼","明珠花园四号楼","幸福嘉园一号楼","幸福嘉园二号楼","幸福嘉园三号楼","幸福嘉园四号楼"],"pubTime":"2018-07-24 00:00:00","statusName":"已发送","initiatorName":"张三","id":113,"verifyTime":"2018-07-24 15:11:15","title":"公告6","verifierName":"张三","content":"<p>公告6内容<\/p >","status":5}
     */

    private int statusCode;
    private String message;
    private ItemsBean items;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ItemsBean getItems() {
        return items;
    }

    public void setItems(ItemsBean items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * verifyReply : 同意
         * createTime : 2018-07-24 15:09:23
         * buildingNames : ["南郡水云天能用的1号楼","南郡水云天1","龍首壹号院一号楼","龍首壹号院二号楼","龍首壹号院三号楼","龍首壹号院四号楼","隆德华府一号楼","隆德华府二号楼","隆德华府三号楼","隆德华府四号楼","长春豪园一号楼","长春豪园二号楼","长春豪园三号楼","长春豪园四号楼","明珠花园一号楼","明珠花园二号楼","明珠花园三号楼","明珠花园四号楼","幸福嘉园一号楼","幸福嘉园二号楼","幸福嘉园三号楼","幸福嘉园四号楼"]
         * pubTime : 2018-07-24 00:00:00
         * statusName : 已发送
         * initiatorName : 张三
         * id : 113
         * verifyTime : 2018-07-24 15:11:15
         * title : 公告6
         * verifierName : 张三
         * content : <p>公告6内容</p >
         * status : 5
         */

        private String verifyReply;
        private String createTime;
        private String pubTime;
        private String statusName;
        private String initiatorName;
        private int id;
        private String verifyTime;
        private String title;
        private String verifierName;
        private String content;
        private int status;
        private List<String> buildingNames;

        public String getVerifyReply() {
            return verifyReply;
        }

        public void setVerifyReply(String verifyReply) {
            this.verifyReply = verifyReply;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getInitiatorName() {
            return initiatorName;
        }

        public void setInitiatorName(String initiatorName) {
            this.initiatorName = initiatorName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVerifyTime() {
            return verifyTime;
        }

        public void setVerifyTime(String verifyTime) {
            this.verifyTime = verifyTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getVerifierName() {
            return verifierName;
        }

        public void setVerifierName(String verifierName) {
            this.verifierName = verifierName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getBuildingNames() {
            return buildingNames;
        }

        public void setBuildingNames(List<String> buildingNames) {
            this.buildingNames = buildingNames;
        }
    }
}
