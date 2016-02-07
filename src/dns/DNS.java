package dns;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

/**
 * Clase que se usa para consultar un dominio, si es local, y si no, si podemos encontrar un host que lo maneje
 * Tomado de: http://captechconsulting.com/blog/david-tiller/accessing-the-dusty-corners-dns-java
 */

public class DNS {
	
	public static final String LOCAL_DOMAIN = "LabSMT.com";
	private static final String MX_ATTRIB= "A";
	private static final String[] MX_ATTRIBS= {MX_ATTRIB};
	private InitialDirContext idc;
	private String error;
	

	public DNS() throws NamingException {
		Properties env= new Properties();
		env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
		env.setProperty(Context.DNS_URL, "dns://127.0.0.1");
		idc = new InitialDirContext(env);
        error = null;
	}
	
    /**
     * Determina si un dominio de la forma "nombre.com" o similar es el dominio local
     * @param dominio el dominio a comparar
     * @return true si es el dominio local, false si no lo es
     */
	public boolean esDominioLocal(String dominio){
		return LOCAL_DOMAIN.equals(dominio);
	}
	
	/**
     * Obtiene las direcciones ip que pueden resolver un dominio. Si no consigue IPs, guarda un mensaje de error para consulta.
     * @param dominio
     * @return dirección IP que atiende el dominio, null si no se consigue o hubo un error. Llamar "getError()" si regresa null.
     */
    public List<String> obtenerServidores(String dominio) {
        try {
            error = null;
            List<String> servers = new ArrayList<>();
            Attributes attrs = idc.getAttributes(dominio, MX_ATTRIBS);
            Attribute attr = attrs.get(MX_ATTRIB);
            
            if (attr != null) {
                for (int i = 0; i < attr.size(); i++) {
                    String mxAttr = (String) attr.get(i);
                    String[] parts = mxAttr.split(" ");
                    
                    // Split off the priority, and take the last field
                    servers.add(parts[parts.length - 1]);
                }
            }
            
            return servers;
        } catch (Exception ex) {
            error = ex.getLocalizedMessage();
            return null;
        }
    }

    /**
     * Obtiene el mensaje de error de la última operación.
     * @return 
     */
    public String getError() {
        return error;
    }
	
}
