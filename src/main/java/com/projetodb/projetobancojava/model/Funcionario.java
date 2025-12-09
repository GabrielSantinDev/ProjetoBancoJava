package com.projetodb.projetobancojava.model;

import com.projetodb.projetobancojava.util.FormatarCampos;

import java.time.LocalDate;

public class Funcionario {

    private int id;
    private String nome;
    private String cpf;
    private String cpfFormatado;
    private String email;
    private String senha;
    private String telefone;
    private String telefoneFormatado;
    private LocalDate dataNascimento;
    private String dataNascimentoFormatada;
    private double salario;
    private String cargo;

    public Funcionario() {
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", salario=" + salario +
                ", cargo='" + cargo + '\'' +
                '}';
    }
}
