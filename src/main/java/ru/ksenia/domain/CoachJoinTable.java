package ru.ksenia.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "coaches_join_table")
public class CoachJoinTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_request_id")
    private CommandRequest commandRequest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "command_coach_id")
    private CommandCoach commandCoach;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public CommandRequest getCommandRequest() {
        return commandRequest;
    }

    public void setCommandRequest(CommandRequest commandRequest) {
        this.commandRequest = commandRequest;
    }

    public CommandCoach getCommandCoach() {
        return commandCoach;
    }

    public void setCommandCoach(CommandCoach commandCoach) {
        this.commandCoach = commandCoach;
    }
}
