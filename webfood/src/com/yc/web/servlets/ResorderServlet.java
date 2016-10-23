package com.yc.web.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yc.bean.Resfood;
import com.yc.biz.ResfoodBiz;
import com.yc.biz.impl.ResfoodBizImpl;
import com.yc.utils.YcConstant;
import com.yc.web.model.JsonModel;


public class ResorderServlet extends BasicServlet {
	private static final long serialVersionUID = 1L;
       

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(   "order".equals(op)){
			orderOp( request, response);
		}
	}


	private void orderOp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Resfood resfood=(Resfood) super.parseRequest(request, Resfood.class	);   //取出  fid
		
		HttpSession session=request.getSession();
		//判断session中的map是否已经存在
		Map<Integer, Resfood> cart=new HashMap<Integer, Resfood>();
		if(  session.getAttribute(  YcConstant.CART_NAME  ) !=null ){
			cart=(Map<Integer, Resfood>) session.getAttribute(  YcConstant.CART_NAME  );
		}
		JsonModel jm = new JsonModel();
		try {
			//不存在，说明这是这个用户第一次下单
			//           创建一个ｍａｐ，作为购物车存到session中
			//如果存在，说明用户已经买过东西了，从session中取出这个   map
			//处理数量
			if(   cart.containsKey(   resfood.getFid()  )){
				//如果存在，则数量要相加
				Resfood rfold=cart.get(    resfood.getFid() );
				rfold.setNum(    resfood.getNum()+   rfold.getNum()  );   //更新数量
				cart.put(  resfood.getFid()  , rfold);
			}else{
				//如果不存在，则数量为1
				ResfoodBiz rb=new ResfoodBizImpl();
				Resfood rf=rb.getResfoodByFid(    resfood.getFid()   );
				//数量
				rf.setNum(    resfood.getNum() );
				cart.put(   resfood.getFid(),    rf);  //  编号      num    ->  产品名，价格不存在
			}
			
			session.setAttribute(     YcConstant.CART_NAME, cart);
			
			
			jm.setCode(1);
			jm.setObj(cart);
		} catch (Exception e) {
			e.printStackTrace();
			jm.setCode(0);
			jm.setErrorMsg(e.toString());
		}
		
		super.outJson(jm, response);
		
		
	}

}
