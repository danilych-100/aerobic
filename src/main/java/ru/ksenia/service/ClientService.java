package ru.ksenia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.domain.*;
import ru.ksenia.repository.*;
import ru.ksenia.service.mapper.CommandMapper;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;
import ru.ksenia.web.rest.dto.admin.CommandRequestAdminInfoDTO;
import ru.ksenia.web.rest.dto.admin.CommandUserInfoDTO;
import ru.ksenia.web.rest.dto.admin.RequestInfoDTO;
import ru.ksenia.web.rest.errors.InternalServerErrorException;

import java.io.UnsupportedEncodingException;
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
    private CommandRequestRepository commandRequestRepository;

    @Autowired
    private CoachJoinRepository coachJoinRepository;

    @Autowired
    private MemberJoinRepository memberJoinRepository;


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


    @Transactional
    public void updateCommand(CommandDTO command) {
        User currentUser = userService.getUserWithAuthorities().orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        List<Command> commands = commandRepository.findAllByUserId(currentUser.getId());
        if(commands != null && commands.size() != 0){
            List<CoachJoinTable> coachJoinTables = coachJoinRepository.findAllByUserId(currentUser.getId());
            if(coachJoinTables != null){
                coachJoinTables.forEach(coachJoinTable -> {
                    coachJoinRepository.delete(coachJoinTable);
                });
                coachJoinRepository.flush();
            }
            List<MemberJoinTable> memberJoinTables = memberJoinRepository.findAllByUserId(currentUser.getId());
            if(memberJoinTables != null){
                memberJoinTables.forEach(memberJoinTable -> {
                    memberJoinRepository.delete(memberJoinTable);
                });
                memberJoinRepository.flush();
            }
            commandRepository.deleteById(commands.get(0).getId());
            commandRepository.flush();
        }
        commandRepository.saveAndFlush(registerCommand(command, currentUser.getId(), null));
    }

    public List<CommandRequestAdminInfoDTO> getAllRequests() throws UnsupportedEncodingException {
        List<CommandRequestAdminInfoDTO> commandRequestAdminInfoDTOS = new ArrayList<>();
        List<Command> commands = commandRepository.findAll();
        for(Command command : commands){
            if(command.getRequests() == null || command.getRequests().size() == 0){
                continue;
            }

            for(CommandRequest commandRequest : command.getRequests()){
                CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = getCommandRequestAdminInfoDTO(command,
                                                                                                      commandRequest);
                commandRequestAdminInfoDTOS.add(commandRequestAdminInfoDTO);
            }
        }
        return commandRequestAdminInfoDTOS;
    }

    public RequestInfoDTO getRequestInfo(Long requestId) throws UnsupportedEncodingException {
        CommandRequest commandRequest = commandRequestRepository.findById(requestId).get();
        CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = getCommandRequestAdminInfoDTO(commandRequest.getCommand(),
                                                                                              commandRequest);

        RequestInfoDTO requestInfoDTO = new RequestInfoDTO();
        requestInfoDTO.setGeneralInfo(commandRequestAdminInfoDTO);
        requestInfoDTO.setPhoneNumber(commandRequest.getCommand().getPhoneNumber());
        requestInfoDTO.setMail(commandRequest.getCommand().getEmail());

        List<CommandMemberDTO> commandMemberDTOS = new ArrayList<>();
        commandRequest.getMembers().forEach(commandMember -> {
            commandMemberDTOS.add(CommandMapper.mapCommandMemberEntityToDTO(commandMember));
        });
        requestInfoDTO.setMembers(commandMemberDTOS);

        List<CommandCoachDTO> commandCoachDTOS = new ArrayList<>();
        commandRequest.getCoaches().forEach(commandCoach -> {
            commandCoachDTOS.add(CommandMapper.mapCommandCoachEntityToDTO(commandCoach));
        });
        requestInfoDTO.setCoaches(commandCoachDTOS);

        return requestInfoDTO;
    }

    private CommandRequestAdminInfoDTO getCommandRequestAdminInfoDTO(Command command, CommandRequest commandRequest)
        throws UnsupportedEncodingException {
        CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = new CommandRequestAdminInfoDTO();
        commandRequestAdminInfoDTO.setId(commandRequest.getId());
        commandRequestAdminInfoDTO.setName(commandRequest.getName());
        commandRequestAdminInfoDTO.setAgeCategory(commandRequest.getAgeCategory());
        commandRequestAdminInfoDTO.setNomination(commandRequest.getNomination());
        if(commandRequest.getMusic() != null){
            commandRequestAdminInfoDTO.setMusic(new String(commandRequest.getMusic(), "UTF-8"));
        }
        commandRequestAdminInfoDTO.setMusicFileName(commandRequest.getMusicFileName());
        commandRequestAdminInfoDTO.setCommandName(command.getName());
        commandRequestAdminInfoDTO.setRegion(command.getRegion());
        return commandRequestAdminInfoDTO;
    }

    public List<CommandUserInfoDTO> getAllCommandUserInfo() {
        List<CommandUserInfoDTO> commandUserInfoDTOS = new ArrayList<>();
        List<Command> commands = commandRepository.findAll();
        for(Command command : commands){
            if(command.getRequests() == null || command.getRequests().size() == 0){
                continue;
            }

            User user = userService.getUserWithAuthorities(command.getUserId()).get();

            CommandUserInfoDTO commandUserInfoDTO = new CommandUserInfoDTO();
            commandUserInfoDTO.setCommandId(command.getId());
            commandUserInfoDTO.setCommandName(command.getName());
            commandUserInfoDTO.setRegion(command.getRegion());
            commandUserInfoDTO.setPhoneNumber(command.getPhoneNumber());
            commandUserInfoDTO.setMail(command.getEmail());
            if(user.getLastName() != null && user.getFirstName() != null){
                commandUserInfoDTO.setUserName(user.getLastName() + " " + user.getFirstName());
            } else if(user.getLastName() != null){
                commandUserInfoDTO.setUserName(user.getLastName());
            } else if(user.getFirstName() != null){
                commandUserInfoDTO.setUserName(user.getFirstName());
            } else {
                commandUserInfoDTO.setUserName(user.getEmail());
            }

            commandUserInfoDTOS.add(commandUserInfoDTO);
        }
        return commandUserInfoDTOS;
    }

    public CommandDTO getCommand(Long commandId) {
        Command command = commandRepository.findById(commandId).get();
        return CommandMapper.mapEntityToDTO(command);
    }
}
