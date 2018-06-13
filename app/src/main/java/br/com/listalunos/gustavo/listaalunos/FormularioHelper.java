package br.com.listalunos.gustavo.listaalunos;

import android.widget.EditText;
import android.widget.RatingBar;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class FormularioHelper
{

    private EditText edtNome;
    private EditText edtEndereco;
    private EditText edtTelefone;
    private EditText edtSite;
    private RatingBar rbAvaliacao;
    private Aluno aluno;

    //Criando um construtor que recupera os elementos de interface diminuindo a repetição de códgo
    public FormularioHelper( FormularioActivity activity )
    {
        edtNome = activity.findViewById(R.id.edtNome);
        edtEndereco = activity.findViewById(R.id.edtEndereco);
        edtTelefone = activity.findViewById(R.id.edtTelefone);
        edtSite = activity.findViewById(R.id.edtSite);
        rbAvaliacao = activity.findViewById(R.id.rbAvaliacao);
        aluno = new Aluno();
    }

    //Retornando um Aluno com todos os dados já obtidos
    public Aluno getAluno()
    {
        aluno.setNome( edtNome.getText().toString() );
        aluno.setEndereco( edtEndereco.getText().toString() );
        aluno.setTelefone( edtTelefone.getText().toString() );
        aluno.setSite( edtSite.getText().toString() );
        aluno.setNota( Double.valueOf( rbAvaliacao.getProgress() ) );
        return aluno;
    }

    public void preencherFormulario(Aluno aluno)
    {
        edtNome.setText(aluno.getNome());
        edtEndereco.setText(aluno.getEndereco());
        edtTelefone.setText(aluno.getTelefone());
        edtSite.setText(aluno.getSite());
        rbAvaliacao.setProgress(aluno.getNota().intValue());
        this.aluno = aluno;
    }
}
