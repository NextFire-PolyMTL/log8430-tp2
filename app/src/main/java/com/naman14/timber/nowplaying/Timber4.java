/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.naman14.timber.nowplaying;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.naman14.timber.MusicPlayer;
import com.naman14.timber.MusicService;
import com.naman14.timber.R;
import com.naman14.timber.adapters.SlidingQueueAdapter;
import com.naman14.timber.dataloaders.QueueLoader;
import com.naman14.timber.utils.ImageUtils;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

public class Timber4 extends BaseNowplayingFragmentEvenMode {

    ImageView mBlurredArt;
    RecyclerView horizontalRecyclerview;
    SlidingQueueAdapter horizontalAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_timber4, container, false);

        setMusicStateListener();
        setSongDetails(rootView);

        mBlurredArt = (ImageView) rootView.findViewById(R.id.album_art_blurred);
        horizontalRecyclerview = (RecyclerView) rootView.findViewById(R.id.queue_recyclerview_horizontal);

        setupHorizontalQueue();
        initGestures(mBlurredArt);

        return rootView;
    }

    private void setupHorizontalQueue() {
        horizontalRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        horizontalAdapter = new SlidingQueueAdapter(getActivity(), QueueLoader.getQueueSongs(getActivity()));
        horizontalRecyclerview.setAdapter(horizontalAdapter);
        horizontalRecyclerview.scrollToPosition(MusicPlayer.getQueuePosition() - 3);
    }

    private class setBlurredAlbumArt extends AsyncTask<Bitmap, Void, Drawable> {

        @Override
        protected Drawable doInBackground(Bitmap... loadedImage) {
            Drawable drawable = null;
            try {
                drawable = ImageUtils.createBlurredImageFromBitmap(loadedImage[0], getActivity(), 6);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return drawable;
        }

        @Override
        protected void onPostExecute(Drawable result) {
            if (result != null) {
                if (mBlurredArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    mBlurredArt.getDrawable(),
                                    result
                            });
                    mBlurredArt.setImageDrawable(td);
                    td.startTransition(200);

                } else {
                    mBlurredArt.setImageDrawable(result);
                }
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }


}
