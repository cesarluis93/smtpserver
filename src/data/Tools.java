package data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tools {
	public Tools(){}

	/**
	 * Metodo para visualizar un archivo .json con tabulaciones.
	 * @param data - Contenido del archivo json.
	 * @return Contenido tabulada.
	 */
	public String convertToContentJsonView(String data){
		String toshow = "", tabs;
		int numTabs = 0;
		for (int i=0; i<data.length(); i++){			
			if ("{[".contains(String.valueOf(data.charAt(i)))){
				numTabs += 1;
				tabs = "";
				for (int j=0; j<numTabs; j++)
					tabs += "\t";
				toshow += String.valueOf(data.charAt(i)) + "\n" + tabs;				
			}
			else if ("]}".contains(String.valueOf(data.charAt(i)))){
				numTabs -= 1;
				tabs = "";
				for (int j=0; j<numTabs; j++)
					tabs += "\t";
				toshow += "\n" + tabs + String.valueOf(data.charAt(i));
			}
			else if (data.charAt(i) == ','){
				tabs = "";
				for (int j=0; j<numTabs; j++)
					tabs += "\t";
				toshow += String.valueOf(data.charAt(i)) + "\n" + tabs;
			}
			else {
				toshow += String.valueOf(data.charAt(i));	
			}			
		}			
		return toshow;
	}
	
	public String convertSelectResultToContentJsonView(JSONArray data){
		String toshow = "";
		for (int i=0; i<data.length(); i++){
			try {
				toshow += String.valueOf(i+1) + " : " + data.getJSONArray(i).toString() + "\n";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return toshow;
	}
    
    /**
     * Verifica si un JSONArray de Strings contiene un String determinado.
     * @param array - Arreglo de strings.
     * @param item - String a buscar en el arreglo.
     * @return true - Si item existe en array.
     * @return false - Si item no existe en array.
     */
    public boolean jsonArrayContain(JSONArray array, String item){
    	for (int i=0; i<array.length(); i++){
    		try {
				if (array.getString(i).equals(item))
					return true;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return false;
    }
    
    /**
     * Verifica si un JSONArray de Objetos contiene un Objeto determinado.
     * @param array
     * @param object
     * @param objectItems
     * @return
     */
    public boolean jsonArrayContain(JSONArray array, JSONObject object, ArrayList<String> objectItems){
    	int cont;
    	for (int i=0; i<array.length(); i++){
    		JSONObject obj;
			try {
				obj = array.getJSONObject(i);
				cont = 0;
	    		for (String objItem: objectItems){
	    			cont++;
	    			if (!obj.get(objItem).equals(object.get(objItem))){	    				
	    				break;
	    			}
	    			if (cont == objectItems.size()){
    					return true;
    				}
	    		}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return false;
    }    
    
}
