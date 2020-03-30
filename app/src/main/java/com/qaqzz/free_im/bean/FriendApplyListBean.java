package com.qaqzz.free_im.bean;

import java.util.List;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/19 11:42
 */
public class FriendApplyListBean {

    private List<ApplyListBean> apply_list;

    public List<ApplyListBean> getApply_list() {
        return apply_list;
    }

    public void setApply_list(List<ApplyListBean> apply_list) {
        this.apply_list = apply_list;
    }

    public static class ApplyListBean {
        /**
         * avatar : https://cdn.qaqzz.com/admin.png
         * gender : wz
         * member_id : 1
         * nickname : 超级账号
         * signature : 作者联系方式: QQ395173209
         * add_friend_remark": "我是你哥呀"
         * status": "1"
         * id": "1"
         */

        private String avatar;
        private String gender;
        private String member_id;
        private String nickname;
        private String signature;
        private String remark;
        private String status;
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemark() {
            return remark;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
