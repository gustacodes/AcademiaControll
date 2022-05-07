package com.academia.model;

public class Aluno extends Cadastro {

    private float mensalidade = 0;
    private int matricula;
    private String atrasados;
    private int atraso = 0;
    private String vencimento;

    public Aluno(){

    }

    public Aluno(int matricula, String nome, String endereco , String bairro, String contato, String email, float mensalidade, String vencimento, String atrasados, int atraso){
        super(nome, endereco, bairro, contato, email);
        this.mensalidade = mensalidade;
        this.matricula = matricula;
        this.atrasados = atrasados;
        this.atraso = atraso;
        this.vencimento = vencimento;
    }

    public Float getMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(float mensalidade) {
        this.mensalidade = mensalidade;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getAtrasados() {
        return atrasados;
    }

    public void setAtrasados(String atrasados) {
        this.atrasados = atrasados;
    }

    public int getAtraso() {
        return atraso;
    }

    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

}