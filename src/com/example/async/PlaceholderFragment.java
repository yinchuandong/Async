package com.example.async;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.model.School;
import com.quanta.async.QuantaAsync;
import com.quanta.async.QuantaBaseMessage;
import com.quanta.async.QuantaAsync.OnQuantaAsyncListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

	public static final String base = "http://api.superlib.cn/index.php/Api/";
	
	/**
	 * 异步框架的对象
	 */
	private QuantaAsync quantaAsync = null;
	private Button getBtn;
	private Button postBtn;
	
	public PlaceholderFragment() {
		Log.i("fragment", "-------");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
	 * 事件绑定
	 */
	private void bindEvent(){
		
		//测试get方法
		getBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				quantaAsync.get(1, base + "School/getSchoolList");
			}
		});
		
		//测试post方法
		postBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HashMap<String, String> form = new HashMap<String, String>();
				form.put("username", "wangjiewen");
				form.put("age", "21");
				form.put("class", "software engineer");
				quantaAsync.post(2, base + "School/getSchoolList", form);
			}
		});
		
		
		//设置异步事件的回调
		quantaAsync.setQuantaAsyncListener(new OnQuantaAsyncListener() {
			
			@Override
			public void onNetWorkError(int taskId, String errorMsg) {
				Toast.makeText(getActivity(), "error:" + errorMsg, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onComplete(int taskId) {
				Toast.makeText(getActivity(), "请求成功", Toast.LENGTH_LONG).show();
				Log.i("fragment", "-----" + taskId);
			}
			
			@Override
			public void onComplete(int taskId, QuantaBaseMessage baseMessage) {
				switch (taskId) {
				//get 方法返回
				case 1:
					try {
						//通过反射机制直接根据model的名称转化为对应的数据结构
						@SuppressWarnings("unchecked")
						ArrayList<School> schoolList = (ArrayList<School>)baseMessage.getDataList("School");
						School school = schoolList.get(0);
						Log.i("Fragment", school.getSchoolName());
						Toast.makeText(getActivity(), "get schoolName:" + school.getSchoolName(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				
				//post 方法返回
				case 2:
					try {
						//通过反射机制直接根据model的名称转化为对应的数据结构
						@SuppressWarnings("unchecked")
						ArrayList<School> schoolList = (ArrayList<School>)baseMessage.getDataList("School");
						School school = schoolList.get(0);
						Log.i("Fragment", school.getSchoolId());
						Toast.makeText(getActivity(), "post schoolId:" + school.getSchoolId(), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
			
		});
	}

}
