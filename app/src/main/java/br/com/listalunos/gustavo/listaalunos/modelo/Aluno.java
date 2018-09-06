package br.com.listalunos.gustavo.listaalunos.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/*
    Aqui é preciso implementar a interface Serializable para que o java consiga transformar esta classe em binário
    assim sendo possível passar informações do objeto entre uma activity e outra
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class Aluno implements Serializable{

    public String id;
    public String Nome;
    public String Endereco;
    public String Telefone;
    public String Site;
    private Double nota;
    private String caminhoFoto;
    private int desativado;

    public int getDesativado() {
        return desativado;
    }

    public void setDesativado(int desativado) {
        this.desativado = desativado;
    }

    @Override
    public String toString()
    {
        return getId() + " - " + getNome();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return Nome;
    }

    public void setNome(String nome)
    {
        Nome = nome;
    }

    public String getEndereco()
    {
        return Endereco;
    }

    public void setEndereco(String endereco)
    {
        Endereco = endereco;
    }

    public String getTelefone()
    {
        return Telefone;
    }

    public void setTelefone(String telefone)
    {
        Telefone = telefone;
    }

    public String getSite()
    {
        return Site;
    }

    public void setSite(String site)
    {
        Site = site;
    }

    public Double getNota()
    {
        return nota;
    }

    public void setNota(Double nota)
    {
        this.nota = nota;
    }

    public String getCaminhoFoto()
    {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto)
    {
        this.caminhoFoto = caminhoFoto;
    }

    public boolean estaDesativado() {
        return desativado == 1;
    }
}
