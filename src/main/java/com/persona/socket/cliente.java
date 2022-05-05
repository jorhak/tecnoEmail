package com.persona.socket;

import java.net.*;

import java.io.*;


public class cliente {

    static final String HOST = "localhost";
    static final int PUERTO=5000;

    public cliente( )

    {
        try{
           Socket skCliente = new Socket( HOST , PUERTO );
           BufferedReader entrada = new BufferedReader(new InputStreamReader(skCliente.getInputStream()));
           System.out.println( " C : Conectado a <"+HOST+">" );
           System.out.println( " S: "+entrada.readLine() );
           skCliente.close();
           System.out.println( " C : Desconectado del <"+HOST+">" );
      } catch( Exception e ){
          System.out.println( " C : "+e.getMessage() );
      }

    }

    public static void main(String[] args) {
         // TODO Auto-generated method stub
         cliente c=new cliente();
    }

}
