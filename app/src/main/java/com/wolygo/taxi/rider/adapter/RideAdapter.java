package com.wolygo.taxi.rider.adapter;

/**
 * Created by Woumtana on 01/12/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.textfield.TextInputLayout;
import com.wolygo.taxi.rider.R;
import com.wolygo.taxi.rider.activity.MainActivity;
import com.wolygo.taxi.rider.circleimage.CircleImageView;
import com.wolygo.taxi.rider.controller.AppController;
import com.wolygo.taxi.rider.model.M;
import com.wolygo.taxi.rider.model.RidePojo;
import com.wolygo.taxi.rider.settings.AppConst;
import com.wolygo.taxi.rider.settings.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.MyViewHolder> {

    private Context context;
    private List<RidePojo> albumList;
    Activity activity;
    private String currentActivity;
    private String distance = "";
    final private String[][] tab = {{}};
    final private String[][] tab1 = { {} };
    private static final int REQUEST_PHONE_CALL = 1;
    public static AlertDialog alertDialog;
    private TextView cancel_note,save_note;
    private RatingBar rate_conducteur,rate_conducteur_princ;
    ConnectionDetector connectionDetector;
    private CircleImageView user_photo;
    private TextInputLayout intput_layout_comment;
    private EditText input_edit_comment;
    private int expandedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView depart,destination,cost_ride,distance_ride,duration_ride,name_driver,
                statut_ride,date_ride,driver_rating,pay,cost_ride_2;
//        private LinearLayout layout_distance_requete;
        private RelativeLayout relative_layout;
        private ImageView call_driver,call_driver_2,date_book,payment_method,round_trip,view_map,cancel,start_ride;
        private LinearLayout layout_rate;
        private TextView place,number_people,heure_retour,date_retour,at,desc_ride;
        private LinearLayout llExpandArea,date_book_layout;
//        private boolean collapsed = false;

        public MyViewHolder(View view) {
            super(view);
            if(currentActivity.equals("RideNewDriver")){
//                user_name_requete = (TextView) view.findViewById(R.id.user_name_requete);
//                distance_client_requete = (TextView) view.findViewById(R.id.distance_client_requete);
            }
            depart = (TextView) view.findViewById(R.id.depart);
            destination = (TextView) view.findViewById(R.id.destination);
            cost_ride = (TextView) view.findViewById(R.id.cost_ride);
            cost_ride_2 = (TextView) view.findViewById(R.id.cost_ride_2);
            distance_ride = (TextView) view.findViewById(R.id.distance_ride);
            duration_ride = (TextView) view.findViewById(R.id.duration_ride);
            name_driver = (TextView) view.findViewById(R.id.name_driver);
            statut_ride = (TextView) view.findViewById(R.id.statut_ride);
            date_ride = (TextView) view.findViewById(R.id.date_ride);
            cancel = (ImageView) view.findViewById(R.id.cancel);
            view_map = (ImageView) view.findViewById(R.id.view_map);
            driver_rating = (TextView) view.findViewById(R.id.driver_rating);
            start_ride = (ImageView) view.findViewById(R.id.start_ride);
            pay = (TextView) view.findViewById(R.id.pay);
//            layout_distance_requete = (LinearLayout) view.findViewById(R.id.layout_distance_requete);
            relative_layout = (RelativeLayout) view.findViewById(R.id.relative_layout);
            call_driver = (ImageView) view.findViewById(R.id.call_driver);
            call_driver_2 = (ImageView) view.findViewById(R.id.call_driver_2);
            date_book = (ImageView) view.findViewById(R.id.date_book);
            payment_method = (ImageView) view.findViewById(R.id.payment_method);
            layout_rate = (LinearLayout) view.findViewById(R.id.layou_rate);
            place = (TextView) view.findViewById(R.id.place);
            number_people = (TextView) view.findViewById(R.id.number_people);
            round_trip = (ImageView) view.findViewById(R.id.round_trip);
            heure_retour = (TextView) view.findViewById(R.id.heure_retour);
            date_retour = (TextView) view.findViewById(R.id.date_retour);
            at = (TextView) view.findViewById(R.id.at);
            desc_ride = (TextView) view.findViewById(R.id.desc_ride);
            connectionDetector=new ConnectionDetector(context);
            llExpandArea = (LinearLayout) itemView.findViewById(R.id.llExpandArea);
            date_book_layout = (LinearLayout) itemView.findViewById(R.id.date_book_layout);

            depart.setTypeface(AppConst.font_quicksand_medium(context));
            destination.setTypeface(AppConst.font_quicksand_medium(context));
            cost_ride.setTypeface(AppConst.font_quicksand_medium(context));
            cost_ride_2.setTypeface(AppConst.font_quicksand_medium(context));
            distance_ride.setTypeface(AppConst.font_quicksand_medium(context));
            duration_ride.setTypeface(AppConst.font_quicksand_medium(context));
            name_driver.setTypeface(AppConst.font_quicksand_medium(context));
            statut_ride.setTypeface(AppConst.font_quicksand_medium(context));
            date_ride.setTypeface(AppConst.font_quicksand_medium(context));
            driver_rating.setTypeface(AppConst.font_quicksand_medium(context));
            pay.setTypeface(AppConst.font_quicksand_medium(context));
            place.setTypeface(AppConst.font_quicksand_medium(context));
            number_people.setTypeface(AppConst.font_quicksand_medium(context));
            heure_retour.setTypeface(AppConst.font_quicksand_medium(context));
            date_retour.setTypeface(AppConst.font_quicksand_medium(context));
            at.setTypeface(AppConst.font_quicksand_medium(context));
            desc_ride.setTypeface(AppConst.font_quicksand_medium(context));

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            RidePojo ridePojo = albumList.get(getAdapterPosition());

            MyViewHolder holder = (MyViewHolder) v.getTag();
//            String theString = albumList.get(holder.getPosition());

            // Check for an expanded view, collapse if you find one
            if (expandedPosition >= 0) {
                int prev = expandedPosition;
                notifyItemChanged(prev);
            }
            // Set the current position to "expanded"
            expandedPosition = getAdapterPosition();
            notifyItemChanged(expandedPosition);

//            Toast.makeText(context, "Clicked: "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public RideAdapter(Context context, List<RidePojo> albumList, Activity activity, String currentActivity) {
        this.context = context;
        this.albumList = albumList;
        this.activity = activity;
        this.currentActivity = currentActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
//        if(currentActivity.equals("RideNewDriver"))
//            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_ride_new_rider, parent, false);
//        else
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card_ride_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RidePojo ridePojo = albumList.get(position);

        if (position == expandedPosition) {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        } else {
            holder.llExpandArea.setVisibility(View.GONE);
        }
//        if (!ridePojo.isCollapse()) {
//            holder.llExpandArea.setVisibility(View.VISIBLE);
//            ridePojo.setCollapse(true);
//        } else {
//            holder.llExpandArea.setVisibility(View.GONE);
//            ridePojo.setCollapse(false);
//        }

        if(ridePojo.getStatut_round().equals("yes")){
            holder.round_trip.setVisibility(View.VISIBLE);
            holder.heure_retour.setVisibility(View.VISIBLE);
            holder.heure_retour.setText(ridePojo.getHeure_retour());
            holder.date_retour.setText(ridePojo.getDate_retour());
            holder.date_retour.setVisibility(View.VISIBLE);
            holder.at.setVisibility(View.VISIBLE);
        }else {
            holder.round_trip.setVisibility(View.INVISIBLE);
            holder.heure_retour.setVisibility(View.INVISIBLE);
            holder.heure_retour.setText("");
            holder.date_retour.setText("");
            holder.date_retour.setVisibility(View.INVISIBLE);
            holder.at.setVisibility(View.INVISIBLE);
        }
        holder.date_book.setVisibility(View.GONE);
        holder.date_book_layout.setVisibility(View.GONE);
        holder.number_people.setText(ridePojo.getNumber_poeple());
        if(currentActivity.equals("RideNew")){
            holder.cancel.setVisibility(View.VISIBLE);
            holder.call_driver.setVisibility(View.VISIBLE);
            holder.call_driver_2.setVisibility(View.GONE);
        }else{
            holder.cancel.setVisibility(View.GONE);
            holder.call_driver.setVisibility(View.GONE);
            holder.call_driver_2.setVisibility(View.VISIBLE);
        }
        if(ridePojo.getStatut().equals("completed")){
            holder.pay.setVisibility(View.VISIBLE);
            if(ridePojo.getStatut_paiement().equals("yes")){
                holder.pay.setText(context.getResources().getString(R.string.paid));
                holder.pay.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_valide));
                holder.pay.setEnabled(false);
                holder.pay.setTextColor(Color.WHITE);
            }else{
                holder.pay.setText(context.getResources().getString(R.string.pay));
                holder.pay.setBackground(context.getResources().getDrawable(R.drawable.custom_bg_statut_en_cours));
                holder.pay.setEnabled(true);
                holder.pay.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
            }
        }else{
            holder.pay.setVisibility(View.GONE);
        }
        if(currentActivity.equals("RideOnRide")){
            holder.start_ride.setVisibility(View.VISIBLE);
        }else{
            holder.start_ride.setVisibility(View.GONE);
        }
        holder.driver_rating.setText(ridePojo.getMoyenne()+"/5");
        distance = ridePojo.getDistance();
        if(distance.length() > 3) {
            String virgule = distance.substring(distance.length() - 3,distance.length() - 2);
            distance = distance.substring(0, distance.length() - 3);
            distance = distance+"."+virgule+" km";
        }else
            distance = distance+" m";
//        holder.status_ride.setTextColor(context.getResources().getColor(R.color.colorLogoBlack));
        holder.desc_ride.setText(ridePojo.getDepart_name()+" => "+ridePojo.getDestination_name());
        holder.depart.setText(ridePojo.getDepart_name());
        holder.destination.setText(ridePojo.getDestination_name());
        holder.date_ride.setText(ridePojo.getDate());
        holder.cost_ride.setText(ridePojo.getCout()+" "+ M.getCurrency(context));
        holder.cost_ride_2.setText(ridePojo.getCout()+" "+ M.getCurrency(context));
        holder.distance_ride.setText(distance);
        holder.duration_ride.setText(ridePojo.getDuree());
        holder.statut_ride.setText(ridePojo.getStatut());
        holder.name_driver.setText(ridePojo.getConducteur_name());
        holder.view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogViewMap(ridePojo.getTrajet());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessageCancel(ridePojo.getId(),position,ridePojo.getConducteur_id(),ridePojo.getUser_name());
            }
        });
        holder.call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(ridePojo.getDriver_phone());
            }
        });
        holder.call_driver_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber(ridePojo.getDriver_phone());
            }
        });
        holder.layout_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogRate(ridePojo.getNote(), String.valueOf(ridePojo.getConducteur_id()), position, ridePojo.getDriver_photo(), ridePojo.getComment());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        holder.start_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGoogleMapsInstalled())
                    loadNavigationView(ridePojo.getLatitude_destination(),ridePojo.getLongitude_destination());
                else
                    Toast.makeText(context, context.getResources().getString(R.string.you_need_to_install_google_map_app), Toast.LENGTH_SHORT).show();
            }
        });
        holder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
//                    Toast.makeText(context, ""+ridePojo.getPayment_method(), Toast.LENGTH_SHORT).show();
                    MainActivity.dialogPaymentAmount(ridePojo.getCout(),String.valueOf(ridePojo.getId()),String.valueOf(position),String.valueOf(ridePojo.getConducteur_id()),ridePojo.getPayment_method(),
                            ridePojo.getNote(), ridePojo.getDriver_photo(), ridePojo.getComment());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // loading model cover using Glide library
//        Glide.with(context).load(AppConst.Server_urlMain+"assets/images/payment_method/"+ridePojo.getImg_payment_method())
        Glide.with(context).load(AppConst.Server_urlMain+ridePojo.getImg_payment_method())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
//                    .centerCrop()
//                .asBitmap()
//                .error(R.drawable.ic_thumb_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.payment_method);
        holder.place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogPlace(ridePojo.getPlace());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //This method would confirm the otp
    private void dialogSucess(String message) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_subscribe_success, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView close = (TextView) confirmDialog.findViewById(R.id.close);
        TextView msg = (TextView) confirmDialog.findViewById(R.id.msg);

        msg.setText(message);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    //This method would confirm the otp
    private void dialogPlace(String place) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_exactly_place, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        TextView input_place = (TextView) confirmDialog.findViewById(R.id.input_place);
        TextView cancel = (TextView) confirmDialog.findViewById(R.id.cancel);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        input_place.setText(place);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }

    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(navigationIntent);
    }

    public boolean isGoogleMapsInstalled() {
        try{
            ApplicationInfo info = context.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //This method would confirm the otp
    private void dialogRate(final String note_, final String id_driver, final int position, final String img, final String comment) throws JSONException {
        final String[] note = {note_};
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_noter, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        save_note = (TextView) confirmDialog.findViewById(R.id.save_note);
        cancel_note = (TextView) confirmDialog.findViewById(R.id.cancel_note);
        rate_conducteur = (RatingBar) confirmDialog.findViewById(R.id.rate_conducteur);
        user_photo = (CircleImageView) confirmDialog.findViewById(R.id.user_photo);
        intput_layout_comment = (TextInputLayout) confirmDialog.findViewById(R.id.intput_layout_comment);
        input_edit_comment = (EditText) confirmDialog.findViewById(R.id.input_edit_comment);

        input_edit_comment.setText(comment);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

//        Toast.makeText(context, ""+note, Toast.LENGTH_SHORT).show();
        if(note[0].trim().length() > 0)
            rate_conducteur.setRating(Float.parseFloat(note[0]));
        else
            rate_conducteur.setRating(0f);
        //Creating an alert dialog
        alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
        save_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
//                    M.showLoadingDialog(context);
//                    new setNote().execute(note[0],id_driver, String.valueOf(position));
                    submitNote(note[0],id_driver, String.valueOf(position));
                }else{
                    Toast.makeText(context, context.getResources().getString(R.string.pas_de_connexion_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        rate_conducteur.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                note[0] = String.valueOf(ratingBar.getRating());
            }
        });

        if(!img.equals("")) {
            // loading model cover using Glide library
            Glide.with(context).load(AppConst.Server_urlMain + "images/app_user/" + img)
                    .skipMemoryCache(true)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(user_photo);
        }else{
            user_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile));
        }
//        new getPhoto().execute(id_driver);
    }

    private void submitNote(String note, String id_driver, String position) {
        if (!validateNote()) {
            return;
        }
        alertDialog.hide();
        M.showLoadingDialog(context);
        new setNote().execute(note,id_driver,position,input_edit_comment.getText().toString());
    }

    private boolean validateNote() {
        if (input_edit_comment.getText().toString().trim().isEmpty()) {
            intput_layout_comment.setError(context.getResources().getString(R.string.enter_your_comment));
            requestFocus(input_edit_comment);
            return false;
        } else {
            intput_layout_comment.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /** Enregistrer la note d'un conducteur **/
    private class setNote extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"set_note.php";
            final String note_value = params[0];
            final String id_conducteur = params[1];
            final String position = params[2];
            final String comment = params[3];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                M.hideLoadingDialog();
                                if(etat.equals("1")){
                                    alertDialog.cancel();
                                    JSONObject note = msg.getJSONObject("note");
                                    String nb_avis = note.getString("nb_avis");
                                    String niveau = note.getString("niveau");
                                    String moyenne = note.getString("moyenne");
                                    String comment = note.getString("comment");

                                    albumList.get(Integer.parseInt(position)).setNb_avis(nb_avis);
                                    albumList.get(Integer.parseInt(position)).setNote(niveau);
                                    albumList.get(Integer.parseInt(position)).setMoyenne(moyenne);
                                    albumList.get(Integer.parseInt(position)).setComment(comment);

                                    notifyItemChanged(Integer.parseInt(position));

                                    Toast.makeText(context, context.getResources().getString(R.string.rating_awarded_successfully), Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, context.getResources().getString(R.string.failed_to_assign), Toast.LENGTH_SHORT).show();
                                    alertDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, context.getResources().getString(R.string.failed_to_edit), Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_user_app",M.getID(context));
                    params.put("id_conducteur",id_conducteur);
                    params.put("note_value",note_value);
                    params.put("comment",comment);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    /** Récuperer la photo d'un conducteur **/
    /*private class getPhoto extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String url = AppConst.Server_url+"get_conducteur_photo.php";
            final String id_conducteur = params[0];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    String photo_path = msg.getString("photo_path");

                                    if(!photo_path.equals("")) {
                                        // loading model cover using Glide library
                                        Glide.with(context).load(AppConst.Server_urlMain + "images/app_user/" + photo_path)
                                                .skipMemoryCache(true)
                                                .listener(new RequestListener<Drawable>() {
                                                    @Override
                                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                        return false;
                                                    }
                                                })
                                                .into(user_photo);
                                    }else{
                                        user_photo.setImageDrawable(context.getResources().getDrawable(R.drawable.user_profile));
                                    }
                                }else{

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_conducteur",id_conducteur);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }*/

    public void showMessageCancel(final int idRide, final int position, final int id_driver, final String user_name){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.do_you_want_to_cancel_your_request))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        M.showLoadingDialog(context);
                        new cancelRide().execute(String.valueOf(idRide),String.valueOf(position),String.valueOf(id_driver),user_name);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /** Appeler numéro de téléphone **/
    public void callNumber(String numero){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+numero.trim()));
            if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            context.startActivity(callIntent);
        }
    }

    //This method would confirm the otp
    private void dialogViewMap(String img) throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(context);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_layout_view_map, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        ImageView image = (ImageView) confirmDialog.findViewById(R.id.image);
        final ProgressBar progressBar = (ProgressBar) confirmDialog.findViewById(R.id.progressBar);

        // loading model cover using Glide library
        Glide.with(context).load(AppConst.Server_urlMain+"images/recu_trajet_course/"+ img)
                .skipMemoryCache(false)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(image);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();
    }

    /** Annuler un requête **/
    public class cancelRide extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
