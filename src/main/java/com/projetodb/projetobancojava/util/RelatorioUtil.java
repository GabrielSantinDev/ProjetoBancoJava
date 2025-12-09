package com.projetodb.projetobancojava.util;

import com.projetodb.projetobancojava.database.ConexaoDB;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class RelatorioUtil {
    public static void abrirPdf(String caminho, Map<String, Object> param, Connection con) {
        try {
            JasperReport relatorio = (JasperReport) JRLoader.loadObjectFromFile(caminho);
            JasperPrint print = JasperFillManager.fillReport(relatorio, param, con);

            File pdfTemp = File.createTempFile("relatorio_", ".pdf");
            pdfTemp.deleteOnExit();

            JasperExportManager.exportReportToPdfFile(print, pdfTemp.getAbsolutePath());
            Desktop.getDesktop().open(pdfTemp);

        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gerarRelatorio(String tipo) {
        try {
            String caminho = "src/main/resources/relatorios/" + tipo + ".jasper";

            Connection con = ConexaoDB.getConexao();

            Map<String, Object> param = new HashMap<>();

            RelatorioUtil.abrirPdf(caminho, param, con);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
