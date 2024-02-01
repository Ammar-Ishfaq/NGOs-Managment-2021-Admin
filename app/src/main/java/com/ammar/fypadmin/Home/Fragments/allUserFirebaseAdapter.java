package com.ammar.fypadmin.Home.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammar.fypadmin.FirebaseCrud;
import com.ammar.fypadmin.Interface.response;
import com.ammar.fypadmin.Models.ModelUser;
import com.ammar.fypadmin.ModuleDirection.FollowRoute;
import com.ammar.fypadmin.R;
import com.ammar.fypadmin.tools.Alert;
import com.ammar.fypadmin.tools.ImageUtils;
import com.ammar.fypadmin.tools.MyMapUtils;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class allUserFirebaseAdapter extends FirebaseRecyclerAdapter<ModelUser, allUserFirebaseAdapter.ViewHolder> {
    public OnItemClickListener mListener;

    private LayoutInflater mInflater;
    private String TAG = "DchatRecycularAdapter";
    //custom data
    private ImageView imgview;
    private TextView email, colorVerifier, firstAndLastName, NumberPersonal;
    private CardView recyclerViewCard;
    private Context context;
    private View mview;
    private final int GPS_requestCode = 111;

    public void blockUser(int adapterPosition) {
        String mail = getSnapshots().get(adapterPosition).getEmail();
        FirebaseCrud.getInstance().blockUser(mail, new response() {
            @Override
            public void onSuccess(String msg) {
                Alert.getInstance().showSnackbar(((Activity) context).findViewById(android.R.id.content), msg, false);
            }

            @Override
            public void onFail(String msg) {
                Alert.getInstance().showSnackbar(((Activity) context).findViewById(android.R.id.content), msg, false);

            }
        });
    }

    public void unBlockUser(int adapterPosition) {
        String mail = getSnapshots().get(adapterPosition).getEmail();
        FirebaseCrud.getInstance().unBlockUser(mail, new response() {
            @Override
            public void onSuccess(String msg) {
                Alert.getInstance().showSnackbar(((Activity) context).findViewById(android.R.id.content), msg, false);
            }

            @Override
            public void onFail(String msg) {
                Alert.getInstance().showSnackbar(((Activity) context).findViewById(android.R.id.content), msg, false);

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        private CardView recyclerViewCard;
//        private ImageView img;
//        private TextView isDonor;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
//            recyclerViewCard = itemView.findViewById(R.id.cardviewMain);
//            img = itemView.findViewById(R.id.chatImg);
//            isDonor = itemView.findViewById(R.id.isDonor);
//            recyclerViewCard.setOnClickListener(view -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//
//                        listener.onImageClick(itemView, position, (isDonor.getText().toString()));
//                    }
//                }
//
//            });
        }

    }

    public allUserFirebaseAdapter(Context context, @NonNull FirebaseRecyclerOptions<ModelUser> options) {
        super(options);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);


    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ModelUser model) {
//  Will Set the image view will starting

        imgview = holder.itemView.findViewById(R.id.chatImg);
        email = holder.itemView.findViewById(R.id.email);
        colorVerifier = holder.itemView.findViewById(R.id.colorVerifier);
        recyclerViewCard = holder.itemView.findViewById(R.id.cardviewMain);
        firstAndLastName = holder.itemView.findViewById(R.id.firstAndLastName);
        NumberPersonal = holder.itemView.findViewById(R.id.NumberPersonal);


        Glide.with(holder.itemView)
                .load(model.getImage())
                .into(imgview);
        email.setText(model.getEmail());
        NumberPersonal.setText(model.getNumberPersonal());
        firstAndLastName.setText(model.getFirstName() + " " + model.getLastName());
//setting the color view references
        colorVerifier.setBackgroundColor(model.getAccountType().equals("NGO") ?
                context.getResources().getColor(R.color.NGO) : context.getResources().getColor(R.color.DONOR)
        );
        if (model.getIsAllow().equals("false"))
            colorVerifier.setBackgroundColor(context.getResources().getColor(R.color.BLOCK));
//setting the color view references finished

        recyclerViewCard.setOnClickListener(view -> {
            if (mListener != null) {
                mListener.onImageClick(holder.itemView, position);
                showDialog(model);
            }
        });


    }

    public void showDialog(ModelUser model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_detail_dialog_box);
        CircularImageView profile = dialog.findViewById(R.id.dpersonImage);
        Glide.with(dialog.getContext()).load(model.getImage()).into(profile);
        setText(dialog.findViewById(R.id.demail), model.getEmail());
        setText(dialog.findViewById(R.id.dfirstname), model.getFirstName());
        setText(dialog.findViewById(R.id.dlastname), model.getLastName());
        setText(dialog.findViewById(R.id.dNumberPersonal), model.getNumberPersonal());
        setText(dialog.findViewById(R.id.dnumberEaisiPaisa), model.getNumberEasyPaissa());
        setText(dialog.findViewById(R.id.dnumberJazzcash), model.getNumberJazzCash());
        setText(dialog.findViewById(R.id.daccountType), model.getAccountType());
        setText(dialog.findViewById(R.id.ddateOfBirth), model.getDateOfBirth());
        setText(dialog.findViewById(R.id.dgender), model.getGender());

        dialog.findViewById(R.id.dclose).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.dok).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.findViewById(R.id.dlocation).setOnClickListener(v -> {
            Intent intent
                    = new Intent(v.getContext(), FollowRoute.class);
            intent.putExtra("longlat", model.getLonglat());
            v.getContext().startActivity(intent);
        });

        new ImageUtils().enablePopUpZoom(dialog.getContext(), profile, model.getImage());


        ImageView map = dialog.findViewById(R.id.dlocation);

        Glide.with(dialog.getContext()).load(MyMapUtils.getInstance().getImgfromLongLat(model.getLonglat())).into(map);
        dialog.show();

    }

    private void
    setText(TextView textView, String text) {
        textView.setText(text);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //when data is fetched and
        mview = mInflater.inflate(R.layout.recycularview_users, parent, false);
        mListener.showingEnable();
        return new ViewHolder(mview, (OnItemClickListener) mListener);
    }

    public interface OnItemClickListener {
        public void onImageClick(View v, int position);

        /**
         * Hide the loader as the data is fetching succesfully from the firebase
         *
         * @param
         */
        public void showingEnable();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}


