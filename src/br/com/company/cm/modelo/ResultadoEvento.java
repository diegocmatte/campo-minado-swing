package br.com.company.cm.modelo;

public class ResultadoEvento {

    private final boolean resultado;

    public ResultadoEvento(boolean resultado) {
        this.resultado = resultado;
    }

    public boolean isGanhou() {
        return resultado;
    }
}
