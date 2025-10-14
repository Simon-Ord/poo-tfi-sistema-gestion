

import com.unpsjb.poo.persistence.GestorDeConexion;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        Connection conn = GestorDeConexion.getInstancia().getConexion();
        if (conn != null) {
            System.out.println("Conectado correctamente a Neon");
        } else {
            System.out.println(" No se pudo conectar a Neon");
        }
    }
}

