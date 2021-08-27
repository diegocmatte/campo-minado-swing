package br.com.company.cm;

import br.com.company.cm.modelo.Tabuleiro;
import br.com.company.cm.visao.TabuleiroConsole;

public class Aplicacao {

    public static void main(String[] args) {

        Tabuleiro tabuleiro = new Tabuleiro(6, 6, 6);
        new TabuleiroConsole(tabuleiro);

    }

}
