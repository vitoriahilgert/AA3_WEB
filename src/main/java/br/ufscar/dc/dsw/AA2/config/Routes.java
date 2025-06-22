package br.ufscar.dc.dsw.AA2.config;

public class Routes {
    // public routes
    public static final String ROOT = "/";
    public static final String HOME = "/home";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String STRATEGIES = "/strategies";
    public static final String DELETE = "/delete";
    public static final String CREATE = "/create";


    // resources
    public static final String CSS = "/css/**";
    public static final String JS = "/js/**";
    public static final String IMAGES = "/images/**";


    // protected routes
    public static final String PROJETOS = "/projetos";

    public static final String SESSIONS = "/sessions";

    private Routes() {}
}
