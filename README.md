android的http异步框架
=====

使用接口回调，实现了cookie持久化，保持session一致
-----
### 入口
    com.example.async.MainActivity.java

### 用法案例
      
      getBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
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
