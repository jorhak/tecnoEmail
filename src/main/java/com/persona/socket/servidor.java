package com.persona.socket;

import java.io.*;
import java.net.*;

public class servidor {
	static final int PUERTO = 5000;

	public servidor()

	{
		try {
			ServerSocket skServidor = new ServerSocket(PUERTO);
			System.out.println(" S : Escucho el puerto " + PUERTO);
			for (int numCli = 0; numCli < 3; numCli++)

			{
				Socket skCliente = skServidor.accept(); // Crea objeto
				System.out.println(" S : Sirvo al cliente " + numCli);
				DataOutputStream salida = new DataOutputStream(skCliente.getOutputStream());
				salida.writeBytes("Hola cliente " + numCli + " Su IP es:"
						+ skCliente.getInetAddress().toString());
				skCliente.close();
			}
			System.out.println(" S : Demasiados clientes por hoy");
			skServidor.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		servidor s = new servidor();
	}
}
