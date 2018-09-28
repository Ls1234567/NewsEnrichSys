new Vue({
	el: '#app',
	data: {
		newsList:[{
			'name': 'hhh'
		}]
	},
	//Vue对象实例创建成功后自动调用
	created: function(){
		this.getlist();
	},
	methods: {
		getlist:function(){
			this.$http.post('http://localhost:8080/NewsEnrichSys/TitleServlet?option=show')
			.then(function(response){
				this.newsList = response.body;
			});
		}
	}
});

