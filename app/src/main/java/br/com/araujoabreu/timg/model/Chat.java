package br.com.araujoabreu.timg.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private String data;
    private String tipo;
    private String caminho;
    private boolean isseen;

    public Chat(String sender, String receiver, String message, String data, String tipo, String caminho, boolean isseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.data = data;
        this.tipo = tipo;
        this.caminho = caminho;
        this.isseen = isseen;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) { this.data = data;}

    public String getData() {
        return data;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getCaminho() {
        return caminho;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
