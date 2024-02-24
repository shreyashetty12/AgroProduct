package com.agroprod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agroprod.model.Buyer;
import com.agroprod.model.Orders;
import com.agroprod.model.Product;
import com.agroprod.model.Seller;
import com.agroprod.model.Stock;
import com.agroprod.service.BuyerService;
import com.agroprod.service.OrderService;
import com.agroprod.service.SellerService;
import com.agroprod.util.TimeUtil;

@Controller
@RequestMapping("/seller")
public class SellerController {
	@Autowired
	private SellerService sellerService;

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private TimeUtil timeUtil;

	public String status = "";

	public static String redirectionToGoodPriceList = "redirect:/seller/check";

	@GetMapping("/list")
	public String listSellers(Model model) {
		List<Seller> sellers = sellerService.findAll();
		model.addAttribute("sellers", sellers);
		model.addAttribute("isSeller", sellerService.isSeller());
		model.addAttribute("isBuyer", buyerService.isBuyer());

		return "seller-list";
	}

	@GetMapping("/signup")
	public String formForAdd(Model model) {
		Seller theSeller = new Seller();
		model.addAttribute("theSeller", theSeller);
		model.addAttribute("isSeller", sellerService.isSeller());
		model.addAttribute("isBuyer", buyerService.isBuyer());

		model.addAttribute("userStatus", status);
		status = "";
		return "seller-signup-form";

	}

	@PostMapping("/save")
	public String saveSeller(@ModelAttribute("theSeller") Seller theSeller) {

		boolean existingSeller = sellerService.existingSeller(theSeller.getSellerName());

		if (!existingSeller) {
			theSeller.setSellerPassword("{noop}" + theSeller.getSellerPassword());
			sellerService.save(theSeller);
			return "redirect:/user/login";
		} else {
			status = "User already Exists";
			return "redirect:/seller/signup";
		}

	}

	@GetMapping("/{sellerId}/stock")
	public String getStocks(@PathVariable("sellerId") int sellerId, Model model) {
		Seller seller = sellerService.findBySellerId(sellerId);
		List<Stock> stocks = seller.getStocks();
		model.addAttribute("stocks", stocks);
		model.addAttribute("isSeller", sellerService.isSeller());
		model.addAttribute("isBuyer", buyerService.isBuyer());

		return "stock-list";
	}

	@GetMapping("/check")
	public String checkGetGoodPrice(Model model) {

		List<Orders> goodPriceForProduct = orderService.getGoodPriceForProduct();

		model.addAttribute("orders", goodPriceForProduct);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		redirectionToGoodPriceList = "redirect:/seller/check";
		return "orders";

	}

	@GetMapping("/check/{productId}")
	public String checkGetGoodPriceByProdID(Model model, @PathVariable("productId") int productId) {

		List<Orders> goodPriceForProduct = orderService.getGoodPriceForProductByProdId(productId);

		model.addAttribute("orders", goodPriceForProduct);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		redirectionToGoodPriceList = "redirect:/seller/check/" + productId;
		return "orders";

	}

	@RequestMapping(value = "/confirm/{orderId}", method = RequestMethod.GET)
	public String confirmOrder(@ModelAttribute("orders") Orders orders, @PathVariable("orderId") int orderId) {

		Orders order = orderService.getOrderById(orderId);

		order.setStatus("ACCEPTED");
		order.setAcceptedTime(timeUtil.getCurrTime());

		orderService.save(order);

		return redirectionToGoodPriceList;
	}

	@RequestMapping(value = "/reject/{orderId}", method = RequestMethod.GET)
	public String rejectOrder(@ModelAttribute("orders") Orders orders, @PathVariable("orderId") int orderId) {

		Orders order = orderService.getOrderById(orderId);

		order.setStatus("REJECTED");

		orderService.save(order);

		return redirectionToGoodPriceList;
	}

}
