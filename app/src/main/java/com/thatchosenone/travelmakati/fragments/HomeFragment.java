package com.thatchosenone.travelmakati.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thatchosenone.travelmakati.adapters.CategoriesAdapter;
import com.thatchosenone.travelmakati.adapters.JustAdapter;
import com.thatchosenone.travelmakati.adapters.MostViewAdapter;
import com.thatchosenone.travelmakati.adapters.PopularSpotAdapter;
import com.thatchosenone.travelmakati.adapters.RecommendedAdapter;
import com.thatchosenone.travelmakati.connection.ConnectivityReceiver;
import com.thatchosenone.travelmakati.connection.MyApplication;
import com.thatchosenone.travelmakati.R;
import com.thatchosenone.travelmakati.models.AveRateModel;
import com.thatchosenone.travelmakati.models.CategoriesModel;
import com.thatchosenone.travelmakati.models.LeisureModel;
import com.thatchosenone.travelmakati.models.TotalViewsModel;
import com.thatchosenone.travelmakati.models.UserTotal;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {


    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-5157557253648277/2334066487";
    private static final String TAG = "";

    private UnifiedNativeAd nativeAd;

    //Categories vars
    RecyclerView rvCategories, rvRecommend, rvMost, rvRecently, rvPopularSpot, rvJust;

    //arraylist
    List<LeisureModel> whatsnewList, popularLists, mostList, recommendList, justList;
    List<TotalViewsModel> totalList;
    List<AveRateModel> aveRateList;
    List<CategoriesModel> categList;
    //adapter
    PopularSpotAdapter popularAdapter;
    MostViewAdapter mostAdapater;
    CategoriesAdapter categoriesAdapter;
    RecommendedAdapter recommendedAdapter;
    JustAdapter justAdapter;

    private static final String COMMON_TAG = "HomeFragment";
    private View home;

    //firebase
        FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference totalRef, leisureRef, totalViewsRef, aveRateRef, userTotal, categRef;
    FirebaseUser firebaseUser;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        home = inflater.inflate(R.layout.fragment_home, container, false);

        rvCategories = (RecyclerView) home.findViewById(R.id.fh_rv_categories);
        rvRecommend = (RecyclerView) home.findViewById(R.id.fh_rv_recommend);
        rvMost = (RecyclerView) home.findViewById(R.id.fh_rv_views);
        rvPopularSpot = (RecyclerView) home.findViewById(R.id.fh_rv_popular);
        rvJust = (RecyclerView) home.findViewById(R.id.fh_rv_just);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        leisureRef = firebaseDatabase.getReference("leisures");
        totalViewsRef = firebaseDatabase.getReference("total");
        aveRateRef = firebaseDatabase.getReference("leisureAveRates");
        userTotal = firebaseDatabase.getReference("userTotal");
        categRef = firebaseDatabase.getReference("categoryName");


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        
        refreshAd();

//        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//
//            }
//        });

//        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.native_advanced_ad_unit_id));
//        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
//            @Override
//            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                TemplateView templateView = home.findViewById(R.id.my_template);
//                templateView.setNativeAd(unifiedNativeAd);
//            }
//        });
//
//        AdLoader adLoader = builder.build();
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adLoader.loadAd(adRequest);

        //design horizontal layout
        LinearLayoutManager categManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        categManager.setStackFromEnd(true);
        categManager.setReverseLayout(true);
        rvCategories.setLayoutManager(categManager);
        rvCategories.setItemAnimator(new DefaultItemAnimator());
        //set layout to recycler view
        rvCategories.setLayoutManager(categManager);
        //init arraylist
        categList = new ArrayList<>();
        loadCateg();


        //Recommended Users List
        LinearLayoutManager recommendManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        recommendManager.setStackFromEnd(true);
        recommendManager.setReverseLayout(true);
        //set layout to recycler view
        rvRecommend.setLayoutManager(recommendManager);
        //init arraylist
        recommendList = new ArrayList<>();
        loadRecommended();

        //Popular Spot List
        LinearLayoutManager popularManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        popularManager.setStackFromEnd(true);
        popularManager.setReverseLayout(true);
        //set layout to recycler view
        rvPopularSpot.setLayoutManager(popularManager);
        //init arraylist
        popularLists = new ArrayList<>();
        loadPopular();

        //Most Viewed List
        LinearLayoutManager mostViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        mostViewManager.setStackFromEnd(true);
        mostViewManager.setReverseLayout(true);
        //set layout to recycler view
        rvMost.setLayoutManager(mostViewManager);
        //init arraylist
        mostList = new ArrayList<>();
        loadMostView();

        //Most Viewed List
        LinearLayoutManager justManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        //show newest post first, for this load from last
        justManager.setStackFromEnd(true);
        justManager.setReverseLayout(true);
        //set layout to recycler view
        rvJust.setLayoutManager(justManager);
        //init arraylist
        justList = new ArrayList<>();
        loadJust();

        aveRateList = new ArrayList<>();
        totalList = new ArrayList<>();
        checkConnection();
        return home;
    }


    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.

    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     */
    private void refreshAd() {


        AdLoader.Builder builder = new AdLoader.Builder(getContext(), ADMOB_AD_UNIT_ID);

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            // OnUnifiedNativeAdLoadedListener implementation.
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                FrameLayout frameLayout = home.findViewById(R.id.fl_adplaceholder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                        .inflate(R.layout.ad_unified, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });







        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {


            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());


    }

    private void loadCateg() {
        //get all data from this ref

//         double startIndex = (int)(Math.random() * 12);
//        String my = String.valueOf(startIndex);

        categRef.orderByChild("publish").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    CategoriesModel categoriesModel = ds.getValue(CategoriesModel.class);

                    if(categoriesModel!=null){
                        categList.add(categoriesModel);
                    }

                    //adapter
                    categoriesAdapter = new CategoriesAdapter(getContext(), categList);
                    categoriesAdapter.notifyDataSetChanged();
                    //set adapter to recyclerview
                    rvCategories.setAdapter(categoriesAdapter);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadJust() {
        /*1. if userViewRef is not success throw an error
         * 2. else query to get the highest total views based in your activities
         * 3. once you get the highest value
         * 4. iterate the data of total snapshot
         * 5. then get the value of UserTotal Model
         * 6. after that, query the leisureRef data where price == favs.getprice
         * 7. if the condition not equal then the justList is clear
         * 8. else iterate the data of leisure snapshot
         * 10. and the justList is added to the views*/
        userTotal.child(firebaseUser.getUid()).orderByChild("totalViews").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //totalList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    UserTotal favs = ds.getValue(UserTotal.class);

                    Log.d("TAG",  "amamma" + favs.getLeisureID());

                    leisureRef.orderByChild("price").equalTo(favs.getPrice()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            justList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                LeisureModel leisureModel = ds.getValue(LeisureModel.class);
                                justList.add(leisureModel);
                                //adapter
                                justAdapter = new JustAdapter(getContext(), justList);
                                justAdapter.notifyDataSetChanged();
                                //set adapter to recyclerview
                                rvJust.setAdapter(justAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //in case of error
                            Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecommended() {
        /*1. if totalViewRef is not success throw an error
        * 2. else query to get the highest total views in total snapshot
        * 3. once you get the highest value
        * 4. iterate the data of total snapshot
        * 5. then get the value of TotalViews Model
        * 6. after that, query the leisureRef data where price == favs.getprice
        * 7. if the condition not equal then the recommendList is clear
        * 8. else iterate the data of leisure snapshot
        * 9. then execution time of each data
        * 10. and the recommendList is added to the views*/
        totalViewsRef.orderByChild("totalViews").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //totalList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TotalViewsModel favs = ds.getValue(TotalViewsModel.class);

                    leisureRef.orderByChild("price").equalTo(favs.getPrice()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            recommendList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                
                                LeisureModel leisureModel = ds.getValue(LeisureModel.class);
                                long startTime = System.nanoTime();
                                // TODO - Call Function Here
                                long endTime = System.nanoTime() - startTime;
                                Log.d("TAG",  "runtime: " + leisureModel.getName() + " - " + endTime);
                                recommendList.add(leisureModel);

                                //adapter
                                recommendedAdapter = new RecommendedAdapter(getContext(), recommendList);
                                recommendedAdapter.notifyDataSetChanged();
                                //set adapter to recyclerview
                                rvRecommend.setAdapter(recommendedAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //in case of error
                            Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    //xtotalList.add(favs);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMostView() {
        totalViewsRef.orderByChild("totalViews").limitToLast(15).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TotalViewsModel favs = ds.getValue(TotalViewsModel.class);
                    totalList.add(favs);
                }
                loadAllMost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllMost() {
        leisureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mostList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);

                    for (TotalViewsModel fav : totalList) {
                        if (leisureModel.getLeisureID() != null && leisureModel.getLeisureID().equals(fav.getLeisureID())) {
                            mostList.add(leisureModel);
                            break;
                        }
                    }
                    //adapter
                    mostAdapater = new MostViewAdapter(getContext(), mostList);
                    mostAdapater.notifyDataSetChanged();
                    //set adapter to recyclerview
                    rvMost.setAdapter(mostAdapater);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPopular() {
        aveRateRef.orderByChild("totalRate").limitToLast(15).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aveRateList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AveRateModel ave = ds.getValue(AveRateModel.class);
                    aveRateList.add(ave);
                }
                loadAllPopular();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllPopular() {

        leisureRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popularLists.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    LeisureModel leisureModel = ds.getValue(LeisureModel.class);

                    for (AveRateModel ave : aveRateList) {
                        if (leisureModel.getLeisureID() != null && leisureModel.getLeisureID().equals(ave.getLeisureID())) {
                            popularLists.add(leisureModel);
                            break;
                        }
                    }
                    //adapter
                    popularAdapter = new PopularSpotAdapter(getContext(), popularLists);
                    popularAdapter.notifyDataSetChanged();
                    //set adapter to recyclerview
                    rvPopularSpot.setAdapter(popularAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //in case of error
                Toast.makeText(getContext(), "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (isConnected) {

        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_frame, new InternetConnection());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    /*Inflate option menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflating menu
        inflater.inflate(R.menu.main_menu, menu);
//        menu.findItem(R.id.add_post_mm).setVisible(false);
//        menu.findItem(R.id.search_all_mm).setVisible(false);
//
//        MenuItem searchAll = menu.findItem(R.id.add_search_mm);
//        Intent intent = new Intent(getContext(), SearchAllLeisure.class);
//        searchAll.setIntent(intent);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDestroy() {
        if (nativeAd != null) {
            nativeAd.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkChange(boolean inConnected) {
        showSnack(inConnected);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(COMMON_TAG, "HomeFragment onSaveInstanceState");
    }
}
