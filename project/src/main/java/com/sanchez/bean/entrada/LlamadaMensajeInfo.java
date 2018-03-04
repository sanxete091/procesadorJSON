package com.sanchez.bean.entrada;

public class LlamadaMensajeInfo {

	 //Parametros comunes para tipos CALL o MSG
	 private String message_type;
	 private Object timestamp;
	 private Object origin;
	 private Object destination;
	 //Parametros especifidos para tipo CALL
	 private String status_code;
	 private String status_description;
	 private Object duration;
	 //Parametros especificos para tipo MSG
	 private String message_content;
	 private String message_status;
	
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public Object getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Object timestamp) {
		this.timestamp = timestamp;
	}
	public Object getOrigin() {
		return origin;
	}
	public void setOrigin(Object origin) {
		this.origin = origin;
	}
	public Object getDestination() {
		return destination;
	}
	public void setDestination(Object destination) {
		this.destination = destination;
	}
	public Object getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getStatus_description() {
		return status_description;
	}
	public void setStatus_description(String status_description) {
		this.status_description = status_description;
	}
	public String getMessage_content() {
		return message_content;
	}
	public void setMessage_content(String message_content) {
		this.message_content = message_content;
	}
	public String getMessage_status() {
		return message_status;
	}
	public void setMessage_status(String message_status) {
		this.message_status = message_status;
	}
	
	public Boolean tieneCamposVacios() {	

		if(this.message_type==null || this.message_type.equals("")) {
			return true;
		}
		if(this.timestamp==null || this.timestamp.equals("")) {
			return true;
		}
		if(this.origin==null || this.origin.equals("")) {
			return true;
		}
		if(this.destination==null || this.destination.equals("")) {
			return true;
		}
		
		if(this.message_type.equals("CALL")) {
			if(this.status_code==null || this.status_code.equals("")) {
				return true;
			}
			if(this.status_description==null || this.status_description.equals("")) {
				return true;
			}
			if(this.duration==null || this.duration.equals("")) {
				return true;
			}
		}else {
			if(this.message_content==null || this.message_content.equals("")) {
				return true;
			}
			if(this.message_status==null || this.message_status.equals("")) {
				return true;
			}
		}
			
		return false;
	}
	public Boolean tieneMensajeVacio() {
		
		if(this.message_type!=null) {
			if(this.message_type.equals("MSG")) {
				if(this.message_content==null || this.message_content.equals("")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Integer getcodPaisOrigen() {
		if(this.origin!=null) {
			try {
				if(this.origin instanceof String) {
					Long origin = Long.parseLong((String)this.origin);
					String origenString = Long.toString(origin);
					String origenSubString = origenString.substring(0, 2);
					return Integer.parseInt(origenSubString);		
				} else if(this.origin instanceof Double) {
					Long origin = (new Double((Double)this.origin)).longValue();
					String origenString = Long.toString(origin);
					String origenSubString = origenString.substring(0, 2);
					return Integer.parseInt(origenSubString);		
				}else {
					String origenString = Long.toString((Long)origin);
					String origenSubString = origenString.substring(0, 2);
					return Integer.parseInt(origenSubString);	
				}
				
				
			} catch (Exception e) {
				System.out.println("Ha ocurrido un error al parsear a String origin -- " + e.getMessage());
			}
			
		}
		return -1;
	}
	public Integer getcodPaisDestino() {
		if(this.destination!=null) {
			try {
				if(this.destination instanceof String) {
					Long destination = Long.parseLong((String)this.destination);
					String destinationString = Long.toString(destination);
					String destinationSubString = destinationString.substring(0, 2);
					return Integer.parseInt(destinationSubString);		
				} else if(this.destination instanceof Double) {
					Long destination = (new Double((Double)this.destination)).longValue();
					String destinationString = Long.toString(destination);
					String destinationSubString = destinationString.substring(0, 2);
					return Integer.parseInt(destinationSubString);		
				}else {
					String destinationString = Long.toString((Long)destination);
					String destinationSubString = destinationString.substring(0, 2);
					return Integer.parseInt(destinationSubString);	
				}
				
				
			} catch (Exception e) {
				System.out.println("Ha ocurrido un error al parsear a String origin -- " + e.getMessage());
			}
			
		}
		return -1;
	}
}
