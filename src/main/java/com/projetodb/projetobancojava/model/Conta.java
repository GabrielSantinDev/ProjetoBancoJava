package com.projetodb.projetobancojava.model;

public class Conta {

    private int id;
    private String numero;
    private String tipo;
    private double saldo;
    private String senha;

    private int clienteId;

    public Conta(String numero, String tipo, double saldo, String senha, int clienteId) {
        this.numero = numero;
        this.tipo = tipo;
        this.saldo = saldo;
        this.senha = senha;
        this.clienteId = clienteId;
    }

    public Conta() {
    }

    @Override
    public String toString() {
        return "Conta{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", tipo='" + tipo + '\'' +
                ", saldo=" + saldo +
                ", senha='" + senha + '\'' +
                ", cliente_id=" + clienteId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int cliente) {
        this.clienteId = cliente;
    }
}
