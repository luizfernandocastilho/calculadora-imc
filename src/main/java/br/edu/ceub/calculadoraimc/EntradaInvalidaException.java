package br.edu.ceub.calculadoraimc;

public class EntradaInvalidaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EntradaInvalidaException(final String mensagem) {
        super(mensagem);
    }

    public EntradaInvalidaException(final String mensagem, final Throwable causa) {
        super(mensagem, causa);
    }
}
