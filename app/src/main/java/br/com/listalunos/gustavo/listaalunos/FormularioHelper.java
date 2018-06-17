package br.com.listalunos.gustavo.listaalunos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class FormularioHelper
{

    private EditText edtNome;
    private EditText edtEndereco;
    private EditText edtTelefone;
    private EditText edtSite;
    private RatingBar rbAvaliacao;
    private ImageView ivPerfil;
    private Aluno aluno;

    //Criando um construtor que recupera os elementos de interface diminuindo a repetição de códgo
    public FormularioHelper( FormularioActivity activity )
    {
        edtNome = activity.findViewById(R.id.edtNome);
        edtEndereco = activity.findViewById(R.id.edtEndereco);
        edtTelefone = activity.findViewById(R.id.edtTelefone);
        edtSite = activity.findViewById(R.id.edtSite);
        rbAvaliacao = activity.findViewById(R.id.rbAvaliacao);
        ivPerfil = activity.findViewById(R.id.ivPerfil);
        aluno = new Aluno();
    }

    //Retornando um Aluno com todos os dados já obtidos
    public Aluno getAluno()
    {
        aluno.setNome( edtNome.getText().toString() );
        aluno.setEndereco( edtEndereco.getText().toString() );
        aluno.setTelefone( edtTelefone.getText().toString() );
        aluno.setSite( edtSite.getText().toString() );
        aluno.setNota( Double.valueOf( rbAvaliacao.getProgress() ) );
        //Estamos retornando o valor vinculado a foto (ivPerfil)
        aluno.setCaminhoFoto((String) ivPerfil.getTag());
        return aluno;
    }

    public void preencherFormulario(Aluno aluno)
    {
        edtNome.setText(aluno.getNome());
        edtEndereco.setText(aluno.getEndereco());
        edtTelefone.setText(aluno.getTelefone());
        edtSite.setText(aluno.getSite());
        rbAvaliacao.setProgress(aluno.getNota().intValue());
        carregarFoto(aluno.getCaminhoFoto());

        this.aluno = aluno;
    }

    public void carregarFoto(String caminhoFoto)
    {
        if (caminhoFoto != null)
        {
            //Criando um bitmap a partir do caminho da nossa foto
            Bitmap bitmapPerfil = BitmapFactory.decodeFile(caminhoFoto);
            //Aqui estamos 'formatando' o bitmap que foi criado a partir do caminho da foto indicando as dimensões e um bool para aplicação de filtro
            Bitmap bitmapPerfilReduzido = Bitmap.createScaledBitmap(bitmapPerfil, 300, 300, true);
            //Aqui estamos 'setando' o bitmap criado para o ImageView de perfil
            ivPerfil.setImageBitmap(bitmapPerfilReduzido);
            //Aqui estamos indicando a taxa de escala de nossa foto dentro do ImageView
            ivPerfil.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivPerfil.setBackgroundColor(Color.BLACK);
            //Associando o valor do caminhoFoto a ImageView
            ivPerfil.setTag(caminhoFoto);
        }
    }
}
