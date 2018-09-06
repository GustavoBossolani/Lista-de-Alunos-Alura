package br.com.listalunos.gustavo.listaalunos;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import br.com.listalunos.gustavo.listaalunos.fragments.DetalhesProvasFragment;
import br.com.listalunos.gustavo.listaalunos.fragments.ListaProvasFragment;
import br.com.listalunos.gustavo.listaalunos.modelo.Prova;


public class ProvasActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provas);


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tx = manager.beginTransaction();

        tx.replace(R.id.frame_principal, new ListaProvasFragment());
        if (estaNoModoPaisagem())
        {
            tx.replace(R.id.frame_secundario, new DetalhesProvasFragment());
        }
        tx.commit();


    }

    private boolean estaNoModoPaisagem()
    {
        return getResources().getBoolean(R.bool.modoPaisagem);
    }

    public void selecionarProva(Prova prova)
    {
        //Recuperando uma instância do fragment manager
        FragmentManager manager = getSupportFragmentManager();
        if (!estaNoModoPaisagem())
        {
            //Preparando a transação entre layouts(fragments)
            FragmentTransaction tx = manager.beginTransaction();

            //Passando Parâmetros para que DetalhesProvasFragment consiga popular com os dados da prova
            DetalhesProvasFragment detalhesFragment = new DetalhesProvasFragment();
            Bundle parametros = new Bundle();
            parametros.putSerializable("prova", prova);
            detalhesFragment.setArguments(parametros);

            //'Trocando' os elementos do fragment através de um placeholder(FrameLayout)
            tx.replace(R.id.frame_principal, detalhesFragment);
            //Aqui estamos configurando dizendo que botão de voltar apenas irá voltar um 'passo' atrás ao envez de fechar a activity inteira
            tx.addToBackStack(null);
            //Confirmando a 'troca'
            tx.commit();
        } else
        {
            DetalhesProvasFragment detalhesFragment =
                    (DetalhesProvasFragment) manager.findFragmentById(R.id.frame_secundario);

            detalhesFragment.popularCamposCom(prova);
        }
    }
}
