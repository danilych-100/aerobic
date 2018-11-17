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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "passport_series")
    private Long passportSeries;

    @Column(name = "passport_number")
    private Long passportNumber;

    @Column(name = "passport_desc")
    private String passportDesc;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_id")
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

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public Long getPassportSeries() {
        return passportSeries;
    }

    public void setPassportSeries(Long passportSeries) {
        this.passportSeries = passportSeries;
    }

    public Long getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(Long passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getPassportDesc() {
        return passportDesc;
    }

    public void setPassportDesc(String passportDesc) {
        this.passportDesc = passportDesc;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
