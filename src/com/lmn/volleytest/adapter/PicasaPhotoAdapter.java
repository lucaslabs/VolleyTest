package com.lmn.volleytest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.lmn.volleytest.R;
import com.lmn.volleytest.model.picasa.Entry;
import com.lmn.volleytest.model.picasa.PicasaPhoto;
import com.lmn.volleytest.model.picasa.PicasaResponse;

import java.util.List;

/**
 * Adapter of {@link com.lmn.volleytest.model.picasa.PicasaPhoto}s.
 * <p>
 * Implements ViewHolder design pattern.
 * 
 * @author Lucas Nobile
 */
public class PicasaPhotoAdapter extends ArrayAdapter<PicasaPhoto> {
	private ImageLoader mImageLoader;
	private List<PicasaPhoto> mEntries;

	public PicasaPhotoAdapter(Context context, int textViewResourceId,
			List<PicasaPhoto> objects, ImageLoader imageLoader) {
		super(context, textViewResourceId, objects);
		mImageLoader = imageLoader;
		mEntries = objects;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		PicasaPhotoHolder holder;

		if (view == null) {
			// Recycle views
			holder = new PicasaPhotoHolder();
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.row_dynamic_list, null);

			holder.thumbnail = (NetworkImageView) view
					.findViewById(R.id.ivThumbnail);
			holder.title = (TextView) view.findViewById(R.id.tvTitle);

			view.setTag(holder);
		} else {
			holder = (PicasaPhotoHolder) view.getTag();
		}

		PicasaPhoto entry = getItem(position);

		// Use of NetworkImageView
		if (entry.getThumbnailUrl() != null) {
			holder.thumbnail.setImageUrl(entry.getThumbnailUrl(), mImageLoader);
		} else {
			holder.thumbnail.setImageResource(R.drawable.no_image);
		}

		holder.title.setText(entry.getTitle());

		return view;
	}

	public void addFeeds(PicasaResponse response) {
		for (Entry e : response.getFeed().getEntries()) {
			String title = e.getMediaGroup().getMediaGroupCredits().get(0)
					.getTitle();
			String thumbnailUrl = e.getMediaGroup().getMediaGroupContents()
					.get(0).getUrl();
			PicasaPhoto picasaEntry = new PicasaPhoto(title, thumbnailUrl);
			mEntries.add(picasaEntry);
		}
		notifyDataSetChanged();
	}

	// ViewHolder pattern to use for ListView
	private class PicasaPhotoHolder {
		NetworkImageView thumbnail;
		TextView title;
	}
}
