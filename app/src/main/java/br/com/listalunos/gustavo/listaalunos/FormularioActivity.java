package br.com.listalunos.gustavo.listaalunos;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;
import br.com.listalunos.gustavo.listaalunos.retrofit.InicializadorRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioActivity extends AppCompatActivity
{
    public static final int REQUEST_CAMERA = 456;
    public static final String TITULO_APPBAR = "Formulário";
    private FormularioHelper helper;
    private FloatingActionButton fabTirarFoto;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        setTitle(TITULO_APPBAR);

        fabTirarFoto = findViewById(R.id.fabTirarFoto);

        helper = new FormularioHelper(this);

        //Devemos verificar caso tenha alguma informação pendurada com a Intent para que não dê problemas
        //Caso seja nula o comando será ignorado
        Intent it = getIntent();
        Aluno aluno = (Aluno) it.getSerializableExtra("aluno");
        if (aluno != null)
        {
            helper.preencherFormulario(aluno);
        }


        fabTirarFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Esta constante se refere a ação de buscar uma aplicação para tirar uma foto
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*
                    A variável caminhoFoto contém o caminho e o nome do arquivo de foto,
                    o  comando getEternalDir indica a pasta onde nossa aplicação esta salvando arquivos
                    estamos passando null no parâmetro pois não queremos subpastas;

                    Estamos usando o comando System.currentTimeMillis pois se o nome da foto for estático(padrão)
                    a foto sempre sera sobrescrita e nunca teremos fotos unicas na aplicação, para isso
                    usamos o currentMillis para gerar nomes dinâmicos que não se repetem.
                 */
                caminhoFoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                //Criando um novo objeto do tipo File que irá cuidar de salvar nossa foto a partir do caminho passado através de uma String
                File arquivoFoto = new File(caminhoFoto);

                /*
                    Para conseguirmos salvar a foto é preciso usar o comando putExtra para que ao tirar a foto,
                    a mesma venha 'pendurada' para ser recuperada;

                    Estamos usando no primeiro parâmentro no puExtra uma constante acordada como padrão para salvar fotos,
                    pois existem diversos aplicativos para tirar fotos e cada um dele pode possuir uma chave de acesso diferente
                    e recuperár arquivos de foto seria um grande problema;

                    O segundo parâmetro é o caminho em que a foto será salva, lembrando que cada aplicação só poderá 'escrever' dentro de sua própria
                    pasta raiz podendo também conter subpastas;

                    Ainda no segundo parâmentro do comando putExtra, é preciso passar uma Uri indicando aonde será salvo nosso arquivo de foto,
                    passando como parâmetro um objeto do tipo File contendo nele o caminho e o nome do arquivo foto.
                */
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));

                /*
                    Aqui precisamos usar o método para startActivityForResult pois perdemos o controle de nossa activity (Ciclo de vida),
                    E queremos também um jeito de saber quando a foto foi tirada.
                */
                startActivityForResult(intentCamera, REQUEST_CAMERA);
                //Não podemos abrir a foto quando ainda não a temos, para isso é preciso chamar o método onActivityResult()
            }
        });
    }

    //Aqui recebemos o 'status' da ação de tirar a foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Aqui estamos checando caso a foto foi salva, para não criarmos um bitmap através de um arquivo nulo
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == REQUEST_CAMERA)
            {
                //Montando a foto
                helper.carregarFoto(caminhoFoto);
            }
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
                    alunoDAO.altera(aluno);
                }else {alunoDAO.insere(aluno);}

                alunoDAO.close();
                //new InsereAlunoTask(aluno).execute();

                Call<Void> call = new InicializadorRetrofit().getAlunoService().insere(aluno);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i("onResponse", "requisição com o servidor Funcionou!");
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("onFailure", "requisição com o servidor Falhou!" );
                    }
                });

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
