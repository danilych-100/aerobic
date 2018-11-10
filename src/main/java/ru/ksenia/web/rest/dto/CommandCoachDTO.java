package ru.ksenia.web.rest.dto;


import java.time.LocalDateTime;
import java.util.Date;

public class CommandCoachDTO {
    private String name;
    private Date birthDate;
    private Long passportSeries;
    private Long passportNumber;
    private String passportDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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
}
