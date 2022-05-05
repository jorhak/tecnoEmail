package com.persona.practica;


import com.persona.database.database;
import com.persona.expresion.obtener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class enviarEmail {

    private String servidor;
    private String user_receptor;
    private String user_emisor;
    private int puerto;
    private String contenido;
    String line;
    String comando = "";

    obtener o = new obtener();
    leerEmail e = new leerEmail();

    public enviarEmail() {
        this("", "", "", 0, "");
    }

    public enviarEmail(String servidor, String receptor, String emisor,
            int puerto, String contenido) {
        this.servidor = servidor;
        this.user_receptor = receptor;
        this.user_emisor = emisor;
        this.puerto = puerto;
        this.contenido = contenido;
    }

    public void setData(Map<String, String> data) {
        servidor = data.getOrDefault("servidor", "");
        user_receptor = data.getOrDefault("user_receptor", "");
        user_emisor = data.getOrDefault("user_emisor", "");
        puerto = Integer.parseInt(data.getOrDefault("puerto", "0"));
        contenido = data.getOrDefault("contenido", "");
    }

    public void sendEmail() {
        try {
            //se establece conexion abriendo un socket especificando el servidor y puerto SMTP
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            DataOutputStream salida = new DataOutputStream(socket.
                    getOutputStream());
            // Escribimos datos en el canal de salida establecido con el puerto del protocolo SMTP del servidor
            if (socket != null && entrada != null && salida != null) {
                System.out.println("S : " + entrada.readLine());

                comando = "HELO " + servidor + " \r\n";
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());

                comando = "MAIL FROM : " + user_emisor + " \r\n";
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());

                comando = "RCPT TO : " + user_receptor + " \r\n";
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());

                comando = "DATA\n";
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());

                comando = "Subject:RESPUESTA\r\n"+contenido;
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());

                comando = "QUIT\r\n";
                System.out.print("C : " + comando);
                salida.writeBytes(comando);
                System.out.println("S : " + entrada.readLine());
            }
            // Cerramos los flujos de salida y de entrada y el socket cliente
            salida.close();
            entrada.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println(
                    " S : No se pudo conectar con el servidor indicado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        obtener ob = new obtener();
        leerEmail leer = new leerEmail();
        enviarEmail enviar = new enviarEmail();
        
        String read;
        String subject;
        String nombres = "";
        String emisor;
        String receptor;

        /*
        Agregamos los valores del servidor para 
        poder leer el contenido de un email
        */
        Map<String,String> dataLeer = new HashMap<>();
        dataLeer.put("servidor", "mail.tecnoweb.org.bo");
        dataLeer.put("usuario", "grupo15sa");
        dataLeer.put("contrasena", "grup015grup015");
        dataLeer.put("puerto", "110");
        
        /*
        Cargamos los atributos de leerEmail.
        Luego capturamos el contenido del correo #15
        */
        leer.setData(dataLeer);
        read = leer.read(19);
        
        
        /*
        Capturando valor del Subject para hacer la consulta
        a la base de datos
        */
        ob.setText(read);
        subject = ob.getSubject();
        subject = ob.valueSubject();
        ///////////////////////////////////////////////////////////////////////
        
        /*
        Realizamos la consulta
        */
        String sql = "SELECT per_nom FROM persona WHERE per_nom like '%"+subject+"%'";
        List<Map<String,String>> resultados = database.getInstance().executeSQLResultList(sql);
        for (int i = 0; i < resultados.size(); i++) {
            Map<String,String> row = resultados.get(i);
            nombres = nombres + row.getOrDefault("per_nom", "")+"\r\n";
        }
        ///////////////////////////////////////////////////////////////////////
        
        /*
        Capturar valor del emisor
        */
        emisor = ob.getEmisor();
        emisor = ob.valueEmisor();
        ///////////////////////////////////////////////////////////////////////
        
        
        /*
        Capturar valor del receptor
        */
        receptor = ob.getRecptor();
        receptor = ob.valueRecptor();
        ///////////////////////////////////////////////////////////////////////
        System.out.println(emisor);
        System.out.println(receptor);
        
        /*
        Agregamos los valores que tendra el correo.
        El emisor, receptor, en contenido del email.
        */
        nombres = nombres + ".\r\n";
        Map<String,String> dataEnviar = new HashMap<>();
        dataEnviar.put("servidor", "mail.tecnoweb.org.bo");
        dataEnviar.put("user_receptor", receptor);
        dataEnviar.put("user_emisor", emisor);
        dataEnviar.put("puerto", "25");
        dataEnviar.put("contenido", nombres);
        
        enviar.setData(dataEnviar);
        enviar.sendEmail();
        
    }
}
