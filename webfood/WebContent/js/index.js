$(function(){
	$.ajax({
		  url:"http://localhost:8080/webfood/resfood.action",
		  data:'op=findAll',
		  type:"POST",
		  dataType:"JSON",
		  success: function(  data  ){
		      //data=>     code      obj
			  //   =>     code      msg
			  if(   data.code==0){
				  alert(    "服务器错误 ，"+ data.msg);
			  }else{
				  showAll( data.obj  );
			  }
			  
		  }
	});
});

var haslogined=false;//用来判断用户是否登录
var buyfoodidarr=[];//购买的商品的编号


var  allfoodsarr;
function   showAll(  obj  ){
	allfoodsarr=obj;
	//var allfoods=$("#allfoods");
	for(var i=0;i<allfoodsarr.length;i++){
		var onefood=allfoodsarr[i];    //取出一个菜
		var li=document.createElement("li");
		
		//插入菜名
		var title=document.createElement("h3");
		title.innerHTML=onefood.fname;   //   <h3 id="1">xxx</h3>
		title.id="fid"+onefood.fid;
		li.appendChild(title);     //       <li><h3 id="1">xxx</h3></li>
		
		//插入菜单详情的div
		var fooddesc=document.createElement("div");   //  <div class="fooddesc" id="fooddesc1"></div>
		//TODO:
		yc.addClassName(fooddesc,"fooddesc");
		
		fooddesc.id="fooddesc"+onefood.fid;
		var foodimg=document.createElement("img");   // <img class="foodimg" src="image/foods/50002.jpg">
		foodimg.src="image/foods/"+onefood.fphoto;
		//TODO:
		yc.addClassName(foodimg,"foodimg");
		fooddesc.appendChild(foodimg);    //  <div class="fooddesc" id="fooddesc1"><img class="foodimg" src="image/foods/50002.jpg"></div>
		
		var art=document.createElement("article");   // <article> </article>
		fooddesc.appendChild(art);
		 //  <div class="fooddesc" id="fooddesc1"><img class="foodimg" src="image/foods/50002.jpg"><article class="foodprice"></article></div>
		yc.addClassName(art,"foodprice");
		
		var detail=document.createElement("p");   // <p>菜品描述:无</p>
		if(onefood.detail){
			detail.innerHTML="菜品描述："+onefood.detail;
		}else{
			detail.innerHTML="菜品描述：无";
		}
		art.appendChild(detail);
		 //  <div class="fooddesc" id="fooddesc1"><img class="foodimg" src="image/foods/50002.jpg">
		    //          <article class="foodprice"><p>菜品描述:无</p></article><p></p><p></p></div>
		
		var normprice=document.createElement("p");
		yc.addClassName(normprice,'normprice');
		normprice.innerHTML="原价：￥"+onefood.normprice;
		art.appendChild(normprice);

		var realprice=document.createElement("p");
		yc.addClassName(realprice,'realprice');
		realprice.innerHTML="特价：￥"+onefood.realprice;
		art.appendChild(realprice);
		
		var buybtn=document.createElement("a");
		buybtn.innerHTML="加入购物车";
		yc.addClassName(buybtn,"buybtn");
		art.appendChild(buybtn);
	//  <div class="fooddesc" id="fooddesc1"><img class="foodimg" src="image/foods/50002.jpg">
	    //          <article class="foodprice"><p>菜品描述:无</p></article><p></p><p></p> <a class="buybtn" >加入购物车</a></div>
	
		
		fooddesc.style.display="none";
		li.appendChild(fooddesc);
		
		document.getElementById("allfoods").appendChild(li);
		
		
		
		(function(index,id){
			yc.addEvent(title,"click",function(){
				showdescs(id);
				
				
				//TODO:服务器的cookie记录
				
				//refreshlook();
			});
		})(i,onefood.fid);    // 
		
		(function(index){
			$(buybtn).click(function(){
				var url="resorder.action?num=1&op=order&fid="+index;
				$.ajax({
					  url:url,
					  type:"GET",
					  dataType:"JSON",
					  success: function(  data  ){
					      if(  data.code==1 ){
					    	  //alert(  "下订成功"  );
					    	  showbag(   data.obj   );
					      }else{
					    	  alert("下订失败");
					      }
					  }
				});
			});
		})(onefood.fid);
	}
}



