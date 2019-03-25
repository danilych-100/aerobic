package ru.ksenia.service.dto;

import ru.ksenia.web.rest.dto.CommandMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class ExportRequestDTO {

    private String id;
    private String ageCategory;
    private String nomination;
    private String region;
    private String commandName;
    private List<ExportMemberDTO> members = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public List<ExportMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<ExportMemberDTO> members) {
        this.members = members;
    }
}
