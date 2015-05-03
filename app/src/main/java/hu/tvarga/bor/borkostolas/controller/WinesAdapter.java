package hu.tvarga.bor.borkostolas.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import hu.tvarga.bor.borkostolas.R;
import hu.tvarga.bor.borkostolas.model.bean.ScoredWine;

import static hu.tvarga.bor.borkostolas.UserPage.getFormattedScore;

public class WinesAdapter extends ArrayAdapter<ScoredWine> {

    public WinesAdapter(Context context, ArrayList<ScoredWine> wines) {
        super(context, 0, wines);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ScoredWine wine = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.winerow, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.wineRowName);
        TextView tvScore = (TextView) convertView.findViewById(R.id.wineRowScore);

        tvName.setText(wine.getWine_name());
        Double score = wine.getWine_score();
        tvScore.setText(getFormattedScore(score));

        return convertView;
    }

}