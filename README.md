android的http异步框架
=====

使用接口回调，实现了cookie持久化，保持session一致
-----
### 1. 入口
    com.example.async.MainActivity.java
    
### 2. 包结构
	2.1 用户界面包
	com.example.async
	|—— MainActivity 入口文件
	|—— PlaceholderFragment 用法测试的入口
	
	2.2 用户实体类
	com.example.async
	|—— School 具体的实体，需要继承QuantaBaseModel 
	
	2.3 异步网络通信框架
	com.quanta.async 
	|—— QuantaConfig 异步框架的全局配置文件，用户必须根据自己的包名去配置
	|—— QuantaAsync 异步框架对外的接口，需要传入context对象，该类不做具体操作，负责将任务转发给QuantaTaskThread和QuantaHandler
	|—— QuantaTaskThread 具体处理的任务的线程池，由QuantaAsync调用，处理完完成之后通知QuantaHandler
	|—— QuantaHttpClient 网络通信类，由QuantaThread调用，模拟浏览器请求，通过SharedPreference持久化Cookie
	|—— QuantaHandler 将QuantaTaskThread执行完的结果处理成QuantaBaseMessage的格式，再通过interface 进行回调
	|—— QuantaBaseMessage 消息基类，数据返回的格式需要按照该类的格式设定：如{"data":{}, "info":"success", "status":1},后面详解
	|—— QuantaModel 基类的模型，所有用户自定义的类必须继承自改模型，否则将会出错

### 3. QuantaBaseMessage Json数据类型，根节点data, info, status是固定的，子节点的名称可以自定义
	{
		"data": {
			"User" : {"name": "wangjiewen", "age": 22},
			"Article" : [
				{"title": "如何成为优秀的程序员", "content": "少吃，多打代码"},
				{"title": "如何成为优秀的程序员", "content": "少吃，多打代码"},
				{"title": "如何成为优秀的程序员", "content": "少吃，多打代码"}
			]
		}, 
		"info": "success", 
		"status": 1
	}

### 4. 全局配置
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

### 5.用法案例
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
