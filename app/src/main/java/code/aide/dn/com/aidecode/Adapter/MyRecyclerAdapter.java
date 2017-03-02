package code.aide.dn.com.aidecode.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import code.aide.dn.com.aidecode.Model.DataModel;
import code.aide.dn.com.aidecode.R;

/**
 * Created by 健 on 2017/1/14.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyHolder> {
    private LayoutInflater inflater ;
    private Context context;
    private List<DataModel> mDatas;
    public interface OnItemClickListener{
        public void OnClickListener(View view,int pos);
        public void OnLongClickListener(View view ,int pos);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecyclerAdapter(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
       this.mDatas = new ArrayList<>();
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAG","onCreateViewHolder()");
        View view = inflater.inflate(R.layout.item_one,parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        Log.i("TAG","onBindViewHolder()");
//
//        CardView.LayoutParams params = (CardView.LayoutParams) ((CardView)holder.itemView).getLayoutParams();
//        if (position==0)
//        {
//
//            holder.itemView.setLayoutParams(params);
//        }

        holder.title.setText(mDatas.get(position).getTitle());
        holder.subtitle.setText(mDatas.get(position).getSubtitle());
        holder.imageView.setImageResource(mDatas.get(position).getImageId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.OnClickListener(holder.itemView,pos);
                }
                return;
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener!=null)
                {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.OnLongClickListener(holder.itemView,pos);
                }
                return true;
            }
        });
    }
    public void addData(DataModel data,int postion){
        Log.i("TAG","addData()");
        mDatas.add(data);
        notifyItemInserted(postion);
    }
    public void delete(int postion){
        mDatas.remove(postion);
       notifyItemRemoved(postion);
    }
    @Override
    public int getItemCount() {
//        Log.i("TAG","getItemCount()");
//        Log.i("TAG",mDatas.size()+"");
        return mDatas.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subtitle;
        ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            Log.i("TAG","MyHolder()");
            title = (TextView) itemView.findViewById(R.id.item_title);
            subtitle = (TextView) itemView.findViewById(R.id.item_subtitle);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }
    public void setData(List<DataModel> data){
        this.mDatas = data;
        notifyDataSetChanged();
    }
    //dip转像素
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
