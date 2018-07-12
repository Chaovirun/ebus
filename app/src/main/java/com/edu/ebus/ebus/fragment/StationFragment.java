package com.edu.ebus.ebus.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.edu.ebus.ebus.BusTicketAdapter;
import com.edu.ebus.ebus.R;
import com.edu.ebus.ebus.RecentBookingAdapter;
import com.edu.ebus.ebus.StationFragmentAdapter;
import com.edu.ebus.ebus.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by USER on 7/11/2018.
 */

public class StationFragment extends Fragment {
    RecyclerView recyclerView;
    private StationFragmentAdapter adapter = new StationFragmentAdapter ();
    //private BusTicketAdapter adapters = new BusTicketAdapter ();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.fragment_stations, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        recyclerView = view.findViewById (R.id.rec_station);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager (getActivity ());
        recyclerView.setLayoutManager (layoutManager);
        adapter = new StationFragmentAdapter ();
        recyclerView.setAdapter (adapter);

        loadstationfromfirebase();
    }

    private void loadstationfromfirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance ();
        db.collection ("tickets").get ().addOnCompleteListener (new OnCompleteListener<QuerySnapshot> () {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful ()) {
                    processEventsResult (task);
                } else {
                    Toast.makeText (getActivity (), "Load events error.", Toast.LENGTH_LONG).show ();
                    Log.d ("ckcc", "Load events error: " + task.getException ());
                }

            }

            private void processEventsResult(@NonNull Task<QuerySnapshot> task) {
                Ticket[] events = new Ticket[task.getResult ().size ()];
                int index = 0;
                for (QueryDocumentSnapshot document : task.getResult ()) {
                    // Convert Firestore document to Event object
                    Ticket event = document.toObject (Ticket.class);
                    events[index] = event;
                    index++;
                }
                adapter.setTicketall (events);
            }
        });
    }
}