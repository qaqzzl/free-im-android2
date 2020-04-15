package com.qaqzz.free_im.bean;

import com.qaqzz.framework.utils.BirthdayToAgeConstellationShengXiaoUtil;

/**
 * @author qaqzz
 * @description TODO
 * @date 2020/1/11 1:10
 */
public class MemberInfoBean {
    /**
     * avatar :
     * birthdate : 0
     * city :
     * gender : wz
     * member_id : 1
     * nickname : 会员 - 003363
     * province :
     * signature :
     */

    private String avatar;
    private String birthdate;
    private String city;
    private String gender;
    private String member_id;
    private String nickname;
    private String province;
    private String signature;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAge() {
        return BirthdayToAgeConstellationShengXiaoUtil.BirthdayToAge(birthdate);
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
