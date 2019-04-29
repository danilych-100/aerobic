package ru.ksenia.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class CommandRequestDTO {
    private String id;
    private String name;
    private String ageCategory;
    private String nomination;
    private String music;
    private String musicFileName;
    private Boolean categoryA;
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

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
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

    public String getMusicFileName() {
        return musicFileName;
    }

    public void setMusicFileName(String musicFileName) {
        this.musicFileName = musicFileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCategoryA() {
        return categoryA;
    }

    public void setCategoryA(Boolean categoryA) {
        this.categoryA = categoryA;
    }
}
