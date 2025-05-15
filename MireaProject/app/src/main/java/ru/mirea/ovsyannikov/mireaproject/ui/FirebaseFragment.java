package ru.mirea.ovsyannikov.mireaproject.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mirea.ovsyannikov.mireaproject.R;
import ru.mirea.ovsyannikov.mireaproject.ui.firebase.Post;
import ru.mirea.ovsyannikov.mireaproject.ui.firebase.RetrofitClient;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirebaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirebaseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirebaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirebaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirebaseFragment newInstance(String param1, String param2) {
        FirebaseFragment fragment = new FirebaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firebase, container, false);
        TextView tvData = view.findViewById(R.id.tvData);

        RetrofitClient.getApiService().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    StringBuilder sb = new StringBuilder();
                    for (Post post : response.body()) {
                        sb.append(post.getTitle()).append("\n\n");
                    }
                    tvData.setText(sb.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tvData.setText("Error: " + t.getMessage());
            }
        });

        return view;
    }
}