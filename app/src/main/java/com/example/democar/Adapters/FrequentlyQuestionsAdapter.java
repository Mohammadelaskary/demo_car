package com.example.democar.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.democar.Model.Question;
import com.example.democar.databinding.FrequentlyQuestionItemBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FrequentlyQuestionsAdapter extends RecyclerView.Adapter<FrequentlyQuestionsAdapter.FrequentlyQuestionsViewHolder> implements Filterable {
    List<Question> questionList;
    Context context;
    List<Question> allQuestions = new ArrayList<>();

    public FrequentlyQuestionsAdapter(List<Question> questionList,Context context) {
        this.questionList = questionList;
        this.context = context;
        allQuestions.addAll(questionList);
    }

    @NonNull
    @Override
    public FrequentlyQuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FrequentlyQuestionItemBinding binding = FrequentlyQuestionItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new FrequentlyQuestionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FrequentlyQuestionsViewHolder holder, int position) {
        Question question = questionList.get(position);
        String questionTitle = question.getQuestion();
        String answer        = question.getAnswer();
        List<Integer> images = question.getImages();

        holder.binding.question.setText(questionTitle);
        holder.binding.answer.setText(answer);

        ImagesAdapterDrawable adapter = new ImagesAdapterDrawable(images);
        holder.binding.images.setAdapter(adapter);
        holder.binding.images.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    public int getItemCount() {
        return questionList==null?0: questionList.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Question> filteredList = new ArrayList<>();
                if (constraint.toString().isEmpty() || constraint.length() == 0) {
                    filteredList.addAll(allQuestions);
                } else {
                    for (Question product : allQuestions) {
                        if (product.getQuestion().toLowerCase().contains(constraint.toString())) {
                            filteredList.add(product);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                questionList.clear();
                questionList.addAll((Collection<? extends Question>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static  class FrequentlyQuestionsViewHolder extends RecyclerView.ViewHolder{
        FrequentlyQuestionItemBinding binding;
        public FrequentlyQuestionsViewHolder(@NonNull FrequentlyQuestionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
