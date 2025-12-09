package com.projetodb.projetobancojava.util;

import javafx.scene.control.TextFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormatarCampos {

    public static String formatarTelefone(String tel) {
        tel = tel.replaceAll("[^0-9]", "");
        if (tel.length() == 11)
            return tel.replaceFirst("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        return tel.replaceFirst("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
    }

    public static String formatarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarData(LocalDate data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = data.format(formatter);
        return dataFormatada;
    }

    public static String formatarParaNumeros(String text) {
        return text.replaceAll("[^0-9]", "");
    }

}
