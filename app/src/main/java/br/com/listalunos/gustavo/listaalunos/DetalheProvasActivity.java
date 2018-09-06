package br.com.listalunos.gustavo.listaalunos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import br.com.listalunos.gustavo.listaalunos.modelo.Prova;

public class DetalheProvasActivity extends AppCompatActivity
{

    private ListView detalhe_topicos;
    private TextView detalhe_materia;
    private TextView detalhe_data;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_provas);

        detalhe_data = findViewById(R.id.detalhe_data);
        detalhe_materia = findViewById(R.id.detalhe_materia);
        detalhe_topicos = findViewById(R.id.detalhe_topicos);

        Intent intent = getIntent();
        Prova prova = (Prova) intent.getSerializableExtra("prova");

        detalhe_materia.setText(prova.getMateria());
        detalhe_data.setText(prova.getData());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, prova.getTopicos());
        detalhe_topicos.setAdapter(adapter);
    }
}
