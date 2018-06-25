package br.com.listalunos.gustavo.listaalunos.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import br.com.listalunos.gustavo.listaalunos.R;
import br.com.listalunos.gustavo.listaalunos.modelo.Prova;

public class DetalhesProvasFragment extends Fragment {

    private TextView campoMateria;
    private TextView campoData;
    private ListView listaTopicos;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_detalhes_provas, container, false);

        campoMateria = view.findViewById(R.id.detalhe_materia);
        campoData = view.findViewById(R.id.detalhe_data);
        listaTopicos = view.findViewById(R.id.detalhe_topicos);

        Bundle parametros = getArguments();

        if (parametros != null)
        {
            Prova prova = (Prova) parametros.getSerializable("prova");
            popularCamposCom(prova);
        }
        return view;
    }

    public void popularCamposCom(Prova prova)
    {
        campoMateria.setText(prova.getMateria());
        campoData.setText(prova.getData());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_list_item_1, prova.getTopicos());
        listaTopicos.setAdapter(adapter);
    }
}
