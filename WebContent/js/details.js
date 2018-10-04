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
	url:"http://114.212.84.29:8080/NewsEnrichSys/ContentServlet?option=getContent&id="+id,
	type:"get",
	dataType:"json",
	async:false,
	success:function(d){
		$('#title').html(d.title);
		$('#content').html(d.content);
		$.ajax({
			url:"http://114.212.84.29:8080/NewsEnrichSys/ContentServlet?option=getTypes&id="+id,
			type:"get",
			dataType:"json",
			async:false,
			success:function(d){
				$.each(d, function(i, val){
					var type = $("<button class='btn btn-default' onclick='addTypeEntities(this)'></button>").text(val);
					$('#types').append(type);
				});
			}
		})
	}
})

var car = $('#car');
function addTypeEntities(obj){
	$('#app a').each(function() {
//		console.log($(this).attr('id'));
		if($(this).attr('id').split(";")[1] == obj.innerText){
			addTypeEntity($(this));
		}
			
	});
}

function addTypeEntity(obj) { //使用type添加
	var isContain = false;
	car.children().each(function(){
		if($(this).text() == obj.text())
			isContain = true;
	});
	if(isContain==false){
		var entity = $("<button class='btn btn-info' id='"+obj.attr('id')+"' onclick='removeEntity(this)'></button>").text(obj.text());
		car.append(entity); //添加实体
	}
}

function addEntity(obj) { //单独添加
	var isContain = false;
	car.children().each(function(){
		if($(this).text() == obj.innerText)
			isContain = true;
	});
	if(isContain==false){
		var entity = $("<button class='btn btn-info' id='"+obj.id+"' onclick='removeEntity(this)'></button>").text(obj.innerText);
		car.append(entity);
//		var close = $("<div class='close'></div>");
//		var div = $("<div>"+entity+close+"</div>")
//		car.append(div); //添加实体
	}
}

function removeEntity(obj) {
	obj.remove();
}

function showResults(){
	var uris = "";
	var spec = "";
	$('#car button').each(function(){
		var id = $(this).attr('id');
		uris += id.split(";")[0]+";";
		$('#title a').each(function(){
			if($(this).attr('id') == id){
				spec += id.split(";")[0]+";";
				return false;
			}
		});
	});
//	console.log(uris);
	$.ajax({
		url:"http://114.212.84.29:8080/NewsEnrichSys/ContentServlet?option=getQrel&id="+id+"&uris="+uris+"&spec="+spec,
		type:"get",
		dataType:"json",
		async:false,
		success:function(d){
			console.log(d);
		}
	});
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




