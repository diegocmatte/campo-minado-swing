package br.com.company.cm.modelo;

@FunctionalInterface
public interface CampoObservador {

    public void ocorreuEvento(Campo campo, CampoEvento evento);
}
