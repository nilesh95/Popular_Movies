package app.popular_movies.nilesh.popularmovies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.twotoasters.jazzylistview.effects.HelixEffect;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import app.popular_movies.nilesh.popularmovies.Adapter.*;

import app.popular_movies.nilesh.popularmovies.RecyclerItem.*;


public class Popular extends Fragment implements SearchView.OnQueryTextListener{
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter,smAdapter;
    private GridLayoutManager mLayoutManager;
    ArrayList<String> imglist = new ArrayList<String>();
    ArrayList<String> titlelist = new ArrayList<String>();
    ArrayList<String> back_imglist = new ArrayList<String>();
    ArrayList<String> plotlist = new ArrayList<String>();
    ArrayList<String> user_ratinglist = new ArrayList<String>();
    ArrayList<String> release_datelist = new ArrayList<String>();
    JazzyRecyclerViewScrollListener jazzyScrollListener;
    String API_KEY;

    final ArrayList<String> simglist = new ArrayList<String>();
    final ArrayList<String> stitlelist = new ArrayList<String>();
    final ArrayList<String> sback_imglist = new ArrayList<String>();
    final ArrayList<String> splotlist = new ArrayList<String>();
    final ArrayList<String> suser_ratinglist = new ArrayList<String>();
    final ArrayList<String> srelease_datelist = new ArrayList<String>();



    public Popular() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        setHasOptionsMenu(true);
        mRecyclerView.setHasFixedSize(true);
        API_KEY=getActivity().getResources().getString(R.string.API_KEY);


