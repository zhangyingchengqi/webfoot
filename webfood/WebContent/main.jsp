<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    
    	<div class="head">
		小萌神订餐网
		<div class="right">
			<span id="showlogin"><a>登录</a></span>
			<span id="exitspan" style="display:none;"></span>
		</div>
	</div>
	<div class="mubu" id="mubu"></div>
	<div class="login" id="login">
		<span id="unshow">X</span>
		<form name="myform">
			<input type="hidden" name="op" value="login">
			<table>
				<tr id="pp">
					<td class="labname"><label for="username">用户名:</label></td>
					<td colspan="2"><input name="username" type="text" placeholder="请输入用户名" id="username" /></td>
				</tr>
				<tr id="pp2">
					<td class="labname"><label for="pwd">密码:</label></td>
					<td colspan="2"><input type="password" id="pwd" placeholder="请输入密码" name="pwd" /></td>
				</tr>
				<tr id="pp3">
					<td class="labname"><label for="">验证码:</label></td>
					<td><input type="text" class="yzm" name="valcode" id="yzm" placeholder="请输入验证码" /></td>
					<td><img src="" id="yzm_img"></td>
				</tr>
			</table>
		</form>
		<input type="button" value="login" class="btn" id="btn">
	</div>

	<div class="login" id="myinfo">
		<span id="unshowinfo">X</span>
		<form name="forminfo">
			<input type="hidden" name="op" value="confirmOrder">
			<table>
				<tr>
					<td class="labname"><label for="address">送货地址:</label></td>
					<td><input name="address" type="text" placeholder="请输入送货地址" id="address" /></td>
				</tr>
				<tr>
					<td class="labname"><label for="tel">联系电话:</label></td>
					<td><input type="text" id="tel" placeholder="请输入联系电话" name="tel" /></td>
				</tr>
				<tr>
					<td class="labname"><label for="deliverytime">送货时间:</label></td>
					<td><input type="text" name="deliverytime" id="deliverytime" placeholder="请输入送货时间" /></td>	
				</tr>
				<tr>
					<td class="labname"><label for="ps">附言:</label></td>
					<td><input type="text" id="ps" placeholder="请输入附言" name="ps" /></td>
				</tr>
			</table>
			
		</form>
		<input type="button" value="submit" class="btn" id="submit">
	</div>


	<div id="content" class="content">
		<ul id="allfoods" class="allfoods">
			
		</ul>
	</div>

	
	<span id="showlook" class="showlook"></span>

	<div class="look" id="look">
		<span>浏览记录</span>
		<ul id="ull" class="ull">
			
		</ul>
	</div>

	<div id="gou" class="gou">
		<div class="carbag" id="carbag">
			<p>购物车<span id="delall">[清空]</span></p>
			<table id="bagcontainer" cellspacing="0" cellpadding="0">
				
			</table>
		</div>
		<span id="foodcount" class="nofood">购物车是空的</span>
		<div class="car" id="car">
			
		</div>
	</div>
	