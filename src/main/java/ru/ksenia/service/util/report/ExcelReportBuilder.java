package ru.ksenia.service.util.report;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.apache.poi.ss.usermodel.Font.BOLDWEIGHT_BOLD;

/**
 * Created by wanderer on 21.03.2017.
 */
public class ExcelReportBuilder implements IReportBuilder {
    private static final String EXCEL_EXTENSION = "xlsx";

    private static final int MAX_ROW_COUNT = 40000;

    @Override
    public Report buildReport(final List<ColumnDefinition> columnDefinitions, final List<DataGroup> dataGroups) throws
        Exception {
        return buildXLSReport(null, columnDefinitions, dataGroups);
    }

    @Override
    public Report buildReport(final String title, final List<ColumnDefinition> columnDefinitions, final List<DataGroup> dataGroups) throws
        Exception {
        return buildXLSReport(title, columnDefinitions, dataGroups);
    }

    /**
     * Build report.
     * @param title title.
     * @param columnDefinitions column definition.
     * @param dataGroups groups.
     * @return report.
     * @throws Exception
     */
    private Report buildXLSReport(final String title, final List<ColumnDefinition> columnDefinitions, final List<DataGroup> dataGroups) throws
        Exception {
        Workbook wb = new HSSFWorkbook();

        if(dataGroups.size() > MAX_ROW_COUNT){
            for(int i = 0 ; i < dataGroups.size(); i+=MAX_ROW_COUNT){
                int startInd = i;
                int endInd = dataGroups.size() <= i + MAX_ROW_COUNT ? dataGroups.size() : i + MAX_ROW_COUNT;
                List<DataGroup> subDataGroups = dataGroups.subList(startInd, endInd);
                fillXLSSheetData(title, columnDefinitions, wb, subDataGroups);
            }

        } else {
            fillXLSSheetData(title, columnDefinitions, wb, dataGroups);
        }

        try {
            Report report = new Report();
            report.setExtension(EXCEL_EXTENSION);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            wb.write(baos);
            report.setContent(baos.toByteArray());
            return report;
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    private void fillXLSSheetData(String title, List<ColumnDefinition> columnDefinitions, Workbook wb, List<DataGroup> subDataGroups) {
        Sheet sheet = wb.createSheet();

        final int startRowIndex = 0;
        final int startColumnIndex = 0;

        int currentRowIndex = startRowIndex;
        if (title != null) {
            Row titleRow = sheet.createRow(currentRowIndex++);

            Cell cell = titleRow.createCell(0);
            cell.setCellValue(title);
            cell.setCellStyle(createTitleCellStyle(wb));
            sheet.addMergedRegion(new CellRangeAddress(
                    titleRow.getRowNum(),
                    titleRow.getRowNum(),
                    startColumnIndex,
                    startColumnIndex + columnDefinitions.size() - 1)
            );

            sheet.createRow(currentRowIndex++);
        }

        Row headersRow = sheet.createRow(currentRowIndex++);

        CellStyle columnHeadersCellStyle = createColumnHeadersCellStyle(wb, true);
        int currentColumnIndex = startColumnIndex;
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            Cell headersRowCell = headersRow.createCell(currentColumnIndex);
            headersRowCell.setCellValue(columnDefinition.getHeader());
            headersRowCell.setCellStyle(columnHeadersCellStyle);
            sheet.setColumnWidth(currentColumnIndex, (int) Math.round(columnDefinition.getWidth() * 256));

            currentColumnIndex++;
        }

        Row startRow = sheet.createRow(currentRowIndex);

        startRow.setRowStyle(createReportFieldsCellStyle(wb));

        DataGroup syntacticRootGroup = new DataGroup(Collections.<String>emptyList(), subDataGroups);
        new DataGroupProcessingContext(sheet, startRow, startColumnIndex, syntacticRootGroup, columnDefinitions.size()).processGroup(wb.createCellStyle());
    }

    private static class DataGroupProcessingContext {

        private final Sheet sheet;
        private final int columnsCount;
        private final Row rowStart;
        private final CellStyle cellStyle;
        private Row rowEnd;

        private final Integer startGroupColumnIndex;
        private final Integer endGroupColumnIndex;

        private final DataGroup dataGroup;

        DataGroupProcessingContext(final Sheet sheet, final Row rowStart, final Integer startGroupColumnIndex, final DataGroup dataGroup, final int columnsCount) {
            this.sheet = sheet;
            this.rowStart = rowStart;
            this.cellStyle = rowStart.getRowStyle();
            this.startGroupColumnIndex = startGroupColumnIndex;
            this.dataGroup = dataGroup;
            this.columnsCount = columnsCount;

            endGroupColumnIndex = startGroupColumnIndex + dataGroup.getSharedData().size();
        }

        /**
         * отложенные вычисления. нужны дабы не создавать рекурсию конструкторов
         */
        public void processGroup(CellStyle tmp) {
            if (dataGroup.isBold()){
                tmp = createColumnHeadersCellStyle(sheet.getWorkbook(), true);
                sheet.addMergedRegion(new CellRangeAddress(rowStart.getRowNum(), rowStart.getRowNum(), 0, columnsCount - 1));
            } else {
                tmp = createColumnHeadersCellStyle(sheet.getWorkbook(), false);

            }
            // сначала проставляем в первую строчку группы значения общие для группы
            Integer columnIndex = this.startGroupColumnIndex;
            for (String cellValue : dataGroup.getSharedData()) {
                Cell cell = rowStart.createCell(columnIndex);
                columnIndex++;

                cell.setCellValue(cellValue);
                cell.setCellStyle(tmp);
            }

            if (dataGroup.isBold()) {
                for (int i = columnIndex; i < columnsCount; ++i){
                    Cell cell = rowStart.createCell(i);
                    cell.setCellStyle(tmp);
                }
            }
            // затем если это листовая группа то просто вычисляем и создаем следующую строку с i+1 индексом
            if (dataGroup.isLeafGroup()) {
                rowEnd = sheet.createRow(rowStart.getRowNum() + 1);
                rowEnd.setRowStyle(cellStyle);
            } else {
                // если группа содержит подгруппы - то рекурсивно проводим теже вычисления для подгрупп
                // где контекст сдвинут по столбцам на ширину текущей группы,
                // а по строкам - скользит так, что каждая новая группа начинается (по индексу строки) с конца предыдущей
                // (для этого после того как вычислилась очередная подгруппа - мы сохраняем ее последнюю строчку в currentRow)
                // после того как вычислились все подгруппы нам известны индексы строки и столбца начала данной группы (из конструктора при формировании контекста),
                // известен индекс последнего столбца (вычисляется в конструкторе как начальный сдвинутый на ширину sharedData)
                // и становится известен индекс последней строки всех подгрупп данной группы
                // по этим четырем границам мы можем сделать вертикальные мерджи ячеек
                Row currentRow = rowStart;
                for (DataGroup subGroup : dataGroup.getSubGroups()) {
                    DataGroupProcessingContext processingContext = new DataGroupProcessingContext(sheet, currentRow, endGroupColumnIndex, subGroup, this.columnsCount);
                    processingContext.processGroup(tmp);
                    currentRow = processingContext.getRowEnd();
                }
                rowEnd = currentRow;

                for (int i = startGroupColumnIndex; i < endGroupColumnIndex; i++) {
                    // если высота группы = 1, то мердж на единичную ячейку не нужен и не возможен
                    if (rowEnd.getRowNum() - rowStart.getRowNum() > 1) {
                        // тк CellRangeAddress включает последние индексы то нужно делать -1

                        for (int j = rowStart.getRowNum() + 1; j < rowEnd.getRowNum(); ++j) {
                            Cell emptyCell = sheet.getRow(j).createCell(i);
                            emptyCell.setCellStyle(createColumnHeadersCellStyle(sheet.getWorkbook(), false));
                        }
                        sheet.addMergedRegion(new CellRangeAddress(rowStart.getRowNum(), rowEnd.getRowNum() - 1, i, i));
                    }
                }
            }
        }

        public Row getRowEnd() {
            return rowEnd;
        }
    }

    /**
     * Create style for title.
     * @param workbook workbook.
     * @return cellStyle.
     */
    private static CellStyle createTitleCellStyle(final Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // Перенос текста
        cellStyle.setWrapText(true);

        // Позиционирование
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setIndention((short) 1);

        // шрифт Arial - 14 - bold
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 14);
        titleFont.setFontName("Arial");
        titleFont.setBoldweight(BOLDWEIGHT_BOLD);
        cellStyle.setFont(titleFont);

        return cellStyle;
    }

