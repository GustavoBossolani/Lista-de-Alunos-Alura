package br.com.listalunos.gustavo.listaalunos.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class AlunoConverter
{
    public String converteParaJSON(List<Aluno> alunos)
    {
        //Criando o objeto que criará nossa string json
        JSONStringer js = new JSONStringer();

        try
        {
            //Aqui estamos criando as 'aberturas' do json
            js.object().key("list").array().object().key("aluno").array();

            /*
                Nosso arquivo JSON até este ponto está assim:
                {
                   "list":[
                     {
                       "alunos":[
            */

            //Estamos 'passando' todos os alunos da lista para o json através do for
            for (Aluno aluno: alunos)
            {
                js.object();
                js.key("id").value(aluno.getId());
                js.key("nome").value(aluno.getNome());
                js.key("endereco").value(aluno.getEndereco());
                js.key("nota").value(aluno.getNota());
                js.endObject();

            /*
                Nosso arquivo JSON até este ponto está assim:
                {
                   "list":[
                     {
                       "alunos":[
                         {
                            "id": "1"
                            "nome": "Gustavo"
                            "endereco": "Algum lugar"
                            "nota": "5"
                      },
            */
            }
            js.endArray().endObject().endArray().endObject();
              /*
                Nosso arquivo JSON até este ponto está assim:
                {
                   "list":[
                     {
                       "alunos":[
                       {
                            "id": "1"
                            "nome": "Gustavo"
                            "endereco": "Algum lugar"
                            "nota": "5"
                      },
                   ]
                }
              ]
            }

            */


        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return js.toString();
    }
}
