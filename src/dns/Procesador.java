package dns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

import data.Connector;

public class Procesador {

    private DNS Dns;
    

    public Procesador() {
        try {
            Dns = new DNS();
        } catch (NamingException ex) {
            Logger.getLogger(Procesador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //recibe una entrada con los datos ya verificados
    //nos da una respuesta
    public String procesarHelo(String dominio) {
        return "Hello " + dominio + ", I am glad to meet you ";
    }

    public String procesarMail(String correo) throws SQLException {
        //aqui se tiene que verificar con DNS si esta direccion existe
        String retorno;
        String usuario;
        String dominio;
        if (correo.contains("@") && !correo.endsWith("@") && !correo.startsWith("@")) {
            String[] datosDir = correo.split("@");
            if (datosDir.length == 2) {
                usuario = datosDir[0];
                dominio = datosDir[1];

                //preguntar si el dominio es local?
                if (Dns.esDominioLocal(dominio)) {
                    // si el usuario existe? falta conexion a la bd y retornar booleano
                    
                	Connector c = new Connector();
                    
                    c.connect();
                    ResultSet datosDeTabla = c.datosDeTabla("username");
                    while (datosDeTabla.next()) {

                        String usuarioBd =datosDeTabla.getString(2);
                        if(usuario.equals(usuarioBd)){
                            retorno="250 Ok";
                           
                        }else{
                            retorno="550 Access Denied";
                        }
                    }
                    
                } else {
                    //si no es local
                    List<String> ips = Dns.obtenerServidores(dominio);
                    if (ips == null) {
                        //retornar mensaje de error
                        //dns.getError();
                    } else {
                        //jalar la primera IP
                    }
                }

                //pedirle una IP al DNS

            } else {
                //error el formato de correo es <usuario>@<dominio>
            }
        } else {
            //error el formato de correo es <usuario>@<dominio>
        }

        //if true...
        return "250 Ok";
    }

    public String procesarRCPT(ArrayList<String> correos) {
        //aqui se tiene que verificar con DNS si esta direccion existe
        return "250 Ok";
    }

    public String procesarData() {
        return "250 Ok";
    }

    public String procesarDataEnd() {
        return "250 Ok";
    }

    public String procesarQuit() {
        return "221 Bye";
    }
}
