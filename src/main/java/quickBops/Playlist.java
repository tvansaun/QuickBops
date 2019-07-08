package quickBops;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Playlist {

    //map index -> song path
    private HashMap<Integer, String> map;

    private String directory;

    public Playlist(String directory){
        this.directory = directory;
        map = new HashMap<Integer, String>();
        initPlaylist();
    }

    private void initPlaylist(){
        File folder = new File(directory);
        String[] songs = folder.list();
        int index = 0;

        for(String song : songs) {

            //if file is an mp3 or wav put it in the map
            if(Pattern.matches(".*\\.mp3", song) || Pattern.matches(".*\\.wav", song)){
                map.put(++index, directory+song);
                System.out.printf("Added %s at index %d\n", song, index);
            }
            else{
                System.out.println(song + " is not an mp3");
            }

        }

    }

    public HashMap<Integer,String> getPlayList() {
        return map;
    }
}
