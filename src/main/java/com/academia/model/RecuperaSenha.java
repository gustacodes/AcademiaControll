package com.academia.model;

public class RecuperaSenha {
    private String email;
    private String nSenha;
    private String nSenhaRep;
    private String capturaEmail;
    private String capturaSenha;
    private int codigo;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNSenha() {
        return nSenha;
    }

    public void setNSenha(String nSenha) {
        this.nSenha = nSenha;
    }

    public String getNSenhaRep() {
        return nSenhaRep;
    }

    public void setNSenhaRep(String nSenhaRep) {
        this.nSenhaRep = nSenhaRep;
    }

    public String getCapturaEmail() {
        return capturaEmail;
    }

    public void setCapturaEmail(String capturaEmail) {
        this.capturaEmail = capturaEmail;
    }

    public String getCapturaSenha() {
        return capturaSenha;
    }

    public void setCapturaSenha(String capturaSenha) {
        this.capturaSenha = capturaSenha;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

}