//            String url = AppConst.Server_url+"canceled_requete.php";
            String url = AppConst.Server_url+"requestorders/cancel";
            final String id_ride = params[0];
            final String position = params[1];
            final String id_driver = params[2];
            final String user_name = params[3];
            StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                M.hideLoadingDialog();
                                JSONObject json = new JSONObject(response);
                                JSONObject msg = json.getJSONObject("msg");
                                String etat = msg.getString("etat");
                                if(etat.equals("1")){
                                    delete(Integer.parseInt(position));
                                    notifyDataSetChanged();
                                    dialogSucess(context.getResources().getString(R.string.canceled_successfull));
                                }else{
                                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    M.hideLoadingDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id_ride", id_ride);
                    params.put("id_driver", id_driver);
                    params.put("user_name", user_name);
                    return params;
                }

            };
            AppController.getInstance().addToRequestQueue(jsonObjReq);
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            //to add spacing between cards
            if (this != null) {

            }

        }

        @Override
        protected void onPreExecute() {

        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void delete(int position){
        albumList.remove(position);
        notifyItemRemoved(position);
        return;
    }

    public RidePojo getRequete(int id){
        RidePojo ridePojo = null;
        for (int i=0; i< albumList.size(); i++){
            if(albumList.get(i).getId() == id){
                ridePojo = albumList.get(i);
                break;
            }
        }
        return ridePojo;
    }

    public void restoreItem(RidePojo ridePojo, int position) {
        albumList.add(position, ridePojo);
        notifyItemInserted(position);
    }

    public List<RidePojo> getData() {
        return albumList;
    }
}
