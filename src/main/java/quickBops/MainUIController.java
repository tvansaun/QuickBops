package quickBops;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.text.Text;

import java.util.*;
import java.io.*;
import java.lang.*;
import com.google.common.collect.EvictingQueue;
import javafx.stage.Stage;

public class MainUIController {

    //controller stage
    private Stage mainStage;

    //reference object to start stage
    private final StartupUIController startupController;

    //Map of song index -> mp3 file path
    private HashMap<Integer, String> playlist;

    //Removes oldest element once queue is full
    private EvictingQueue<Integer> recentlyPlayed;

    private String directory;

    @FXML
    private Button playButton;

    @FXML
    private Text songTitle;


    public MainUIController(StartupUIController startupController){
        //make startup controller usable here
        this.startupController = startupController;

        //create new stage
        mainStage = new Stage();

        //load FXML
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainUI.fxml"));
            loader.setController(this);
            mainStage.setScene(new Scene(loader.load()));
            mainStage.setTitle("QuickBops");
        }
        catch (IOException e){
            e.printStackTrace();
        }

        this.directory = startupController.getEnteredDirectory();
        Playlist pl = new Playlist(directory);
        this.playlist = pl.getPlayList();
        this.recentlyPlayed = EvictingQueue.create((int)(playlist.size() - 1));
    }

    public void showStage() {
        playButton();
        mainStage.showAndWait();
    }


    MediaPlayer player;
    @FXML
    private void playButton() {

        //get next song and start playback
        player = new MediaPlayer(nextSong());
        player.play();
        playButton.setText("Pause");

        //play pause functionality
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Status status = player.getStatus();

                if(status == Status.UNKNOWN || status == Status.HALTED){
                    return;
                }

                if(status == Status.PAUSED || status == Status.READY || status == Status.STOPPED){
                    playButton.setText("Pause");
                    player.play();
                }

                else {
                    playButton.setText("Play");
                    player.pause();
                }

            }
        });

        //when song is over, play next song
        player.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                playButton();
            }
        });
    }

    //stops playback and moves to next song
    @FXML
    private void nextButton() {
        player.stop();
        playButton();
    }

    @FXML
    private void close() {
        if(player != null) {
            player.stop();
        }
        mainStage.close();
    }

    //returns next song to be played and updates recently played queue
    private Media nextSong() {

        //generate random index
        int randomIndex = (int) (Math.random() * (playlist.size())) + 1;

        //if the index is saved in the recentlyPlayed queue,
        //keep generating until an unused index is found
        while(recentlyPlayed.contains(randomIndex)){
            randomIndex = (int) (Math.random() * (playlist.size())) + 1;
        }

        //return next random song
        try {
            String uriString = new File(playlist.get(randomIndex)).toURI().toString();
            Media song = new Media(uriString);
            songTitle.setText(sanitize(playlist.get(randomIndex)));
            recentlyPlayed.add(randomIndex);
            return song;
        }
        catch(NullPointerException e){
            System.out.println("No song at index " + randomIndex);
        }

        return null;
    }

    //remove path and file extension from song
    private String sanitize(String str){
        String[] words = str.split("/");
        String withExtension = words[words.length-1];
        String withoutExtension = withExtension.substring(0,withExtension.length()-4);
        return withoutExtension;
    }

}