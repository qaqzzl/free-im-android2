package com.qaqzz.free_im.bean;

import java.util.List;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/1/15 3:30
 */
public class FriendListBean {
    private List<FriendBean> friend_list;

    public List<FriendBean> getFriend_list() {
        return friend_list;
    }

    public void setFriend_list(List<FriendBean> friend_list) {
        this.friend_list = friend_list;
    }

    public static class FriendBean {
        /**
         * avatar :
         * gender : wz
         * member_id : 10
         * nickname : 测试用户10
         * signature :
         */

        private String avatar;
        private String gender;
        private String member_id;
        private String nickname;
        private String signature;

        public boolean isSex() {
            if (getGender().toString().equals("m")) {
                return false;
            } else {
                return true;
            }
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
