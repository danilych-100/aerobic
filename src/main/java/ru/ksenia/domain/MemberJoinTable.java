package ru.ksenia.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "members_join_table")
public class MemberJoinTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_request_id")
    private CommandRequest commandRequest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_member_id")
    private CommandMember commandMember;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommandRequest getCommandRequest() {
        return commandRequest;
    }

    public void setCommandRequest(CommandRequest commandRequest) {
        this.commandRequest = commandRequest;
    }

    public CommandMember getCommandMember() {
        return commandMember;
    }

    public void setCommandMember(CommandMember commandMember) {
        this.commandMember = commandMember;
    }
}
