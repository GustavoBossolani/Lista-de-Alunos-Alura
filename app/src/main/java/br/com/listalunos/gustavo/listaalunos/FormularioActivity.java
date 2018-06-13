package br.com.listalunos.gustavo.listaalunos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class FormularioActivity extends AppCompatActivity
{
    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        helper = new FormularioHelper(this);

        //Devemos verificar caso tenha alguma informação pendurada com a Intent para que não dê problemas
        //Caso seja nula o comando será ignorado
        Intent it = getIntent();
        Aluno aluno = (Aluno) it.getSerializableExtra("aluno");
        if (aluno != null)
        {
            helper.preencherFormulario(aluno);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.formulario_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.formulario_salvar:
                //Criando uma instância do objeto Aluno e pegando todos os valores digitados
                Aluno aluno = helper.getAluno();
                AlunoDAO alunoDAO = new AlunoDAO(this);

                if(aluno.getId() != null)
                {
                    alunoDAO.editar(aluno);
                }else {alunoDAO.adcionar(aluno);}

                alunoDAO.close();

                Toast.makeText(getApplicationContext(), "Aluno " + aluno.getNome() + " Salvo!", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.formulario_cancelar:

                Toast.makeText(getApplicationContext(), "Operação Cancelada.", Toast.LENGTH_SHORT).show();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
