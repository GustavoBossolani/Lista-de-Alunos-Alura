package br.com.listalunos.gustavo.listaalunos;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import br.com.listalunos.gustavo.listaalunos.dao.AlunoDAO;
import br.com.listalunos.gustavo.listaalunos.modelo.Aluno;

public class MapaFragment extends SupportMapFragment implements OnMapReadyCallback
{
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        //Recuperando uma Instância do googleMaps, com isso será possivel altera o mapa
        getMapAsync(this);
    }

    //Este método será chamado assim que o mapa estiver pronto(retorno de getMapAsync)
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //Recuperando as coordenadas a partir de um endereço e guardando em uma variável do tipo LatLng
        LatLng posicaoInicial = retornarCoordenadas("Rua Ascânio 247, Vila Diva, São Paulo");

        //Caso a posicao não seja nula, iremos atualizar a camera do maps
        if (posicaoInicial != null)
        {
            //Aqui estamos 'setando' a posição inicial e o zoom da camera do maps
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(posicaoInicial, 17);
            googleMap.moveCamera(update);
        }

        exibirEnderecoAlunos(googleMap);
        exibirEnderecoInicial(googleMap,"Rua Ascânio 247, Vila Diva, São Paulo");

    }

    private void  exibirEnderecoInicial(GoogleMap googleMap, String endereco)
    {
        LatLng enderecoLatLng = retornarCoordenadas(endereco);
        if (enderecoLatLng != null)
        {
            MarkerOptions marcador = new MarkerOptions();
            marcador.position(enderecoLatLng);
            marcador.title("Posição Inicial");
            marcador.snippet("A escola se encontra aqui!");
            marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(marcador);
        }
    }

    private void exibirEnderecoAlunos(GoogleMap googleMap)
    {
        AlunoDAO dao = new AlunoDAO(getContext());
        for (Aluno aluno: dao.buscarAlunos())
        {
            //Recuperando o endereço dos alunos em coordenadas
            LatLng coordenada = retornarCoordenadas(aluno.getEndereco());
            if(coordenada != null)
            {
                //Criando uma instância de um marcador
                MarkerOptions marcador = new MarkerOptions();

                //Posição do marker
                marcador.position(coordenada);

                //Titulo do marker
                marcador.title(aluno.getNome());

                //Descrição do marker
                marcador.snippet(String.valueOf(aluno.getNota()));

                //Definindo uma cor custmizada para o marker
                marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                //Adcionando de fato a marcação no mapa
                googleMap.addMarker(marcador);
            }
            dao.close();
        }
    }

    private LatLng retornarCoordenadas(String endereco)
    {
        try
        {
            //O Geocoder, converte uma string(endereço) para o padrão Latitude e Longitude (LatLng)
            Geocoder geocoder = new Geocoder(getContext());

            //Armazanando os resultados de pesquisa de um endereço dentro de uma lista
            List<Address> resultadosPesquisaEndereco = geocoder.getFromLocationName
                    (endereco, 1);

            //Caso a pesquisa por endereço for diferente de nulo, retornaremos o endereço convertido em LatLng
            if (!resultadosPesquisaEndereco.isEmpty())
            {
                //Retornando o endereço em longitude e latitude
                LatLng posicao = new LatLng(
                        resultadosPesquisaEndereco.get(0).getLatitude(),
                        resultadosPesquisaEndereco.get(0).getLongitude());
                return posicao;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}















