package kiki__000.walkingstoursapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by kiki__000 on 14-Oct-15.
 */
public class MyAdapter extends BaseAdapter {

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> icons = new ArrayList<>();
    private LayoutInflater inflater;

    static class ViewHolder {
        ImageView picture;
        TextView name;
        ProgressBar progress;
        int position;
    }


    public MyAdapter(Context context, ArrayList<String> names, ArrayList<String> icons) {
        inflater = LayoutInflater.from(context);
        this.names = names;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            //convertView = inflater.inflate(R.layout.list_view_item, parent, false);
            convertView = inflater.inflate(R.layout.list_view_item, null);

            holder = new ViewHolder();

            holder.picture = (ImageView) convertView.findViewById(R.id.picture);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.progress = (ProgressBar) convertView.findViewById(R.id.progress_bar);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(names.get(position));
        holder.picture.setVisibility(View.GONE);
        holder.progress.setVisibility(View.VISIBLE);

        //async task to load the image
        if (holder.picture != null) {
            new ImageDownloaderTask(holder.picture, holder).execute(icons.get(position));
        }

        return convertView;

    }

    //async class to download the image
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final ViewHolder holder;

        public ImageDownloaderTask(ImageView imageView, ViewHolder holder) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.holder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String image = params[0];

            if (image == null){

                return null;
            }else{

                //convert to Bitmap
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                return decodedByte;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        // if image is ok then set to the imageView
                        holder.progress.setVisibility(View.GONE);
                        holder.picture.setVisibility(View.VISIBLE);
                        holder.picture.setImageBitmap(bitmap);

                    } else {
                        //else set the ic launcher
                        holder.progress.setVisibility(View.GONE);
                        holder.picture.setVisibility(View.VISIBLE);
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
                        holder.picture.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

}
