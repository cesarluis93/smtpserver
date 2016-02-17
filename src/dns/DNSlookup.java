package dns;

import java.util.Arrays;
import java.util.Comparator;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public class DNSlookup {

	
	
	public void getNames(String[] names) 
    {

        try
        {   

          for (String element: names){  // Lista de DNS que recibe
            // print the sorted mail exhchange servers
            for (String mailHost: lookupMailHosts(element)) // CAMBIE arg[0] por element porque es para cada uno de la lista :P
            {
                System.out.println(mailHost);            
            }
          }  
        }
        catch (NamingException e)
        {
            System.err.println("ERROR: No DNS record for '" + names + "'");
            System.exit(-2);
        }
     }
	
    static String[] lookupMailHosts(String domainName) throws NamingException
    {
    	//Obtiene el Directory Context inicial 
    	InitialDirContext iDirC= new InitialDirContext();
    	//Obtiene los records MX del directorio DNS por default del servicio proveedor
    	//	NamingException: si no existe record del nombre del dominio en el DNS
    	Attributes attributes = iDirC.getAttributes("dns:/" + domainName, new String[] {"MX"});
    	// attributeMX es un atributo ('lista') del Mail Exchange(MX) Resource Records(RR)}
    	Attribute attributeMX = attributes.get("MX");
    	
    	//Si no existe MX RRs entonces RFC 974
    	if(attributeMX == null)
    	{
    		return (new String[] {domainName});
    	}
    	
    	// Separar MX RRs en valores de preferencia( pvhn[0] y el nombre del host (pvhn[1])
    	String [][] pvhn= new String [attributeMX.size()][2];
    	
    	for (int i=0; i< attributeMX.size(); i++)
    	{
    		pvhn[i] = (""+ attributeMX.get(i)).split("\\s+");
    	}
    	
    	//guardar los MX RRs po valor RR (de preferencia ddel menor al mayor)
    	Arrays.sort(pvhn, new Comparator<String[]>()
    	{
    		public int compare(String[] o1, String[] o2)
    		{
    			return (Integer.parseInt(o1[0])- Integer.parseInt(o2[0]));
    		
			} 			
    	});
    	
    	//colocar los nombres de dominio en un array, 
    	
    	String [] sortedHostNames= new String[pvhn.length];
    	for(int i=0; i< pvhn.length; i++)
    	{
    		sortedHostNames[i] = pvhn[i][1].endsWith(".") ?
    				pvhn[i][1].substring(0, pvhn[i][1].length()-1) : pvhn[i][1];
    	}
    	return sortedHostNames;

    }

}
