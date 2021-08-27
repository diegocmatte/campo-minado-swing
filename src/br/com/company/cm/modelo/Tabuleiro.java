package br.com.company.cm.modelo;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Tabuleiro implements CampoObservador{

    private final int qtdLinhas;
    private final int qtdColunas;
    private final int qtdMinas;

    private final List<Campo> campos = new ArrayList<>();
    private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

    public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
        this.qtdLinhas = qtdLinhas;
        this.qtdColunas = qtdColunas;
        this.qtdMinas = qtdMinas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void paraCadaCampo(Consumer<Campo> funcao){
        campos.forEach(funcao);
    }

    public void registrarObservador(Consumer<ResultadoEvento> observador){
        observadores.add(observador);
    }

    public void notificaObservadores(Boolean resultado){
        observadores.stream()
                .forEach(obs -> obs.accept(new ResultadoEvento(resultado)));
    }

    public void abrir(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
    }

    private void mostrarMinas(){
        campos.stream()
                .filter(c -> c.isMinado())
                .filter(c -> !c.isMarcado())
                .forEach(c -> c.setAberto(true));
    }

    public void alternarMarcacao(int linha, int coluna) {
        campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.alternarMarcacao());
    }

    private void gerarCampos() {
        for (int linha = 0; linha < qtdLinhas; linha++) {
            for (int coluna = 0; coluna < qtdColunas; coluna++) {
                Campo campo = new Campo(linha, coluna);
                campo.registrarObsevardor(this);
                campos.add(campo);
            }
        }
    }

    private void associarVizinhos() {
        for(Campo c1: campos){
            for(Campo c2: campos){
                c1.adicionarViznho(c2);
            }
        }
    }

    private void sortearMinas() {
        long minasArmadas = 0;
        Predicate<Campo> minado = c -> c.isMinado();

        do{
            int aleatorio = (int) (Math.random() * campos.size());
            campos.get(aleatorio).minar();
            minasArmadas = campos.stream().filter(minado).count();
        } while(minasArmadas < qtdMinas);
    }

    public boolean objetivoAlcancado(){
        return campos.stream().allMatch(c -> c.objetivoAlcancado());
    }

    public void reiniciarJogo(){
        campos.stream().forEach(c -> c.reiniciarJogo());
        sortearMinas();
    }

    public int getQtdLinhas() {
        return qtdLinhas;
    }

    public int getQtdColunas() {
        return qtdColunas;
    }

    @Override
    public void ocorreuEvento(Campo campo, CampoEvento evento) {
        if(evento == CampoEvento.EXPLODIR){
            mostrarMinas();
            notificaObservadores(false);
        } else if (objetivoAlcancado()) {
            notificaObservadores(true);
        }
    }
}
