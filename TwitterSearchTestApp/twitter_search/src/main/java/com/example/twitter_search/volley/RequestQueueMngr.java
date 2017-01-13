package com.example.twitter_search.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Sagi on 1/12/2017.
 * Using Volley for executing all of our network requests
 * Since network requests is mainly what this library do, we better create requests queue once
 * using this singleton
 */
public class RequestQueueMngr {
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static final Object syncObj = new Object();
    private static RequestQueueMngr mInstance;

    private RequestQueueMngr(Context ctx) {
        mContext = ctx;

        // Initialize request queue
        getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Use thread safe singleton accessing.
     * Can't use holder-initialization-idiom because we require to accept context when creating the manager
     * @param ctx
     * @return
     */
    public static RequestQueueMngr getInstance(Context ctx) {
        if (mInstance == null) {
            synchronized (syncObj) {
                if (mInstance == null) {
                    mInstance = new RequestQueueMngr(ctx);
                }
            }
        }

        return mInstance;
    }
}
