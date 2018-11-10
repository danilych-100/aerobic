package ru.ksenia.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "command")
public class Command {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age_category")
    private String ageCategory;

    @Column(name = "nomination")
    private String nomination;

    @Column(name = "member_count")
    private Long memberCount;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "coaches_join_table",
        joinColumns = @JoinColumn(name = "command_id"),
        inverseJoinColumns = @JoinColumn(name = "command_coach_id")
    )
    private List<CommandCoach> coaches = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "members_join_table",
        joinColumns = @JoinColumn(name = "command_id"),
        inverseJoinColumns = @JoinColumn(name = "command_member_id")
    )
    private List<CommandMember> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
