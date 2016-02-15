package com.xc0ffee.nytimes.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xc0ffee.nytimes.R;
import com.xc0ffee.nytimes.models.Article;

import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Article article = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_image);
        imageView.setImageResource(0);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_title);
        textView.setText(article.getmHeadline());

        String thumbnail = article.getmThumbnail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }
        return convertView;
    }
}
