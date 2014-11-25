android的http异步框架
=====

使用接口回调，实现了cookie持久化，保持session一致
-----
### 入口
    com.example.async.MainActivity.java

### 全局配置
	/**
	 * 针对quanta包的全局配置文件
	 * @author wangjiewen
	 *
	 */
	public class QuantaConfig {
	
		/**
		 * 每个应用放实体model的包名，必须设置，否则将会找不到路径
		 */
		public final static String MODEL_PACKAGE = "com.example.model";
		
		/**
		 * 全局sharepreference的名字, 建议根据自己的应用设置
		 */
		public final static String SHARE_PREFERENCE_NAME = "com.example.async.sp.global";
		
		/**
		 * 持久化cookie的键值，保存在sharepreference中
		 */
		public final static String PERSISTENT_COOKIE = "_persistent_cookie";
	}

### 用法案例
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
