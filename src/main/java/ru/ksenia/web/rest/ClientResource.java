package ru.ksenia.web.rest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.ksenia.domain.CommandRequest;
import ru.ksenia.domain.DownloadRequest;
import ru.ksenia.repository.DownloadRequestRepository;
import ru.ksenia.service.ClientService;
import ru.ksenia.web.rest.dto.CommandCoachDTO;
import ru.ksenia.web.rest.dto.CommandDTO;
import ru.ksenia.web.rest.dto.CommandMemberDTO;
import ru.ksenia.web.rest.dto.CommandRequestDTO;
import ru.ksenia.web.rest.dto.admin.*;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api")
public class ClientResource {

    @Autowired
    private ClientService clientService;

    @Autowired
    private DownloadRequestRepository downloadRequestRepository;

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

            CommandRequestAdminInfoDTO commandRequestDTO2 = new CommandRequestAdminInfoDTO();
            commandRequestDTO2.setName("Веселая команда");
            commandRequestDTO2.setAgeCategory("12—14");
            commandRequestDTO2.setNomination("Смешанные пары");
            commandRequestDTO2.setMusicFileName("Команда.mp3");
            commandRequestDTO2.setCommandName("Екатеринбуржский ансамбль");
            commandRequestDTO2.setRegion("Свердловская Область");

            CommandRequestDTO commandRequestDTOOld1 = new CommandRequestDTO();
            commandRequestDTOOld1.setName("Веселая команда");
            commandRequestDTOOld1.setAgeCategory("12—14");
            commandRequestDTOOld1.setNomination("Смешанные пары");
            commandRequestDTOOld1.setMusicFileName("Команда.mp3");
            commandRequestDTOOld1.setMusic("123asdawdadawWE@32313=i62523");
            commandRequestDTOOld1.setMembers(Arrays.asList(commandMemberDTO2, commandMemberDTO3));
            commandRequestDTOOld1.setCoaches(Collections.singletonList(commandCoach2));

            commandDTO.getRequests().add(commandRequestDTOOld1);
            commandDTO.getRequests().add(commandRequestDTOOld1);

            requestDTOS.add(commandRequestDTO1);
            requestDTOS.add(commandRequestDTO2);

            commandDTOS.add(commandDTO);
        }

    }

    @GetMapping("/getCommand")
    public ResponseEntity<CommandDTO> getCommand(@RequestParam Long commandId) {
        return ResponseEntity.ok(clientService.getCommand(commandId));
    }

    @GetMapping("/getAllCommandUserInfo")
    public ResponseEntity<List<CommandUserInfoDTO>> getAllCommandUserInfo() {
        return ResponseEntity.ok(clientService.getAllCommandUserInfo());
    }

    @GetMapping("/getRequestInfo")
    public ResponseEntity<RequestInfoDTO> getRequestInfo(@RequestParam String requestId)
        throws UnsupportedEncodingException {
        return ResponseEntity.ok(clientService.getRequestInfo(requestId));
    }

    @GetMapping("/getAllRequests")
    public ResponseEntity<List<CommandRequestAdminInfoDTO>> getAllRequests() throws UnsupportedEncodingException {
        return ResponseEntity.ok(clientService.getAllRequests());
    }

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

    @PostMapping(value = "/downloadFileMusic")
    public @ResponseBody ResponseEntity<byte[]> downloadFileMusic(final @RequestBody(required = true)DonwloadFileRequest
                                                          donwloadFileReques, HttpServletResponse response) {

        String[] splittedFileName = donwloadFileReques.getMusicFileName().split(".");
        String ext = splittedFileName.length == 2 ? splittedFileName[1] : "mp3";

        DateTime now = DateTime.now();
        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("dd.MM.YY-HHmm");

        response.setHeader("Content-Disposition", String.format("attachment; filename=%s-%s.%s", transliterate
            (donwloadFileReques.getCommandName()), fmt.print(now), ext));
        response.setContentType("audio/mpeg");

        return ResponseEntity.ok(donwloadFileReques.getMusicFile().getBytes());
    }

    @PostMapping(value = "/saveFileToRequest")
    @ResponseStatus(HttpStatus.OK)
    public void saveFileToRequest(@RequestParam("file") MultipartFile file,
                                  @RequestParam("requestId") String requestId,
                                  @RequestParam("requestName") String requestName,
                                  @RequestParam("musicFileName") String musicFileName) throws IOException {
        clientService.saveFileToDownloadRequest(requestId, file.getBytes(), requestName, musicFileName);
    }

    @GetMapping("/downloadMusicFile")
    public void saveDownloadedMusicFile(final @RequestParam(required = true)String id,
                                        final @RequestParam(required = true)String commandName,
                                        final HttpServletResponse httpServletResponse) throws IOException {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("cache-control", "must-revalidate");

        DownloadRequest downloadRequest = downloadRequestRepository.getAllByRequestId(id).get(0);

        String[] splittedFileName = downloadRequest.getMusicFileName().split(".");
        String ext = splittedFileName.length == 2 ? splittedFileName[1] : "mp3";

        String name = downloadRequest.getRequestName() != null && !downloadRequest.getRequestName().isEmpty()
            ? downloadRequest.getRequestName()
            : "Unnamed";
        headers.put("Content-Disposition", String.format("attachment; filename=%s.%s", transliterate(name), ext));

        writeDataToResponse(
            httpServletResponse,
            "application/octet-stream;",
            headers,
            downloadRequest.getMusicFile()
        );
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

    private String transliterate(String message) {
        char[] abcCyr = {' ', 'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', 'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ',', '.', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        String[] abcLat = {" ", "a", "b", "v", "g", "d", "e", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "h", "ts", "ch", "sh", "sch", "", "i", "", "e", "ju", "ja", "A", "B", "V", "G", "D", "E", "E", "Zh", "Z", "I", "Y", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "H", "Ts", "Ch", "Sh", "Sch", "", "I", "", "E", "Ju", "Ja", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", ",", ".", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }


    @GetMapping("/getAllCommands")
    public ResponseEntity<List<CommandDTO>> getAllCommands() {
        return ResponseEntity.ok(commandDTOS);
    }

    @GetMapping("/getCommands")
    public List<CommandDTO> getCommands() {
        return clientService.getCommands();
    }
}
