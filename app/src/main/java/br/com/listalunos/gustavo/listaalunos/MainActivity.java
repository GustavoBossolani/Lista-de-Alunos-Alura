package br.com.listalunos.gustavo.listaalunos;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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

        /*
            Intent Explícita:
            A Intent que explicita qual é a activity que seremos direcionados, chamamos de Intent Explícita.
            Exp: Intent itExplicita = new Intent(primeiraActivity.this, segundaActivity.class);

            Intent Implícita:
            Quando dizemos que o usuários irá escolher e nós só sabemos qual ação queremos realizar,
            nós iremos utilizar uma Intent implícita. Você informa para o Android 'quero abrir um site'
            e ele fará o possível para executar a ação.
            Exp: Int itImplícita = new Intent(Intent.ACTION_VIEW);
         */

        //Aqui estamos falando que a informação menuInfo vem de dentro de um Adapter
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //Obtendo o objeto que foi clicado através do info.postion e jogando a uma variável do tipo Aluno
        final Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(info.position);


        //Criando um novo botão que fará a visita ao site do aluno
        MenuItem itemSite = menu.add("Visitar Site");
        //ACTION_VIEW se refere a visualizar algo, a partir disso o android retornara a função adequada
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        //'Linkando' nossa intent com a botão 'Visitar Site'
        itemSite.setIntent(intentSite);
        //Para o android conseguir realizar o direcionamento correto é preciso passar uma pista sobre o que ele está lidando
        String siteUrl = aluno.getSite();
        if (!siteUrl.startsWith("http://"))
        {
            siteUrl = "http://" + siteUrl;
        }

        /*
            Nesta parte para a intent implicita funcionar é OBRIGATÓRIO a declaração da Uri(Identificador Uniforme de recursos) para o Android saber qual função acionar;
            A Uri é uma forma de identificar todos os tipo de recursos presentes no sistema operacional Android;
            No comando 'setData()' é preciso retornar uma Uri e para converter um protocólo 'http' como um String para Uri, é preciso usar o Uri.parse.
        */
        intentSite.setData(Uri.parse(siteUrl));

        //Criando Item para Enviar SMS
        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);


        //Criando Item para visualizar o Endereço
        MenuItem itemMapa = menu.add("Ver Endereço no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        MenuItem itemDeletar = menu.add("Deletar Aluno");
        itemDeletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                AlunoDAO dao = new AlunoDAO(MainActivity.this);
                dao.deletar(aluno);
                dao.close();
                carregarLista();
                return false;
            }
        });

    }
}
