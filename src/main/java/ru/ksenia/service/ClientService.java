package ru.ksenia.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.web.rest.dto.CommandDTO;

import java.util.List;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class ClientService {

    public List<CommandDTO> getCommands(){
        return null;
    }

    @Transactional
    public void registerCommand(CommandDTO command) {

    }
}
