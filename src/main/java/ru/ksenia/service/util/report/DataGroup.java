package ru.ksenia.service.util.report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sokolov_as on 06.03.2017.
 */
public class DataGroup {
    private boolean isBold;
    private List<String> sharedData;

    private List<DataGroup> subGroups;

    public DataGroup() {

    }

    public DataGroup(final List<String> sharedData) {
        this.sharedData = sharedData;
        this.subGroups = new ArrayList<DataGroup>();
    }

    public DataGroup(final List<String> sharedData, final boolean isBold) {
        this.sharedData = sharedData;
        this.subGroups = new ArrayList<DataGroup>();
        this.isBold = isBold;
    }

    public DataGroup(final List<String> sharedData, final List<DataGroup> subGroups) {
        this.sharedData = sharedData;
        this.subGroups = subGroups;
    }

    /**
     * Add sub group.
     * @param subGroup sub group.
     */
    public void addSubGroup(final DataGroup subGroup) {
        this.subGroups.add(subGroup);
    }

    /**
     * определяет является ли группа листом - не имеет подгрупп и соответственно в sharedData содержит негруппируемый кортеж строк
     *
     * @return true - если является листовой группой
     */
    public boolean isLeafGroup() {
        return subGroups == null || subGroups.isEmpty();
    }

    public List<String> getSharedData() {
        return sharedData;
    }

    public List<DataGroup> getSubGroups() {
        return subGroups;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(final boolean bold) {
        isBold = bold;
    }
}
