package ru.ksenia.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "members_join_table")
@IdClass(MemberJoinTable.class)
public class MemberJoinTable implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "command_request_id", nullable = false)
    private Long commandRequestId;

    @Id
    @Column(name = "command_member_id", nullable = false)
    private Long commandMemberId;

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

    public Long getCommandMemberId() {
        return commandMemberId;
    }

    public void setCommandMemberId(Long commandMemberId) {
        this.commandMemberId = commandMemberId;
    }
}
