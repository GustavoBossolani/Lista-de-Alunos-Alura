package br.com.listalunos.gustavo.listaalunos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.listalunos.gustavo.listaalunos.R;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class AlunosAdapter extends BaseAdapter
{
    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) 
    {
        this.alunos = alunos;
        this.context = context;
    }

    /*
       Em algum momento, a lista precisará parar de devolver estas Views.
       Precisaremos definir quantos itens têm na lista de alunos,
       ela fará isto perguntando novamente para o adapter, usando o método getCount.
     */
    @Override
    public int getCount()
    {
        //Estamos retornando a quantidade de objetos na lista de Alunos assim a lista poderá instânciar a quantidade certa de objetos
        return alunos.size();
    }

    @Override
    public Object getItem(int position)
    {
        return alunos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return alunos.get(position).getId();
    }

    /*
        O método mais importânte que temos no AlunoAdapter é o getView,
        que é invocado pela lista quando precisa mostrar algum item
    */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Aluno aluno = alunos.get(position);

        //Obtendo a instância de um inflater atravé do método from
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = convertView;
        if (convertView == null)
        {
            //Aqui podemos recuperar o layout list_item através da variável view
            view = inflater.inflate(R.layout.list_item_layout, parent, false);
        }

        //Recuperando os itens de nosso layout
        TextView campoNome = view.findViewById(R.id.list_item_nome);
        campoNome.setText(aluno.getNome());

        TextView campoTelefone = view.findViewById(R.id.list_item_telefone);
        campoTelefone.setText(aluno.getTelefone());

        TextView campoEndereco = view.findViewById(R.id.list_item_endereco);
        if (campoEndereco != null)
        {
            campoEndereco.setText(aluno.getEndereco());
        }

        TextView campoSite = view.findViewById(R.id.list_item_site);
        if (campoEndereco != null)
        {
            campoSite.setText(aluno.getSite());
        }

        ImageView campoFoto = view.findViewById(R.id.list_item_foto);
        //Para recuperar a foto do usuário, usamos o código do FormulárioHelper para apontar o diretório da imagem
        String caminhoFoto = aluno.getCaminhoFoto();
        if (caminhoFoto != null)
        {
            Bitmap bitmapPerfil = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapPerfilReduzido = Bitmap.createScaledBitmap(bitmapPerfil, 100, 100, true);
            campoFoto.setImageBitmap(bitmapPerfilReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else
            {
                campoFoto.setImageResource(R.drawable.ic_person_white_48dp);
                campoFoto.setBackgroundColor(Color.argb(255, 73, 89,154));
            }
        return view;
    }
}
