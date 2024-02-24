package com.agroprod.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.agroprod.model.Product;
import com.agroprod.model.Stock;
import com.agroprod.service.BuyerService;
import com.agroprod.service.ProductService;
import com.agroprod.service.SellerService;
import com.agroprod.service.StockService;
import com.fasterxml.jackson.databind.util.JSONPObject;

@Controller
@RequestMapping("/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private StockService stockService;

	@GetMapping("/list")
	public String getProducts(Model model) {
		List<Product> products = productService.findAll();
		model.addAttribute("products", products);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		return "product-list";
	}

	@GetMapping("/add")
	public String addProduct(Model model) {
		Product theProduct = new Product();
		model.addAttribute("theProduct", theProduct);
		model.addAttribute("isBuyer", buyerService.isBuyer());
		model.addAttribute("isSeller", sellerService.isSeller());
		return "product-add-form";
	}

	@PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // @RequestParam("file")
																					// MultipartFile file,
	public String saveProduct(@ModelAttribute("theProduct") Product theProduct, Model model,
			@RequestParam MultipartFile file) {

		JSONObject upload = upload(file, null);
		if (upload.getBoolean("status")) {
			theProduct.setProductImage(upload.getString("fileName"));
			productService.save(theProduct);
			Stock theStock = new Stock();
			model.addAttribute("theStock", theStock);
			model.addAttribute("theProduct", theProduct);
			model.addAttribute("isBuyer", buyerService.isBuyer());
			model.addAttribute("isSeller", sellerService.isSeller());
		}

		return "stock-form";
	}

	@PostMapping(value = "/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String updateProduct(@ModelAttribute("theProduct") Product theProduct, Model model,
			@RequestParam MultipartFile file, @PathVariable("productId") int productId) {

		JSONObject upload = upload(file, null);
		if (upload.getBoolean("status")) {
			Product product = productService.findByProductId(theProduct.getProductId());

			product.setProductName(theProduct.getProductName());
			product.setProductPrice(theProduct.getProductPrice());
			product.setProductDesc(theProduct.getProductDesc());
			product.setProductImage(upload.getString("fileName"));

			productService.save(product);
			int theStockId = stockService.findByProductId(product);
			Stock theStock = stockService.findByStockId(theStockId);
			System.out.println("theStock ::: " + theStock);
			model.addAttribute("theStock", theStock);
			model.addAttribute("theProduct", theProduct);
			model.addAttribute("isBuyer", buyerService.isBuyer());
			model.addAttribute("isSeller", sellerService.isSeller());
		}

		return "stock-update-form";
	}

	public JSONObject upload(@RequestParam MultipartFile file, HttpSession session) {
		String path = "D:\\Shreya-InternshiProject\\AgroProd14thMay (2)\\AgroProd14thMay\\AgroProductApp\\src\\main\\resources\\static";
		boolean status = true;

		JSONObject jsonObject = new JSONObject();

		String filename = file.getOriginalFilename();
		String nameOfFile = "/";
		System.out.println(path + " " + filename);
		try {

			String name = file.getOriginalFilename();

			String randomID = UUID.randomUUID().toString();

			String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

			String filepath = path + File.separator + fileName1;

			File f = new File(path);

			if (!f.exists()) {
				f.mkdir();
			}
			nameOfFile = nameOfFile + fileName1;
			Files.copy(file.getInputStream(), Paths.get(filepath));

		} catch (Exception e) {
			status = false;
			System.out.println(e);
		}

		jsonObject.put("status", status);
		jsonObject.put("fileName", nameOfFile);
		String s = "upload-success" + " filename " + path + "/" + filename;

		System.out.println("File is saved :: " + s);
		return jsonObject;
	}

}
