package br.com.listalunos.gustavo.listaalunos.receiver;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.SmsMessage;
import android.widget.Toast;

import br.com.listalunos.gustavo.listaalunos.R;
import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;

import static android.telephony.SmsMessage.createFromPdu;

/*
    O BroadcastReceiver fará o papel de tratar eventos do sistema
    relacionados a mensagens de SMS de um aluno, porém essa classe também
    pode tratar diversos eventos de sistemas presentes no android

    Boradcast:
        Quando o Android recebe um evento de sistema (Ex:SMS), ele irá querer verificar a quais aplicativos interessa este evento.
        Assim que ele identifica, é feito uma cópia do evento para os aplicativos que irão tratá-lo.
        Isto significa que o Android "baterá na porta" de todos os apps - esta operação nós chamamos de BROADCAST.
        Porém, os aplicativos terão que manifestar esse interesse, é o que a nossa agenda precisará fazer também.

    É preciso aplicar permissões para recebimento de mensagem no arquivo manifest e também aplicar uma ação
*/


public class SMSReceiver extends BroadcastReceiver
{
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //Recebendo a mensagem, e colocando em um array caso venha dividida em PDUS
        Object[] pdus = (Object[]) intent.getSerializableExtra("pdus");

        byte[] pdu = (byte[]) pdus[0];
        String formato = (String) intent.getSerializableExtra("format");

        //Recuperando a mensagem recebida
        SmsMessage sms = SmsMessage.createFromPdu(pdu, formato);

        //Recuperando o numero de telefone da mensagem recebida
        String telefone = sms.getDisplayOriginatingAddress();

        AlunoDAO dao = new AlunoDAO(context);

        //Verificando se o numero de telefone etá registrado como um Aluno
        if (dao.ehAluno(telefone))
        {
            Toast.makeText(context, "Chegou uma SMS de um Aluno!", Toast.LENGTH_LONG).show();
            //Criando uma instância de um media player já indicando o arquivo de audio que ele irá reproduzir
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.msg);
            //Reproduzindo
            mediaPlayer.start();
        }
        dao.close();
    }
}
