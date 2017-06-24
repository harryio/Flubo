package com.harryio.flubo.customview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.harryio.flubo.R;
import com.harryio.flubo.adapters.ColorPickerAdapter;

public class ColorPickerFragment extends DialogFragment {
    private static final String ARG_SELECTED_COLOR_POSITION = "com.harryio.flubo." + "selected_color_position";

    private int selectedPosition;
    private int selectedColor;
    private OnColorPickerFragmentInteraction listener;

    public static ColorPickerFragment newInstance(int selectedColorPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_SELECTED_COLOR_POSITION, selectedColorPosition);
        ColorPickerFragment fragment = new ColorPickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedPosition = getArguments().getInt(ARG_SELECTED_COLOR_POSITION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_color_picker, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        setupRecyclerView(recyclerView);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_color_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onColorPositionSelected(selectedPosition, selectedColor);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setView(view)
                .create();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        int[] colors = getActivity().getResources()
                .getIntArray(R.array.color_array);
        ColorPickerAdapter adapter = new ColorPickerAdapter(colors, selectedPosition);
        selectedColor = colors[selectedPosition];
        adapter.setOnClickListener(new ColorPickerAdapter.OnClickListener() {
            @Override
            public void onColorSelected(int color, int selectedPosition) {
                ColorPickerFragment.this.selectedPosition = selectedPosition;
                ColorPickerFragment.this.selectedColor = color;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public interface OnColorPickerFragmentInteraction {
        void onColorPositionSelected(int selectedPosition, int color);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnColorPickerFragmentInteraction) {
            listener = (OnColorPickerFragmentInteraction) context;
        } else throw new RuntimeException(context.toString()
                + " must implement " + OnColorPickerFragmentInteraction.class.getSimpleName());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        listener = null;
    }
}