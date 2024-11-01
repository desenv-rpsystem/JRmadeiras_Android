package com.rp_grf.jrmadeiras.Utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Autor: André
 * Data de criação: 02/02/2021
 *
 * Descrição: recebe a url de requisição como String, executa e retorna a resposta como String
 */

public class HTTPRequest extends AsyncTask<String, Void, String>{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... urls) {

        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == HttpURLConnection.HTTP_OK) {

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                String response = stringBuilder.toString();

                return response;
            }

            urlConnection.disconnect();

            /*
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Scanner sc = new Scanner(urlConnection.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (sc.hasNext())
                    sb.append(sc.next());
                String json = sb.toString();
                JSONObject obj = new JSONObject(json);
                //double distance = obj.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getDouble("value");
                urlConnection.disconnect();
                sc.close();
                //Uri uri = Uri.parse(json);

                System.out.println("Json: " + json.toString());
                System.out.println("Points: " + obj.getJSONArray("polyline"));
            } else {
                System.out.println("HttpURLConnection" + statusCode);
            }
             */

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return "Sem_Retorno";
    }

}
