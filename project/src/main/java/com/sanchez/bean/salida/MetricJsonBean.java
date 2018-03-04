package com.sanchez.bean.salida;

import java.util.List;

public class MetricJsonBean {

	private Integer numFicherosProcesados;
	private Integer numFilasProcesadas;
	private Integer num_CALL_Totales;
	private Integer num_MSG_Totales;
	private Integer numCodigosPaisOrigenDiferentes;
	private Integer numcodidosPaisDestinoDiferentes;
	private List<TiempoEjecucionPorFichero> tiemposEjecucionPorFichero;
	public Integer getNumFicherosProcesados() {
		return numFicherosProcesados;
	}
	public void setNumFicherosProcesados(Integer numFicherosProcesados) {
		this.numFicherosProcesados = numFicherosProcesados;
	}
	public Integer getNumFilasProcesadas() {
		return numFilasProcesadas;
	}
	public void setNumFilasProcesadas(Integer numFilasProcesadas) {
		this.numFilasProcesadas = numFilasProcesadas;
	}
	public Integer getNum_CALL_Totales() {
		return num_CALL_Totales;
	}
	public void setNum_CALL_Totales(Integer num_CALL_Totales) {
		this.num_CALL_Totales = num_CALL_Totales;
	}
	public Integer getNum_MSG_Totales() {
		return num_MSG_Totales;
	}
	public void setNum_MSG_Totales(Integer num_MSG_Totales) {
		this.num_MSG_Totales = num_MSG_Totales;
	}
	public List<TiempoEjecucionPorFichero> getTiemposEjecucionPorFichero() {
		return tiemposEjecucionPorFichero;
	}
	public void setTiemposEjecucionPorFichero(List<TiempoEjecucionPorFichero> tiemposEjecucionPorFichero) {
		this.tiemposEjecucionPorFichero = tiemposEjecucionPorFichero;
	}
	public Integer getNumCodigosPaisOrigenDiferentes() {
		return numCodigosPaisOrigenDiferentes;
	}
	public void setNumCodigosPaisOrigenDiferentes(Integer numCodigosPaisOrigenDiferentes) {
		this.numCodigosPaisOrigenDiferentes = numCodigosPaisOrigenDiferentes;
	}
	public Integer getNumcodidosPaisDestinoDiferentes() {
		return numcodidosPaisDestinoDiferentes;
	}
	public void setNumcodidosPaisDestinoDiferentes(Integer numcodidosPaisDestinoDiferentes) {
		this.numcodidosPaisDestinoDiferentes = numcodidosPaisDestinoDiferentes;
	}
	
	
	
}
