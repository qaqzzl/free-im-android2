package com.qaqzz.free_im.bean;

import java.util.List;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/18 16:18
 */
public class SearchFriendBean {
    private List<SearchListBean> search_list;

    public List<SearchListBean> getSearch_list() {
        return search_list;
    }

    public void setSearch_list(List<SearchListBean> search_list) {
        this.search_list = search_list;
    }

    public static class SearchListBean {
        /**
         * avatar :
         * gender : wz
         * member_id : 10
         * nickname : 会员 - 009252
         * signature :
         */

        private String avatar;
        private String gender;
        private String member_id;
        private String nickname;
        private String signature;

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