//显示购物车数据
// cart指的是购物车中的商品数据:     {"编号":{resfood}, "编号":{resfood}}
function showbag(  cart  ){
	
	
	var count=0;    //统计总共有多少个商品条目
	for(var key in cart){
		if(cart.hasOwnProperty(key)){
			count++;
		}
	}
	//取出显示购物车信息的table
	var bag=$("#bagcontainer");
	//如果购物车为空，则...
	if(  count<=0){
		removebuy();
		calprice(cart);
		//var empcar=document.createElement("p");
		//bag.innerHTML="";
		//empcar.innerHTML="购物车是空的，赶紧选购吧";   //<p>购物车是空的，赶紧选购吧</p>
		//var newbottom={"bottom":-300-5+"px"};
		//yc.editCSSRule(".carbag",newbottom,"css/index.css");
		//bag.style.height=260+"px";
		bag.html("<p >购物车是空的，赶紧选购吧</p>");
		$(".carbag").css(   {"bottom":"50px"}   );
		bag.css({"height":"260px"});
		return;
	}
	//TODO:不为空，则执行..
	calprice( cart);
	
	addbuy();//
	
	bag.html("");
	buyfoodidarr=[];
	var theight=0;
	
	var str="";
	for(var key in cart){    // key ->  fid
		if(cart.hasOwnProperty(key)){
			var buyfood=cart[key];    // buyfood就是商品
			buyfoodidarr.push(key);
			str+="<tr>";
			
			str+="<td width='140px'>";
			str+=buyfood.fname;
			str+="</td>";
			
			str+="<td width='130px' class='editfoodnum'>";
			str+="<span>"+ buyfood.num+"</span>";
			str+="<b class='subfoodx' onclick='removefood("+ key+")'>X</b>";
			str+="<input type='button' value='+' onclick='editfood(1,"+  key+"  )'/>";
			str+="<input type='button' value='-' onclick='editfood(-1,"+ key+")' />";
			str+="</td>";
			
			str+="<td width='80px' style='color:#F69C30'>";
			str+="￥"+   (buyfood.num*buyfood.realprice);
			str+="</td>";
			
			str+="</tr>";
			theight++;
		}
	}
	bag.html( str );
	//alert( bag.html() );
	//var newbottom={"bottom":-(theight*40+40+5)+"px"};
	//yc.editCSSRule(".carbag",newbottom,"css/index.css");
	//bag.style.height=theight*40+"px";
	//$(".carbag").css({"bottom":-(theight*40+40+5)+"px"});
	$(".carbag").css({"bottom":"50px"});
	bag.height(   theight*40  );
	
}

//TODO:   
function removefood(id){
	$.ajax({
		  url: "resorder.action?op=delorder&fid="+id,
		  type:"GET",
		  dataType:"JSON",
		  success: function(  data  ){
		      if(  data.code==1 ){
		    	  showbag(   data.obj   );
		      }else{
		    	  alert("修改失败");
		      }
		  }
	});
}

//修改数量
function editfood(num,id){
	$.ajax({
		  url: "resorder.action?op=order&num="+num+"&fid="+id,
		  type:"GET",
		  dataType:"JSON",
		  success: function(  data  ){
		      if(  data.code==1 ){
		    	  showbag(   data.obj   );
		      }else{
		    	  alert("修改失败");
		      }
		  }
	});
}

