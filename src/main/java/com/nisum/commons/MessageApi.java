package com.nisum.commons;

public class MessageApi {
	
	private String mensaje;

	public MessageApi(String mensaje) {
		this.mensaje = mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
