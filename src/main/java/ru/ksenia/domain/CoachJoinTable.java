package ru.ksenia.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "coaches_join_table")
@IdClass(CoachJoinTable.class)
public class CoachJoinTable implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "command_request_id", nullable = false)
    private Long commandRequestId;

    @Id
    @Column(name = "command_coach_id", nullable = false)
    private Long commandCoachId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommandRequestId() {
        return commandRequestId;
    }

    public void setCommandRequestId(Long commandRequestId) {
        this.commandRequestId = commandRequestId;
    }

    public Long getCommandCoachId() {
        return commandCoachId;
    }

    public void setCommandCoachId(Long commandCoachId) {
        this.commandCoachId = commandCoachId;
    }
}
