package ru.ksenia.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "command_member")
public class CommandMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "birth_date")
    private Instant birthDate;

    @Column(name = "passport_series")
    private Long passportSeries;

    @Column(name = "passport_number")
    private Long passportNumber;

    @Column(name = "passport_desc")
    private String passportDesc;

    @Column(name = "birth_certificate_number")
    private Long birthCertificateNumber;

    @Column(name = "birth_certificate_desc")
    private String birthCertificateDesc;

    @Column(name = "quality")
    private String quality;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_member_id")
    private Command command;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "members_join_table",
               joinColumns = @JoinColumn(name = "command_member_id"),
               inverseJoinColumns = @JoinColumn(name = "command_request_id")
    )
    private List<CommandRequest> commandRequests = new ArrayList<>();

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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getBirthCertificateNumber() {
        return birthCertificateNumber;
    }

    public void setBirthCertificateNumber(Long birthCertificateNumber) {
        this.birthCertificateNumber = birthCertificateNumber;
    }

    public String getBirthCertificateDesc() {
        return birthCertificateDesc;
    }

    public void setBirthCertificateDesc(String birthCertificateDesc) {
        this.birthCertificateDesc = birthCertificateDesc;
    }

    public List<CommandRequest> getCommandRequests() {
        return commandRequests;
    }

    public void setCommandRequests(List<CommandRequest> commandRequests) {
        this.commandRequests = commandRequests;
    }
}
