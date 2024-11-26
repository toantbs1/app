package a.btl.myapplication.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import a.btl.myapplication.R;
import a.btl.myapplication.databinding.FragmentNotificationsBinding;
import a.btl.myapplication.entity.dto.UserSession;
import a.btl.myapplication.ui.login.LoginActivity;

public class SettingFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private Button btnLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel settingViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        settingViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        btnLogout= root.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserSession.getInstance(getContext()).clearSession();
                startActivity(new Intent(getActivity(), LoginActivity.class));
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