package com.agroprod.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import com.agroprod.model.Cart;
import com.agroprod.model.Orders;
import com.agroprod.model.Product;
import com.agroprod.model.Seller;
import com.agroprod.model.Stock;
import com.agroprod.service.BuyerService;
import com.agroprod.service.CartService;
import com.agroprod.service.OrderService;
import com.agroprod.service.SellerService;
import com.agroprod.service.StockService;
import com.agroprod.util.PDFGenerator;
import com.agroprod.util.SendMail;
import com.agroprod.util.TimeUtil;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private StockService stockService;

	@Autowired
	private TimeUtil timeUtil;

	@Autowired
	private PDFGenerator pdfGenerator;

	@Autowired
	private SendMail sendMail;

	public static final String filePath = "D:\\Shreya-InternshiProject\\AgroProd14thMay (2)\\AgroProd14thMay\\AgroProductApp\\Invoices";

	@GetMapping("/list")
	public String getOrders(Model model) {
		Buyer currentBuyer = buyerService.getCurrentBuyer();
		List<Orders> orders = currentBuyer.getOrders();

		for (Orders order : orders) {

			List<Product> products = order.getProducts();
			System.out.println(order.getOrderId());
			for (Product product : products) {
				int stockId = stockService.findByProductId(product);
				Stock stock = stockService.findByStockId(stockId);
				order.setAvailableStock(stock.getStockQuantity());
				System.out.println(order.getOrderId() + " " + product);
//				orderService.save(order);

			}

			if (order.getRequestedPrice() == 0 || order.getRequestedQuantity() == 0) {
				System.out.println("deleted " + order.getOrderId());
				orderService.deletOrderById(order.getOrderId());
				return "redirect:/order/list";
			}

			if (!order.getAcceptedTime().equalsIgnoreCase("PENDING")
					&& order.getStatus().equalsIgnoreCase("ACCEPTED")) {
				boolean exceeded = timeUtil.getTimeDifference(order.getAcceptedTime());

				if (exceeded) {
					order.setStatus("CANCELLED");
//					orderService.save(order);
				}
			}

			orderService.save(order); // new
		}

//		String mailFilePath = "F:\\Shreya\\AgroProd14thMay\\AgroProductApp\\Invoices\\Mango52d324e1-8bd7-4d1e-b7e1-0fd1be437172";
//		sendMail.sendItinerary("shreyashetty8686@gmail.com", "Invoice", mailFilePath,
//				"Mango52d324e1-8bd7-4d1e-b7e1-0fd1be437172.pdf","Shreya"); // testing

		model.addAttribute("orders", orders);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		return "orders";
	}

	@GetMapping("")
	public String order(Model model) {
		Orders order = new Orders();
		Buyer currentBuyer = buyerService.getCurrentBuyer();
		Cart theCart = currentBuyer.getCart();
		List<Product> products = theCart.getProducts();
		List<Product> orderProducts = new ArrayList<Product>(products);
		order.setProducts(orderProducts);
		order.setBuyer(currentBuyer);

		orderService.save(order);
		theCart.removeCartProducts();
		cartService.save(theCart);

//		List<Orders> orders = currentBuyer.getOrders();
//		int orderslength = orders.size();
//		
//		Orders orders2 = orders.get(orderslength-1);

		model.addAttribute("orders", order);
//		model.addAttribute("orders", orders2);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		return "cart-edit-form";

//		return "redirect:/order/list";
	}

	@PostMapping("/update/{orderId}")
	public String updateOrder(@ModelAttribute("orders") Orders orders, @PathVariable("orderId") int orderId) {

		System.out.println(orders);

		Buyer currentBuyer = buyerService.getCurrentBuyer();
		List<Orders> orderz = currentBuyer.getOrders();

		Orders orders2 = currentBuyer.getOrders().get(orderz.size() - 1);

		List<Product> products = orders2.getProducts();
		for (Product product : products) {
			int stockId = stockService.findByProductId(product);

			Stock stock = stockService.findByStockId(stockId);

			orders2.setAvailableStock(stock.getStockQuantity());
		}

		orders2.setRequestedPrice(orders.getRequestedPrice());
		orders2.setRequestedQuantity(orders.getRequestedQuantity());
		orders2.setStatus("PENDING WITH SELLER");
		orders2.setAcceptedTime("PENDING");
		orderService.save(orders2);
		System.out.println("orders2 " + orders2);
		return "redirect:/order/list";

	}

	@RequestMapping(value = "/payment/{orderId}", method = RequestMethod.GET)
	public String payment(@ModelAttribute("orders") Orders orders, @PathVariable("orderId") int orderId, Model model) {

		Orders order = orderService.getOrderById(orderId);

		model.addAttribute("orders", order);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());

		return "credit-card";
	}

	@RequestMapping(value = "/paymentSuccess/{orderId}", method = RequestMethod.GET)
	public String paymentSuccessUpdate(@ModelAttribute("orders") Orders orders, @PathVariable("orderId") int orderId,
			Model model) {
		String randomID = UUID.randomUUID().toString();
		Orders order = orderService.getOrderById(orderId);
		int totalAmount = order.getRequestedPrice() * order.getRequestedQuantity();
		order.setAmountPaid(totalAmount);
		order.setStatus("Payment of Rs " + totalAmount + ".00/- has been done on " + new Date().toLocaleString()
				+ " with payment id " + randomID);
		orderService.save(order);

		List<Product> products = order.getProducts();

		for (Product product : products) {

			int sId = stockService.findByProductId(product);

			Stock stock = stockService.findByStockId(sId);

			stock.setStockQuantity(stock.getStockQuantity() - order.getRequestedQuantity());
			stockService.save(stock);

			Seller seller = stock.getSeller();

			Buyer currentBuyer = buyerService.getCurrentBuyer();

			String file = filePath + product.getProductName() + randomID + ".pdf";
			String filePathForMail = filePath + product.getProductName() + randomID;
			String fileName = product.getProductName() + randomID + ".pdf";
			try {
//				pdfGenerator.generatePDF(file, currentBuyer.getBuyerName(), currentBuyer.getBuyerEmail(), "Bangalore",
//						product.getProductName(), totalAmount, order.getRequestedQuantity(), order.getRequestedPrice(),
//						randomID, seller);
//				System.out.println("file " + file);
//				System.out.println("filePathForMail " + filePathForMail);
//				System.out.println("fileName " + fileName);
////				sendMail.sendItinerary("sshettyshreyainternproj@gmail.com", "Order Details", filePathForMail, fileName);
//				sendMail.sendItinerary(currentBuyer.getBuyerEmail(), "Order Invoice", filePathForMail, fileName,currentBuyer.getBuyerName());

			} catch (Exception e) {
				System.out.println(e);
			}

		}

		return "redirect:/order/list";
	}

}
