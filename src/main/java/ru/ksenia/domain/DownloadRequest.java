package ru.ksenia.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "download_request")
public class DownloadRequest {

    @Id
    private String id;

    @Lob
    @Column(name = "music_file", nullable = false)
    private byte[] musicFile;

    @Column(name = "command_name", nullable = false)
    private String commandName;

    @Column(name = "music_file_name", nullable = false)
    private String musicFileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getMusicFile() {
        return musicFile;
    }

    public void setMusicFile(byte[] musicFile) {
        this.musicFile = musicFile;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getMusicFileName() {
        return musicFileName;
    }

    public void setMusicFileName(String musicFileName) {
        this.musicFileName = musicFileName;
    }
}
