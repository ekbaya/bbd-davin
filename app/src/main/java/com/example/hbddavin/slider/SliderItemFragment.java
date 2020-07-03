package com.example.hbddavin.slider;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.example.hbddavin.R;

public class SliderItemFragment extends Fragment {
    private static final String ARG_POSITION = "slider-position";
    // prepare all title ids arrays
    @StringRes
    private static final int[] PAGE_TITLES =
            new int[] { R.string.love, R.string.kind, R.string.beautiful, R.string.grateful };
    // prepare all subtitle ids arrays
    @StringRes
    private static final int[] PAGE_TEXT =
            new int[] {
                    R.string.love_text, R.string.kind_text, R.string.beautiful_text, R.string.grateful_text
            };
    // prepare all background images arrays
    @StringRes
    private static final int[] BG_IMAGE = new int[] {
            R.drawable.slider_one, R.drawable.slider_two, R.drawable.slider_three,
            R.drawable.slider_four
    };
    private int position;
    public SliderItemFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     *
     * @return A new instance of fragment SliderItemFragment.
     */
    public static SliderItemFragment newInstance(int position) {
        SliderItemFragment fragment = new SliderItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.slideritemfragment, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set page background
        //view.setBackground(requireActivity().getDrawable(BG_IMAGE[position]));
        ImageView imgBG = view.findViewById(R.id.imgBG);

        imgBG.setImageResource(BG_IMAGE[position]);
        TextView title = view.findViewById(R.id.textView);
        TextView titleText = view.findViewById(R.id.textView2);

        // set page title
        title.setText(PAGE_TITLES[position]);
        // set page sub title text
        titleText.setText(PAGE_TEXT[position]);
    }
}