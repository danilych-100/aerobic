package ru.ksenia.service.util.report;

/**
 * Created by sokolov_as on 13.03.2017.
 */
public class ColumnDefinition {

    private String header;

    private double width;

    public ColumnDefinition(final String header, final double width) {
        this.header = header;
        this.width = width;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(final String header) {
        this.header = header;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(final double width) {
        this.width = width;
    }
}
