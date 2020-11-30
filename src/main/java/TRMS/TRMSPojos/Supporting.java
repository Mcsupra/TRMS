package TRMS.TRMSPojos;

import java.util.Objects;

public class Supporting {
    private int docId;
    private String fileType;


    public Supporting() {
    }

    /**
     * 
     * @param docId
     * @param fileType
     */
    public Supporting(int docId, String fileType) {
        this.docId = docId;
        this.fileType = fileType;
    }

    public int getDocId() {
        return this.docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Supporting docId(int docId) {
        this.docId = docId;
        return this;
    }

    public Supporting fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Supporting)) {
            return false;
        }
        Supporting supporting = (Supporting) o;
        return docId == supporting.docId && Objects.equals(fileType, supporting.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, fileType);
    }

    @Override
    public String toString() {
        return "{" +
            " docId='" + getDocId() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }

}