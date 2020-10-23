package com.ahmetaliyilmaz.aay19.bidenco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SaticiFeedRecyclerAdapter extends RecyclerView.Adapter<SaticiFeedRecyclerAdapter.PostHolder> {

    private ArrayList<String> productNameList;
    private ArrayList<String> productCommentList;
    private ArrayList<String> productImageList;
    private ArrayList<String> priceList;
    private ArrayList<String> saticiList;
    private ArrayList<String> categoryList;

    public SaticiFeedRecyclerAdapter(ArrayList<String> productNameList, ArrayList<String> productCommentList, ArrayList<String> productImageList, ArrayList<String> priceList,ArrayList<String> categoryList,ArrayList<String> saticiList) {
        this.productNameList = productNameList;
        this.productCommentList = productCommentList;
        this.productImageList = productImageList;
        this.priceList = priceList;
        this.saticiList = saticiList;
        this.categoryList = categoryList;

    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.productName.setText(productNameList.get(position));
        holder.productComment.setText(productCommentList.get(position));
        Picasso.get().load(productImageList.get(position)).into(holder.imageView);
        holder.fiyatText.setText(priceList.get(position));
        holder.sellerName.setText(saticiList.get(position));
        holder.category.setText(categoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return productNameList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView productName;
        TextView productComment;
        TextView fiyatText;
        TextView sellerName;
        TextView category;


        public PostHolder(@NonNull View itemView) {

            super(itemView);

            imageView = itemView.findViewById(R.id.product_imageview);
            productName = itemView.findViewById(R.id.product_name_text);
            productComment = itemView.findViewById(R.id.product_comment_text);
            fiyatText = itemView.findViewById(R.id.fiyatText);
            sellerName = itemView.findViewById(R.id.sellerName);
            category = itemView.findViewById(R.id.category);
        }
    }
}
