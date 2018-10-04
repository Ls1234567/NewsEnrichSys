$.ajax({
	url:"http://114.212.84.29:8080/NewsEnrichSys/TitleServlet?option=show",
	type:"get",
	dataType:"json",
	async:false,
	success:function(d){
		$.each(d, function(i, val){
			var newsList = $('#news-list');
			var item = $("<a href='http://114.212.84.29:8080/NewsEnrichSys/TitleServlet?option=goDetail&id="+val.id+"'></a>").text(val.name);
			newsList.append(item);
		});
	}
})


//new Vue({
//	el: '#app',
//	data: {
//		newsList:[{
//			'name': 'hhh'
//		}]
//	},
//	//Vue对象实例创建成功后自动调用
//	created: function(){
//		this.getlist();
//	},
//	methods: {
//		getlist:function(){
//			this.$http.post('http://114.212.84.29:8080/NewsEnrichSys/TitleServlet?option=show')
//			.then(function(response){
//				this.newsList = response.body;
//			});
//		}
//	}
//});

