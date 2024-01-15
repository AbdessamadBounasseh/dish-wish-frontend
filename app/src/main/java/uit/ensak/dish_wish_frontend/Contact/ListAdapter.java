package uit.ensak.dish_wish_frontend.Contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import uit.ensak.dish_wish_frontend.R;

public class ListAdapter extends BaseAdapter {
    Context context;
    String questionslist[];
    String reponseslist[];
    LayoutInflater inflater;
    public ListAdapter(Context ctx,String [] questions,String [] responses){
        this.context=ctx;
        this.questionslist=questions;
        this.reponseslist=responses;
        inflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return questionslist.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.list_row,null);
        TextView questions =(TextView)convertView.findViewById(R.id.title);
        TextView responses =(TextView)convertView.findViewById(R.id.detail);
        questions .setText(questionslist[position]);
        responses .setText(reponseslist[position]);
        return convertView;
    }
}
