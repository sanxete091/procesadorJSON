package com.sanchez.bean.salida;

import java.util.HashMap;
import java.util.SortedMap;

public class KpisJsonsBean {
	
	private Integer numFilasConCamposVacios = 0;
	private Integer numMensajesSinContenido = 0;
	
	//numero de filas con campos erroneos
	//Tiempo medio llamadas por codigoCiudad
	
	private HashMap<Integer, Integer> mapaNumOrigenPorCodCiudad;
	private HashMap<Integer, Integer> mapaNumDestinoPorCodCiudad;
	private HashMap<String, Integer> mapaRelacion_OK_KO;
	private SortedMap<String, Integer> mapRankingPalabrasClave;
	public Integer getNumFilasConCamposVacios() {
		return numFilasConCamposVacios;
	}
	public void setNumFilasConCamposVacios(Integer numFilasConCamposVacios) {
		this.numFilasConCamposVacios = numFilasConCamposVacios;
	}
	public Integer getNumMensajesSinContenido() {
		return numMensajesSinContenido;
	}
	public void setNumMensajesSinContenido(Integer numMensajesSinContenido) {
		this.numMensajesSinContenido = numMensajesSinContenido;
	}
	public HashMap<Integer, Integer> getMapaNumOrigenPorCodCiudad() {
		return mapaNumOrigenPorCodCiudad;
	}
	public void setMapaNumOrigenPorCodCiudad(HashMap<Integer, Integer> mapaNumOrigenPorCodCiudad) {
		this.mapaNumOrigenPorCodCiudad = mapaNumOrigenPorCodCiudad;
	}
	public HashMap<Integer, Integer> getMapaNumDestinoPorCodCiudad() {
		return mapaNumDestinoPorCodCiudad;
	}
	public void setMapaNumDestinoPorCodCiudad(HashMap<Integer, Integer> mapaNumDestinoPorCodCiudad) {
		this.mapaNumDestinoPorCodCiudad = mapaNumDestinoPorCodCiudad;
	}
	public HashMap<String, Integer> getMapaRelacion_OK_KO() {
		return mapaRelacion_OK_KO;
	}
	public void setMapaRelacion_OK_KO(HashMap<String, Integer> mapaRelacion_OK_KO) {
		this.mapaRelacion_OK_KO = mapaRelacion_OK_KO;
	}
	public SortedMap<String, Integer> getMapRankingPalabrasClave() {
		return mapRankingPalabrasClave;
	}
	public void setMapRankingPalabrasClave(SortedMap<String, Integer> mapRankingPalabrasClave) {
		this.mapRankingPalabrasClave = mapRankingPalabrasClave;
	}
	
	
}
