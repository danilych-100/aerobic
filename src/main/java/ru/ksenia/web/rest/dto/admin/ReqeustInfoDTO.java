package ru.ksenia.web.rest.dto.admin;

import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;

import java.util.ArrayList;
import java.util.List;

public class ReqeustInfoDTO {

    private CommandRequestAdminInfoDTO generalInfo;
    private String phoneNumber;
    private String mail;

    private List<CommandMemberDTO> members = new ArrayList<>();
    private List<CommandCoachDTO> coaches = new ArrayList<>();

    public CommandRequestAdminInfoDTO getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(CommandRequestAdminInfoDTO generalInfo) {
        this.generalInfo = generalInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
