package ru.ksenia.service.dto;

import ru.ksenia.domain.CoachJoinTable;
import ru.ksenia.domain.Command;
import ru.ksenia.domain.MemberJoinTable;

import java.util.ArrayList;
import java.util.List;

public class MapperCommandDTO {

    private Command command;

    private List<MemberJoinTable> memberJoins = new ArrayList<>();
    private List<CoachJoinTable> coachJoins = new ArrayList<>();

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<MemberJoinTable> getMemberJoins() {
        return memberJoins;
    }

    public void setMemberJoins(List<MemberJoinTable> memberJoins) {
        this.memberJoins = memberJoins;
    }

    public List<CoachJoinTable> getCoachJoins() {
        return coachJoins;
    }

    public void setCoachJoins(List<CoachJoinTable> coachJoins) {
        this.coachJoins = coachJoins;
    }
}
