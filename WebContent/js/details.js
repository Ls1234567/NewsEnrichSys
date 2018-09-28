function getQueryVariable(variable) { //获取url参数
   var query = window.location.search.substring(1);
   var vars = query.split("&");
   for (var i=0;i<vars.length;i++) {
           var pair = vars[i].split("=");
           if(pair[0] == variable){return pair[1];}
   }
   return(false);
}

var id = getQueryVariable("id");
$.ajax({
	url:"http://localhost:8080/NewsEnrichSys/ContentServlet?option=getContent&id="+id,
	type:"get",
	dataType:"json",
	async:false,
	success:function(d){
		$('#title').html(d.title);
		$('#content').html(d.content);
	}
})


var car = $('#car');
function addEntity(obj) {
	var isContain = false;
	car.children().each(function(){
		if($(this).text() == obj.innerText)
			isContain = true;
	});
	if(isContain==false){
		var entity = $("<button class='btn btn-info' onclick='removeEntity(this)'></button>").text(obj.innerText);
		car.append(entity); //添加实体
	}
}

function removeEntity(obj) {
	obj.remove();
}


//new Vue({
//	el: '#app',
//	data: {
//		title: '',
//		content: '',
//	},
//	//Vue对象实例创建成功后自动调用
//	created: function(){
//		var id = this.getQueryVariable("id");
//		this.$http.post('http://localhost:8080/NewsEnrichSys/ContentServlet?option=getContent&id='+id)
//		.then(function(response){
//			document.getElementById("title").innerHTML = response.body.title;
//			document.getElementById("content").innerHTML = response.body.content;
//		});
//	},
//	methods: {
//		getQueryVariable: function(variable) { //获取url参数
//	       var query = window.location.search.substring(1);
//	       var vars = query.split("&");
//	       for (var i=0;i<vars.length;i++) {
//	               var pair = vars[i].split("=");
//	               if(pair[0] == variable){return pair[1];}
//	       }
//	       return(false);
//		},
//		toggleButton: function() {
//			alert('hello');
//		}
//	}
//});




