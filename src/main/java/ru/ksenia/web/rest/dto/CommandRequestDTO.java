package ru.ksenia.web.rest.dto;

import javax.servlet.http.Part;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandRequestDTO {
    private String name;
    private String ageCategory;
    private String nomination;
    private Part music;
    private List<CommandMemberDTO> members = new ArrayList<>();
    private List<CommandCoachDTO> coaches = new ArrayList<>();

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

    public byte[] getMusic() {
        return null;
    }

    public void setMusic(byte[] music) {
        this.music = null;
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
