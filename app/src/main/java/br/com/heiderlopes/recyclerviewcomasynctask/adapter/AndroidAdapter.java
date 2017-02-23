package br.com.heiderlopes.recyclerviewcomasynctask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

import br.com.heiderlopes.recyclerviewcomasynctask.R;
import br.com.heiderlopes.recyclerviewcomasynctask.listener.OnItemClickListener;
import br.com.heiderlopes.recyclerviewcomasynctask.model.Android;

public class AndroidAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<Android> data = Collections.emptyList();

    private OnItemClickListener clickListener;

    public AndroidAdapter(Context context, List<Android> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_android, parent, false);
        AndroidItemHolder holder = new AndroidItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AndroidItemHolder myHolder = (AndroidItemHolder) holder;
        Android current = data.get(position);
        myHolder.tvNome.setText(current.getNome());
        myHolder.tvAPI.setText(context.getString(R.string.label_api)  + current.getApi());
        myHolder.tvVersao.setText(context.getString(R.string.label_versao) + current.getVersao());

        Glide.with(context).load(current.getUrlImagem())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.android_erro)
                .into(myHolder.ivLogo);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public Android getItem(int position) {
        return data.get(position);
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private class AndroidItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivLogo;
        TextView tvNome;
        TextView tvAPI;
        TextView tvVersao;

        private AndroidItemHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.ivLogo);
            tvNome = (TextView) itemView.findViewById(R.id.tvNome);
            tvAPI = (TextView) itemView.findViewById(R.id.tvAPI);
            tvVersao = (TextView) itemView.findViewById(R.id.tvVersao);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }

    }
}