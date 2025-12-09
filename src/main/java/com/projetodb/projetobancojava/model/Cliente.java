package com.projetodb.projetobancojava.model;

import com.projetodb.projetobancojava.util.FormatarCampos;

import java.time.LocalDate;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String cpfFormatado;
    private String email;
    private String telefone;
    private String telefoneFormatado;
    private LocalDate dataNascimento;
    private String dataNascimentoFormatada;

    public Cliente(String nome, String cpf, String email, String telefone, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    public Cliente() {
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
        dataNascimentoFormatada = FormatarCampos.formatarData(dataNascimento);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
        telefoneFormatado = FormatarCampos.formatarTelefone(telefone);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
        cpfFormatado = FormatarCampos.formatarCPF(cpf);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpfFormatado() {
        return cpfFormatado;
    }

    public String getTelefoneFormatado() {
        return telefoneFormatado;
    }

    public String getDataNascimentoFormatada() {
        return dataNascimentoFormatada;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}
