package ru.ksenia.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ksenia.domain.*;
import ru.ksenia.repository.*;
import ru.ksenia.service.dto.MapperCommandDTO;
import ru.ksenia.service.mapper.CommandMapper;
import ru.ksenia.service.util.report.ExcelReportBuilder;
import ru.ksenia.service.util.report.IReportBuilder;
import ru.ksenia.service.util.report.Report;
import ru.ksenia.web.rest.AuditResource;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;
import ru.ksenia.web.rest.dto.admin.CommandRequestAdminInfoDTO;
import ru.ksenia.web.rest.dto.admin.CommandUserInfoDTO;
import ru.ksenia.web.rest.dto.admin.RequestInfoDTO;
import ru.ksenia.web.rest.errors.InternalServerErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.*;

import static org.apache.poi.ss.usermodel.CellStyle.ALIGN_CENTER;
import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;

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

    @Autowired
    private DownloadRequestRepository downloadRequestRepository;

    private IReportBuilder reportBuilder = new ExcelReportBuilder();


    public List<CommandDTO> getCommands() {
        List<CommandDTO> commandDTOS = new ArrayList<>();
        commandRepository.findAll().forEach(command -> {
            commandDTOS.add(CommandMapper.mapEntityToDTO(command));
        });
        return commandDTOS;
    }

    public CommandDTO getCommandForCurrentUser() {
        User currentUser = userService.getUserWithAuthorities()
                                      .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        List<Command> commands = commandRepository.findAllByUserId(currentUser.getId());
        if (commands == null || commands.size() == 0) {
            return new CommandDTO();
        }
        Command currentCommand = commands.get(0);
        return CommandMapper.mapEntityToDTO(currentCommand);
    }

    @Transactional
    public MapperCommandDTO registerCommand(CommandDTO commandDTO, Long userId) {
        Command command = new Command();
        command.setUserId(userId);
        return CommandMapper.mapDTOToEntity(commandDTO, command);
    }


    @Transactional
    public void updateCommand(CommandDTO command) {
        User currentUser = userService.getUserWithAuthorities()
                                      .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        List<Command> commands = commandRepository.findAllByUserId(currentUser.getId());
        if (commands != null && commands.size() != 0) {
            commandRepository.deleteById(commands.get(0).getId());
            commandRepository.flush();

            List<CoachJoinTable> coachJoinTables = coachJoinRepository.findAllByUserId(currentUser.getId());
            if (coachJoinTables != null) {
                coachJoinTables.forEach(coachJoinTable -> {
                    coachJoinRepository.deleteById(coachJoinTable.getId());
                });

                //coachJoinRepository.flush();
            }
            List<MemberJoinTable> memberJoinTables = memberJoinRepository.findAllByUserId(currentUser.getId());
            if (memberJoinTables != null) {
                memberJoinTables.forEach(memberJoinTable -> {
                    memberJoinRepository.deleteById(memberJoinTable.getId());
                });
                //memberJoinRepository.flush();
            }
        }

        MapperCommandDTO mapperCommandDTO = registerCommand(command, currentUser.getId());
        commandRepository.saveAndFlush(mapperCommandDTO.getCommand());

        if (mapperCommandDTO.getMemberJoins() != null && mapperCommandDTO.getMemberJoins().size() > 0) {
            mapperCommandDTO.getMemberJoins().forEach(memberJoinRepository::saveAndFlush);
        }
        if (mapperCommandDTO.getCoachJoins() != null && mapperCommandDTO.getCoachJoins().size() > 0) {
            mapperCommandDTO.getCoachJoins().forEach(coachJoinRepository::saveAndFlush);
        }
    }

    public List<CommandRequestAdminInfoDTO> getAllRequests() throws UnsupportedEncodingException {
        List<CommandRequestAdminInfoDTO> commandRequestAdminInfoDTOS = new ArrayList<>();
        List<Command> commands = commandRepository.findAll();
        for (Command command : commands) {
            if (command.getRequests() == null || command.getRequests().size() == 0) {
                continue;
            }

            for (CommandRequest commandRequest : command.getRequests()) {
                CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = getCommandRequestAdminInfoDTO(command,
                                                                                                      commandRequest);
                commandRequestAdminInfoDTOS.add(commandRequestAdminInfoDTO);
            }
        }
        return commandRequestAdminInfoDTOS;
    }

    public RequestInfoDTO getRequestInfo(String requestId) throws UnsupportedEncodingException {
        CommandRequest commandRequest = commandRequestRepository.findById(requestId).get();
        CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = getCommandRequestAdminInfoDTO(
            commandRequest.getCommand(), commandRequest);

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

    private CommandRequestAdminInfoDTO getCommandRequestAdminInfoDTO(Command command, CommandRequest commandRequest) {
        CommandRequestAdminInfoDTO commandRequestAdminInfoDTO = new CommandRequestAdminInfoDTO();
        commandRequestAdminInfoDTO.setId(commandRequest.getId());
        commandRequestAdminInfoDTO.setName(commandRequest.getName());
        commandRequestAdminInfoDTO.setAgeCategory(commandRequest.getAgeCategory());
        commandRequestAdminInfoDTO.setNomination(commandRequest.getNomination());
        commandRequestAdminInfoDTO.setMusicFileName(commandRequest.getMusicFileName());
        commandRequestAdminInfoDTO.setCommandName(command.getName());
        commandRequestAdminInfoDTO.setRegion(command.getRegion());
        return commandRequestAdminInfoDTO;
    }

    public List<CommandUserInfoDTO> getAllCommandUserInfo() {
        List<CommandUserInfoDTO> commandUserInfoDTOS = new ArrayList<>();
        List<Command> commands = commandRepository.findAll();
        for (Command command : commands) {
            if (command.getRequests() == null || command.getRequests().size() == 0) {
                continue;
            }

            User user = userService.getUserWithAuthorities(command.getUserId()).get();

            CommandUserInfoDTO commandUserInfoDTO = new CommandUserInfoDTO();
            commandUserInfoDTO.setCommandId(command.getId());
            commandUserInfoDTO.setCommandName(command.getName());
            commandUserInfoDTO.setRegion(command.getRegion());
            commandUserInfoDTO.setPhoneNumber(command.getPhoneNumber());
            commandUserInfoDTO.setMail(command.getEmail());
            if (user.getLastName() != null && user.getFirstName() != null) {
                commandUserInfoDTO.setUserName(user.getLastName() + " " + user.getFirstName());
            }
            else if (user.getLastName() != null) {
                commandUserInfoDTO.setUserName(user.getLastName());
            }
            else if (user.getFirstName() != null) {
                commandUserInfoDTO.setUserName(user.getFirstName());
            }
            else {
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

    @Transactional
    public void saveFileToDownloadRequest(
        String requestId, byte[] fileBytes, String requestName, String musicFileName) {
        List<DownloadRequest> founded = downloadRequestRepository.getAllByRequestId(requestId);
        if (founded != null && founded.size() > 0) {
            DownloadRequest downloadRequest = founded.get(0);
            downloadRequest.setMusicFile(fileBytes);
            downloadRequest.setRequestName(requestName);
            downloadRequest.setMusicFileName(musicFileName);
            downloadRequestRepository.save(downloadRequest);
            return;
        }
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.setId(UUID.randomUUID().toString());
        downloadRequest.setRequestId(requestId);
        downloadRequest.setMusicFile(fileBytes);
        downloadRequest.setRequestName(requestName);
        downloadRequest.setMusicFileName(musicFileName);
        downloadRequestRepository.save(downloadRequest);
    }

    public CommandRequest getRequestById(String id) {
        return commandRequestRepository.findById(id).get();
    }

    public Report createUsersExcelReport() throws Exception {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Пользователи");
        sheet.setDefaultColumnWidth(40);

        CellStyle cellStyleHeader = workbook.createCellStyle();

        // Перенос текста
        cellStyleHeader.setWrapText(true);

        // Позиционирование
        cellStyleHeader.setAlignment(ALIGN_CENTER);
        cellStyleHeader.setVerticalAlignment(ALIGN_CENTER);
        cellStyleHeader.setIndention((short) 1);
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyleHeader.setFont(font);

        CellStyle styleSimple = workbook.createCellStyle();
        styleSimple.setAlignment(ALIGN_CENTER);
        styleSimple.setVerticalAlignment(ALIGN_CENTER);

        CellStyle cellStyle = ExcelReportBuilder.createColumnHeadersCellStyle(workbook, true);

        List<CommandUserInfoDTO> commandUserInfos = getAllCommandUserInfo();
        commandUserInfos.sort(new Comparator<CommandUserInfoDTO>() {
            @Override
            public int compare(CommandUserInfoDTO o1, CommandUserInfoDTO o2) {
                return o1.getUserName().compareTo(o2.getUserName());
            }
        });

        int rowCount = 0;
        Row headerForNames = sheet.createRow(rowCount);
        Cell cell1 = headerForNames.createCell(0);
        cell1.setCellValue("ФИО пользователя");
        cell1.setCellStyle(cellStyle);
        Cell cell2 = headerForNames.createCell(1);
        cell2.setCellValue("Название команды");
        cell2.setCellStyle(cellStyle);
        Cell cell3 = headerForNames.createCell(2);
        cell3.setCellValue("Субъект РФ");
        cell3.setCellStyle(cellStyle);
        Cell cell4 = headerForNames.createCell(3);
        cell4.setCellValue("Телефон");
        cell4.setCellStyle(cellStyle);
        Cell cell5 = headerForNames.createCell(4);
        cell5.setCellValue("Почта");
        cell5.setCellStyle(cellStyle);

        rowCount++;

        for (CommandUserInfoDTO commandUserInfoDTO : commandUserInfos) {
            Row headerForRequest = sheet.createRow(rowCount);
            headerForRequest.setRowStyle(styleSimple);
            headerForRequest.createCell(0).setCellValue(commandUserInfoDTO.getUserName());
            headerForRequest.createCell(1).setCellValue(commandUserInfoDTO.getCommandName());
            headerForRequest.createCell(2).setCellValue(commandUserInfoDTO.getRegion());
            headerForRequest.createCell(3).setCellValue(commandUserInfoDTO.getPhoneNumber());
            headerForRequest.createCell(4).setCellValue(commandUserInfoDTO.getMail());

            rowCount++;
        }

        try {
            Report report = new Report();
            report.setExtension("xlsx");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            report.setContent(baos.toByteArray());
            return report;
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public Report createExcelReportFile() throws Exception {
        Workbook workbook = new HSSFWorkbook();

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        CellStyle cellStyleHeader = workbook.createCellStyle();

        // Перенос текста
        cellStyleHeader.setWrapText(true);

        // Позиционирование
        cellStyleHeader.setAlignment(ALIGN_CENTER);
        cellStyleHeader.setVerticalAlignment(ALIGN_CENTER);
        cellStyleHeader.setIndention((short) 1);

        CellStyle styleSimple = workbook.createCellStyle();
        styleSimple.setAlignment(ALIGN_CENTER);
        styleSimple.setVerticalAlignment(ALIGN_CENTER);

        CellStyle cellStyleForMainHeader = ExcelReportBuilder.createColumnHeadersCellStyle(workbook, true);
        CellStyle cellStyleForMainHeaderNonBold = ExcelReportBuilder.createColumnHeadersCellStyle(workbook, false);
        CellStyle cellStyleForSimpleHeader = ExcelReportBuilder.createColumnSimpleHeadersCellStyle(workbook, false);

        List<CommandRequest> commandRequests = commandRequestRepository.findAll();
        commandRequests.sort(new Comparator<CommandRequest>() {
            @Override
            public int compare(CommandRequest o1, CommandRequest o2) {
                int compareRes = o1.getAgeCategory().compareTo(o2.getAgeCategory());

                if (compareRes == 0) {
                    compareRes = o1.getNomination().compareTo(o2.getNomination());
                }
                if (compareRes == 0) {
                    compareRes = o1.getCommand().getRegion().compareTo(o2.getCommand().getRegion());
                }
                if (compareRes == 0 && o1.getName() != null && o2.getName() != null) {
                    compareRes = o1.getName().compareTo(o2.getName());
                }

                return compareRes;
            }
        });

        int rowCount = 0;
        String lastCategory = "";
        String lastNomenee = "";

        Sheet sheet = null;
        for (CommandRequest commandRequest : commandRequests) {

            if(!lastCategory.equals(commandRequest.getAgeCategory())){
                sheet = workbook.createSheet(commandRequest.getAgeCategory());
                sheet.setDefaultColumnWidth(30);
                rowCount = 0;
                Row header = sheet.createRow(rowCount);
                Cell headerCell = header.createCell(0);
                headerCell.setCellValue("Возрастная категория: " + commandRequest.getAgeCategory());
                headerCell.setCellStyle(cellStyleForMainHeader);
                header.createCell(1);
                header.createCell(2);
                header.createCell(3);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 3));

                lastCategory = commandRequest.getAgeCategory();
                rowCount++;
            }

            if(sheet == null){
                sheet = workbook.createSheet(commandRequest.getAgeCategory());
                sheet.setDefaultColumnWidth(40);
            }

            if(!lastNomenee.equals(commandRequest.getNomination())){
                Row header = sheet.createRow(rowCount);
                Cell headerCell = header.createCell(0);
                headerCell.setCellValue("Номинация: " + commandRequest.getNomination());
                headerCell.setCellStyle(cellStyleForMainHeaderNonBold);
                header.createCell(1);
                header.createCell(2);
                header.createCell(3);
                sheet.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 0, 3));

                rowCount++;
                Row headerForNames = sheet.createRow(rowCount);
                Cell cell1 = headerForNames.createCell(0);
                cell1.setCellValue("Субъект РФ");
                cell1.setCellStyle(cellStyleForSimpleHeader);
                Cell cell2 = headerForNames.createCell(1);
                cell2.setCellValue("Название команды");
                cell2.setCellStyle(cellStyleForSimpleHeader);
                Cell cell3 = headerForNames.createCell(2);
                cell3.setCellValue("ФИО участников");
                cell3.setCellStyle(cellStyleForSimpleHeader);
                Cell cell4 = headerForNames.createCell(3);
                cell4.setCellValue("ФИО тренеров");
                cell4.setCellStyle(cellStyleForSimpleHeader);

                lastNomenee = commandRequest.getNomination();
                rowCount++;
            }


            Row headerForRequest = sheet.createRow(rowCount);
            Cell reg = headerForRequest.createCell(0);
            reg.setCellValue(commandRequest.getCommand().getRegion());
            reg.setCellStyle(styleSimple);
            Cell commandName = headerForRequest.createCell(1);
            commandName.setCellValue(commandRequest.getCommand().getName());
            commandName.setCellStyle(styleSimple);

            int startReqRow = rowCount;

            if(commandRequest.getMembers().size() >= commandRequest.getCoaches().size()){
                for(int i = 0; i < commandRequest.getMembers().size(); i++){
                    rowCount++;

                    CommandMember commandMember = commandRequest.getMembers().get(i);
                    Row reqRow = sheet.createRow(rowCount);
                    reqRow.createCell(2).setCellValue(i + ") участник" + commandMember.getName() + " " + commandMember.getBirthDate());
                    if(commandRequest.getCoaches().size() > i){
                        CommandCoach commandCoach = commandRequest.getCoaches().get(i);
                        reqRow.createCell(3).setCellValue(i + ") тренер" + commandCoach.getName() + " " + commandCoach
                            .getBirthDate());
                    }
                }
            }
            if(commandRequest.getMembers().size() < commandRequest.getCoaches().size()){
                for(int i = 0; i < commandRequest.getCoaches().size(); i++){
                    rowCount++;

                    CommandCoach commandCoach = commandRequest.getCoaches().get(i);
                    Row reqRow = sheet.createRow(rowCount);
                    reqRow.createCell(3).setCellValue(i + ") тренер" + commandCoach.getName() + " " + commandCoach.getBirthDate());
                    if(commandRequest.getMembers().size() > i){
                        CommandMember commandMember = commandRequest.getMembers().get(i);
                        reqRow.createCell(2).setCellValue(i + ") участник" + commandMember.getName() + " " + commandMember.getBirthDate());
                    }
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(startReqRow, rowCount, 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(startReqRow, rowCount, 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(startReqRow, rowCount, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(startReqRow, rowCount, 3, 3));

            rowCount++;
        }

        try {
            Report report = new Report();
            report.setExtension("xlsx");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            report.setContent(baos.toByteArray());
            return report;
        } catch (IOException e) {
            throw new Exception(e);
        }
        //return reportBuilder.buildReport();
    }
}
