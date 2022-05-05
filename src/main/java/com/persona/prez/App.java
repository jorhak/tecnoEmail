package com.persona.prez;



public class App {
	private String nombre;
	private String apellido;

	public App() {
		this.nombre = "";
		this.apellido = "";
	}

	public App(String nombre, String apellido) {
		this.nombre = nombre;
		this.apellido = apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre){ 
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int compareTo(App p) {
		return this.nombre.compareTo(p.getNombre());
	}

	public static void main(String[] args) {
		App p = new App();
		App p1 = new App();
		// System.out.println("Hello World!");
		p.setNombre("Carolina");
		p.setApellido("Lopez");
		p1.setNombre("Sol");
		p1.setApellido("Perez");
		System.out.println(p1.compareTo(p));
		System.out.println(p.getNombre());

	}

}
