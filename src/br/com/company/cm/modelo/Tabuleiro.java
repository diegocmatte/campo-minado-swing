package br.com.company.cm.modelo;

import br.com.company.cm.excecoes.ExplosaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Tabuleiro {

    private int qtdLinhas;
    private int qtdColunas;
    private int qtdMinas;

    private final List<Campo> campos = new ArrayList<>();

    public Tabuleiro(int qtdLinhas, int qtdColunas, int qtdMinas) {
        this.qtdLinhas = qtdLinhas;
        this.qtdColunas = qtdColunas;
        this.qtdMinas = qtdMinas;

        gerarCampos();
        associarVizinhos();
        sortearMinas();
    }

    public void abrir(int linha, int coluna) {
        try{
            campos.parallelStream()
                .filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
                .findFirst()
                .ifPresent(c -> c.abrir());
        } catch (ExplosaoException e){
            campos.forEach(c -> c.setAberto(true));
            throw e;
        }
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
                campos.add(new Campo(linha,coluna));
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

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("  ");
        for (int c = 0; c < qtdColunas; c++) {
            sb.append(" ");
            sb.append(c);
            sb.append(" ");
        }

        sb.append("\n");

        int i = 0;
        for (int linha = 0; linha < qtdLinhas; linha++) {
            sb.append(linha);
            sb.append(" ");
            for (int coluna = 0; coluna < qtdColunas; coluna++) {
                sb.append(" ");
                sb.append(campos.get(i));
                sb.append(" ");
                i++;
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
