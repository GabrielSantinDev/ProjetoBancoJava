package com.projetodb.projetobancojava.model;

import com.projetodb.projetobancojava.util.FormatarCampos;

import java.time.LocalDate;

public class Transacao {

    private int id;
    private String tipo;
    private double valor;
    private LocalDate dataEnvio;
    private String dataEnvioFormatada;

    private Integer contaDestinoId;
    private Integer contaOrigemId;

    private String contaOrigemFormatada;
    private String contaDestinoFormatada;

    public Transacao() {
    }

    public Transacao(Integer contaOrigemId, Integer contaDestinoId, LocalDate dataEnvio, double valor, String tipo) {
        this.contaOrigemId = contaOrigemId;
        this.contaDestinoId = contaDestinoId;
        this.dataEnvio = dataEnvio;
        this.valor = valor;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
        dataEnvioFormatada = FormatarCampos.formatarData(dataEnvio);
    }

    public Integer getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Integer contaDestinoId) {
        this.contaDestinoId = contaDestinoId;

        if (contaDestinoId == null) {
            contaDestinoFormatada = "vazio";
        } else {
            contaDestinoFormatada = contaDestinoId.toString();
        }
    }

    public Integer getContaOrigemId() {
        return contaOrigemId;
    }

    public void setContaOrigemId(Integer contaOrigemId) {
        this.contaOrigemId = contaOrigemId;

        if (contaOrigemId == null) {
            contaOrigemFormatada = "vazio";
        } else {
            contaOrigemFormatada = contaOrigemId.toString();
        }
    }

    public String getDataEnvioFormatada() {
        return dataEnvioFormatada;
    }

    public String getContaOrigemFormatada() {
        return contaOrigemFormatada;
    }

    public String getContaDestinoFormatada() {
        return contaDestinoFormatada;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", dataEnvio=" + dataEnvio +
                ", contaDestinoId=" + contaDestinoId +
                ", contaOrigemId=" + contaOrigemId +
                '}';
    }
}
