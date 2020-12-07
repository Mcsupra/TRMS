package TRMS.TRMSPojos;

import java.util.Objects;

public class Message {

    private int messageId;
    private int reqId;
    private int fromEmp;
    private int toEmp;
    private String message;

    public Message() {
    }

    public Message(int fromEmp, int toEmp, String message, int reqId) {
        this.fromEmp = fromEmp;
        this.toEmp = toEmp;
        this.message = message;
        this.reqId = reqId;
    }


    public Message(int messageId, int reqId, int fromEmp, int toEmp, String message) {
        this.messageId = messageId;
        this.reqId = reqId;
        this.fromEmp = fromEmp;
        this.toEmp = toEmp;
        this.message = message;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getReqId() {
        return this.reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getFromEmp() {
        return this.fromEmp;
    }

    public void setFromEmp(int fromEmp) {
        this.fromEmp = fromEmp;
    }

    public int getToEmp() {
        return this.toEmp;
    }

    public void setToEmp(int toEmp) {
        this.toEmp = toEmp;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message messageId(int messageId) {
        this.messageId = messageId;
        return this;
    }

    public Message reqId(int reqId) {
        this.reqId = reqId;
        return this;
    }

    public Message fromEmp(int fromEmp) {
        this.fromEmp = fromEmp;
        return this;
    }

    public Message toEmp(int toEmp) {
        this.toEmp = toEmp;
        return this;
    }

    public Message message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Message)) {
            return false;
        }
        Message message = (Message) o;
        return messageId == message.messageId && reqId == message.reqId && fromEmp == message.fromEmp && toEmp == message.toEmp && Objects.equals(message, message.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, reqId, fromEmp, toEmp, message);
    }

    @Override
    public String toString() {
        return "{" +
            " messageId='" + getMessageId() + "'" +
            ", reqId='" + getReqId() + "'" +
            ", fromEmp='" + getFromEmp() + "'" +
            ", toEmp='" + getToEmp() + "'" +
            ", message='" + getMessage() + "'" +
            "}";
    }
    
}
