package com.projetodb.projetobancojava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ContaLogs {

    private int id;
    private double saldoAntigo;
    private double saldoNovo;
    private LocalDateTime dataMudanca;
    private String tempoFormatado;

    private int contaId;

    public ContaLogs(double saldoAntigo, double saldoNovo, LocalDateTime dataMudanca, int conta) {
        this.saldoAntigo = saldoAntigo;
        this.saldoNovo = saldoNovo;
        this.dataMudanca = dataMudanca;
        this.contaId = conta;
    }

    public ContaLogs() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSaldoAntigo() {
        return saldoAntigo;
    }

    public void setSaldoAntigo(double saldoAntigo) {
        this.saldoAntigo = saldoAntigo;
    }

    public double getSaldoNovo() {
        return saldoNovo;
    }

    public void setSaldoNovo(double saldoNovo) {
        this.saldoNovo = saldoNovo;
    }

    public LocalDateTime getDataMudanca() {
        return dataMudanca;
    }

    public void setDataMudanca(LocalDateTime dataMudanca) {
        this.dataMudanca = dataMudanca;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String tempo = dataMudanca.format(dtf);
        this.tempoFormatado = tempo;
    }

    public String getTempoFormatado() {
        return tempoFormatado;
    }

    public void setTempoFormatado(String tempoFormatado) {
        this.tempoFormatado = tempoFormatado;
    }

    public int getContaId() {
        return contaId;
    }

    public void setContaId(int conta) {
        this.contaId = conta;
    }

    @Override
    public String toString() {
        return "ContaLogs{" +
                "id=" + id +
                ", saldoAntigo=" + saldoAntigo +
                ", saldoNovo=" + saldoNovo +
                ", dataMudanca=" + dataMudanca +
                ", contaId=" + contaId +
                '}';
    }
}