        if (getResources().getConfiguration().orientation == 2) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
            mRecyclerView.setLayoutManager(mLayoutManager);
          final String url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                }

                                mAdapter = new MyAdapter(imglist);
                                mRecyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();

                                //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();

                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    // do something...
                    String url = "https://api.themoviedb.org/3/movie/popular?page=" + current_page + "&&api_key="+API_KEY;

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // the response is already constructed as a JSONObject!
                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();
                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();
                                   // Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });


            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener2(getActivity(), new RecyclerItemClickListener2.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (simglist.size() != 0) {


                                Intent intent = new Intent(getActivity(), Movie_description.class);
                                intent.putExtra("title", stitlelist.get(position));
                                intent.putExtra("b_img", sback_imglist.get(position));
                                intent.putExtra("overview", splotlist.get(position));
                                intent.putExtra("vote", suser_ratinglist.get(position));
                                intent.putExtra("r_date", srelease_datelist.get(position));
                                intent.putExtra("p_img", simglist.get(position));
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(getActivity(), Movie_description.class);
                                intent.putExtra("title", titlelist.get(position));
                                intent.putExtra("b_img", back_imglist.get(position));
                                intent.putExtra("overview", plotlist.get(position));
                                intent.putExtra("vote", user_ratinglist.get(position));
                                intent.putExtra("r_date", release_datelist.get(position));
                                intent.putExtra("p_img", imglist.get(position));
                                startActivity(intent);
                            }
                        }
                    })
            );

        } else if (getResources().getConfiguration().orientation == 1) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
            mRecyclerView.setLayoutManager(mLayoutManager);
            String url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                }

                                mAdapter = new MyAdapter(imglist);
                                mRecyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();

                                //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();


                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);

            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {


                @Override
                public void onLoadMore(int current_page) {
                    // do something...
                    String url = "https://api.themoviedb.org/3/movie/popular?page=" + current_page + "&&api_key="+API_KEY;

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // the response is already constructed as a JSONObject!
                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();
                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();

                                    //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);
                }
            });


            jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            mRecyclerView.addOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(new HelixEffect());


                mRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener2(getActivity(), new RecyclerItemClickListener2.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(simglist.size()!=0)
                                {


                                    Intent intent = new Intent(getActivity(), Movie_description.class);
                                    intent.putExtra("title", stitlelist.get(position));
                                    intent.putExtra("b_img", sback_imglist.get(position));
                                    intent.putExtra("overview", splotlist.get(position));
                                    intent.putExtra("vote", suser_ratinglist.get(position));
                                    intent.putExtra("r_date", srelease_datelist.get(position));
                                    intent.putExtra("p_img", simglist.get(position));
                                    startActivity(intent);

                                }
                                else {
                                    Intent intent = new Intent(getActivity(), Movie_description.class);
                                    intent.putExtra("title", titlelist.get(position));
                                    intent.putExtra("b_img", back_imglist.get(position));
                                    intent.putExtra("overview", plotlist.get(position));
                                    intent.putExtra("vote", user_ratinglist.get(position));
                                    intent.putExtra("r_date", release_datelist.get(position));
                                    intent.putExtra("p_img", imglist.get(position));
                                    startActivity(intent);
                                }
                            }
                        })
                );

        }



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);





        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);

        API_KEY=getActivity().getResources().getString(R.string.API_KEY);


        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        if(!searchView.isActivated())
        {
            simglist.clear();
            stitlelist.clear();
            sback_imglist.clear();
            splotlist.clear();
            srelease_datelist.clear();
            suser_ratinglist.clear();

            Log.i("MainActivity", "s2");
            imglist.clear();
            titlelist.clear();
            back_imglist.clear();
            plotlist.clear();
            user_ratinglist.clear();
            release_datelist.clear();
            String url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

           JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                }

                                mAdapter = new MyAdapter(imglist);
                                mRecyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();

                                //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();

                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);
            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    // do something...
                    String url = "https://api.themoviedb.org/3/movie/popular?page=" + current_page + "&&api_key="+API_KEY;

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // the response is already constructed as a JSONObject!
                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();
                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();
                                    //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });

        }

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search),
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {

                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {


                        simglist.clear();
                        stitlelist.clear();
                        sback_imglist.clear();
                        splotlist.clear();
                        srelease_datelist.clear();
                        suser_ratinglist.clear();
                        imglist.clear();
                        titlelist.clear();
                        back_imglist.clear();
                        plotlist.clear();
                        user_ratinglist.clear();
                        release_datelist.clear();
                        String url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

                      JsonObjectRequest jsonRequest = new JsonObjectRequest
                                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // the response is already constructed as a JSONObject!
                                        try {
                                            JSONArray json = response.getJSONArray("results");
                                            for (int i = 0; i < json.length(); i++) {
                                                JSONObject jsonObject = json.getJSONObject(i);
                                                String movy = jsonObject.getString("poster_path");
                                                String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                                imglist.add(site);
                                                String title = jsonObject.getString("title");
                                                titlelist.add(title);
                                                String backdrop_path = jsonObject.getString("backdrop_path");
                                                String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                                back_imglist.add(back_img);
                                                String plot = jsonObject.getString("overview");
                                                plotlist.add(plot);
                                                String user_rating = jsonObject.getString("vote_average");
                                                user_ratinglist.add(user_rating);
                                                String release_date = jsonObject.getString("release_date");
                                                release_datelist.add(release_date);
                                            }

                                            mAdapter = new MyAdapter(imglist);
                                            mRecyclerView.setAdapter(mAdapter);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            isOnline();

                                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        isOnline();

                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        Volley.newRequestQueue(getActivity()).add(jsonRequest);

                        return true;
                    }
                });

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        simglist.clear();
        stitlelist.clear();
        sback_imglist.clear();
        splotlist.clear();
        srelease_datelist.clear();
        suser_ratinglist.clear();
        onChange(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
    public void onChange(String query) {
        query = query.replaceAll(" ", "%20");



        String url =  "http://api.themoviedb.org/3/search/movie?api_key="+API_KEY+"&query='"+query+"'";




        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            JSONArray json = response.getJSONArray("results");
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonObject = json.getJSONObject(i);
                                String movy = jsonObject.getString("poster_path");
                                String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                simglist.add(site);
                                String title = jsonObject.getString("title");
                                stitlelist.add(title);
                                String backdrop_path = jsonObject.getString("backdrop_path");
                                String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                sback_imglist.add(back_img);
                                String plot = jsonObject.getString("overview");
                                splotlist.add(plot);
                                String user_rating = jsonObject.getString("vote_average");
                                suser_ratinglist.add(user_rating);
                                String release_date = jsonObject.getString("release_date");
                                srelease_datelist.add(release_date);
                            }
                            if(simglist.size()!=0) {

                                smAdapter = new MyAdapter(simglist);
                                mRecyclerView.setAdapter(smAdapter);

                            }
                            else {
                                onEmpty();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            onEmpty();

                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        onEmpty();
                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(getActivity()).add(jsonRequest);


    }

    public void isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        API_KEY=getActivity().getResources().getString(R.string.API_KEY);
        if(activeNetwork==null){
            Snackbar.make(mRecyclerView, "Please Connect to the Internet", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOnline();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
        else
        {

            Log.i("MainActivity", "s5");
            imglist.clear();
            titlelist.clear();
            back_imglist.clear();
            plotlist.clear();
            user_ratinglist.clear();
            release_datelist.clear();
            String url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY;

            JsonObjectRequest jsonRequest = new JsonObjectRequest
                    (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // the response is already constructed as a JSONObject!
                            try {
                                JSONArray json = response.getJSONArray("results");
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject jsonObject = json.getJSONObject(i);
                                    String movy = jsonObject.getString("poster_path");
                                    String site = "http://image.tmdb.org/t/p/w185/" + movy;
                                    imglist.add(site);
                                    String title = jsonObject.getString("title");
                                    titlelist.add(title);
                                    String backdrop_path = jsonObject.getString("backdrop_path");
                                    String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                    back_imglist.add(back_img);
                                    String plot = jsonObject.getString("overview");
                                    plotlist.add(plot);
                                    String user_rating = jsonObject.getString("vote_average");
                                    user_ratinglist.add(user_rating);
                                    String release_date = jsonObject.getString("release_date");
                                    release_datelist.add(release_date);
                                }

                                mAdapter = new MyAdapter(imglist);
                                mRecyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                isOnline();

                                //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            isOnline();

                            //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(getActivity()).add(jsonRequest);
            mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    // do something...
                    String url = "https://api.themoviedb.org/3/movie/popular?page=" + current_page + "&&api_key="+API_KEY;

                    JsonObjectRequest jsonRequest = new JsonObjectRequest
                            (Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // the response is already constructed as a JSONObject!
                                    try {
                                        JSONArray json = response.getJSONArray("results");
                                        for (int i = 0; i < json.length(); i++) {
                                            JSONObject jsonObject = json.getJSONObject(i);
                                            String movy = jsonObject.getString("poster_path");
                                            String site = "http://image.tmdb.org/t/p/w300/" + movy;
                                            imglist.add(site);
                                            String title = jsonObject.getString("title");
                                            titlelist.add(title);
                                            String backdrop_path = jsonObject.getString("backdrop_path");
                                            String back_img = "http://image.tmdb.org/t/p/w500/" + backdrop_path;
                                            back_imglist.add(back_img);
                                            String plot = jsonObject.getString("overview");
                                            plotlist.add(plot);
                                            String user_rating = jsonObject.getString("vote_average");
                                            user_ratinglist.add(user_rating);
                                            String release_date = jsonObject.getString("release_date");
                                            release_datelist.add(release_date);
                                        }
                                        mAdapter.notifyItemInserted(imglist.size() - 1);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        isOnline();
                                        //Toast.makeText(getActivity(), "Something went wrong!!please check your connection 111", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    isOnline();
                                    //Toast.makeText(getActivity(), "Something went wrong!!please check your connection", Toast.LENGTH_SHORT).show();
                                }
                            });

                    Volley.newRequestQueue(getActivity()).add(jsonRequest);

                }
            });

        }


    }

    public void onEmpty() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork == null) {
            Snackbar.make(mRecyclerView, "Please Connect to the Internet", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOnline();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();

        } else {
            Snackbar.make(mRecyclerView, "No matching word found", Snackbar.LENGTH_LONG)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isOnline();
                        }
                    })
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .show();

        }
    }


}
