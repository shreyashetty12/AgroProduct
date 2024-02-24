package com.agroprod.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agroprod.model.Product;
import com.agroprod.model.Seller;
import com.agroprod.model.Stock;
import com.agroprod.service.BuyerService;
import com.agroprod.service.OrderService;
import com.agroprod.service.ProductService;
import com.agroprod.service.SellerService;
import com.agroprod.service.StockService;

@Controller
@RequestMapping("/stock")
public class StockController {
	@Autowired
	private StockService stockService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;

	@GetMapping("/list")
	public String getStocks(Model model) {
		Seller currentSeller = sellerService.getCurrentSeller();
		List<Stock> stocks = currentSeller.getStocks();
		model.addAttribute("stocks", stocks);
		model.addAttribute("isSeller", sellerService.isSeller());
		model.addAttribute("isBuyer", buyerService.isBuyer());
		
		orderService.getGoodPriceForProduct();
		return "stock-list";
	}

	@PostMapping("/save/{productId}")
	public String addStock(@ModelAttribute("theStock") Stock theStock, @PathVariable("productId") int productId) {
		Product theProduct = productService.findByProductId(productId);
		theStock.setProduct(theProduct);
		Seller currentSeller = sellerService.getCurrentSeller();
		theStock.setSeller(currentSeller);
		stockService.save(theStock);
		return "redirect:/stock/list";
	}

	@GetMapping(value = "/delete/{stockId}")
	public String deleteStock(@PathVariable("stockId") Integer stockId) {

		Stock theStock = stockService.findByStockId(stockId);
		theStock.setProduct(theStock.getProduct());
		theStock.setSeller(sellerService.getCurrentSeller());
		stockService.delete(theStock);
		stockService.save(theStock);
		
		return "redirect:/stock/list";
	}
	
	@GetMapping(value="/edit/{stockId}")
	public String editStock(Model model, @PathVariable ("stockId") Integer stockId) {
		Stock theStock = stockService.findByStockId(stockId);
		Product theProduct = productService.findByProductId(theStock.getProduct().getProductId());
		
		model.addAttribute("theProduct", theProduct);
		model.addAttribute("stocks", theStock);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		
		return "product-edit-form";
	}

	@PostMapping("/update/{productId}")
	public String updateStock(@ModelAttribute("theStock") Stock theStock, @PathVariable("productId") int productId) {
		Product theProduct = productService.findByProductId(productId);
		int stockId = stockService.findByProductId(theProduct);
		Stock stock = stockService.findByStockId(stockId);
		System.out.println("theStock "+theStock);
		System.out.println("stock "+stock);
		stock.setProduct(theProduct);
		stock.setStockQuantity(theStock.getStockQuantity());
		stock.setUom(theStock.getUom());
		Seller currentSeller = sellerService.getCurrentSeller();
		theStock.setSeller(currentSeller);
		stockService.save(stock);
		return "redirect:/stock/list";
	}

}
