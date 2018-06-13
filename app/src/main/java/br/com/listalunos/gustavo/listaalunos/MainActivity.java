package br.com.listalunos.gustavo.listaalunos;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class MainActivity extends AppCompatActivity
{

    private FloatingActionButton fabAdcionar;
    private ListView lvAlunos;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAdcionar = findViewById(R.id.fabAdcionar);
        lvAlunos = findViewById(R.id.lvAlunos);
        carregarLista();


        fabAdcionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MainActivity.this, FormularioActivity.class);
                startActivity(it);
            }
        });


        lvAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id)
            {
                Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(position);
                Intent itVaiProFormulario = new Intent(MainActivity.this, FormularioActivity.class);
                itVaiProFormulario.putExtra("aluno", aluno);
                startActivity(itVaiProFormulario);
            }
        });


        registerForContextMenu(lvAlunos);
    }

    private void carregarLista()
    {
        //Instânciando a classe que faz conexão com o banco buscando todos os alunos registrados e passando para uma lista
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscarAlunos();
        dao.close();


        ArrayAdapter<Aluno> adapter = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos);
        lvAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        carregarLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuItem deletar = menu.add("Deletar Aluno");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                //Aqui estamos falando que a informação menuInfo vem de dentro de um Adapter
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                //Obtendo o objeto que foi clicado através do info.postion e jogando a uma variável do tipo Aluno
                Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(info.position);

                AlunoDAO dao = new AlunoDAO(MainActivity.this);
                dao.deletar(aluno);
                dao.close();
                carregarLista();
                return false;
            }
        });

    }
}
