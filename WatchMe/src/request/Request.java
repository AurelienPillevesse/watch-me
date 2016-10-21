/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;
import viewmodel.AllSerieVM;
import viewmodel.SeasonVM;
import viewmodel.SerieVM;

/**
 *
 * @author Aurélien
 */
public class Request {
    
    /**
     * Obtenir une réponse JSON
     * @param myURL l'URL grâce à laquelle on souhaite des informations
     * @return un JSONObject avec la réponse JSON
     */
    public JSONObject callURL(String myURL) throws RuntimeException {
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(myURL);
            urlConn = url.openConnection();
            callUrlTry(urlConn, in, sb);
        } catch (Exception e) {
            throw new RuntimeException("Exception while calling URL: "+ myURL, e);
        }
        return new JSONObject(sb.toString());
    }
    
    /**
     * Partie try de la méthode CallURL
     * @param urlConn une instance URLConnection
     * @param in une instance InputStreamReader
     * @param sb une instance StringBuilder
     * @throws IOException 
     */
    private void callUrlTry(URLConnection urlConn, InputStreamReader in, StringBuilder sb) throws IOException {
        if (urlConn != null) {
            urlConn.setReadTimeout(60 * 1000);
        }
        if (urlConn != null && urlConn.getInputStream() != null) {
            in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
            BufferedReader bufferedReader = new BufferedReader(in);
            bufferedReaderNotNull(bufferedReader, sb);
        }
        in.close();
    }
    
    /**
     * Concatène le bufferedReader pour en former une chaine de caractère
     * @param bufferedReader instance BufferedReader
     * @param sb instance StringBuilder
     * @throws IOException 
     */
    private void bufferedReaderNotNull(BufferedReader bufferedReader, StringBuilder sb) throws IOException {
        if (bufferedReader != null) {
            int cp;
            while ((cp = bufferedReader.read()) != -1) {
                sb.append((char) cp);
            }
            bufferedReader.close();
        }
    }
    
    /**
     * Obtenir les 20 séries les plus populaires
     * @param viewModelAllSerie l'instance de AllSerieVM
     * @param l la listview affichant les séries
     * @param progress la progressbar
     * @return la tâche effectuant l'obtension et l'ajout des 20 séries les plus populaires
     */
    public Task getApiInformationPopular(AllSerieVM viewModelAllSerie, ListView l, ProgressBar progress) throws RuntimeException {
        Task t = new Task() {
            @Override
            protected Object call() throws Exception {
                JSONArray jsa = callURL("https://api.themoviedb.org/3/tv/popular?api_key=1ea8af8e26eb6927c38617820bd70287&language=fr-FR&page=1").getJSONArray("results");
                for (int i = 0; i < jsa.length(); i++) {
                    addSerieCurrentJsonObject(jsa, i, viewModelAllSerie);
                    updateProgress(i+1, 20);
                }
                actionWhenFinish(l, progress);
                return true;
            }
        };
        return t;
    }
    
    /**
     * Ajouter la série avec l'élément courant du tableau de séries dans la réponse JSON
     * @param jsa le tableau JSON d'épisodes
     * @param i l'élément courant
     * @param viewModelAllSerie l'instance AllSerieVM
     */
    private void addSerieCurrentJsonObject(JSONArray jsa, int i, AllSerieVM viewModelAllSerie) {
        JSONObject j = (JSONObject) jsa.get(i);
        viewModelAllSerie.addSerie(j.get("name").toString(), j.get("overview").toString(), j.get("original_name").toString(), Integer.parseInt(j.get("id").toString()), j.get("poster_path").toString());
    }
    
    /**
     * Actions à effectuer lorsque toutes les séries ont été ajoutés
     * @param l la listview des séries
     * @param progress la barre de progression
     */
    private void actionWhenFinish(ListView l, ProgressBar progress) {
        if(l != null) {
            l.setDisable(false);
        }
        progress.setVisible(false);
    }
    
    /**
     * Ajouter l'épisode avec l'élément courant du tableau d'épisodes dans la réponse JSON
     * @param jsa le tableau JSON d'épisodes
     * @param i l'élément courant
     * @param sVM l'instance de SeasonVM
     */
    private void addEpisodeCurrentJsonObject(JSONArray jsa, int i, SeasonVM sVM) {
        JSONObject j = (JSONObject) jsa.get(i);
        sVM.addEpisode((int) j.get("episode_number"), j.get("name").toString(), j.get("overview").toString());
    }
    
    /**
     * Obtenir les saisons et épisodes d'une série depuis l'API TheMovieDB
     * @param id l'id de la série
     * @param tv instance de SerieVM
     */
    public void getApiInformationSpecified(int id, SerieVM tv) throws RuntimeException {
        JSONObject jso = callURL("https://api.themoviedb.org/3/tv/" + id + "?api_key=1ea8af8e26eb6927c38617820bd70287&language=fr-FR");
        int numberSeason = (int) (jso.get("number_of_seasons"));
                
        for (int indexSeason = 1; indexSeason <= numberSeason; indexSeason++) {
            tv.addSeason(indexSeason);
            SeasonVM s = (SeasonVM) tv.getListViewModelSeason().get(indexSeason-1);
            jso = callURL("https://api.themoviedb.org/3/tv/"+ id +"/season/"+ indexSeason +"?api_key=1ea8af8e26eb6927c38617820bd70287&language=fr-FR");
            JSONArray jsa = jso.getJSONArray("episodes");
            for (int i = 0; i < jsa.length(); i++) {
                addEpisodeCurrentJsonObject(jsa, i, s);
            }
        }
    }
}
