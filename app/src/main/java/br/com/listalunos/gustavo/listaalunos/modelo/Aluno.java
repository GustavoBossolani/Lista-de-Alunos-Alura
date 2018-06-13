package br.com.listalunos.gustavo.listaalunos.modelo;

import java.io.Serializable;

/*
    Aqui é preciso implementar a interface Serializable para que o java consiga transformar esta classe em binário
    assim sendo possível passar informações do objeto entre uma activity e outra
*/

public class Aluno implements Serializable
{
    public Long id;
    public String Nome;
    public String Endereco;
    public String Telefone;
    public String Site;
    private Double nota;

    @Override
    public String toString()
    {
        return getId() + " - " + getNome();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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
}
