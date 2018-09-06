package br.com.listalunos.gustavo.listaalunos.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.WebClient;
import br.com.listalunos.gustavo.listaalunos.converter.AlunoConverter;
import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class EnviaAlunosTask extends AsyncTask <Void, Void, String>
{
    private Context context;
    private ProgressDialog dialog;

    public EnviaAlunosTask(Context context)
    {
        this.context = context;
    }


    //Este método executa na main thread antes do doInBackground
    @Override
    protected void onPreExecute()
    {
        dialog = ProgressDialog.show(context, "Aguarde", "Enviando Alunos...");
        dialog.show();
    }

    //Neste método de fato está acontecendo a execução e segundo plano, sem afetar a main thread e a experiência do usuário
    @Override
    protected String doInBackground(Void... objects)
    {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscarAlunos();
        dao.close();

        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converteParaJSON(alunos);

        WebClient client = new WebClient();
        String resposta = client.post(json);
        //Este retorno irá para o parâmentro do método onPostExecute
        return resposta;
    }

    //Este método executa na main thread após do doInBackground e passa o retorno para os parâmetros (resposta)
    @Override
    protected void onPostExecute(String resposta)
    {
        dialog.dismiss();
        Toast.makeText(context, resposta, Toast.LENGTH_LONG).show();
    }
}
