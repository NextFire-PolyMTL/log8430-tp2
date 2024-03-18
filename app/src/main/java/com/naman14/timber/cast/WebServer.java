package com.naman14.timber.cast;

import android.content.Context;
import android.net.Uri;

import com.naman14.timber.utils.Constants;
import com.naman14.timber.utils.TimberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {

    private Context context;
    private Uri songUri, albumArtUri;

    public WebServer(Context context) {
        super(Constants.CAST_SERVER_PORT);
        this.context = context;
    }

    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header,
                          Map<String, String> parameters,
                          Map<String, String> files) {
        String Id = parameters.get("id");
        if (uri.contains("albumart")) {
            return serveImg(Id);
        } else if (uri.contains("song")) {
            return serveSong(Id);
        }
        return newFixedLengthResponse("Error");
    }

    protected Response serveSong(String songId){
        this.songUri = TimberUtils.getSongUri(context, Long.parseLong(songId));
        if (songUri == null) {
            return newFixedLengthResponse("Error");
        }
        FileInputStream fisSong = null;
        File song = new File(songUri.getPath());
        try {
            fisSong = new FileInputStream(song);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //serve the song
        return newFixedLengthResponse(Response.Status.OK, "audio/mp3", fisSong, song.length());
    }

    protected Response serveImg(String albumId){
        this.albumArtUri = TimberUtils.getAlbumArtUri(Long.parseLong(albumId));
        if (albumArtUri != null) {
            return newFixedLengthResponse("Error");
        }
        InputStream fisAlbumArt = null;
        try {
            fisAlbumArt = context.getContentResolver().openInputStream(albumArtUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //serve the song
        return newChunkedResponse(Response.Status.OK, "image/jpg", fisAlbumArt);
    }

}
