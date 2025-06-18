package br.ufscar.dc.dsw.AA2.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String entity, String key, String value) {
        super(entity + " already exists with " + key + " equals " + value + ".");
    }
}
