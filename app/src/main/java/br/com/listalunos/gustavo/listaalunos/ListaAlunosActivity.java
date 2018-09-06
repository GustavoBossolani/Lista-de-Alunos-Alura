package br.com.listalunos.gustavo.listaalunos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.adapter.AlunosAdapter;
import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.dto.AlunoSync;
import br.com.listalunos.gustavo.listaalunos.event.AtualizaListaAlunoEvent;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;
import br.com.listalunos.gustavo.listaalunos.retrofit.InicializadorRetrofit;
import br.com.listalunos.gustavo.listaalunos.tasks.EnviaAlunosTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosActivity extends AppCompatActivity {

    private FloatingActionButton fabAdcionar;
    private ListView lvAlunos;
    private SwipeRefreshLayout swipe;
    EventBus eventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        swipe = findViewById(R.id.swipe_lista_alunos);
        fabAdcionar = findViewById(R.id.fabAdcionar);
        lvAlunos = findViewById(R.id.lvAlunos);

        eventBus = EventBus.getDefault();
        eventBus.register(this);

        carregarLista();

        //Fazendo a verificação de permissão apra recebimento de SMS
        permissaoSMS();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                buscaAlunos();
            }
        });

        fabAdcionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(it);
            }
        });
        lvAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Aluno aluno = (Aluno) lvAlunos.getItemAtPosition(position);
                Intent itVaiProFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                itVaiProFormulario.putExtra("aluno", aluno);
                startActivity(itVaiProFormulario);
            }
        });
        registerForContextMenu(lvAlunos);
        buscaAlunos();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void atualizaListaAlunoEvent(AtualizaListaAlunoEvent event){
        carregarLista();
    }

    private void permissaoSMS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.RECEIVE_SMS};
            ActivityCompat.requestPermissions(this, permissions, 456);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lista_enviar_notas:
                new EnviaAlunosTask(this).execute();
                break;

            case R.id.lista_verificar_provas:
                Intent itProvas = new Intent(ListaAlunosActivity.this, ProvasActivity.class);
                startActivity(itProvas);
                break;

            case R.id.lista_mapa:
                Intent itMapas = new Intent(ListaAlunosActivity.this, MapaActivity.class);
                startActivity(itMapas);
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregarLista() {
        //Instânciando a classe que faz conexão com o banco buscando todos os alunos registrados e passando para uma lista
        AlunoDAO dao = new AlunoDAO(this);
        List<Aluno> alunos = dao.buscarAlunos();
        dao.close();

        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        lvAlunos.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarLista();
    }

    @Override
    protected void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    private void buscaAlunos() {
        Call<AlunoSync> call = new InicializadorRetrofit().getAlunoService().lista();
        call.enqueue(new Callback<AlunoSync>() {
            @Override
            public void onResponse(Call<AlunoSync> call, Response<AlunoSync> response) {
                AlunoSync alunoSync = response.body();
                AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                dao.sincroniza(alunoSync.getAlunos());
                dao.close();
                carregarLista();
            }

            @Override
            public void onFailure(Call<AlunoSync> call, Throwable t) {
                Log.e("onFailure chamado: ", t.getMessage());
            }
        });
        swipe.setRefreshing(false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

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


        //Aqui estamos colocando um ClickListener no MenuItem por que é preciso fazer uma verificação antes de fazer a ligação
        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Criando um array de strings que vai armazenar nosso 'kit' de permissões
                String[] permissions = {Manifest.permission.CALL_PHONE};
                //Verificando se a permissão para usar o aplicativo de ligação foi aprovada
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //Caso não for aprovada um pop up pedindo-a será aberto com a função abaixo retornando um request code
                    //Request code é um código que faz referência a determinadas ações
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this, permissions, 123);
                } else {
                    //Executando a ligação
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }

                return false;
            }
        });


        //Criando um novo botão que fará a visita ao site do aluno
        MenuItem itemSite = menu.add("Visitar Site");
        //ACTION_VIEW se refere a visualizar algo, a partir disso o android retornara a função adequada
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        //'Linkando' nossa intent com a botão 'Visitar Site'
        itemSite.setIntent(intentSite);
        //Para o android conseguir realizar o direcionamento correto é preciso passar uma pista sobre o que ele está lidando
        String siteUrl = aluno.getSite();
        if (!siteUrl.startsWith("http://")) {
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
        itemDeletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Call call = new InicializadorRetrofit().getAlunoService().deleta(aluno.getId());
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        AlunoDAO dao = new AlunoDAO(ListaAlunosActivity.this);
                        dao.deletar(aluno);
                        dao.close();
                        carregarLista();
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(ListaAlunosActivity.this, "Houve um problema na remoção do Aluno", Toast.LENGTH_SHORT).show();
                    }
                });
                return false;
            }
        });

    }

    //Este método é chamado logo em seguida quando uma permissão requisitada é aceitada
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Caso eu tenha mais de uma permissão é preciso realizar um if comparando o requestCode para não executar alguma ação errada
        if (requestCode == 123) {
            //faz a ligação
            Toast.makeText(ListaAlunosActivity.this, "Permissão Consedida, tente fazer a ligação novamente", Toast.LENGTH_SHORT).show();
        }
    }
}
