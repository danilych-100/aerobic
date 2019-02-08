package ru.ksenia.service.util.report;

/**
 * Created by wanderer on 21.03.2017.
 */
public class Report {
    private byte[] content;
    private String extension;

    public byte[] getContent() {
        return content;
    }

    public void setContent(final byte[] content) {
        this.content = content;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }
}
