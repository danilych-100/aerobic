package ru.ksenia.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class CommandDTO {
    private String region;
    private String name;
    private String ageCategory;
    private String nomination;
    private Long memberCount;
    private String phoneNumber;
    private String email;
    private List<CommandMemberDTO> members = new ArrayList<>();
    private List<CommandCoachDTO> coaches = new ArrayList<>();

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public void setAgeCategory(String ageCategory) {
        this.ageCategory = ageCategory;
    }

    public String getNomination() {
        return nomination;
    }

    public void setNomination(String nomination) {
        this.nomination = nomination;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CommandMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<CommandMemberDTO> members) {
        this.members = members;
    }

    public List<CommandCoachDTO> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<CommandCoachDTO> coaches) {
        this.coaches = coaches;
    }
}
