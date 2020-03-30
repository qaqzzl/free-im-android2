package com.qaqzz.free_im.bean;

import java.util.List;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/3/28 20:24
 */
public class DynamicListBean {
    /**
     * list : [{"address_name":"","avatar":"https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg","birthdate":"1123200","comment":"0","content":"","created_at":"0","deleted_at":"0","dynamic_id":"3","gender":"m","image_url":"","latitude_and_longitude":"","member_id":"10","nickname":"测试用户10","purview":"public","review":"normal","type":"common","video_cover":"https://cdn.qaqzz.com/free-im/test/FiiuNOLxmxW1IsJk8800Bgj0WgDW.png","video_cover_height":"640","video_cover_width":"368","video_url":"","zan":"0"},{"address_name":"","avatar":"https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg","birthdate":"1123200","comment":"0","content":"啦啦啦???????????????????????????????????????????????????","created_at":"0","deleted_at":"0","dynamic_id":"4","gender":"m","image_url":"","latitude_and_longitude":"","member_id":"10","nickname":"测试用户10","purview":"public","review":"normal","type":"common","video_cover":"视频封面图","video_cover_height":"0","video_cover_width":"0","video_url":"","zan":"0"},{"address_name":"","avatar":"https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg","birthdate":"1123200","comment":"0","content":"哈哈哈，图片","created_at":"0","deleted_at":"0","dynamic_id":"5","gender":"m","image_url":"https://cdn.qaqzz.com/free-im/test/FiiuNOLxmxW1IsJk8800Bgj0WgDW.png","latitude_and_longitude":"","member_id":"10","nickname":"测试用户10","purview":"public","review":"normal","type":"common","video_cover":"视频封面图","video_cover_height":"0","video_cover_width":"0","video_url":"","zan":"0"},{"address_name":"","avatar":"https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg","birthdate":"1123200","comment":"0","content":"拍照","created_at":"0","deleted_at":"0","dynamic_id":"6","gender":"m","image_url":"https://cdn.qaqzz.com/free-im/test/FkgSJBcQPOeRFhArswGsF56aKW5e.jpg","latitude_and_longitude":"","member_id":"10","nickname":"测试用户10","purview":"public","review":"normal","type":"common","video_cover":"视频封面图","video_cover_height":"0","video_cover_width":"0","video_url":"","zan":"0"},{"address_name":"","avatar":"https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg","birthdate":"1123200","comment":"0","content":"测试","created_at":"0","deleted_at":"0","dynamic_id":"7","gender":"m","image_url":"","latitude_and_longitude":"","member_id":"10","nickname":"测试用户10","purview":"public","review":"normal","type":"common","video_cover":"","video_cover_height":"0","video_cover_width":"0","video_url":"","zan":"0"}]
     * total : 5
     */

    private int total;
    private List<ListBean> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * address_name :
         * avatar : https://cdn.qaqzz.com/free-im/test/Fq5GaRTtHHvKOyrGGJWkZsasLSlj.jpg
         * birthdate : 1123200
         * comment : 0
         * content :
         * created_at : 0
         * deleted_at : 0
         * dynamic_id : 3
         * gender : m
         * image_url :
         * latitude_and_longitude :
         * member_id : 10
         * nickname : 测试用户10
         * purview : public
         * review : normal
         * type : common
         * video_cover : https://cdn.qaqzz.com/free-im/test/FiiuNOLxmxW1IsJk8800Bgj0WgDW.png
         * video_cover_height : 640
         * video_cover_width : 368
         * video_url :
         * zan : 0
         */

        private String address_name;
        private String avatar;
        private String birthdate;
        private String comment;
        private String content;
        private String created_at;
        private String deleted_at;
        private String dynamic_id;
        private String gender;
        private String image_url;
        private String latitude_and_longitude;
        private String member_id;
        private String nickname;
        private String purview;
        private String review;
        private String type;
        private String video_cover;
        private String video_cover_height;
        private String video_cover_width;
        private String video_url;
        private String zan;

        public String getAddress_name() {
            return address_name;
        }

        public void setAddress_name(String address_name) {
            this.address_name = address_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public void setBirthdate(String birthdate) {
            this.birthdate = birthdate;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getDynamic_id() {
            return dynamic_id;
        }

        public void setDynamic_id(String dynamic_id) {
            this.dynamic_id = dynamic_id;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getLatitude_and_longitude() {
            return latitude_and_longitude;
        }

        public void setLatitude_and_longitude(String latitude_and_longitude) {
            this.latitude_and_longitude = latitude_and_longitude;
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

        public String getPurview() {
            return purview;
        }

        public void setPurview(String purview) {
            this.purview = purview;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVideo_cover() {
            return video_cover;
        }

        public void setVideo_cover(String video_cover) {
            this.video_cover = video_cover;
        }

        public String getVideo_cover_height() {
            return video_cover_height;
        }

        public void setVideo_cover_height(String video_cover_height) {
            this.video_cover_height = video_cover_height;
        }

        public String getVideo_cover_width() {
            return video_cover_width;
        }

        public void setVideo_cover_width(String video_cover_width) {
            this.video_cover_width = video_cover_width;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getZan() {
            return zan;
        }

        public void setZan(String zan) {
            this.zan = zan;
        }
    }
}
