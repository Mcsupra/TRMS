package TRMS.TRMSPojos;

import java.util.Objects;

public class Supporting {
    private int docId;
    private String fileType;
    private byte[] file;
    private int reqId;
  

    public Supporting() {
    }

    public Supporting( String fileType, byte[] file, int reqId) {
        this.fileType = fileType;
        this.file = file;
        this.reqId = reqId;
    }

    public Supporting( String fileType, int reqId) {
        this.fileType = fileType;
        this.reqId = reqId;
    }

    public Supporting(int docId, String fileType, byte[] file, int reqId) {
        this.docId = docId;
        this.fileType = fileType;
        this.file = file;
        this.reqId = reqId;
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

    public byte[] getFile() {
        return this.file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public int getReqId() {
        return this.reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public Supporting docId(int docId) {
        this.docId = docId;
        return this;
    }

    public Supporting fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public Supporting file(byte[] file) {
        this.file = file;
        return this;
    }

    public Supporting reqId(int reqId) {
        this.reqId = reqId;
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
        return docId == supporting.docId && Objects.equals(fileType, supporting.fileType) && Objects.equals(file, supporting.file) && reqId == supporting.reqId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(docId, fileType, file, reqId);
    }

    @Override
    public String toString() {
        return "{" +
            " docId='" + getDocId() + "'" +
            ", fileType='" + getFileType() + "'" +
            ", file='" + getFile() + "'" +
            ", reqId='" + getReqId() + "'" +
            "}";
    }

    
}