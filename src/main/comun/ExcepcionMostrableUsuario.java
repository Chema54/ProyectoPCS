/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.comun;

/**
 *
 * @author josem
 */
public class ExcepcionMostrableUsuario extends Exception {
    private final String title;
    private final String header;
    private final String detail;

    public ExcepcionMostrableUsuario(String title, String header, String detail) {
        super(detail);
        this.title = title;
        this.header = header;
        this.detail = detail;
    }
    
    public ExcepcionMostrableUsuario(String title, String header, String detail, Throwable cause) {
        super(detail, cause);
        this.title = title;
        this.header = header;
        this.detail = detail;
    }

    public String getTitle() { return title; }
    public String getHeader() { return header; }
    public String getDetail() { return detail; }
}
