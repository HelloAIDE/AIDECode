package code.aide.dn.com.aidecode.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import code.aide.dn.com.aidecode.Model.Project;
import code.aide.dn.com.aidecode.R;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 大牛哥 on 2017/2/16.
 * QQ:201309512
 * EMAIL:201309512@QQ.COM
 */

public class MyRecyclerAdapter2  extends RecyclerView.Adapter<MyRecyclerAdapter2.MyHolder> {
    private LayoutInflater inflater ;
    private Context context;
    private List<Project> mDatas;
    public interface OnItemClickListener{
        public void OnClickListener(View view, int pos);
        public void OnLongClickListener(View view ,int pos);
    }
    private MyRecyclerAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(MyRecyclerAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MyRecyclerAdapter2(Context context){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mDatas = new ArrayList<>();
    }
    @Override
    public MyRecyclerAdapter2.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("TAG","onCreateViewHolder()");
        View view = inflater.inflate(R.layout.item_two,parent,false);
        MyRecyclerAdapter2.MyHolder holder = new MyRecyclerAdapter2.MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyRecyclerAdapter2.MyHolder holder, final int position) {
        Log.i("TAG","onBindViewHolder()");
//
//        CardView.LayoutParams params = (CardView.LayoutParams) ((CardView)holder.itemView).getLayoutParams();
//        if (position==0)
//        {
//
//            holder.itemView.setLayoutParams(params);
//        }
        holder.title.setText(mDatas.get(position).getTitle());
        holder.name.setText(mDatas.get(position).getUid());
        String imageUrl= mDatas.get(position).getIcon().getFileUrl();
        //执行下载操作
        DownLoadTask task = new DownLoadTask(holder.imageView);
        task.execute(imageUrl);
        //执行下载操作
        DownLoadTask task1 = new DownLoadTask(holder.userAvater);
        task1.execute(imageUrl);
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
    public void addData(Project data,int postion){
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
        ImageView imageView;
        ImageView userAvater;
        TextView name;
        public MyHolder(View itemView) {
            super(itemView);
            Log.i("TAG","MyHolder()");
            title = (TextView) itemView.findViewById(R.id.item_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            userAvater = (ImageView) itemView.findViewById(R.id.two_iv_avater);
            name = (TextView) itemView.findViewById(R.id.item_two_name);


        }
    }
    public void setData(List<Project> data){
        this.mDatas = data;
        notifyDataSetChanged();
    }
    //dip转像素
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * 异步加载图片
     */
    class DownLoadTask extends AsyncTask<String ,Void,BitmapDrawable> {
        private ImageView mImageView;
        String url;
        public DownLoadTask(ImageView imageView){
            mImageView = imageView;
        }
        @Override
        protected BitmapDrawable doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = downLoadBitmap(url);
            BitmapDrawable drawable = new BitmapDrawable(context.getResources(),bitmap);
            return  drawable;
        }

        private Bitmap downLoadBitmap(String url) {
            Bitmap bitmap = null;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            super.onPostExecute(drawable);

            if ( mImageView != null && drawable != null){
                mImageView.setImageDrawable(drawable);
            }
        }
    }
}
