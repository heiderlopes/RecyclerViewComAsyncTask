package br.com.heiderlopes.recyclerviewcomasynctask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.heiderlopes.recyclerviewcomasynctask.adapter.AndroidAdapter;
import br.com.heiderlopes.recyclerviewcomasynctask.listener.OnItemClickListener;
import br.com.heiderlopes.recyclerviewcomasynctask.model.Android;
import br.com.heiderlopes.recyclerviewcomasynctask.model.DetalheActivity;

public class MainActivity extends AppCompatActivity {

    // CONNECTION_TIMEOUT e READ_TIMEOUT são em milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private AndroidAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AsyncFetch().execute("http://www.mocky.io/v2/58af1fb21000001e1cc94547");
    }

    //Params: Tipo do Objeto que será passado por parâmetro, caso não precise mandar algum parâmetro, colocar colo "Void";
    //Progress: Tipo do Objeto usado para informar o progresso do processo, utilizados no método publishProgress(Progress...) e onProgressUpdate(Progress...);
    //Result: Tipo do Objeto de Retorno. Usado no método onInBackground(Params...) como sendo o seu retorno e o onPostExecute(Result).
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        //Este é o primeiro método chamado quando executado o execute(Params...);
        //Este método é utilizado, entre outras coisas, para realizar as configurações
        // do processo e/ou ativar algum Status Loader indicando que está acontecendo algum processo.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        //Este método já é chamado após o método onPreExecute() ser finalizado.
        // É nele que o processo será realizado em uma thread separada da Thread Principal;
        // Aqui você também poderá chamar o método publishProgress(Progress...) informando o valor
        // de andamento do seu processo, o mesmo, quando chamado,
        // irá executar o onProgressUpdate(Progress...)
        // passando esse valor que você informou no publishProgress(Progress...);
        // Também é aconcelhável que, no método, seja validado sempre o método isCancelled()
        // (este método retornará TRUE caso sejá chamado o método cancel()) para, caso retornar TRUE,
        // você realize a parada do seu processamento.
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return e.toString();
            }
            try {
                // Setup HttpURLConnection class to send and receive data
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Realiza a leitura dos dados do servidor
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Envia os dados para o onPostExecute
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }


        // Este método é chamado logo após o término do método onInBackground(Params...);
        // Ele recebe o retorno do onInBackground(Params...).
        // A partir do tipo configurado como retorno na configuração da Classe.
        // É aqui que você terá a oportunidade de trabalhar com o Parâmetro recebido, por exemplo,
        // caso você tenha feito o download de uma image, aqui você irá receber os bytes desta imagem
        // e irá coloca-la no seu ImageView.
        @Override
        protected void onPostExecute(String result) {

            //Este método roda na UI thread
            pdLoading.dismiss();
            List<Android> data = new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONObject json = new JSONObject(result);
                JSONArray jArray = json.getJSONArray(Android.TAG_ANDROID);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonData = jArray.getJSONObject(i);
                    Android android = new Android();
                    android.setApi(jsonData.getString(Android.TAG_API));
                    android.setNome(jsonData.getString(Android.TAG_NOME));
                    android.setUrlImagem(jsonData.getString(Android.TAG_URL_IMAGEM));
                    android.setVersao(jsonData.getString(Android.TAG_VERSAO));
                    data.add(android);
                }

                setUpRecyclerView(data);

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    private void setUpRecyclerView(List<Android> data) {

        RecyclerView rcLista = (RecyclerView) findViewById(R.id.rcLista);
        mAdapter = new AndroidAdapter(MainActivity.this, data);
        rcLista.setAdapter(mAdapter);
        rcLista.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mAdapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                final Android android = mAdapter.getItem(position);
                Intent i = new Intent(MainActivity.this, DetalheActivity.class);
                i.putExtra(Android.TAG_NOME, android.getNome());
                startActivity(i);

            }
        });
    }
}
