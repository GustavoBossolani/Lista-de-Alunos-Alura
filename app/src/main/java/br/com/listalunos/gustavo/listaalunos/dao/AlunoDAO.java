package br.com.listalunos.gustavo.listaalunos.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;


/*
    Objeto de acesso de dados(ou DAO, acrônimo de Data Access Object)
    padrão para persistência de dados que permite separar regras de negócio
    de acesso a banco de dados.
*/


// SQLiteOpenHelper ajuda com a verificação de versões do nosso Banco SQLite
public class AlunoDAO extends SQLiteOpenHelper
{

    public AlunoDAO(Context context)
    {
        super(context, "Agenda", null, 2);
    }

    /* onCreate():
        Método usado para criar um novo banco, caso este seja o primeiro acesso
    */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQLite = "CREATE TABLE Alunos(id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT, telefone TEXT, site TEXT, nota REAL);";
        db.execSQL(SQLite);
    }

    /* onUpgrade():
        Método usado para atualizar o banco de acordo com a versão passada por parâmetro na classe super(AlunoDAO);
        É usado também para recriar uma nova tabela, excluindo a versão antiga e criando a versão nova,
        isso ajuda em relação a erros de sintaxe nos comandos do onCreate.
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String SQLite = "DROP TABLE IF EXISTS Alunos";
        db.execSQL(SQLite);
        onCreate(db);
    }

    public void adcionar(Aluno aluno)
    {
        //Criando uma referência ao nosso banco de dados
        SQLiteDatabase db = getWritableDatabase();
        //Criando um ContentValues, que irá tratar os valores digitados antes de inserir no banco
        ContentValues dados = pegarDadosDoAluno(aluno);
        //Inserindo dados no banco
        db.insert("Alunos", null, dados);
    }

    //Retorna um ContentValue de um aluno
    @NonNull
    private ContentValues pegarDadosDoAluno(Aluno aluno)
    {
        ContentValues dados = new ContentValues();
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        return dados;
    }

    public List<Aluno> buscarAlunos()
    {
        String SQLite = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SQLite, null);
        List<Aluno> alunos = new ArrayList<Aluno>();

        while (c.moveToNext())
        {
            Aluno aluno = new Aluno();

            aluno.setId(c.getLong(c.getColumnIndex("id")));
            aluno.setNome(c.getString(c.getColumnIndex("nome")));
            aluno.setEndereco(c.getString(c.getColumnIndex("endereco")));
            aluno.setTelefone(c.getString(c.getColumnIndex("telefone")));
            aluno.setSite(c.getString(c.getColumnIndex("site")));
            aluno.setNota(c.getDouble(c.getColumnIndex("nota")));

            alunos.add(aluno);
        }
        c.close();
        return alunos;
    }

    public void deletar(Aluno aluno)
    {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
    }

    public void editar(Aluno aluno)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegarDadosDoAluno(aluno);
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id =?", params);

    }
}
