package ru.ksenia.web.rest.dto;

import org.joda.time.LocalDate;

public class Record {
    private String nameClient;
    public Long police;
    public LocalDate birthDay;
    public String gender;

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public Long getPolice() {
        return police;
    }

    public void setPolice(Long police) {
        this.police = police;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
