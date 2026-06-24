package main.comun;

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
