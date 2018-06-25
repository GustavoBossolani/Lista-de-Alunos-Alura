package br.com.listalunos.gustavo.listaalunos.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.listalunos.gustavo.listaalunos.DetalheProvasActivity;
import br.com.listalunos.gustavo.listaalunos.ProvasActivity;
import br.com.listalunos.gustavo.listaalunos.R;
import br.com.listalunos.gustavo.listaalunos.modelo.Prova;

public class ListaProvasFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_lista_provas, container, false);

        ListView lista_provas = view.findViewById(R.id.lista_provas);

        List<String> topicosGameDesign = Arrays.asList("Probabilidade","Curva entre tédio e desafio","Profundida de mecânicas");
        Prova provaGameDesign = new Prova("Game Design", "15/08", topicosGameDesign);

        List<String> topicosModelagem = Arrays.asList("Retopologia", "Animação", "Texturização");
        Prova provaModelagem = new Prova("Modelagem 3D", "19/07", topicosModelagem);

        List<String> topicosProgramacao = Arrays.asList("Orientação Objeto", "Shaders", "Player");
        Prova provaProgramacao = new Prova("Programação em C#", "01/09", topicosProgramacao);

        List<Prova> provas = Arrays.asList(provaGameDesign,provaModelagem,provaProgramacao);
        ArrayAdapter adapter = new ArrayAdapter<Prova>(getContext(), android.R.layout.simple_list_item_1, provas);
        lista_provas.setAdapter(adapter);

        lista_provas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Prova prova = (Prova) parent.getItemAtPosition(position);
                Toast.makeText(getContext(), "Exibindo detalhes da prova de " + prova, Toast.LENGTH_SHORT).show();

                ProvasActivity provasActivity = (ProvasActivity) getActivity();
                provasActivity.selecionarProva(prova);
            }
        });

        return view;
    }
}
