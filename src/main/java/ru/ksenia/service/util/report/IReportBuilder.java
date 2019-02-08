package ru.ksenia.service.util.report;

import java.util.List;

/**
 * Created by wanderer on 21.03.2017.
 */
public interface IReportBuilder {

    /**
     * Build report without title.
     * @param columnDefinitions column definition.
     * @param dataGroups group.
     * @return report.
     * @throws Exception
     */
    Report buildReport(List<ColumnDefinition> columnDefinitions, List<DataGroup> dataGroups) throws Exception;

    /**
     * Build report without title.
     * @param columnDefinitions column definition.
     * @param dataGroups group.
     * @param title title.
     * @return report.
     * @throws Exception
     */
    Report buildReport(String title, List<ColumnDefinition> columnDefinitions, List<DataGroup> dataGroups) throws
        Exception;
}
