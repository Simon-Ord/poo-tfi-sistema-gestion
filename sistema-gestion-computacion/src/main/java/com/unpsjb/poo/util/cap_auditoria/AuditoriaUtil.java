package com.unpsjb.poo.util.cap_auditoria;

/**
 * Auditoría genérica para registrar acciones globales del sistema.
 */
public class AuditoriaUtil extends AuditoriaBase {

    /**
     * Registra una acción genérica usando la lógica heredada.
     * Evita duplicar la creación del evento manualmente.
     */
    public static void registrarAccion(String accion, String entidad, String detalles) {
        new AuditoriaUtil().registrarEvento(accion, entidad, detalles);
    }

    @Override
    public void registrarAccionEspecifica(Object original, Object modificado) {
        // No aplica comparación de entidades
    }

    @Override
    public void registrarCreacion(Object nuevo) {
       
    }
}
