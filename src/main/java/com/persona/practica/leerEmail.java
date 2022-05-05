
package com.persona.practica;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;


public class leerEmail {

    private String servidor;
    private String usuario;
    private String contrasena ;
    private int puerto;
    String comando = "";
    String linea = "";
    String resultado = "";


    public leerEmail() {
        this("", "", "", 0);
    }

    public leerEmail(String servidor, String usuario, String contrasena,
            int puerto) {
        this.servidor = servidor;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.puerto = puerto;
    }

    public void setData(Map<String, String> data) {
        servidor = data.getOrDefault("servidor", "");
        usuario = data.getOrDefault("usuario", "");
        contrasena = data.getOrDefault("contrasena", "");
        puerto = Integer.parseInt(data.getOrDefault("puerto", "0"));
    }
    
    /*
    Con esta función leemos el email sin embargo le pasamos por parametro
    el # del email que queremos leer.
    Previamente podemos utilizar la funcion count() para saber la cantidad 
    de email que tiene ese usuario.
    */
    public String read(int correo) {
        try {
            //se establece conexion abriendo un socket especificando el servidor y puerto SMTP
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            DataOutputStream salida = new DataOutputStream(socket.
                    getOutputStream());
            // Escribimos datos en el canal de salida establecido con el puerto del protocolo SMTP del servidor
            if (socket != null && entrada != null && salida != null) {
                linea = entrada.readLine()+"\r\n";
                comando = "USER " + usuario + "\r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";

                comando = "PASS " + contrasena + "\r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";

                comando = "STAT \r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";

                comando = "RETR "+correo+"\n";
                salida.writeBytes(comando);
                linea = getMultiline(entrada);
                resultado = linea;
                

                comando = "QUIT\r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";
            }
            // Cerramos los flujos de salida y de entrada y el socket cliente
            salida.close();
            entrada.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println(
                    " S : no se pudo conectar con el servidor indicado");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
    /*
    Con esta función obtenemos el # de email que tiene ese usuario.
    Sin embargo no nos muestra el # de inicio para ello cambiamos a STAT por 
    LIST y ocupamos la funcion getMultiline(entrada)
    */
    public String count(){
        try {
            //se establece conexion abriendo un socket especificando el servidor y puerto SMTP
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            DataOutputStream salida = new DataOutputStream(socket.
                    getOutputStream());
            // Escribimos datos en el canal de salida establecido con el puerto del protocolo SMTP del servidor
            if (socket != null && entrada != null && salida != null) {
                linea = entrada.readLine()+"\r\n";
                comando = "USER " + usuario + "\r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";

                comando = "PASS " + contrasena + "\r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine()+"\r\n";

                comando = "STAT \r\n";
                salida.writeBytes(comando);
                linea = entrada.readLine();//+"\r\n";
                resultado = linea;
            }
            // Cerramos los flujos de salida y de entrada y el socket cliente
            salida.close();
            entrada.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println(
                    " S : no se pudo conectar con el servidor indicado");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
    }

    static protected String getMultiline(BufferedReader in) throws IOException {
        String lines = "";
        while (true) {
            String line = in.readLine();
            if (line == null) {
                // Server closed connection
                throw new IOException(
                        " S : Server unawares closed the connection.");
            }
            if (line.equals(".")) {
                // No more lines in the server response
                break;
            }
            if ((line.length() > 0) && (line.charAt(0) == '.')) {
                // The line starts with a "." - strip it off.
                line = line.substring(1);
            }
            // Add read line to the list of lines
            lines = lines + "\n" + line;
        }
        return lines;
    }
    
    

    public static void main(String[] args) {
        leerEmail r = new leerEmail();
        Map<String, String> data = new HashMap<>();
        data.put("servidor", "mail.tecnoweb.org.bo");
        data.put("usuario", "grupo15sa");
        data.put("contrasena", "grup015grup015");
        data.put("puerto", "110");
        r.setData(data);
//        System.out.println(r.read(19));
        System.out.println("|"+r.count()+"|");
    }

}
