package com.unrulyrecursion.partkeeprconnector;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.zxing.integration.android.IntentIntegrator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.unrulyrecursion.partkeeprconnector.model.Part;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PartDetailFragment extends Fragment {

	private FragmentActivity mActivity;
	private Part cur;
	private IntentIntegrator ii;

	public static PartDetailFragment newInstance(Part in) {
		Log.d("Part Detail Fragment", "Statically creating fragment");
		PartDetailFragment p = new PartDetailFragment();

		Bundle args = new Bundle();
		args.putSerializable("part", in);
		p.setArguments(args);

		return p;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Part Detail Fragment", "Creating View");
		mActivity = getActivity();
		
		ii = new IntentIntegrator(this);
		
		if (container == null) {
			return null;
		}

		View v = inflater.inflate(R.layout.fragment_part_detail, container,
				false);

		cur = (Part) getArguments().getSerializable("part");

		if (cur != null) {
			// Populate Attributes of Part
			TextView title = (TextView) v.findViewById(R.id.detailTitle);
			title.setText(cur.getName());
			Log.d("Part Detail Fragment", "Creating view for " + cur.getName());

			TextView sl = (TextView) v.findViewById(R.id.detailStockLevel);
			sl.setText(cur.getStockLevel()+"");
			
			TextView msl = (TextView) v.findViewById(R.id.detailMinStockLevel);
			msl.setText(cur.getMinStockLevel()+"");

			TextView desc = (TextView) v.findViewById(R.id.detailDescription);
			desc.setText(cur.getDescription());

			TextView category = (TextView) v.findViewById(R.id.detailCategory);
			category.setText(cur.getPcName());

			TextView storLoc = (TextView) v.findViewById(R.id.detailStorageLocation);
			storLoc.setText(cur.getStorageLocName());

			TextView comment = (TextView) v.findViewById(R.id.detailComment);
			comment.setText(cur.getComment());

			TextView createDate = (TextView) v
					.findViewById(R.id.detailCreateDate);
			createDate.setText(cur.getCreateDate());
			Log.d("Part Details Fragment", cur.getAttachmentCount()+"");
			if (cur.getAttachmentCount() > 0) {
				ImageView img = (ImageView) v.findViewById(R.id.imgTest);
				Log.d("Part Details Fragment", "Grabbed ImageView");
				
				ImageLoader il = ImageLoader.getInstance();
				il.displayImage(((PartKeeprConnectorApp)mActivity.getApplication()).getBase_url() + "file.php?type=PartAttachment&id=0", img);
				/*
//				for (int n : cur.getAttachIds()) {
					Log.d("Part Detail Fragment", "Adding Img Attachment " + 1);
					AsyncTask<Integer, Integer, Drawable> it = new getImgTask().execute(1);
					try {
						img.setImageDrawable(it.get());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//				}
 */

			}
			


			/*
			 * 
			 * TextView category = (TextView)
			 * v.findViewById(R.id.detailCategory);
			 * category.setText(cur.getPcName());
			 */
		} else {
			Log.d("Part Detail Fragment", "No part passed in");
		}

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();

		getView().findViewById(R.id.detailScanBarcode).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				scanCode(arg0);
			}});
	}

	public int getShownIndex() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void addImage(View v) {
		
	}

	// src - http://wowjava.wordpress.com/2011/02/25/scanning-barcode-from-android-application/
	public void scanCode(View v) {
		Log.d("Part Detail Fragment", "Initiating Scan");
		ii.initiateScan();
		/*
		Intent i = new Intent("com.google.zxing.client.android.SCAN");
		i.setPackage("com.google.zxing.client.android");
		i.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
		startActivityForResult(i,0);
		*/
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Log.d("Part Detail Fragment", "onActivityResult");
	    if (requestCode == IntentIntegrator.REQUEST_CODE) {
	    	Log.d("result code", resultCode+"");
	    	
	        if (resultCode == Activity.RESULT_OK) {
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            Toast toast = Toast.makeText(getActivity().getBaseContext(), contents,
	    				Toast.LENGTH_SHORT);
	    		toast.show();
	            //et_code.setText(contents);
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	            //et_code.setText("Please scan again");
	        }
	        
	    }
	}

	private class getImgTask extends AsyncTask<Integer, Integer, Drawable> {

		@Override
		protected Drawable doInBackground(Integer... arg0) {
			Log.d("getImgTask","Doing in Background");
			// from Stack Overflow answer
			// src - http://stackoverflow.com/questions/14867278/android-app-display-image-from-url

			// TODO may need to extend this to return arraylist of bitmaps to handle multiple attachments
			try {
				URL url = new URL(((PartKeeprConnectorApp)mActivity.getApplication()).getBase_url() + "file.php?type=PartAttachment&id=" + arg0[0]);
				Log.d("getImgTask", url.toString());
				HttpGet httpRequest = new HttpGet(url.toURI());
				httpRequest.setHeader("Content-Type","image/jpeg"); // TODO might need to pass this in and have switch
				httpRequest.setHeader("session", ((PartKeeprConnectorApp)mActivity.getApplication()).getmSession().getSessId());

				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);

				HttpEntity entity = response.getEntity();
				BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);

				InputStream input = b_entity.getContent();
				Drawable d = Drawable.createFromStream(input, "src");
				return d;
//				Bitmap bitmap = BitmapFactory.decodeStream(input);

//				return bitmap;

			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	}
}
