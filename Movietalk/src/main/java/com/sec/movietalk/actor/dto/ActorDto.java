package com.sec.movietalk.actor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ActorDto {
    private int id; // 배우 ID
    private String name; // 배우 이름

    @JsonProperty("profile_path")
    private String profilePath; // 프로필 이미지

    @JsonProperty("biography")
    private String biography; // 배우 소개 및 이력 설명

    @JsonProperty("birthday")
    private String birthday; // 생일 (YYYY-MM-DD)

    @JsonProperty("place_of_birth")
    private String placeOfBirth; // 출생지

    @JsonProperty("gender")
    private int gender; // 성별

    @JsonProperty("known_for_department")
    private String knownForDepartment; // 대표 활동 분야

    public String getKnownForDepartment() {
        return knownForDepartment;
    }

    public void setKnownForDepartment(String knownForDepartment) {
        this.knownForDepartment = knownForDepartment;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

}
