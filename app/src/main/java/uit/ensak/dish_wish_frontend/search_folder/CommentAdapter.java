package uit.ensak.dish_wish_frontend.search_folder;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import uit.ensak.dish_wish_frontend.Models.Comment;
import uit.ensak.dish_wish_frontend.dto.CommentResponseDTO;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final List<CommentResponseDTO> comments;
    private  final SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "comment_prefs";
    private static final String KEY_COMMENTS = "comments_key";
    public CommentAdapter(Context context, List<CommentResponseDTO> comments) {
        this.comments = comments;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentResponseDTO comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }



    public void addAll(List<CommentResponseDTO> commentResponses) {
        for (CommentResponseDTO commentResponse : commentResponses) {
//            Comment comment = new Comment(commentResponse.getClient().getFirstName() +" "+ commentResponse.getClient().getLastName(), commentResponse.getContent());
            comments.add(commentResponse);
        }
        notifyDataSetChanged();
    }


    private void saveCommentsToPrefs(List<Comment> comments) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Comment comment : comments) {
            stringBuilder.append(comment.getUsername()).append(": ").append(comment.getCommentContent()).append(",");
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_COMMENTS, stringBuilder.toString());
        editor.apply();
    }

    public List<Comment> getCommentsFromPrefs() {
        String commentsString = sharedPreferences.getString(KEY_COMMENTS, "");
        String[] commentsArray = commentsString.split(",");
        List<Comment> commentList = new ArrayList<>();
        for (String comment : commentsArray) {
            String[] parts = comment.split(": ");
            if (parts.length == 2) {
                Comment newComment = new Comment(parts[0], parts[1]);
                commentList.add(newComment);
            }
        }
        return commentList;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewComment;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(android.R.id.text1);
        }

        public void bind(CommentResponseDTO comment) {
            String displayText = comment.getClient().getFirstName()+ "\n " + comment.getContent();
            textViewComment.setText(displayText);
        }
    }
}