    /**
     * Create style for column header.
     * @param workbook workbook.
     * @param isBold flag whehter font is bold
     * @return cell style.
     */
    private static CellStyle createColumnHeadersCellStyle(final Workbook workbook, final boolean isBold) {
        CellStyle cellStyle = workbook.createCellStyle();

        // Перенос текста
        cellStyle.setWrapText(true);


        // Позиционирование - центровка
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        // Шрифт Arial - 12
        Font columnHeadersFont = workbook.createFont();
        columnHeadersFont.setFontHeightInPoints((short) 12);
        columnHeadersFont.setBoldweight(BOLDWEIGHT_BOLD);
        columnHeadersFont.setFontName("Arial");
        cellStyle.setFont(columnHeadersFont);

        // Границы
        final IndexedColors color = IndexedColors.BLACK;
        // top
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(color.getIndex());
        // right
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(color.getIndex());
        // left
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(color.getIndex());

        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(color.getIndex());
        return cellStyle;
    }

    /**
     * Create style for report field.
     * @param workbook workbook.
     * @return cellStyle.
     */
    private static CellStyle createReportFieldsCellStyle(final Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        // Перенос текста
        cellStyle.setWrapText(true);

        // Позиционирование - центровка по вертикали
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);

        // Шрифт Arial - 11
        Font fieldsFont = workbook.createFont();
        fieldsFont.setFontHeightInPoints((short) 11);
        fieldsFont.setFontName("Arial");
        cellStyle.setFont(fieldsFont);
        return cellStyle;
    }
}
