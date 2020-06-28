package com.socialcodia.socialui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.socialcodia.socialui.R;
import com.socialcodia.socialui.adapter.AdapterUser;
import com.socialcodia.socialui.api.ApiClient;
import com.socialcodia.socialui.model.ModelUser;
import com.socialcodia.socialui.model.ResponseUser;
import com.socialcodia.socialui.storage.SharedPrefHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UsersFragment extends Fragment {

    private List<ModelUser> modelUserList;
    private RecyclerView userRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users,container,false);

        userRecyclerView = view.findViewById(R.id.userRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        userRecyclerView.setLayoutManager(layoutManager);

        modelUserList = new ArrayList<>();

        getUsers();
        return view;
    }

    private void getUsers()
    {
        String token = SharedPrefHandler.getInstance(getContext()).getUser().getToken();
        Call<ResponseUser> call = ApiClient.getInstance().getApi().users(token);
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                ResponseUser responseUser = response.body();
                if (responseUser !=null)
                {
                    if (responseUser.getError())
                    {
                        Toast.makeText(getContext(), responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        modelUserList = responseUser.getUsers();
                        AdapterUser adapterUser = new AdapterUser(modelUserList,getContext());
                        userRecyclerView.setAdapter(adapterUser);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Response From Server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}