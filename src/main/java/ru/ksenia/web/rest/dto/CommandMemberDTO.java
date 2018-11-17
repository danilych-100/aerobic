package ru.ksenia.web.rest.dto;


import java.time.LocalDateTime;
import java.util.Date;

public class CommandMemberDTO {
    private String name;
    private Date birthDate;
    private String gender;
    private Long passportSeries;
    private Long passportNumber;
    private String passportDesc;
    private Long birthCertificateNumber;
    private String birthCertificateDesc;
    private String quality;

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

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
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
}
