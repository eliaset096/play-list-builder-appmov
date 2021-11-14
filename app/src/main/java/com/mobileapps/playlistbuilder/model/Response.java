package com.mobileapps.playlistbuilder.model;

public class Response {

    private Result tracks;

    public Response() {
    }
    public Response(Result tracks) {
        this.tracks = tracks;
    }

    public Result getTracks() {
        return tracks;
    }

    public void setTracks(Result tracks) {
        this.tracks = tracks;
    }
}
