package tm.fantom.exchangerate.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import tm.fantom.exchangerate.databinding.ItemRateBinding;
import tm.fantom.exchangerate.repo.model.RateModel;
import tm.fantom.exchangerate.ui.base.BaseAdapter;
import tm.fantom.exchangerate.ui.base.BaseViewHolder;


public class RatesAdapter extends BaseAdapter<RateModel, BaseViewHolder<RateModel>> {

    private OnRateClickListener clickListener;

    public interface OnRateClickListener {
        void onRateClick(String name);
    }

    @NonNull
    @Override
    public BaseViewHolder<RateModel> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRateBinding binding = ItemRateBinding.inflate(inflater, parent, false);
        return new ItemViewHolder(binding.getRoot());
    }

    public RatesAdapter setClickListener(OnRateClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    class ItemViewHolder extends BaseViewHolder<RateModel> {

        ItemRateBinding layout;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = ItemRateBinding.bind(itemView);
        }

        @Override
        public void onBind(RateModel item) {
            layout.tvTitle.setText(item.getName());
            layout.tvRate.setText(item.getRate());
            layout.getRoot().setOnClickListener(v -> {
                if (clickListener != null)
                    clickListener.onRateClick(item.getName());
            });
        }
    }
}
