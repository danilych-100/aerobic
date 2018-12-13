package ru.ksenia.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "download_request")
public class DownloadRequest {

    @Id
    private String id;

    @Column(name = "request_id", nullable = false)
    private String requestId;

    @Lob
    @Column(name = "music_file", nullable = false)
    private byte[] musicFile;


    @Column(name = "request_name", nullable = false)
    private String requestName;

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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMusicFileName() {
        return musicFileName;
    }

    public void setMusicFileName(String musicFileName) {
        this.musicFileName = musicFileName;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }
}
