package com.example.gedimatapplication;

public class Realisation {
    private Integer id_real;
    private String titre;
    private String descriptif;

    public Realisation()
    {
        this.id_real=null;
        this.titre="";
        this.descriptif="";
    }

    public Integer getIdReal(){
        return id_real;
    }

    public void setIdRealisation(Integer id_real){
        this.id_real = id_real;
    }

    public String getTitre(){
        return titre;
    }

    public void setTitre(String titre){
        this.titre = titre;
    }

    public String getDescriptif(){
        return descriptif;
    }

    public void setDescriptif(String descriptif){
        this.descriptif = descriptif;
    }

    @Override
    public String toString() {
        return "Realisation{" +
                "id_real='" + id_real +
                ", nom='" + titre +
                ", descriptif='" + descriptif +
                '}';
    }
}
