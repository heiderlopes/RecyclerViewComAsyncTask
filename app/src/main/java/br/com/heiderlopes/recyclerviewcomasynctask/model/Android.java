package br.com.heiderlopes.recyclerviewcomasynctask.model;

public class Android {

    public final static String TAG_ANDROID = "android";
    public final static String TAG_NOME = "nome";
    public final static String TAG_VERSAO = "versao";
    public final static String TAG_API = "api";
    public final static String TAG_URL_IMAGEM = "urlImagem";

    private String versao;
    private String nome;
    private String api;
    private String urlImagem;

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
