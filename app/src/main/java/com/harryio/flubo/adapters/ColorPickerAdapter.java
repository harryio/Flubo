package com.harryio.flubo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harryio.flubo.R;
import com.harryio.flubo.customview.ColoredCircleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ColorPickerViewHolder> {
    private int[] colors;
    private int selectedColorPosition;
    private OnClickListener onClickListener;

    public ColorPickerAdapter(int[] colors, int selectedColorPosition) {
        this.colors = colors;
        this.selectedColorPosition = selectedColorPosition;
    }

    static class ColorPickerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.colorView)
        ColoredCircleView colorView;
        @BindView(R.id.selector)
        View selectorView;

        private OnClickListener clickListener;

        ColorPickerViewHolder(View itemView, OnClickListener clickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.clickListener = clickListener;
            if (clickListener == null) throw new IllegalStateException("On Click Listener is null");
        }

        @OnClick
        public void onRootViewClicked() {
            clickListener.onColorSelected(colorView.getColor(), getAdapterPosition());
        }
    }

    @Override
    public ColorPickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_color_picker_item, parent, false);
        return new ColorPickerViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(ColorPickerViewHolder holder, int position) {
        holder.colorView.setcolor(colors[position]);
        holder.selectorView.setVisibility(position == selectedColorPosition ? VISIBLE : GONE);
    }

    @Override
    public int getItemCount() {
        return colors.length;
    }

    private void setSelectedColorPosition(int selectedColorPosition) {
        int previousPosition = this.selectedColorPosition;
        this.selectedColorPosition = selectedColorPosition;

        notifyItemChanged(previousPosition);
        notifyItemChanged(selectedColorPosition);
    }

    public interface OnClickListener {
        void onColorSelected(int color, int selectedPosition);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}