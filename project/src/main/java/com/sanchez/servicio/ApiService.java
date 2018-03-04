package com.sanchez.servicio;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.sanchez.bean.entrada.JsonBean;
import com.sanchez.bean.entrada.LlamadaMensajeInfo;
import com.sanchez.bean.salida.KpisJsonsBean;

@Service
public class ApiService {

	private Integer numFilasConCamposVacios = 0;
	private Integer numMensajesSinContenido = 0;
	
	//numero de filas con campos erroneos
	//Tiempo medio llamadas por codigoCiudad
	
	private HashMap<Integer, Integer> mapaNumOrigenPorCodCiudad;
	private HashMap<Integer, Integer> mapaNumDestinoPorCodCiudad;
	private HashMap<String, Integer> mapaRelacion_OK_KO;
	private SortedMap<String, Integer> mapRankingPalabrasClave;
	private Integer numLineas=0;
	private Integer numCALLS = 0;
	private Integer numMSGS = 0;
	
	private static String PALABRA_CLAVE_ARE = "ARE";
	private static String PALABRA_CLAVE_YOU ="YOU";
	private static String PALABRA_CLAVE_FINE = "FINE";
	private static String PALABRA_CLAVE_HELLO = "HELLO";
	private static String PALABRA_CLAVE_NOT = "NOT";
	
	
	public String obtenFicheroJson(String fecha) throws IOException,MalformedURLException {
		
		StringBuilder sb = new StringBuilder (); 
		DataInputStream dis = null;
		try {
		    URL urlPrueba = new URL("https://raw.githubusercontent.com/vas-test/test1/master/logs/MCP_"+fecha+".json");
		    URLConnection urlConnection = urlPrueba.openConnection();
            dis = new DataInputStream(urlConnection.getInputStream());
            
            sb.append("{\"informacion\":[");
            String lineaJson;
            
            List<String> listaJson = new ArrayList<String>();
            
            while ((lineaJson = dis.readLine()) != null) {
            	listaJson.add(lineaJson);
            	this.numLineas++;
            }
            
            //Como cada linea del fichero es un json, los agrupamos en la lista
            
            for (int i = 0; i < listaJson.size(); i++) {
				sb.append(listaJson.get(i));
				if(i+1 < listaJson.size()) {
					sb.append(",");
				}
			}
            
            sb.append("]}");
            
            
		} catch (MalformedURLException e) {    
			System.out.println("MalformedURLException--> " + e);
			throw e;
		} catch (IOException e) {               
			System.out.println("IOException--> " + e);
			throw e;
		} finally {
			try {
				dis.close();
			} catch (IOException e) {
				System.out.println("IOException--> " + e);
				throw e;
			}
		}
		
		return sb.toString();
	}

