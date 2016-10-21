package com.technology.gisgz.mo5todo.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim.Lee on 2016/4/29.
 */
public class FormDetailPOJO extends BasePOJO {
    private String formName;
    private int formId;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private String updatedAtForProgram;
    private List<AttachmentFile> Attachment= new ArrayList<>();;



    private String formType;



    private List<FormRows> rows = new ArrayList<>();
    private List<FormRows> infoRow = new ArrayList<>();

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }
    public String getUpdatedAtForProgram() {
        return updatedAtForProgram;
    }

    public void setUpdatedAtForProgram(String updatedAtForProgram) {
        this.updatedAtForProgram = updatedAtForProgram;
    }
    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<FormRows> getRows() {
        return rows;
    }

    public void setRows(List<FormRows> rows) {
        this.rows = rows;
    }

    public List<FormRows> getInfoRow() {
        return infoRow;
    }

    public void setInfoRow(List<FormRows> infoRow) {
        this.infoRow = infoRow;
    }

    public List<AttachmentFile> getAttachment() {
        return Attachment;
    }

    public void setAttachment(List<AttachmentFile> attachment) {
        Attachment = attachment;
    }

    public static class FormRows {
        private List<FormField> formFields;
        public List<FormField> getFormFields() {
            return formFields;
        }

        public void setFormFields(List<FormField> formFields) {
            this.formFields = formFields;
        }
    }

    public static class FormField {
        @SerializedName("filedId")
        private int fieldId;
        @SerializedName("filedName")
        private String fieldName;
        @SerializedName("filedType")
        private int fieldType;
        @SerializedName("filedValue")
        private String fieldValue;

        public int getFieldId() {
            return fieldId;
        }

        public void setFieldId(int fieldId) {
            this.fieldId = fieldId;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public int getFieldType() {
            return fieldType;
        }

        public void setFieldType(int fieldType) {
            this.fieldType = fieldType;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }
    }

    public static class AttachmentFile {
        private String FileName;
        private String Path;
        private double Size;
        private String Ext;
        private int Id;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public String getPath() {
            return Path;
        }

        public void setPath(String path) {
            Path = path;
        }

        public double getSize() {
            return Size;
        }

        public void setSize(double size) {
            Size = size;
        }

        public String getExt() {
            return Ext;
        }

        public void setExt(String ext) {
            Ext = ext;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }
    }
}
