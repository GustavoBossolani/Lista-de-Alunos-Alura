package br.com.listalunos.gustavo.listaalunos;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WebClient
{

    public String post(String json)
    {
        try
        {
            //Informando o endereço que desejamos conectar
            URL url = new URL("https://www.caelum.com.br/mobile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            //Especificando o tipo de arquivo que estamos enviado (JSON)
            connection.setRequestProperty("Content-type", "application/json");
            //Especificando o tipo de arquivo que queremos receber (JSON)
            connection.setRequestProperty("Accept", "application/json");

            //Aqui é indicado que estamos enviando parametros no corpo da requisição e não direto na url (POST)
            connection.setDoOutput(true);

            //Aqui estamos colocando os parametros no corpo da requisição
            PrintStream output = new PrintStream(connection.getOutputStream());
            output.println(json);

            //Estabelecendo a conexão de fato
            connection.connect();

            //Lendo a resposta do servidor
            Scanner scanner = new Scanner(connection.getInputStream());
            String resposta = scanner.next();
            return resposta;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