	public KpisJsonsBean procesaJSON(String ficheroJson) {
		
		JsonBean beanJson = new Gson().fromJson(ficheroJson, JsonBean.class);
		
		this.mapaNumOrigenPorCodCiudad = new HashMap<Integer, Integer>();
		this.mapaNumDestinoPorCodCiudad = new HashMap<Integer, Integer>();
		this.mapaRelacion_OK_KO = new HashMap<String, Integer>();
		this.mapRankingPalabrasClave = new TreeMap<String, Integer>(Collections.reverseOrder());
		
		List<LlamadaMensajeInfo> listaJson = beanJson.getInformacion();
		
		for (LlamadaMensajeInfo llamadaMensajeInfo : listaJson) {
			
			if(llamadaMensajeInfo.getMessage_type()!=null && llamadaMensajeInfo.getMessage_type().equals("CALL")) {
				this.numCALLS++;
			}else if(llamadaMensajeInfo.getMessage_type()!=null && llamadaMensajeInfo.getMessage_type().equals("MSG")) {
				this.numMSGS++;
			}
			
			if(llamadaMensajeInfo.tieneCamposVacios()) {
				this.numFilasConCamposVacios = this.numFilasConCamposVacios+1;
			}
			if(llamadaMensajeInfo.tieneMensajeVacio()) {
				this.numMensajesSinContenido = this.numMensajesSinContenido+1;
			}
			
			Integer codPaisOrigen = llamadaMensajeInfo.getcodPaisOrigen();
			Integer codPaisDestino = llamadaMensajeInfo.getcodPaisDestino();
			
			if(this.mapaNumOrigenPorCodCiudad.containsKey(codPaisOrigen)) {
				Integer numLlamadas = this.mapaNumOrigenPorCodCiudad.get(codPaisOrigen);
				if(llamadaMensajeInfo.getOrigin()!=null) {
					numLlamadas = numLlamadas+1;
				}
				this.mapaNumOrigenPorCodCiudad.put(codPaisOrigen, numLlamadas);
			}else {
				Integer numLlamadas = llamadaMensajeInfo.getOrigin()!=null?1:0;
				this.mapaNumOrigenPorCodCiudad.put(codPaisOrigen, numLlamadas);
			}
			
			if(this.mapaNumDestinoPorCodCiudad.containsKey(codPaisDestino)) {
				Integer numLlamadas = this.mapaNumDestinoPorCodCiudad.get(codPaisDestino);
				if(llamadaMensajeInfo.getDestination()!=null) {
					numLlamadas = numLlamadas+1;
				}
				
				this.mapaNumDestinoPorCodCiudad.put(codPaisDestino, numLlamadas);
			}else {
				Integer numLlamadas = llamadaMensajeInfo.getDestination()!=null?1:0;
				this.mapaNumDestinoPorCodCiudad.put(codPaisDestino, numLlamadas);
			}
			
			if(llamadaMensajeInfo.getMessage_type()!=null && llamadaMensajeInfo.getMessage_type().equals("CALL")) {
				if(llamadaMensajeInfo.getStatus_code()!=null && !llamadaMensajeInfo.getStatus_code().equals("")) {
					if(llamadaMensajeInfo.getStatus_code().equals("OK")) {
						if(this.mapaRelacion_OK_KO.containsKey("OK")) {
							Integer numOK = this.mapaRelacion_OK_KO.get("OK");
							numOK = numOK+1;
							this.mapaRelacion_OK_KO.put("OK", numOK);	
						}else {
							this.mapaRelacion_OK_KO.put("OK", 1);	
						}
						
					}else if(llamadaMensajeInfo.getStatus_code().equals("KO")){
						if(this.mapaRelacion_OK_KO.containsKey("KO")) {
							Integer numKO = this.mapaRelacion_OK_KO.get("KO");
							numKO = numKO+1;
							this.mapaRelacion_OK_KO.put("KO", numKO);	
						}else {
							this.mapaRelacion_OK_KO.put("KO", 1);	
						}
					}
				}
			}
			
			if(llamadaMensajeInfo.getMessage_content()!=null && llamadaMensajeInfo.getMessage_content().contains(PALABRA_CLAVE_ARE)) {
				if(this.mapRankingPalabrasClave.containsKey(PALABRA_CLAVE_ARE)) {
					Integer numOcurrencias = this.mapRankingPalabrasClave.get(PALABRA_CLAVE_ARE);
					numOcurrencias = numOcurrencias+1;
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_ARE, numOcurrencias);
				}else {
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_ARE, 1);
				}
			}
			if(llamadaMensajeInfo.getMessage_content()!=null && llamadaMensajeInfo.getMessage_content().contains(PALABRA_CLAVE_FINE)) {
				if(this.mapRankingPalabrasClave.containsKey(PALABRA_CLAVE_FINE)) {
					Integer numOcurrencias = this.mapRankingPalabrasClave.get(PALABRA_CLAVE_FINE);
					numOcurrencias = numOcurrencias+1;
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_FINE, numOcurrencias);
				}else {
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_FINE, 1);
				}
			}
			if(llamadaMensajeInfo.getMessage_content()!=null && llamadaMensajeInfo.getMessage_content().contains(PALABRA_CLAVE_HELLO)) {
				if(this.mapRankingPalabrasClave.containsKey(PALABRA_CLAVE_HELLO)) {
					Integer numOcurrencias = this.mapRankingPalabrasClave.get(PALABRA_CLAVE_HELLO);
					numOcurrencias = numOcurrencias+1;
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_HELLO, numOcurrencias);
				}else {
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_HELLO, 1);
				}
			}
			if(llamadaMensajeInfo.getMessage_content()!=null && llamadaMensajeInfo.getMessage_content().contains(PALABRA_CLAVE_NOT)) {
				if(this.mapRankingPalabrasClave.containsKey(PALABRA_CLAVE_NOT)) {
					Integer numOcurrencias = this.mapRankingPalabrasClave.get(PALABRA_CLAVE_NOT);
					numOcurrencias = numOcurrencias+1;
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_NOT, numOcurrencias);
				}else {
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_NOT, 1);
				}
			}
			if(llamadaMensajeInfo.getMessage_content()!=null && llamadaMensajeInfo.getMessage_content().contains(PALABRA_CLAVE_YOU)) {
				if(this.mapRankingPalabrasClave.containsKey(PALABRA_CLAVE_YOU)) {
					Integer numOcurrencias = this.mapRankingPalabrasClave.get(PALABRA_CLAVE_YOU);
					numOcurrencias = numOcurrencias+1;
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_YOU, numOcurrencias);
				}else {
					this.mapRankingPalabrasClave.put(PALABRA_CLAVE_YOU, 1);
				}
			}
			
		}
		
		KpisJsonsBean beanSalida = new KpisJsonsBean();
		beanSalida.setMapaNumDestinoPorCodCiudad(mapaNumDestinoPorCodCiudad);
		beanSalida.setMapaNumOrigenPorCodCiudad(mapaNumOrigenPorCodCiudad);
		beanSalida.setMapaRelacion_OK_KO(mapaRelacion_OK_KO);
		beanSalida.setMapRankingPalabrasClave(mapRankingPalabrasClave);
		beanSalida.setNumMensajesSinContenido(numMensajesSinContenido);
		beanSalida.setNumFilasConCamposVacios(numFilasConCamposVacios);
		
		
		return beanSalida;
	}

	public void reseteaValoresEspecificos() {
		this.numFilasConCamposVacios = 0;
		this.numMensajesSinContenido = 0;
		this.numLineas=0;
		this.numCALLS = 0;
		this.numMSGS = 0;
		
	}

	public Integer getNumLineas() {
		return this.numLineas;
	}

	public Integer getNumCALLS() {
		return this.numCALLS;
	}

	public Integer getNumMSGS() {
		return this.numMSGS;
	}

}
