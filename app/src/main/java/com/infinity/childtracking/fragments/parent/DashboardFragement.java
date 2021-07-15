package com.infinity.childtracking.fragments.parent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.infinity.childtracking.R;

import java.util.ArrayList;

public class  DashboardFragement extends Fragment implements View.OnClickListener {

    ArrayList<CardView> cardView;
    CardView card;
    TextView tvToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parent_dashboard,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        try {
            tvToolbar = getActivity().findViewById(R.id.tvToolbar);
            tvToolbar.setText("Child Tracking");
        }catch (Exception e){
            e.printStackTrace();
        }

        cardView = new ArrayList<>();
        card = view.findViewById(R.id.card_alllog);
        cardView.add(card);
        card = view.findViewById(R.id.card_calllog);
        cardView.add(card);
        card = view.findViewById(R.id.card_smslog);
        cardView.add(card);
        card = view.findViewById(R.id.card_notifylog);
        cardView.add(card);
        card = view.findViewById(R.id.card_loclog);
        cardView.add(card);
        card = view.findViewById(R.id.card_curlog);
        cardView.add(card);
        card = view.findViewById(R.id.card_children);
        cardView.add(card);
        card = view.findViewById(R.id.card_prevent);
        cardView.add(card);

        for(int i=0;i<cardView.size();i++){
            cardView.get(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.card_alllog:
                intent = new Intent(getContext(),LogsFragement.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.card_calllog:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CalllogFragement(R.layout.call_log,getString(R.string.call_log),0)).commit();
                break;
            case R.id.card_smslog:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MsglogFragment(R.layout.msg_log,getString(R.string.msg_log),1)).commit();
                break;
            case R.id.card_notifylog:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new NotifylogFragement(R.layout.notify_log,getString(R.string.notify_log),2)).commit();
                break;
            case R.id.card_loclog:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LocationlogFragement(R.layout.location_log,getString(R.string.location_log),3)).commit();
                break;
            case R.id.card_curlog:
                intent = new Intent(getContext(), LocationFragmentActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.card_children:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChildrenFragement()).commit();
                break;
            case R.id.card_prevent:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new UsageFragement()).commit();
                break;
        }
    }
}