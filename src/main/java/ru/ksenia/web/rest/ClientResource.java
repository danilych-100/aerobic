package ru.ksenia.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ksenia.service.ClientService;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.admin.CommandRequestAdminInfoDTO;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class ClientResource {

    @Autowired
    private ClientService clientService;

    private List<CommandDTO> commandDTOS = new ArrayList<>();
    private List<CommandRequestAdminInfoDTO> requestDTOS = new ArrayList<>();

    @PostConstruct
    public void init() {
        List<String> regions = new ArrayList<String>() {{
            add("Свердловская Область");
            add("Архангельская Область");
            add("Воронежская Область");
            add("Калужская Область");
            add("Московская Область");
        }};

        for (int i = 0; i < 50; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
            int randomName = ThreadLocalRandom.current().nextInt(0, 4);

            CommandDTO commandDTO = new CommandDTO();
            commandDTO.setRegion(regions.get(randomNum));
            commandDTO.setName("Комманда " + randomName);
            commandDTO.setMemberCount((long) randomNum);
            commandDTO.setPhoneNumber("89222933305");
            commandDTO.setEmail("dani@mail.ru");

            CommandCoachDTO commandCoach1 = new CommandCoachDTO();
            commandCoach1.setName("Виктор Борисович");
            commandCoach1.setBirthDate(new Date(123123123123L));
            commandCoach1.setPassportDesc("ОУФМС");
            commandCoach1.setPassportNumber(132354L);
            commandCoach1.setPassportSeries(5523L);

            CommandCoachDTO commandCoach2 = new CommandCoachDTO();
            commandCoach2.setName("Манина Виктория Павловна");
            commandCoach2.setBirthDate(new Date(223123123123L));
            commandCoach2.setPassportDesc("ОУФМС");
            commandCoach2.setPassportNumber(662354L);
            commandCoach2.setPassportSeries(5663L);

            CommandCoachDTO commandCoach3 = new CommandCoachDTO();
            commandCoach3.setName("Глеб Иваноский");
            commandCoach3.setBirthDate(new Date(323123123123L));
            commandCoach3.setPassportDesc("ОУФМС");
            commandCoach3.setPassportNumber(152354L);
            commandCoach3.setPassportSeries(5223L);

            commandDTO.getCoaches().add(commandCoach1);
            commandDTO.getCoaches().add(commandCoach2);
            commandDTO.getCoaches().add(commandCoach3);

            CommandMemberDTO commandMemberDTO1 = new CommandMemberDTO();
            commandMemberDTO1.setName("Игорек");
            commandMemberDTO1.setGender("М");
            commandMemberDTO1.setBirthDate(new Date(388823123123L));
            commandMemberDTO1.setPassportDesc("ОУФМС");
            commandMemberDTO1.setPassportNumber(152311L);
            commandMemberDTO1.setPassportSeries(5444L);
            commandMemberDTO1.setBirthCertificateNumber(52123223L);
            commandMemberDTO1.setBirthCertificateDesc("dfasdasda");
            commandMemberDTO1.setQuality("Мастер спорта");

            CommandMemberDTO commandMemberDTO2 = new CommandMemberDTO();
            commandMemberDTO2.setName("Регина Валентина");
            commandMemberDTO2.setGender("Ж");
            commandMemberDTO2.setBirthDate(new Date(1111993231230L));
            commandMemberDTO2.setQuality("I разряд");

            CommandMemberDTO commandMemberDTO3 = new CommandMemberDTO();
            commandMemberDTO3.setName("Вяхин Иван");
            commandMemberDTO3.setGender("М");
            commandMemberDTO3.setBirthDate(new Date(1111993231230L));
            commandMemberDTO3.setQuality("I разряд");

            commandDTO.getMembers().add(commandMemberDTO1);
            commandDTO.getMembers().add(commandMemberDTO2);
            commandDTO.getMembers().add(commandMemberDTO3);

            CommandRequestAdminInfoDTO commandRequestDTO1 = new CommandRequestAdminInfoDTO();
            commandRequestDTO1.setName("Ласточки мои");
            commandRequestDTO1.setAgeCategory("18+");
            commandRequestDTO1.setNomination("Индивидуальные");
            commandRequestDTO1.setMusicFileName("Ласточки.mp3");
            commandRequestDTO1.setCommandName("Забавушка");
            commandRequestDTO1.setRegion("Архангельская Область");
            commandRequestDTO1.setId((long) (i + 100));

            CommandRequestAdminInfoDTO commandRequestDTO2 = new CommandRequestAdminInfoDTO();
            commandRequestDTO2.setName("Веселая команда");
            commandRequestDTO2.setAgeCategory("12—14");
            commandRequestDTO2.setNomination("Смешанные пары");
            commandRequestDTO2.setMusicFileName("Команда.mp3");
            commandRequestDTO2.setCommandName("Екатеринбуржский ансамбль");
            commandRequestDTO2.setRegion("Свердловская Область");
            commandRequestDTO2.setId((long) (i + 200));

    /*        commandDTO.getRequests().add(commandRequestDTO1);
            commandDTO.getRequests().add(commandRequestDTO2);*/

            requestDTOS.add(commandRequestDTO1);
            requestDTOS.add(commandRequestDTO2);

            commandDTOS.add(commandDTO);
        }

    }

    @GetMapping("/getAllRequests")
    public ResponseEntity<List<CommandRequestAdminInfoDTO>> getAllRequests() {
        return ResponseEntity.ok(requestDTOS);
    }

    @GetMapping("/getAllCommands")
    public ResponseEntity<List<CommandDTO>> getAllCommands() {
        return ResponseEntity.ok(commandDTOS);
    }

    @GetMapping("/getCommands")
    public List<CommandDTO> getCommands() {
        return clientService.getCommands();
    }

    /*   @PostMapping("/registerCommand")
       public ResponseEntity<Void> registerCommand(@RequestBody CommandDTO command) {
           try {
               clientService.registerCommand(command);
               return ResponseEntity.ok().build();
           } catch (Exception e) {
               return ResponseEntity.noContent().build();
           }
       }*/
    @GetMapping("/getCommandForCurrentUser")
    public ResponseEntity<CommandDTO> getCommandForCurrentUser() {
        try {
            return ResponseEntity.ok(clientService.getCommandForCurrentUser());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/updateCommand")
    public ResponseEntity<Void> updateCommand(@RequestBody CommandDTO command) {
        try {
            clientService.updateCommand(command);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    /*@GetMapping("/downloadReport")
    public void downloadReport(final @RequestParam(required = true) String reportId, final HttpServletResponse httpServletResponse){

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");
        headers.put("Content-Disposition", String.format("attachment; filename=report-%s.%s", fmt.print(now), "txt"));

        try {
            writeDataToResponse(
                httpServletResponse,
                "application/octet-stream;",
                headers,
                new byte[0]
            );
        } catch (IOException e) {
            throw new InternalServerErrorException("Не удалось скачать квитанцию");
        }
    }

    private void writeDataToResponse(final HttpServletResponse response, final String contentType, final Map<String, String> headers, final byte[] data) throws IOException {
        response.setContentType(contentType);
        response.setContentLength(data.length);

        for (String header : headers.keySet()) {
            response.setHeader(header, headers.get(header));
        }

        ServletOutputStream outputStream = response.getOutputStream();

        try {
            outputStream.write(data);
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }
*/

}
