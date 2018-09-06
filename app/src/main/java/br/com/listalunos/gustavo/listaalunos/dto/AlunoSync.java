package br.com.listalunos.gustavo.listaalunos.dto;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

// DTO é uma sigla para Data Transfer Object, Objeto de transferência de dados.
public class AlunoSync {

    private List<Aluno> alunos;
    private String momentoDaUltimaModificacao;

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public String getMomentoDaUltimaModificacao() {
        return momentoDaUltimaModificacao;
    }
}
