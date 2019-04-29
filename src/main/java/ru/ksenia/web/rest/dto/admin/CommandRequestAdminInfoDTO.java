package ru.ksenia.web.rest.dto.admin;

import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class CommandRequestAdminInfoDTO {
    private String id;
    private String name;
    private String commandName;
    private String region;
    private String ageCategory;
    private String nomination;
    private String musicFileName;
    private Boolean categoryA;

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

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getCategoryA() {
        return categoryA;
    }

    public void setCategoryA(Boolean categoryA) {
        this.categoryA = categoryA;
    }
}
