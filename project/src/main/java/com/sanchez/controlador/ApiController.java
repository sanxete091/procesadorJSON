package com.sanchez.controlador;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.sanchez.bean.salida.KpisJsonsBean;
import com.sanchez.bean.salida.MetricJsonBean;
import com.sanchez.bean.salida.TiempoEjecucionPorFichero;
import com.sanchez.servicio.ApiService;

@Controller
public class ApiController {
	
	@Autowired
	private ApiService servicio;
	
	@RequestMapping("/{fecha}/metrics")
	@ResponseBody
	public String procesaFicheroJson(@PathVariable("fecha") String fecha,HttpServletRequest request) {
		
		String ficheroJson;
		long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
		TInicio = System.currentTimeMillis(); //Tomamos la hora en que inicio el algoritmo y la almacenamos en la variable inicio
		try {
			ficheroJson = this.servicio.obtenFicheroJson(fecha);
			System.out.println("Json--> " + ficheroJson);
			KpisJsonsBean salida = this.servicio.procesaJSON(ficheroJson);
			
			HttpSession sesion = request.getSession();
			/************************************** Guardamos informacion en sesion ***************************************/
			//Numero de ficheros procesados
			Integer numFicherosProcesados = (Integer) sesion.getAttribute("numeroFicherosProcesados");
			if(numFicherosProcesados == null) {
				//Primero fichero en procesar
				numFicherosProcesados = 1;
			}else {
				numFicherosProcesados = numFicherosProcesados+1;
			}
			sesion.setAttribute("numeroFicherosProcesados", numFicherosProcesados);
			
			
			//Numero de filas procesadas
			Integer numFilasProcesadas = (Integer) sesion.getAttribute("numFilasProcesadas");
			if(numFilasProcesadas == null) {
				//Primero fichero en procesar
				numFilasProcesadas = this.servicio.getNumLineas();
			}else {
				numFilasProcesadas = numFilasProcesadas+this.servicio.getNumLineas();
			}
			sesion.setAttribute("numFilasProcesadas", numFilasProcesadas);
			
			
			//Numero de llamadas procesadas
			Integer numCALLProcesadas = (Integer) sesion.getAttribute("numCALLProcesadas");
			if(numCALLProcesadas == null) {
				//Primero fichero en procesar
				numCALLProcesadas = this.servicio.getNumCALLS();
			}else {
				numCALLProcesadas = numCALLProcesadas+this.servicio.getNumCALLS();
			}
			sesion.setAttribute("numCALLProcesadas", numCALLProcesadas);
			
			
			//Numero de mensajes procesadas
			Integer numMSGProcesadas = (Integer) sesion.getAttribute("numMSGProcesadas");
			if(numMSGProcesadas == null) {
				//Primero fichero en procesar
				numMSGProcesadas = this.servicio.getNumMSGS();
			}else {
				numMSGProcesadas = numMSGProcesadas+this.servicio.getNumMSGS();
			}
			sesion.setAttribute("numMSGProcesadas", numMSGProcesadas);
			
			
			//Tiempo de ejecucion
			List<TiempoEjecucionPorFichero> listaTiempoEjecucion = (List<TiempoEjecucionPorFichero>) sesion.getAttribute("tiempoEjecucionFichero");
			if(listaTiempoEjecucion==null) {
				//Primera ejecucion, lista nulla asi que la inicializamos
				listaTiempoEjecucion = new ArrayList<TiempoEjecucionPorFichero>();
			}
			TiempoEjecucionPorFichero tiempoEjecucion = new TiempoEjecucionPorFichero();
			tiempoEjecucion.setNombreFicheroProcesado("MPC_"+fecha+".json");
			TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
			tiempo = TFin - TInicio; //Calculamos los milisegundos de diferencia
			tiempoEjecucion.setTiempoEjecucion(tiempo);;
			listaTiempoEjecucion.add(tiempoEjecucion);
			sesion.setAttribute("tiempoEjecucionFichero", listaTiempoEjecucion);
			sesion.setAttribute("algunaEjecucion",true);
			this.servicio.reseteaValoresEspecificos();
			return new Gson().toJson(salida);
		} catch (MalformedURLException e) {
			return "Ha ocurrido un error al conectar a la URL https://raw.githubusercontent.com/vas-test/test1/master/logs/MCP" +fecha+".json -- " + e.getMessage() ;
		} catch (IOException e) {
			return "Ha ocurrido un error al manejar la URL https://raw.githubusercontent.com/vas-test/test1/master/logs/MCP" +fecha+".json -- " + e.getMessage() ;
		}catch (Exception e) {
			return "Ha ocurrido un error inesperado -- " + e;
		}	
	}
	
	@RequestMapping("/kpis")
	@ResponseBody
	public String devuelveInformeFicherosProcesados(HttpServletRequest request) {
		
		HttpSession sesion = request.getSession();
		
		Boolean algunaEjecucion = (Boolean) sesion.getAttribute("algunaEjecucion");
		if(algunaEjecucion==null || !algunaEjecucion) {
			return "NO SE HA REALIZADO NINGUN PROCESADO";
		}
		MetricJsonBean salida = new MetricJsonBean();
		salida.setNum_CALL_Totales((Integer) sesion.getAttribute("numCALLProcesadas"));
		salida.setNum_MSG_Totales((Integer) sesion.getAttribute("numMSGProcesadas"));
		salida.setNumFicherosProcesados((Integer) sesion.getAttribute("numeroFicherosProcesados"));
		salida.setNumFilasProcesadas((Integer) sesion.getAttribute("numFilasProcesadas"));
		salida.setTiemposEjecucionPorFichero((List<TiempoEjecucionPorFichero>) sesion.getAttribute("tiempoEjecucionFichero"));
		return new Gson().toJson(salida);
	}
}
