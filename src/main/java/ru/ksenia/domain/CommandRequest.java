package ru.ksenia.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "command_request")
public class CommandRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age_category")
    private String ageCategory;

    @Column(name = "nomination")
    private String nomination;

    @Column(name = "music_file_name")
    private String musicFileName;

    @Lob
    @Column(name = "music")
    private byte[] music;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "coaches_join_table",
        joinColumns = @JoinColumn(name = "command_request_id"),
        inverseJoinColumns = @JoinColumn(name = "command_coach_id")
    )
    private List<CommandCoach> coaches = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "members_join_table",
        joinColumns = @JoinColumn(name = "command_request_id"),
        inverseJoinColumns = @JoinColumn(name = "command_member_id")
    )
    private List<CommandMember> members = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_request_id")
    private Command command;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<CommandCoach> getCoaches() {
        return coaches;
    }

    public void setCoaches(List<CommandCoach> coaches) {
        this.coaches = coaches;
    }

    public List<CommandMember> getMembers() {
        return members;
    }

    public void setMembers(List<CommandMember> members) {
        this.members = members;
    }

    public byte[] getMusic() {
        return music;
    }

    public void setMusic(byte[] music) {
        this.music = music;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getMusicFileName() {
        return musicFileName;
    }

    public void setMusicFileName(String musicFileName) {
        this.musicFileName = musicFileName;
    }
}
