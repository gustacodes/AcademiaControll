package com.academia.model;

public class Cadastro {
    //SUPERCLASSE PARA CRIAÇÃO DE CONTA PARA ALUNOS E CONTAS ADMINISTRATIVAS
    private String nome;
    private String email;
    private String endereco;
    private String bairro;
    private String contato;

    public Cadastro(){

    }

    public Cadastro(String nome, String endereco, String bairro, String contato, String email) {
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.bairro = bairro;
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

}