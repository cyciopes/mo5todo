package com.technology.gisgz.mo5todo.model;

import java.util.List;

/**
 * Created by Jim.Lee on 2016/8/11.
 */
public class FormDetailDataGirdPOJO extends BasePOJO {
    private List<List<String>> DataGridTable;
    private List<DataGridSummary> dataGridSummary;
    public List<List<String>> getDataGridTable() {
        return DataGridTable;
    }

    public void setDataGridTable(List<List<String>> dataGridTable) {
        DataGridTable = dataGridTable;
    }

    public List<DataGridSummary> getDataGridSummary() {
        return dataGridSummary;
    }

    public void setDataGridSummary(List<DataGridSummary> dataGridSummary) {
        this.dataGridSummary = dataGridSummary;
    }

    private class DataGridSummary {
        private String currencyFrom;
        private String currencyTo;
        private String subtotalFrom;
        private String subtotalTo;

        public String getCurrencyFrom() {
            return currencyFrom;
        }

        public void setCurrencyFrom(String currencyFrom) {
            this.currencyFrom = currencyFrom;
        }

        public String getCurrencyTo() {
            return currencyTo;
        }

        public void setCurrencyTo(String currencyTo) {
            this.currencyTo = currencyTo;
        }

        public String getSubtotalFrom() {
            return subtotalFrom;
        }

        public void setSubtotalFrom(String subtotalFrom) {
            this.subtotalFrom = subtotalFrom;
        }

        public String getSubtotalTo() {
            return subtotalTo;
        }

        public void setSubtotalTo(String subtotalTo) {
            this.subtotalTo = subtotalTo;
        }
    }
}
