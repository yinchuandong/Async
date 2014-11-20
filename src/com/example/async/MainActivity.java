package com.example.async;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.model.School;
import com.quanta.async.QuantaAsync;
import com.quanta.async.QuantaAsync.OnQuantaAsyncListener;
import com.quanta.async.QuantaBaseMessage;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public static final String base = "http://api.superlib.cn/index.php/Api/";
		
		private QuantaAsync quantaAsync = null;
		private Button getBtn;
		private Button postBtn;
		
		public PlaceholderFragment() {
			Log.i("fragment", "-------");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			initView(rootView);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			quantaAsync = new QuantaAsync(getActivity().getApplicationContext());
			bindEvent();
		}
		
		private void initView(View view){
			getBtn = (Button)view.findViewById(R.id.get);
			postBtn = (Button)view.findViewById(R.id.post);
		}
		
		/**
		 * 绑定时间
		 */
		private void bindEvent(){
			getBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					quantaAsync.get(1, "http://192.168.233.33/android/cookie.php");
					quantaAsync.get(1, "http://api.superlib.cn/index.php/Api/School/getSchoolList");
				}
			});
			
			postBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					HashMap<String, String> form = new HashMap<String, String>();
					form.put("username", "yinchuandong");
					form.put("age", "21");
					form.put("class", "software engineer");
//					quantaAsync.post(2, "http://192.168.233.33/android/post.php", form);
					quantaAsync.post(2, "http://api.superlib.cn/index.php/Api/School/getSchoolList", form);
				}
			});
			
			
			/**
			 * 设置异步事件的回调
			 */
			quantaAsync.setQuantaAsyncListener(new OnQuantaAsyncListener() {
				
				@Override
				public void onNetWorkError(int taskId, String errorMsg) {
					Toast.makeText(getActivity(), "error:" + errorMsg, Toast.LENGTH_LONG).show();
				}
				
				@Override
				public void onComplete(int taskId) {
					Toast.makeText(getActivity(), "get成功", Toast.LENGTH_LONG).show();
					Log.i("fragment", "-----" + taskId);
				}
				
				@Override
				public void onComplete(int taskId, QuantaBaseMessage baseMessage) {
					try {
						@SuppressWarnings("unchecked")
						ArrayList<School> schoolList = (ArrayList<School>)baseMessage.getDataList("School");
						School school = schoolList.get(0);
						Log.i("Fragment", school.getSchoolName());
						Toast.makeText(getActivity(), "schoolName:" + school.getSchoolName(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
