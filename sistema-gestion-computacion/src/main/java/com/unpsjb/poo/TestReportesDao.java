package com.unpsjb.poo;


import com.unpsjb.poo.model.EventoAuditoria;
import com.unpsjb.poo.persistence.dao.ReportesDAO;

import java.util.List;

public class TestReportesDao {
    public static void main(String[] args) {
        try {
            ReportesDAO dao = new ReportesDAO();

            // 🔍 Probá buscar todos los eventos
            System.out.println("Buscando todos los registros de auditoría...");
            List<EventoAuditoria> lista = dao.obtenerEventos("", "", "");

            if (lista.isEmpty()) {
                System.out.println("⚠️ No se encontraron registros en la tabla audit.events");
            } else {
                System.out.println("✅ Se encontraron " + lista.size() + " registros:");
                for (EventoAuditoria ev : lista) {
                    System.out.println("-----------------------------");
                    System.out.println("ID: " + ev.getId());
                    System.out.println("Fecha: " + ev.getFechaHora());
                    System.out.println("Usuario: " + ev.getUsuario());
                    System.out.println("Acción: " + ev.getAccion());
                    System.out.println("Entidad: " + ev.getEntidad());
                    System.out.println("Detalles: " + ev.getDetalles());
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error ejecutando el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
