package br.ufscar.dc.dsw.AA2.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entity, String key, String value) {
        super(entity + " não existe com " + key + " igual a " + value + ".");
    }
}
