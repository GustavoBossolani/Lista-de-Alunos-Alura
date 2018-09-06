package br.com.listalunos.gustavo.listaalunos.retrofit;

import br.com.listalunos.gustavo.listaalunos.retrofit.services.AlunoService;
import br.com.listalunos.gustavo.listaalunos.retrofit.services.DispositivoService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class InicializadorRetrofit {


    private final String BASE_URL = "http://192.168.0.12:8080/api/";
    private final Retrofit retrofit;

    public InicializadorRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public AlunoService getAlunoService() {
        return retrofit.create(AlunoService.class);
    }

    public DispositivoService getDispositivoService() {
        return  retrofit.create(DispositivoService.class);
    }
}
