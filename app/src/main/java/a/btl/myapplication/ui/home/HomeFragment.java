package a.btl.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import a.btl.myapplication.ui.exercise.ExercisesActivity;
import a.btl.myapplication.R;
import a.btl.myapplication.ui.notification.TimeChooseActivity;
import a.btl.myapplication.databinding.FragmentHomeBinding;
import a.btl.myapplication.entity.ListExercises;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getItems(getContext()).observe(getViewLifecycleOwner(), new Observer<List<ListExercises>>() {
            @Override
            public void onChanged(List<ListExercises> listExercises) {
                ListExercisesItemAdapter adapter = new ListExercisesItemAdapter(listExercises, listExercise -> {
                    Intent intent = new Intent(getActivity(), ExercisesActivity.class);
                    intent.putExtra("list_exercises", listExercise);
                    startActivity(intent);
                });
                recyclerView.setAdapter(adapter);
            }
        });
        Button btnTime = root.findViewById(R.id.btn_time_choose);
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TimeChooseActivity.class));
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}