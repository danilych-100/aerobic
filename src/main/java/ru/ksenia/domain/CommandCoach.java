package ru.ksenia.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "command_coach")
public class CommandCoach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ageCategory")
    private String ageCategory;

    @Column(name = "nomination")
    private String nomination;

    @Column(name = "memberCount")
    private Long memberCount;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "command_id")
    private CommandCoach post;;


    private String region;
    private String name;
    private String ageCategory;
    private String nomination;
    private Long memberCount;
    private String phoneNumber;
    private String email;
    private members;
    private  coaches;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Instant birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPolice() {
        return police;
    }

    public void setPolice(Long police) {
        this.police = police;
    }

    public String getNumberTicket() {
        return numberTicket;
    }

    public void setNumberTicket(String numberTicket) {
        this.numberTicket = numberTicket;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
