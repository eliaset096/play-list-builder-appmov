package com.mobileapps.playlistbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.mobileapps.playlistbuilder.model.Response;
import com.mobileapps.playlistbuilder.model.Song;
import com.mobileapps.playlistbuilder.util.HTTPSWebUtilDomi;

import java.util.UUID;

import okio.GzipSink;

public class MainActivity extends AppCompatActivity {

    private String userID = "SaqGenGApOhV8Mb7VXNI";
    private FirebaseFirestore DB = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Agregar canción
        //addSong(new Song(UUID.randomUUID().toString(), "Nombre Otra Canción", "Nombre Otro Artista"));
        // Obtiene canciones
        //getSongs();
        // Busca una canción por el nombre
        searchSong("Be Happy");
    }


    /**
     * Agrega una canción a la base de datos
     * @param song
     */
    public void addSong(Song song){
        DB.collection("PlayList").document(userID)
                .collection("songs").document(song.getId()).set(song);
    }

    /**
     * Obtiene las canciones
     */
    public void getSongs(){
        DB.collection("PlayList")
                .document(userID).collection("songs").get().addOnCompleteListener(
                        task -> {
                            for (DocumentSnapshot snapshot:
                                 task.getResult()) {
                                Song song = snapshot.toObject(Song.class);
                                Log.w(">>>", song.getName());
                            }
                        }
        );
    }


    /**
     * Busca una canción en el API de Spotify
     * @param songName
     */
    public void searchSong(String songName){
        new Thread(
                ()->{
                    HTTPSWebUtilDomi utilDomi = new HTTPSWebUtilDomi();
                    String json = utilDomi.GETrequest(songName);

                    Gson gson =  new Gson();
                    Response response = gson.fromJson(json, Response.class);

                    Log.w(">>>", response.getTracks().getItems()[0].getName());
                    Log.w(">>>", response.getTracks().getItems()[0].getArtists()[0].getName());

                    addSong(new Song(
                                    UUID.randomUUID().toString(),
                                    response.getTracks().getItems()[0].getName(),
                                    response.getTracks().getItems()[0].getArtists()[0].getName())
                    );
                }
        ).start();
    }



}