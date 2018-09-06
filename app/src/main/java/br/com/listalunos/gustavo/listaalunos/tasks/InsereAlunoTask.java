package br.com.listalunos.gustavo.listaalunos.tasks;

import android.os.AsyncTask;

import br.com.listalunos.gustavo.listaalunos.WebClient;
import br.com.listalunos.gustavo.listaalunos.converter.AlunoConverter;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class InsereAlunoTask extends AsyncTask{

    private Aluno aluno;


    public InsereAlunoTask(Aluno aluno) {
        this.aluno = aluno;
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        String json = new AlunoConverter().converterParaJSONCompleto(aluno);
        new WebClient().insere(json);

        return null;
    }
}
