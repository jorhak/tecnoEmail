package com.persona.expresion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*
Esta clase se encarga de obtener los SUBJECT, EMISOR, RECEPTOR.
Previamente le pasamos por parametro o lo seteamos(setTexto) el texto de
donde vamos a obtener el SUBJECT, EMISOR, RECEPTOR.
*/
public class obtener {

    private String texto;
    private String subject;
    private String emisor;
    private String recptor;

    public obtener() {
        this("");
    }

    public obtener(String texto) {
        this.texto = texto;
        this.subject = "";
        this.emisor = "";
        this.recptor = "";
    }

    /*
    getSubject con este metodo capturamos el SUBJECT con el fomato:
    Subject:XXXX para asi luego capturar el contenido XXXX
    */
    public String getSubject() {
        Pattern p = Pattern.compile(
                "[_A-Za-z\\+]+(\\.[_A-Za-z]+)*:[A-Za-z]+(\\[A-Za-z]+)*");
        Matcher m = p.matcher(this.texto);
        while (m.find()) {
            setSubject(m.group());
        }
        return this.subject;
    }
    
    /*
    getEmisor con este metodo capturarmos al emisor del email con le formato:
    From: XXXXXX@gmail.com para asi luego capturar el 
    contenido XXXXXX@gmail.com
    */
    public String getEmisor() {
        Pattern p = Pattern.compile(
                "[_A-Za-z\\+]+(\\.[_A-Za-z]+)*:[ _A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[com]{2,})");
        Matcher m = p.matcher(this.texto);
        while (m.find()) {
            setEmisor(m.group());
        }
        return this.emisor;
    }

    /*
    getRecptor con este metodo capturamos al receptor con el formato:
    for grupoXXZZ@tecnoweb.org.bo para asi luego capturar el 
    contenido grupoXXZZ@tecnoweb.org.bo
    */
    public String getRecptor(){
        Pattern p = Pattern.compile(
                "[for\\+]+(\\.[a-z]+)*[ _a-z0-9-\\+]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9]+)*(\\.[org]{2,})*\\.[bo]{2,}");
        Matcher m = p.matcher(this.texto);
        while (m.find()) {
            setRecptor(m.group());
        }
        return this.recptor;
    }
    
    /*
    Aquí ya capturamos el valor del SUBJECT
    */
    public String valueSubject() {
        Pattern p = Pattern.compile("Subject:");
        Matcher m = p.matcher(this.subject);
        return m.replaceAll("");
    }
    
    /*
    Aquí ya capturarmos el valor del EMISOR
    */
    public String valueEmisor(){
        Pattern p = Pattern.compile("From: ");
        Matcher m = p.matcher(this.emisor);
        return m.replaceAll("");
    }
    
    /*
    Aquí ya capturamos el valor del RECEPTOR
    */
    public String valueRecptor(){
        Pattern p = Pattern.compile("for ");
        Matcher m = p.matcher(this.recptor);
        return m.replaceAll("");
    }
    
    /*
    Le asignamos el valor, al atriburo texto
    */
    public void setText(String texto) {
        this.texto = texto;
    }
    
    /*
    Le asignamos el valor, al atributo subject
    */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /*
    Le asignamos el valor, al atributo emisor
    */
    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }
    
    /*
    Le asignamos el valor, al atributo receptor
    */
    public void setRecptor(String receptor){
        this.recptor = receptor;
    }

    

    public static void main(String[] args) {
        obtener o = new obtener();
        o.setText("+OK 476 octets\r\n" +
                "Return-Path: <carlos-lopez@gmail.com>\r\n" +
                "Received: from mail.tecnoweb.org.bo (ip-adsl-190.186.253.101.cotas.com.bo [190.186.253.101] (may be forged))\r\n" +
                "by www.tecnoweb.org.bo (8.16.1/8.16.1) with SMTP id 242E1NpL475365\r\n" +
                "for grupo15sa@tecnoweb.org.bo; Mon, 2 May 2022 10:01:25 -0400" +
                "Date: Mon, 2 May 2022 10:01:23 -0400" +
                "From: carlos-lopez@gmail.com " +
                "Message-Id: <202205021401.242E1NpL475365@www.tecnoweb.org.bo>\r\n" +
                "Subject:Saludos Terricolas\r\n" + "Sin sobrevivientes\r\n" +
                "sin piedad.\r\n");

//        o.setText("+OK 459 octets\r\n"+ "Return-Path: <carlos-lopez@gmail.com>\r\n"+
//"Received: from mail.tecnoweb.org.bo (ip-adsl-190.186.253.101.cotas.com.bo [190.186.253.101] (may be forged))\r\n"+
//	"by www.tecnoweb.org.bo (8.16.1/8.16.1) with SMTP id 244NO0md660995\r\n"+
//	"for grupo15sa@tecnoweb.org.bo; Wed, 4 May 2022 19:24:02 -0400\r\n"+
//"Date: Wed, 4 May 2022 19:24:00 -0400\r\n"+
//"From: carlos-lopez@gmail.com\r\n"+
//"Message-Id: <202205042324.244NO0md660995@www.tecnoweb.org.bo>\r\n"+
//"Subject:E\r\n"+
//"\r\n"+
//"Sin sobrevivientes\r\n"+
//"sin piedad.\r\n");

        System.out.println(o.getSubject());
        System.out.println(o.valueSubject());
        System.out.println(o.getEmisor());
        System.out.println(o.valueEmisor());
        System.out.println(o.getRecptor());
        System.out.println(o.valueRecptor());
    }

}
