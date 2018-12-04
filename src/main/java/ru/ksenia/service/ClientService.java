package ru.ksenia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.domain.*;
import ru.ksenia.repository.CommandCoachRepository;
import ru.ksenia.repository.CommandMemberRepository;
import ru.ksenia.repository.CommandRepository;
import ru.ksenia.repository.CommandRequestRepository;
import ru.ksenia.service.mapper.CommandMapper;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;
import ru.ksenia.web.rest.errors.InternalServerErrorException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing audit events.
 * <p>
 * This is the default implementation to support SpringBoot Actuator AuditEventRepository
 */
@Service
@Transactional
public class ClientService {

    @Autowired
    private UserService userService;

    @Autowired
    private CommandRepository commandRepository;

    @Autowired
    private CommandCoachRepository commandCoachRepository;

    @Autowired
    private CommandMemberRepository commandMemberRepository;

    @Autowired
    private CommandRequestRepository commandRequestRepository;

    public List<CommandDTO> getCommands(){
        List<CommandDTO> commandDTOS = new ArrayList<>();
        commandRepository.findAll().forEach(command -> {
            commandDTOS.add(CommandMapper.mapEntityToDTO(command));
        });
        return commandDTOS;
    }

    public CommandDTO getCommandForCurrentUser(){
        User currentUser = userService.getUserWithAuthorities().orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        List<Command> commands = commandRepository.findAllByUserId(currentUser.getId());
        if(commands == null || commands.size() == 0){
            return new CommandDTO();
        }
        Command currentCommand = commands.get(0);
        return CommandMapper.mapEntityToDTO(currentCommand);
    }

    @Transactional
    public Command registerCommand(CommandDTO commandDTO, Long userId, Command existedCommand) {
        if(existedCommand != null){
            Command command = CommandMapper.mapDTOToEntity(commandDTO, existedCommand);
            command.setUserId(userId);
            return command;
        }
        Command command = new Command();
        command.setUserId(userId);
        return CommandMapper.mapDTOToEntity(commandDTO, command);
    }



    public void updateCommand(CommandDTO command) {
        User currentUser = userService.getUserWithAuthorities().orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        List<Command> commands = commandRepository.findAllByUserId(currentUser.getId());
        if(commands != null && commands.size() != 0){
            commandRepository.deleteById(commands.get(0).getId());
            commandRepository.flush();
        }
        commandRepository.saveAndFlush(registerCommand(command, currentUser.getId(), null));
    }
}
