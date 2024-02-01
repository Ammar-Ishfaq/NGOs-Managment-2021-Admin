package com.ammar.fypadmin.Home.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ammar.fypadmin.MainActivity;
import com.ammar.fypadmin.Models.ModelUser;
import com.ammar.fypadmin.R;
import com.ammar.fypadmin.tools.SwipeHelper;
import com.ammar.fypadmin.tools.fragmentUtil;
import com.ammar.fypadmin.tools.mProgressDialog;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUsers extends Fragment {
    private static final String TAG = "AllUserFragment";
    private View view;

    @BindView(R.id.all_users_recycularView)
    RecyclerView recyclerView;
    @BindView(R.id.infoText)
    TextView infotv;
    //    @BindView(R.id.showInfo)
//    ImageView ShowInfo;
    @BindView(R.id.infoLayout)
    LinearLayout infoLayoutDfChat;


    private Query query1;
    allUserFirebaseAdapter adapter;
    private mProgressDialog mProgressDialog;
    //for the maiontenacnce of menui item
    private MenuItem mMenuAllUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alluser, container, false);
        ButterKnife.bind(this, view);
        mProgressDialog = new mProgressDialog(view.getContext());
        query1 = FirebaseDatabase.getInstance().getReference().child("User");
        enableSwipeToDeleteAndUndo();
        setTitle("ALL Users");
        return view;
    }

    void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_app_bar, menu);
        setSearchMenuBtn(menu.findItem(R.id.search));
        mMenuAllUser = menu.findItem(R.id.AllUser);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handle the search Button
     *
     * @param searchItem
     */
    private void setSearchMenuBtn(MenuItem searchItem) {
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView != null) {
            //listener for the closed checking of the search Bar
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    query1 = FirebaseDatabase.getInstance().getReference().child("User");
                    onStart();
                    setTitle("ALL Users");
                    mMenuAllUser.setChecked(true);
                    return true; // Return true to collapse action view
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
//                    Log.i(TAG, "onMenuItemActionExpand " + item.getItemId());
                    return true;
                }
            });
            //Listener for the getting text from the Search Bar
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    query1 = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("email").equalTo(s);
                    onStart();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.info:
                fragmentUtil.getInstance(getActivity()).loadFragment(new FInfo());
                return true;//
            case R.id.justBlock:
                item.setChecked(true);
                setTitle("Block Users");
                query1 = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("isAllow").equalTo("false");
                onStart();

                return true;
            case R.id.justNGO:
                setTitle("NGO Users");
                item.setChecked(true);
                query1 = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("accountType").equalTo("NGO");
                onStart();
                return true;
            case R.id.justDonor:
                setTitle("DONOR Users");
                item.setChecked(true);
                query1 = FirebaseDatabase.getInstance().getReference().child("User").orderByChild("accountType").equalTo("DONOR");
                onStart();
                return true;
            case R.id.AllUser:
                setTitle("ALL Users");
                item.setChecked(true);
                query1 = FirebaseDatabase.getInstance().getReference().child("User");
                onStart();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        showNoChatMessage();
        mProgressDialog.show();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hideNoChatMessage();
                    loadRecycularViewData();
                } else {
                    infotv.setText("It Look Like\nThere's No Users Available");
                    mProgressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                infotv.setText("Error: " + error);
                mProgressDialog.dismiss();

            }
        });


    }

    /**
     * Will hide the Layout and make the Chat Visible
     */
    private void hideNoChatMessage() {
        infotv.setVisibility(View.GONE);
        infoLayoutDfChat.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    /**
     * Will hide the Layout and make the Chat Visible
     */
    private void showNoChatMessage() {
        recyclerView.setVisibility(View.GONE);
        infotv.setVisibility(View.VISIBLE);
        infoLayoutDfChat.setVisibility(View.VISIBLE);

    }

    /**
     * <p>Manage the Loading of data to the recycularview so that it continue to the work</p>
     */
    private void loadRecycularViewData() {
        FirebaseRecyclerOptions<ModelUser> options =
                new FirebaseRecyclerOptions.Builder<ModelUser>()
                        .setQuery(query1, ModelUser.class)
                        .build();

        adapter = new allUserFirebaseAdapter(view.getContext(), options);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter.startListening();

        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new allUserFirebaseAdapter.OnItemClickListener() {
            @Override
            public void onImageClick(View v, int position) {

            }

            @Override
            public void showingEnable() {
                mProgressDialog.dismiss();
            }
        });

    }

    /**
     * Functions that helps to maintain the swipe gestures
     */
    private void enableSwipeToDeleteAndUndo() {
        SwipeHelper swipeHelper = new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        " Block ",//⛔
                        0,
                        Color.parseColor("#FF3C30"),
                        pos -> {
                            if (pos > -1)
                                adapter.blockUser(pos);
                        }
                ));

                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        " UnBlock ",
                        0,
                        Color.parseColor("#FF9502"),
                        pos -> {
                            if (pos > -1)
                                adapter.unBlockUser(pos);

                        }
                ));

            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);

    }
}