//修改结算
function addbuy(){
	//yc.$("foodcount").innerHTML="去结算&gt;";
	$("#foodcount").html( "去结算&gt;");
	//yc.addClassName("foodcount","gotobuy");
	$("#foodcount").addClass("gotobuy");
	//yc.addEvent("foodcount","click",tobuy);
	$("#foodcount").click( tobuy   );
}


//当结算，要判断是否登录，如果没有登录过，则显示登录界面
//登录了，则显示送货单
function tobuy(){
	if(haslogined){
		showinfo();
	}else{
		showLogin();
	}
}
//显示登录页
function showLogin() {
	$('#login').show("slow");
	$('#mubu').show("fast");
}

//
function showinfo() {
	$('#myinfo').show("slow");
	$('#mubu').show("fast");
	//yc.$('myinfo').style.display="block";
	//yc.$('mubu').style.display="block";
}

function calprice(cart){//计算价格
	var price=0;
	for(var property in cart){
		if(cart.hasOwnProperty(property)){
			var food=cart[property];
			price+= food.realprice* food.num;
			//price+=hasfood.smallCount;
		}
	}
	if(   $("#pricetext")  ){
		$("#pricetext").html( "￥"+price  );
	}else{
		//var pricetext=document.createElement("p");
		//pricetext.id="pricetext";
		//pricetext.innerHTML="￥"+price;
		//yc.prependChild("car",pricetext);
		$("#car").html("<p id='pricetext'>￥"+price+ "</p>");
		//alert(    $("#car").html() );
	}
}
function removebuy(){
	//yc.$("foodcount").innerHTML="购物车是空的";
	$("#foodcount").html( "购物车是空的"  );
	//yc.removeClassName("foodcount","gotobuy");
	$("#foodcount").removeClass("gotobuy");
	//yc.removeEvent("foodcount","click",tobuy);
	$("#foodcount").off( "click");
}



function refreshlook(){//显示用户浏览记录
	// var options={
	// 	'completeListener':function(){
	// 		var json=this.responseJSON;
	// 		var flag=0;
	// 		if (json.code==1) {
	// 			yc.$("ull").innerHTML="";
	// 			for (var i = 0; i < json.obj.length; i++) {
	// 				var lii=document.createElement("li");
	// 				yc.$('ull').appendChild(lii);
	// 				lii.innerHTML=json.obj[i].fname;
	// 				flag++;
	// 				(function(id){
	// 					yc.addEvent(lii,"click",function(){
	// 						showdescs(id);
	// 						window.scrollTo(0,50*id);
	// 					});
	// 				})(json.obj[i].fid);
					
	// 				if(flag>=9){
	// 					break;
	// 				}
	// 			}
	// 		}
	// 	}
	// };
	//yc.xssRequest("http://218.196.14.220:8080/res/resfood.action?op=findAllSelectedFoods",options);
	
	
}

function showdescs(index){//显示菜品的详情
	var allfoods=yc.$("allfoods");
	//取出你选 的title
	var titles=allfoods.getElementsByTagName("h3");
	var title=yc.$("fid"+index);
	//找到这个title下面对应的   div
	var descs=allfoods.getElementsByTagName("div");
	var desc=yc.$("fooddesc"+index);
	for(var j=0;j<descs.length;j++){
		if(descs[j]==desc) continue;   //找到了，则循环下一个div,
		descs[j].style.display="none"; // 不是这个desc,  则这个diｖ隐藏．．．

		if(index!=allfoodsarr[allfoodsarr.length-1].fid){
		 	yc.removeClassName("fid"+allfoodsarr[allfoodsarr.length-1].fid,"noradius");
		}
	}
	yc.toggleDisplay("fooddesc"+index,"block"); //将当前的层显示
	if(index==allfoodsarr[allfoodsarr.length-1].fid){
		if(yc.hasClassName(title,"noradius")){
			yc.removeClassName(title,"noradius");
		}else{
			yc.addClassName(title,"noradius");
		}
	}
}


