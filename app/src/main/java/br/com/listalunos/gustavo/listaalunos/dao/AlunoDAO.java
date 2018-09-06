package br.com.listalunos.gustavo.listaalunos.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;


/*
    Objeto de acesso de dados(ou DAO, acrônimo de Data Access Object)
    padrão para persistência de dados que permite separar regras de negócio
    de acesso a banco de dados.
*/


// SQLiteOpenHelper ajuda com a verificação de versões do nosso Banco SQLite
public class AlunoDAO extends SQLiteOpenHelper {

    public AlunoDAO(Context context) {
        super(context, "Agenda", null, 5);
    }

    /* onCreate():
        Método usado para criar um novo banco, caso este seja o primeiro acesso
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLite = "CREATE TABLE Alunos(" +
                " id CHAR(36) PRIMARY KEY," +
                " nome TEXT NOT NULL," +
                " endereco TEXT," +
                " telefone TEXT," +
                " site TEXT," +
                " nota REAL," +
                " caminhoFoto TEXT);";

        db.execSQL(SQLite);
    }

    /* onUpgrade():
        Método usado para atualizar o banco de acordo com a versão passada por parâmetro na classe super(AlunoDAO);
        É usado também para recriar uma nova tabela, excluindo a versão antiga e criando a versão nova,
        isso ajuda em relação a erros de sintaxe nos comandos do onCreate.
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQLite = "";

        switch (oldVersion) {
            case 2:
                SQLite = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT";
                db.execSQL(SQLite);

            case 3:
                SQLite = "CREATE TABLE Alunos_novo" +
                        " (id CHAR(36) PRIMARY KEY," +
                        " nome TEXT NOT NULL," +
                        " endereco TEXT," +
                        " telefone TEXT," +
                        " site TEXT," +
                        " nota REAL," +
                        " caminhoFoto TEXT);";
                db.execSQL(SQLite);

                String inserindoAlunosNaTabelaNova = "INSERT INTO Alunos_novo " +
                        "(id, nome, endereco, telefone, site, nota, caminhoFoto) " +
                        "SELECT id, nome, endereco, telefone, site, nota, caminhoFoto " +
                        "FROM Alunos";
                db.execSQL(inserindoAlunosNaTabelaNova);
                String removendoTabelaAntiga = "DROP TABLE Alunos";
                db.execSQL(removendoTabelaAntiga);
                String alterandoNomeTabela = "ALTER TABLE Alunos_novo " +
                        "RENAME TO Alunos";
                db.execSQL(alterandoNomeTabela);

            case 4:
                String buscarAlunos = "SELECT * FROM Alunos";
                Cursor cursor = db.rawQuery(buscarAlunos, null);
                List<Aluno> alunos = populaAlunos(cursor);

                String atualizaIdAluno = "UPDATE Alunos SET id=? WHERE id=?";
                for (Aluno aluno : alunos) {
                    db.execSQL(atualizaIdAluno, new String[]{gerarUUID(), aluno.getId()});
                }
        }
    }

    private String gerarUUID() {
        return UUID.randomUUID().toString();
    }

    public void insere(Aluno aluno) {
        //Criando uma referência ao nosso banco de dados
        SQLiteDatabase db = getWritableDatabase();

        //Inserindo o ID no aluno criado
        insereIdSeNecessario(aluno);

        //Criando um ContentValues, que irá tratar os valores digitados antes de inserir no banco
        ContentValues dados = pegarDadosDoAluno(aluno);

        //Inserindo dados no banco e retornando seu ID
        db.insert("Alunos", null, dados);
    }

    private void insereIdSeNecessario(Aluno aluno) {
        if (aluno.getId() == null) {
            aluno.setId(gerarUUID());
        }
    }

    //Retorna um ContentValue de um aluno
    @NonNull
    private ContentValues pegarDadosDoAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();
        dados.put("id", aluno.getId());
        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());
        return dados;
    }

    public List<Aluno> buscarAlunos() {
        String SQLite = "SELECT * FROM Alunos;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(SQLite, null);

        List<Aluno> alunos = populaAlunos(c);
        c.close();
        return alunos;
    }

    @NonNull
    private List<Aluno> populaAlunos(Cursor cursor) {
        List<Aluno> alunos = new ArrayList<Aluno>();
        while (cursor.moveToNext()) {
            Aluno aluno = new Aluno();

            aluno.setId(cursor.getString(cursor.getColumnIndex("id")));
            aluno.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            aluno.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            aluno.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            aluno.setSite(cursor.getString(cursor.getColumnIndex("site")));
            aluno.setNota(cursor.getDouble(cursor.getColumnIndex("nota")));
            aluno.setCaminhoFoto(cursor.getString(cursor.getColumnIndex("caminhoFoto")));

            alunos.add(aluno);
        }
        return alunos;
    }

    public void deletar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};
        db.delete("Alunos", "id = ?", params);
    }

    public void altera(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegarDadosDoAluno(aluno);
        String[] params = {aluno.getId().toString()};
        db.update("Alunos", dados, "id =?", params);
    }

    public boolean ehAluno(String telefone) {
        //Criando instância do banco de dados
        SQLiteDatabase db = getReadableDatabase();
        //Procurando e comparando os telefones no banco
        Cursor cursor = db.rawQuery("SELECT * FROM Alunos WHERE telefone=?", new String[]{telefone});
        //Retornando a quantidade de telefones que são iguais ao da pesquisa
        int resultado = cursor.getCount();
        //Fechando a instancia do cursor e liberando memória
        cursor.close();
        return resultado > 0;
    }

    public void sincroniza(List<Aluno> alunos) {

        for (Aluno aluno : alunos) {

            if (existe(aluno)) {

                if (aluno.estaDesativado()) {
                    deletar(aluno);
                }else {altera(aluno);}

            } else if (!aluno.estaDesativado()){
                insere(aluno);
            }
        }
    }

    private boolean existe(Aluno aluno) {
        SQLiteDatabase db = getReadableDatabase();
        String verificaAluno = "SELECT id FROM Alunos WHERE id=? LIMIT 1";
        Cursor cursor = db.rawQuery(verificaAluno, new String[]{aluno.getId()});
        int quantidade = cursor.getCount();
        return quantidade > 0;
    }
}
